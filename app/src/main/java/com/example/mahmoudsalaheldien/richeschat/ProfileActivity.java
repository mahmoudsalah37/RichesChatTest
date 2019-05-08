package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    Intent intent_proAct2followAct;
    DatabaseReference databaseReference;
    private TextView following_textView_avtivityPro, followers_textView_avtivityPro;
    private CircleImageView image_circleImageView_profile;
    private String image;
    private ProgressBar loading_progressBar_actPro;
    private ImageView imageViewProfile_actPro;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageViewProfile_actPro = findViewById(R.id.imageViewProfile_actPro);
        imageViewProfile_actPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewProfile_actPro.setVisibility(View.GONE);
            }
        });
        loading_progressBar_actPro = findViewById(R.id.loading_progressBar_actPro);
        intent_proAct2followAct = new Intent(ProfileActivity.this, FollowActivity.class);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        following_textView_avtivityPro = findViewById(R.id.following_textView_avtivityPro);
        following_textView_avtivityPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_proAct2followAct.putExtra("isFollowingAct", true);
                startActivity(intent_proAct2followAct);
            }
        });
        followers_textView_avtivityPro = findViewById(R.id.followers_textView_avtivityPro);
        followers_textView_avtivityPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_proAct2followAct.putExtra("isFollowingAct", false);
                startActivity(intent_proAct2followAct);
            }
        });
        getData();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        final TextView userName_textView_profile = findViewById(R.id.userName_textView_profile);
        final TextView status_textView_profile = findViewById(R.id.status_textView_profile);
        final TextView location_textView_profile = findViewById(R.id.location_textView_profile);
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading_progressBar_actPro.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (dataSnapshot.child("profile").getValue() != null && !dataSnapshot.child("profile").getValue().toString().equals("")) {
                    image = dataSnapshot.child("profile").getValue().toString();
                    Picasso.with(ProfileActivity.this).load(image).resize(65,65).placeholder(R.drawable.man).into(image_circleImageView_profile);
                }
                User user = dataSnapshot.getValue(User.class);
                String userName = user.username;
                userName_textView_profile.setText(userName);
                String nickname = user.nickname;
                if ( nickname != null)
                if (!nickname.equals(""))
                    userName_textView_profile.append(" (" + nickname + ")");
                String location = user.country;
                location_textView_profile.setText(location);
                String status = user.status;
                status_textView_profile.setText(status);
                loading_progressBar_actPro.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        image_circleImageView_profile = findViewById(R.id.image_circleImageView_profile);
        image_circleImageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewProfile_actPro.setVisibility(View.VISIBLE);
                Picasso.with(ProfileActivity.this).load(image).into(imageViewProfile_actPro);
            }
        });

    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long numberFollowings = dataSnapshot.child("following").getChildrenCount();
                long numberFollowers = dataSnapshot.child("followers").getChildrenCount();

                following_textView_avtivityPro.setText("Following \n" + numberFollowings);
                followers_textView_avtivityPro.setText("Followers \n" + numberFollowers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editImage(View view) {
        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(false);
    }

}
