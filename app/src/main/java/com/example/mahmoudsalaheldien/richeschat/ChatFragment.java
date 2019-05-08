package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private List<UsersMyChat> usersMyChatList;
    private Set<String> usersIDSet;
    private ChatAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        usersMyChatList = new ArrayList<>();
        usersIDSet = new HashSet<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.myChat_recycleView_fragChat);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UsersMyChat user = usersMyChatList.get(position);
                Intent otherProAct2ChatRoomAct = new Intent(getActivity(), ChatRoomActivity.class);
                otherProAct2ChatRoomAct.putExtra("userID", user.userID);
                otherProAct2ChatRoomAct.putExtra("username", user.name);
                startActivity(otherProAct2ChatRoomAct);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter = new ChatAdapter(usersMyChatList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        if (firebaseUser != null) {
            DatabaseReference databaseReferenceUserID = databaseReference.child(firebaseUser.getUid()).child("chat");
            databaseReferenceUserID.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.child("userID").getValue() != null) {
                            String id = data.child("userID").getValue().toString();
                            usersIDSet.add(id);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReferenceUserID.keepSynced(true);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if (usersIDSet.contains(user.userID )&& searchPeopleList(user, usersMyChatList)){
                        String userID = user.userID;
                        String name  = user.username;
                        String online = data.child("isonline").getValue().toString();
                        String profile = user.profile;
                        usersMyChatList.add(new UsersMyChat(userID, name, online, profile));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.keepSynced(true);
        return view;
    }
    private boolean searchPeopleList(User user, List<UsersMyChat> usersMyChatList) {
        for (int i = 0 ; i< usersMyChatList.size();i++) {
            if(usersMyChatList.get(i).userID.equals(user.userID)){
                return false;
            }
        }
        return true;
    }
}
