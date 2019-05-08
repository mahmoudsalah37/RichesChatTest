package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class HomeFragment extends Fragment {
    private RecyclerView userOnline_RecyclerView_fragHome;
    private HomeAdapter mAdapter;
    private FirebaseUser firebaseUser;
    private List<UsersOnline> usersOnlineList;
    private List<String> usersFollowingID;
    private DatabaseReference databaseReference;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        usersOnlineList = new ArrayList<>();
        usersFollowingID = new ArrayList<>();
        userList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userOnline_RecyclerView_fragHome = view.findViewById(R.id.userOnline_RecyclerView_fragHome);
        userOnline_RecyclerView_fragHome.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), userOnline_RecyclerView_fragHome, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = userList.get(position);
                intentMain2Other(user);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter = new HomeAdapter(usersOnlineList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        userOnline_RecyclerView_fragHome.setLayoutManager(mLayoutManager);
        userOnline_RecyclerView_fragHome.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        userOnline_RecyclerView_fragHome.setItemAnimator(new DefaultItemAnimator());
        userOnline_RecyclerView_fragHome.setAdapter(mAdapter);
        databaseReference.child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    usersFollowingID.add(data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    String isonline = null;
                    if (data.child("isonline").getValue() != null)
                        isonline = data.child("isonline").getValue().toString();
                    String profile = null;
                    if (data.child("profile").getValue() != null)
                        profile = data.child("profile").getValue().toString();
                    UsersOnline usersOnline = new UsersOnline(user.userID, user.username, isonline, profile);
                    if (usersFollowingID.contains(user.userID) && isonline.equals("true") && searchPeopleList(usersOnline, usersOnlineList)) {
                        usersOnlineList.add(usersOnline);
                        userList.add(user);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private boolean searchPeopleList(UsersOnline usersOnline, List<UsersOnline> usersOnlineList) {
        for (int i = 0; i < usersOnlineList.size(); i++) {
            if (usersOnlineList.get(i).userID.equals(usersOnline.userID)) {
                return false;
            }
        }
        return true;
    }

    private void intentMain2Other(User user) {
        Intent main_2_OtherProfile = new Intent(getActivity(), OtherProfileActivity.class);
        main_2_OtherProfile.putExtra("userID", user.userID);
        main_2_OtherProfile.putExtra("username", user.username);
        main_2_OtherProfile.putExtra("nickname", user.nickname);
        main_2_OtherProfile.putExtra("country", user.country);
        main_2_OtherProfile.putExtra("status", user.status);
        startActivity(main_2_OtherProfile);
    }

}
