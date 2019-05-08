package com.example.mahmoudsalaheldien.richeschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private List<MessageUser> messageUserList;
    private ChatRoomAdapter mAdapter;
    private DatabaseReference databaseReference;
    private EmojiEditText writeMessage_EmojiEditText_actChatRoom;
    private ImageButton sendMessage_imageButton_actChatRoom, sendVoice_imageButton_actChatRoom;
    private String userID, nameUser;
    private Set messageId;
    private boolean check = true;
    private EmojiPopup emojiPopup;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new GoogleEmojiProvider());
        //EmojiManager.install(new EmojiOneProvider());
        //EmojiManager.install(new IosEmojiProvider());
        setContentView(R.layout.activity_chat_room);
        messageId = new HashSet();
        userID = getIntent().getExtras().getString("userID");
        nameUser = getIntent().getExtras().getString("username");
        getSupportActionBar().setTitle(nameUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageUserList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid()).child("chat").child(userID).child("userID").setValue(userID);
        databaseReference.child(userID).child("chat").child(firebaseUser.getUid()).child("userID").setValue(firebaseUser.getUid());
        recyclerView = findViewById(R.id.message_recyclerView_ActChatRoom);
        editRecycleView();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        writeMessage_EmojiEditText_actChatRoom = findViewById(R.id.writeMessage_EmojiEditText_actChatRoom);
        sendMessage_imageButton_actChatRoom = findViewById(R.id.sendMessage_imageButton_actChatRoom);
        sendMessage_imageButton_actChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageVar = writeMessage_EmojiEditText_actChatRoom.getText().toString().trim();
                if (!messageVar.equals("")) {
                    MessageUser mes = new MessageUser(userID, messageVar, false, "TEXT");
                    DatabaseReference data = databaseReference.child(firebaseUser.getUid()).child("chat").child(userID).push();
                    data.setValue(mes);
                    DatabaseReference data1 = databaseReference.child(userID).child("chat").child(firebaseUser.getUid()).push();
                    data1.setValue(mes);
                    writeMessage_EmojiEditText_actChatRoom.setText("");
                }

            }
        });
        sendVoice_imageButton_actChatRoom = findViewById(R.id.sendVoice_imageButton_actChatRoom);
        sendVoice_imageButton_actChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String messageVar = "https://firebasestorage.googleapis.com/v0/b/richeschat-f406b.appspot.com/o/voice%2F%D9%83%D8%B3%D9%85%20%D8%AD%D9%8A%D8%A7%D8%AA%D9%8A.mp3?alt=media&token=00faccc6-2d5d-45da-8571-22055e4c7b24";
                if (messageVar != null && !messageVar.equals("")) {
                    MessageUser mes = new MessageUser(userID, messageVar, false, "Voice");
                    DatabaseReference data = databaseReference.child(firebaseUser.getUid()).child("chat").child(userID).push();
                    data.setValue(mes);
                    DatabaseReference data1 = databaseReference.child(userID).child("chat").child(firebaseUser.getUid()).push();
                    data1.setValue(mes);
                    writeMessage_EmojiEditText_actChatRoom.setText("");
                }
*/
            }
        });
        DatabaseReference databaseReferenceMessage = databaseReference.child(firebaseUser.getUid()).child("chat").child(userID);
        databaseReferenceMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (!messageId.contains(dataSnapshot1.getKey())) {
                        messageId.add(dataSnapshot1.getKey());
                        if (dataSnapshot1.child("from").getValue() != null) {
                            String from = dataSnapshot1.child("from").getValue().toString();
                            String message = dataSnapshot1.child("message").getValue().toString();
                            String seen = dataSnapshot1.child("seen").getValue().toString();
                            String type = dataSnapshot1.child("type").getValue().toString();
                            MessageUser messageUser = new MessageUser(from, message, Boolean.valueOf(seen), type);
                            messageUserList.add(messageUser);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReferenceMessage.keepSynced(true);
    }

    private void editRecycleView() {
        mAdapter = new ChatRoomAdapter(messageUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("isonline").setValue(false);
    }

    public void emoji_button(View view) {
        if (check) {
            emojiPopup = EmojiPopup.Builder.fromRootView(view).build(writeMessage_EmojiEditText_actChatRoom);
            check = false;
        }
        if (emojiPopup != null)
            if (emojiPopup.isShowing()) { // Returns true when Popup is showing.
                emojiPopup.dismiss(); // Dismisses the Popup.
            } else {
                emojiPopup.toggle(); // Toggles visibility of the Popup.
            }
    }
}