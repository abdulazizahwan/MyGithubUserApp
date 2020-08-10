package com.abdulazizahwan.mygithubuserapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulazizahwan.mygithubuserapp.R;
import com.abdulazizahwan.mygithubuserapp.model.Follower;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    private Context context;
    private ArrayList<Follower> followers = new ArrayList<>();

    public FollowerAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Follower> items) {
        followers.clear();
        followers.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github_user, parent, false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder, int position) {
        Follower follower = getFollowers().get(position);

        Glide.with(context)
                .load(follower.getAvatar())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.imgUserAvatar);

        holder.tvUsername.setText(follower.getUserName());

        String totalRepos = context.getResources().getString(R.string.total_repositories, String.valueOf(follower.getRepository()));
        holder.tvRepositories.setText(totalRepos);
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class FollowerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUsername;
        TextView tvRepositories;

        public FollowerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserAvatar = itemView.findViewById(R.id.img_user);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvRepositories = itemView.findViewById(R.id.tv_repositories);
        }
    }
}
