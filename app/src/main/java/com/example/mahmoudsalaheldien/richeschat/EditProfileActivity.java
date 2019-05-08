package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;


public class EditProfileActivity extends AppCompatActivity {
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private TextInputLayout fullName_textInputLayout_editProfile,
            nickname_textInputLayout_editProfile, country_textInputLayout_editProfile, status_textInputLayout_editProfile;
    private Button save_button_editProfile, uploudImage_button_editProfile;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar loading_progressBar_editProAct;
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_progressBar_editProAct = findViewById(R.id.loading_progressBar_editProAct);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fullName_textInputLayout_editProfile = findViewById(R.id.fullName_textInputLayout_editProfile);
        nickname_textInputLayout_editProfile = findViewById(R.id.nickname_textInputLayout_editProfile);
        country_textInputLayout_editProfile = findViewById(R.id.country_textInputLayout_editProfile);
        status_textInputLayout_editProfile = findViewById(R.id.status_textInputLayout_editProfile);
        uploudImage_button_editProfile = findViewById(R.id.uploudImage_button_editProfile);
        uploudImage_button_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);//one can be replaced with any action code
            }
        });
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String fullName = user.username;
                fullName_textInputLayout_editProfile.getEditText().setText(fullName);
                String nickname = user.nickname;
                nickname_textInputLayout_editProfile.getEditText().setText(nickname);
                String country = user.country;
                country_textInputLayout_editProfile.getEditText().setText(country);
                String status = user.status;
                status_textInputLayout_editProfile.getEditText().setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save_button_editProfile = findViewById(R.id.save_button_editProfile);
        save_button_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
                String fullName = fullName_textInputLayout_editProfile.getEditText().getText().toString().trim();
                String nickname = nickname_textInputLayout_editProfile.getEditText().getText().toString().trim();
                String country = country_textInputLayout_editProfile.getEditText().getText().toString().trim();
                String status = status_textInputLayout_editProfile.getEditText().getText().toString().trim();
                Map<String, Object> taskMap = new HashMap();
                taskMap.put("username", fullName);
                taskMap.put("nickname", nickname);
                taskMap.put("country", country);
                taskMap.put("status", status);
                databaseReference.updateChildren(taskMap);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            StorageReference riversRef = storageReference.child("images").child(firebaseUser.getUid()+data.getType());
            final ProgressBar loading_progressBar_editProAct = findViewById(R.id.loading_progressBar_editProAct);
            loading_progressBar_editProAct.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(firebaseUser.getUid()).child("profile");
                            databaseReference.setValue(downloadUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    loading_progressBar_editProAct.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(false);
    }
}
