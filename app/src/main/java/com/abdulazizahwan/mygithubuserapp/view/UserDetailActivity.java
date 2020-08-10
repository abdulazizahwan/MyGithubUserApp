package com.abdulazizahwan.mygithubuserapp.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.abdulazizahwan.mygithubuserapp.R;
import com.abdulazizahwan.mygithubuserapp.model.User;
import com.abdulazizahwan.mygithubuserapp.viewmodel.SectionsPagerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView imgUserPhoto;
    private TextView tvName;
    private TextView tvUserName;
    private TextView tvTotalRepositories;
    private TextView tvTotalFollowers;
    private TextView tvTotalFollowing;

    private NestedScrollView nestedScrollView;
    private ViewPager viewPager;
    private TabLayout tabs;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initView();

        User user = getIntent().getParcelableExtra("EXTRA_USER");

        setUserDetail(user);

        setUpTabLayout(user);
    }

    // Inisiasi komponen View
    private void initView() {
        imgUserPhoto = findViewById(R.id.img_user_detail);
        tvName = findViewById(R.id.tv_name);
        tvUserName = findViewById(R.id.tv_username);
        tvTotalRepositories = findViewById(R.id.tv_repository);
        tvTotalFollowers = findViewById(R.id.tv_followers);
        tvTotalFollowing = findViewById(R.id.tv_following);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
    }

    // Setting komponen yang ada di Detail sesuai data yang dibawa Parcel
    private void setUserDetail(User user) {
        String userName = getResources().getString(R.string.formatted_username, user.getUserName());

        Glide.with(this).load(user.getAvatar()).into(imgUserPhoto);
        tvName.setText(user.getName());
        tvUserName.setText(userName);

        // Data Repositories, Followers, dan Following berupa integer,
        // Maka perlu di parse ke String
        tvTotalRepositories.setText(String.valueOf(user.getRepository()));
        tvTotalFollowers.setText(String.valueOf(user.getFollowers()));
        tvTotalFollowing.setText(String.valueOf(user.getFollowing()));
    }

    // Mengatur TabLayout dan ViewPager
    private void setUpTabLayout(User user) {
        // Agar bisa di swipe kanan kiri
        nestedScrollView.setFillViewport(true);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Mengirimkan username, following dan follower ke ViewPager adapter
        // Username akan diproses ke masing-masing fragment
        // Following akan di proses di FollowingFragment dan Follower akan di proses di FollowerFragment,
        // Jika nilainya 0 maka fungsi getUserFollowing tidak akan dijalankan
        sectionsPagerAdapter.username = user.getUserName();
        sectionsPagerAdapter.following = user.getFollowing();
        sectionsPagerAdapter.follower = user.getFollowers();
        Log.d("Sending data...", sectionsPagerAdapter.username);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }
}
