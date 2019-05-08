package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String userID;
    private ProgressBar loading_progressBar_actOtherPro;
    boolean checkFollow;
    private Button follow_otherPro;
    private CircleImageView image_otherPro;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextView following_textView_otherActPro, followers_textView_otherActPro;
    private ImageView imageViewProfile_actOtherPro;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        image_otherPro = findViewById(R.id.image_otherPro);
        image_otherPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewProfile_actOtherPro.setVisibility(View.VISIBLE);
            }
        });
        imageViewProfile_actOtherPro = findViewById(R.id.imageViewProfile_actOtherPro);
        followers_textView_otherActPro = findViewById(R.id.followers_textView_otherActPro);
        following_textView_otherActPro = findViewById(R.id.following_textView_otherActPro);
        loading_progressBar_actOtherPro = findViewById(R.id.loading_progressBar_actOtherPro);
        imageViewProfile_actOtherPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewProfile_actOtherPro.setVisibility(View.GONE);
            }
        });
        follow_otherPro = findViewById(R.id.follow_otherPro);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        ImageView editImage_otherPro = findViewById(R.id.editImage_otherPro);
        userID = getIntent().getExtras().getString("userID");
        TextView userName_textView_profile = findViewById(R.id.userName_otherPro);
        TextView status_textView_profile = findViewById(R.id.status_otherPro);
        TextView location_textView_profile = findViewById(R.id.location_otherPro);
        final String userName = getIntent().getExtras().getString("username");
        userName_textView_profile.setText(userName);
        String nickname = getIntent().getExtras().getString("nickname");
        if (!nickname.equals(""))
            userName_textView_profile.append(" (" + nickname + ")");
        String location = getIntent().getExtras().getString("country");
        location_textView_profile.setText(location);
        String status = getIntent().getExtras().getString("status");
        status_textView_profile.setText(status);
        databaseReference.child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkFollow = false;
                follow_otherPro.setText("Follow");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue().equals(userID)) {
                        checkFollow = true;
                        follow_otherPro.setText("unFollow");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child(userID).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String image = dataSnapshot.getValue().toString();
                    if (!image.equals("")) {
                        Picasso
                                .with(OtherProfileActivity.this).load(image)
                                .resize(80, 80)
                                .placeholder(R.drawable.man).into(image_otherPro);

                        Picasso
                                .with(OtherProfileActivity.this)
                                .load(image).into(imageViewProfile_actOtherPro);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        follow_otherPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow_otherPro.setClickable(false);
                if (follow_otherPro.getText().equals("Follow"))
                    follow_otherPro.setText("following ...");
                else
                    follow_otherPro.setText("unfollowing ...");
                isFollow(checkFollow);
                follow_otherPro.setClickable(true);
            }
        });
        editImage_otherPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherProAct2ChatRoomAct = new Intent(OtherProfileActivity.this, ChatRoomActivity.class);
                otherProAct2ChatRoomAct.putExtra("userID", userID);
                otherProAct2ChatRoomAct.putExtra("username", userName);
                startActivity(otherProAct2ChatRoomAct);
            }
        });
        getData();
    }

    private void isFollow(boolean check) {
        if (check) {
            databaseReference.child(firebaseUser.getUid()).child("following").child(userID).removeValue();
            databaseReference.child(userID).child("followers").child(firebaseUser.getUid()).removeValue();
        } else {
            databaseReference.child(firebaseUser.getUid()).child("following").child(userID).setValue(userID);
            databaseReference.child(userID).child("followers").child(firebaseUser.getUid()).setValue(firebaseUser.getUid());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(false);
    }

    private void getData() {
        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long numberFollowings = dataSnapshot.child("following").getChildrenCount();
                long numberFollowers = dataSnapshot.child("followers").getChildrenCount();

                following_textView_otherActPro.setText("Following \n" + numberFollowings);
                followers_textView_otherActPro.setText("Followers \n" + numberFollowers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}