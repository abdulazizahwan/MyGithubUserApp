package com.abdulazizahwan.mygithubuserapp.viewmodel;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.abdulazizahwan.mygithubuserapp.R;
import com.abdulazizahwan.mygithubuserapp.view.fragment.FollowersFragment;
import com.abdulazizahwan.mygithubuserapp.view.fragment.FollowingFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    public String username;
    public int following;
    public int follower;

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_followers,
            R.string.tab_following,
    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = FollowersFragment.newInstance(username, follower);
                break;
            case 1:
                fragment = FollowingFragment.newInstance(username, following);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
