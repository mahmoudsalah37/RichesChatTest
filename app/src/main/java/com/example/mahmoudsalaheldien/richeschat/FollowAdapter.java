package com.example.mahmoudsalaheldien.richeschat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {
    private List<User> followList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName_textView_followListItem, status_textView_followListItem;

        public MyViewHolder(View view) {
            super(view);
            fullName_textView_followListItem = view.findViewById(R.id.fullName_textView_followListItem);
            status_textView_followListItem = view.findViewById(R.id.status_textView_followListItem);
        }
    }

    public FollowAdapter(List<User> followList) {
        this.followList = followList;
    }

    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_list_item, parent, false);

        return new FollowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final User user = followList.get(position);

        holder.fullName_textView_followListItem.setText(user.username);
        holder.status_textView_followListItem.setText(user.status);
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

}
