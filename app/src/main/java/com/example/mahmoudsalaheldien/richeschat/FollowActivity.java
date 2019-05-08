package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FollowActivity extends AppCompatActivity {
    private RecyclerView followActivity_recycleView;
    private List<User> followList;
    private FollowAdapter mAdapter;
    private FirebaseUser firebaseUser;
    private Set<String> usersSetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersSetID = new HashSet<>();
        followList = new ArrayList<>();
        followActivity_recycleView = findViewById(R.id.followActivity_recycleView);
        followActivity_recycleView.addOnItemTouchListener(new RecyclerTouchListener(this,followActivity_recycleView , new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                intentMain2Other(followList.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getData();
    }

    private void getData() {
        mAdapter = new FollowAdapter(followList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        followActivity_recycleView.setLayoutManager(mLayoutManager);
        followActivity_recycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        followActivity_recycleView.setItemAnimator(new DefaultItemAnimator());
        followActivity_recycleView.setAdapter(mAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //usersSetID = null;
                        Boolean isFollowingAct = getIntent().getExtras().getBoolean("isFollowingAct");
                        if (isFollowingAct) {
                            for (DataSnapshot data : dataSnapshot.child("following").getChildren()) {
                                String usersID = data.getValue().toString();
                                usersSetID.add(usersID);
                            }
                        }else {
                            for (DataSnapshot data : dataSnapshot.child("followers").getChildren()) {
                                String usersID = data.getValue().toString();
                                usersSetID.add(usersID);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if (usersSetID.contains(user.userID) && searchChatList(user,followList)){
                        followList.add(user);
                    }
                }
                // followList = new ArrayList<>(new HashSet<>(followList));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
    }
    private boolean searchChatList(User user, List<User> chatList) {
        for (int i = 0 ; i< chatList.size();i++) {
            if(chatList.get(i).userID.equals(user.userID)){
                return false;
            }
        }
        return true;
    }

    private void intentMain2Other(User user) {
        Intent main_2_OtherProfile = new Intent(this, OtherProfileActivity.class);
        main_2_OtherProfile.putExtra("userID", user.userID);
        main_2_OtherProfile.putExtra("username", user.username);
        main_2_OtherProfile.putExtra("nickname", user.nickname);
        main_2_OtherProfile.putExtra("country", user.country);
        main_2_OtherProfile.putExtra("status", user.status);
        startActivity(main_2_OtherProfile);
    }
}
