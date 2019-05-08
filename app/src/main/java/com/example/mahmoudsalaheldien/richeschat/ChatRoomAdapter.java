package com.example.mahmoudsalaheldien.richeschat;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vanniktech.emoji.EmojiTextView;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {
    final private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private List<MessageUser> listMessageUser;
    private final MediaPlayer player = new MediaPlayer();

    ChatRoomAdapter(List listMessage) {
        this.listMessageUser = listMessage;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private EmojiTextView message_EmojiTextView_mes;
        private LinearLayout mess_linear_message;
        private ImageButton buttonVoice_MessVoice;
        private SeekBar seekBar_MessVoice;

        MyViewHolder(View view) {
            super(view);
            message_EmojiTextView_mes = view.findViewById(R.id.message_EmojiTextView_mes);
            mess_linear_message = view.findViewById(R.id.mess_linear_message);
            buttonVoice_MessVoice = view.findViewById(R.id.buttonVoice_MessVoice);
            seekBar_MessVoice = view.findViewById(R.id.seekBar_MessVoice);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MessageUser messageUser = listMessageUser.get(position);
        if (messageUser.type.equals("TEXT")) {
            holder.message_EmojiTextView_mes.setText(messageUser.message);
        } else {
            holder.buttonVoice_MessVoice.setVisibility(View.VISIBLE);
            holder.message_EmojiTextView_mes.setVisibility(View.GONE);
            holder.buttonVoice_MessVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    try {
                        holder.buttonVoice_MessVoice.setClickable(false);
                        player.reset();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(messageUser.message);
                        player.prepare();
                        player.start();
                        holder.buttonVoice_MessVoice.setImageResource(R.drawable.pause);
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                holder.buttonVoice_MessVoice.setImageResource(R.drawable.play);
                                holder.buttonVoice_MessVoice.setClickable(true);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (!firebaseUser.getUid().equals(messageUser.from))
            holder.mess_linear_message.setBackgroundResource(R.color.colorPrimary);
        else {
            holder.mess_linear_message.setBackgroundResource(R.color.black_overlay);
        }
    }

    @Override
    public int getItemCount() {
        return listMessageUser.size();
    }


}
