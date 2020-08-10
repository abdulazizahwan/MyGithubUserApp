package com.abdulazizahwan.mygithubuserapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulazizahwan.mygithubuserapp.R;
import com.abdulazizahwan.mygithubuserapp.model.Follower;
import com.abdulazizahwan.mygithubuserapp.viewmodel.FollowerAdapter;
import com.abdulazizahwan.mygithubuserapp.viewmodel.FollowerViewModel;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    // Variabel untuk menerima data ARGUMEN username dan follower dari Adapter
    private static final String ARG_USERNAME = "username";
    private String getArgUsername;

    private static final String ARG_FOLLOWER = "follower";
    private int getArgFollower;

    private RecyclerView recyclerViewFollowers;
    private ProgressBar progressBarFollowers;

    private FollowerAdapter followerAdapter;
    private FollowerViewModel followerViewModel;

    public FollowersFragment() {
        // Required empty public constructor
    }

    // Memberikan dua parameter yakni username dan follower untuk dapat menerima data dari Adapter
    public static FollowersFragment newInstance(String username, int follower) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putInt(ARG_FOLLOWER, follower);
        fragment.setArguments(args);
        return fragment;
    }

    // Mengecek kondisi jika argumennya tidak kosong,
    // Maka akan diisikan ke variabel getArgUsername dan getArgFollower
    // Kemudian diolah di ViewModel
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArgUsername = getArguments().getString(ARG_USERNAME);
            getArgFollower = getArguments().getInt(ARG_FOLLOWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("onReceived...", getArgUsername);

        initView(view);
        showRecyclerView();

        runGetUserFollowers();
        configFollowerViewModel();
    }

    // Inisiasi komponen View
    private void initView(View view) {
        recyclerViewFollowers = view.findViewById(R.id.recyclerViewFragmentFollowers);
        progressBarFollowers = view.findViewById(R.id.progressBarFollowers);

        followerAdapter = new FollowerAdapter(getActivity());
        followerViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel.class);
    }

    private void showRecyclerView() {
        recyclerViewFollowers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFollowers.setAdapter(followerAdapter);
    }

    // Mengecek getArgFollower, jika nilainya nol
    // Maka tidak akan memuat getUserFollowers dari ViewModel
    // Agar ProgressBar tidak berjalan terus menerus
    private void runGetUserFollowers() {
        if (getArgFollower != 0) {
            followerViewModel.getUserFollowers(getActivity(), getArgUsername);
            showLoading(true);
        }
    }

    private void configFollowerViewModel() {
        followerViewModel.getFollowers().observe(getActivity(), new Observer<ArrayList<Follower>>() {
            @Override
            public void onChanged(ArrayList<Follower> followers) {
                if (followers != null) {
                    followerAdapter.setFollowers(followers);
                    showLoading(false);
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarFollowers.setVisibility(View.VISIBLE);
        } else {
            progressBarFollowers.setVisibility(View.INVISIBLE);
        }
    }
}