package com.example.mahmoudsalaheldien.richeschat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<UsersMyChat> myChatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName_textView_my_chat_list, online_textView_my_chat_list;
        private CircleImageView image_circleImageView_my_chat_list;
        public MyViewHolder(View view) {
            super(view);
            fullName_textView_my_chat_list = view.findViewById(R.id.fullName_textView_my_chat_list);
            online_textView_my_chat_list = view.findViewById(R.id.online_textView_my_chat_list);
            image_circleImageView_my_chat_list = view.findViewById(R.id.image_circleImageView_my_chat_list);
        }
    }

    public ChatAdapter(List<UsersMyChat> myChatList) {
        this.myChatList = myChatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_chat_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UsersMyChat user = myChatList.get(position);
        holder.fullName_textView_my_chat_list.setText(user.name);

        if (user.online.equals("true"))
            holder.online_textView_my_chat_list.setText("online");
        else
            holder.online_textView_my_chat_list.setText("offline");

        if (user.profile != null && !user.profile.equals("")) {
            ImageView view = holder.image_circleImageView_my_chat_list;
            Picasso.with(view.getContext()).load(user.profile).resize(50,50).placeholder(R.drawable.man).into(view);
        }
    }

    @Override
    public int getItemCount() {
        return myChatList.size();
    }

}
