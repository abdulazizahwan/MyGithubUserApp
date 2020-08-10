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
import com.abdulazizahwan.mygithubuserapp.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private ArrayList<User> users = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public UserAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> items) {
        users.clear();
        users.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_github_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        User user = getUsers().get(position);

        Glide.with(context)
                .load(user.getAvatar())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.imgUserAvatar);

        holder.tvUsername.setText(user.getUserName());

        String totalRepos = context.getResources().getString(R.string.total_repositories, String.valueOf(user.getRepository()));
        holder.tvRepositories.setText(totalRepos);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(users.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUsername;
        TextView tvRepositories;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserAvatar = itemView.findViewById(R.id.img_user);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvRepositories = itemView.findViewById(R.id.tv_repositories);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
