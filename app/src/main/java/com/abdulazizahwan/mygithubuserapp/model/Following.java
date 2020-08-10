package com.abdulazizahwan.mygithubuserapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Following implements Parcelable {

    private String name;
    private String userName;
    private String avatar;
    private int repository;
    private int followers;
    private int following;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRepository() {
        return repository;
    }

    public void setRepository(int repository) {
        this.repository = repository;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.userName);
        dest.writeString(this.avatar);
        dest.writeInt(this.repository);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
    }

    public Following() {
    }

    protected Following(Parcel in) {
        this.name = in.readString();
        this.userName = in.readString();
        this.avatar = in.readString();
        this.repository = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
    }

    public static final Creator<Following> CREATOR = new Creator<Following>() {
        @Override
        public Following createFromParcel(Parcel source) {
            return new Following(source);
        }

        @Override
        public Following[] newArray(int size) {
            return new Following[size];
        }
    };
}
