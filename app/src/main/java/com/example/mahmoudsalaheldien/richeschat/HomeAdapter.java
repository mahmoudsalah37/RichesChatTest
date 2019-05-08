package com.example.mahmoudsalaheldien.richeschat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image_circleImageView_user_online_list;
        private TextView fullName_textView_user_online_list, online_textView_user_online_list;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_circleImageView_user_online_list = itemView.findViewById(R.id.image_circleImageView_user_online_list);
            fullName_textView_user_online_list = itemView.findViewById(R.id.fullName_textView_user_online_list);
            online_textView_user_online_list = itemView.findViewById(R.id.online_textView_user_online_list);
        }
    }

    private List<UsersOnline> usersOnlineList;

    public HomeAdapter(List<UsersOnline> usersOnlineList) {
        this.usersOnlineList = usersOnlineList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_online_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsersOnline usersOnline = usersOnlineList.get(position);
        holder.fullName_textView_user_online_list.setText(usersOnline.username);
        holder.online_textView_user_online_list.setText("online");
        ImageView imageView = holder.image_circleImageView_user_online_list;
        Picasso.with(imageView.getContext()).load(usersOnline.profile).placeholder(R.drawable.man).into(imageView);
    }

    @Override
    public int getItemCount() {
        return usersOnlineList.size();
    }
}

