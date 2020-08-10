package com.abdulazizahwan.mygithubuserapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulazizahwan.mygithubuserapp.R;
import com.abdulazizahwan.mygithubuserapp.model.Following;
import com.abdulazizahwan.mygithubuserapp.viewmodel.FollowingAdapter;
import com.abdulazizahwan.mygithubuserapp.viewmodel.FollowingViewModel;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {

    // Variabel untuk menerima data ARGUMEN username dan following dari Adapter
    private static final String ARG_USERNAME = "username";
    private String getArgUsername;

    private static final String ARG_FOLLOWING = "following";
    private int getArgFollowing;

    private RecyclerView recyclerViewFollowings;
    private ProgressBar progressBarFollowings;

    private FollowingAdapter followingAdapter;
    private FollowingViewModel followingViewModel;

    public FollowingFragment() {

    }

    // Memberikan dua parameter yakni username dan following untuk dapat menerima data dari Adapter
    public static FollowingFragment newInstance(String username, int following) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putInt(ARG_FOLLOWING, following);
        fragment.setArguments(args);
        return fragment;
    }

    // Mengecek kondisi jika argumennya tidak kosong,
    // Maka akan diisikan ke variabel getArgUsername dan getArgFollowing
    // Kemudian diolah di ViewModel
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArgUsername = getArguments().getString(ARG_USERNAME);
            getArgFollowing = getArguments().getInt(ARG_FOLLOWING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        showRecyclerView();

        runGetUserFollowings();
        configFollowingViewModel();
    }

    // Inisiasi komponen View
    private void initView(View view) {
        recyclerViewFollowings = view.findViewById(R.id.recyclerViewFragmentFollowing);
        progressBarFollowings = view.findViewById(R.id.progressBarFollowing);

        followingAdapter = new FollowingAdapter(getActivity());
        followingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);
    }

    private void showRecyclerView() {
        recyclerViewFollowings.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFollowings.setAdapter(followingAdapter);
    }

    // Mengecek getArgFollowing, jika nilainya nol
    // Maka tidak akan memuat getUserFollowing dari ViewModel
    // Agar ProgressBar tidak berjalan terus menerus
    private void runGetUserFollowings() {
        if (getArgFollowing != 0) {
            followingViewModel.getUserFollowings(getActivity(), getArgUsername);
            showLoading(true);
        }
    }

    private void configFollowingViewModel() {
        followingViewModel.getFollowings().observe(getActivity(), new Observer<ArrayList<Following>>() {
            @Override
            public void onChanged(ArrayList<Following> followings) {
                if (followings != null) {
                    followingAdapter.setFollowings(followings);
                    showLoading(false);
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarFollowings.setVisibility(View.VISIBLE);
        } else {
            progressBarFollowings.setVisibility(View.INVISIBLE);
        }
    }
}