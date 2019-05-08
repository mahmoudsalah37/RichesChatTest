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

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {

    private List<User> peopleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName_textView_peopleListItem, status_textView_peopleListItem;
        private CircleImageView image_circleImageView_peopleListItem;

        public MyViewHolder(View view) {
            super(view);
            fullName_textView_peopleListItem = view.findViewById(R.id.fullName_textView_peopleListItem);
            status_textView_peopleListItem = view.findViewById(R.id.status_textView_peopleListItem);
            image_circleImageView_peopleListItem = view.findViewById(R.id.image_circleImageView_peopleListItem);
        }
    }

    public PeopleAdapter(List<User> peopleList) {
        this.peopleList = peopleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = peopleList.get(position);
        holder.fullName_textView_peopleListItem.setText(user.username);
        holder.status_textView_peopleListItem.setText(user.status);
        ImageView view = holder.image_circleImageView_peopleListItem;
        if (user.profile != null && !user.profile.equals(""))
            Picasso.with(view.getContext()).load(user.profile).resize(50,50).placeholder(R.drawable.man).into(view);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

}
