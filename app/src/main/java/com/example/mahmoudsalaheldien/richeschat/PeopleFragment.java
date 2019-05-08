package com.example.mahmoudsalaheldien.richeschat;

import android.content.Context;
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
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PeopleFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private List<User> peopleList;
    private PeopleAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        peopleList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        recyclerView = view.findViewById(R.id.people_recycleView);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = peopleList.get(position);
                intentMain2Other(user);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getData();
        return view;
    }

    private void getData() {
        mAdapter = new PeopleAdapter(peopleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    final User user = data.getValue(User.class);
                    if (user.userID != null)
                        if (!user.userID.equals(firebaseUser.getUid()) && searchPeopleList(user, peopleList)) {
                            peopleList.add(user);
                        }


                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.keepSynced(true);
        // Inflate the layout for this fragment
    }

    private boolean searchPeopleList(User user, List<User> peopleList) {
        for (int i = 0; i < peopleList.size(); i++) {
            if (peopleList.get(i).userID.equals(user.userID)) {
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
