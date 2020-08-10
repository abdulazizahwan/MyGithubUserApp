package com.abdulazizahwan.mygithubuserapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abdulazizahwan.mygithubuserapp.model.Follower;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowerViewModel extends ViewModel {
    private ArrayList<Follower> listFollowersNonMutable = new ArrayList<>();
    private MutableLiveData<ArrayList<Follower>> listFollowersMutable = new MutableLiveData<>();

    public void getUserFollowers(final Context context, String username) {
        String baseUrl = "https://api.github.com/users/" + username + "/followers";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token REPLACE_WITH_YOUR_OWN_GITHUB_TOKEN");
        client.addHeader("User-Agent", "request");
        client.get(baseUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("Result", result);
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("login");
                        getUserFollowersDetail(username, context);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void getUserFollowersDetail(final String username, final Context context) {
        String baseUserDetailUrl = "https://api.github.com/users/" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token REPLACE_WITH_YOUR_OWN_GITHUB_TOKEN");
        client.addHeader("User-Agent", "request");
        client.get(baseUserDetailUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("Result", result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Follower follower = new Follower();
                    follower.setUserName(jsonObject.getString("login"));
                    follower.setName(jsonObject.getString("name"));
                    follower.setAvatar(jsonObject.getString("avatar_url"));
                    follower.setRepository(jsonObject.getInt("public_repos"));
                    follower.setFollowers(jsonObject.getInt("followers"));
                    follower.setFollowing(jsonObject.getInt("following"));
                    listFollowersNonMutable.add(follower);
                    listFollowersMutable.postValue(listFollowersNonMutable);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Follower>> getFollowers() {
        return listFollowersMutable;
    }
}
