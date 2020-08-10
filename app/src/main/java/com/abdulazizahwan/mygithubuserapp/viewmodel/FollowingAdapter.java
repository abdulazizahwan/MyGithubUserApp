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
import com.abdulazizahwan.mygithubuserapp.model.Following;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {

    private Context context;
    private ArrayList<Following> followings = new ArrayList<>();

    public FollowingAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<Following> items) {
        followings.clear();
        followings.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github_user, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        Following following = getFollowings().get(position);

        Glide.with(context)
                .load(following.getAvatar())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.imgUserAvatar);

        holder.tvUsername.setText(following.getUserName());

        String totalRepos = context.getResources().getString(R.string.total_repositories, String.valueOf(following.getRepository()));
        holder.tvRepositories.setText(totalRepos);
    }

    @Override
    public int getItemCount() {
        return followings.size();
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUsername;
        TextView tvRepositories;

        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserAvatar = itemView.findViewById(R.id.img_user);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvRepositories = itemView.findViewById(R.id.tv_repositories);
        }
    }
}
