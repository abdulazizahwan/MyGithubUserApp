package com.abdulazizahwan.mygithubuserapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abdulazizahwan.mygithubuserapp.model.Follower;
import com.abdulazizahwan.mygithubuserapp.model.Following;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowingViewModel extends ViewModel {
    private ArrayList<Following> listFollowingNonMutable = new ArrayList<>();
    private MutableLiveData<ArrayList<Following>> listFollowingMutable =new MutableLiveData<>();


    public void getUserFollowings(final Context context, String username) {
        String baseUrl = "https://api.github.com/users/" + username + "/following";

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
                    Following following = new Following();
                    following.setUserName(jsonObject.getString("login"));
                    following.setName(jsonObject.getString("name"));
                    following.setAvatar(jsonObject.getString("avatar_url"));
                    following.setRepository(jsonObject.getInt("public_repos"));
                    following.setFollowers(jsonObject.getInt("followers"));
                    following.setFollowing(jsonObject.getInt("following"));
                    listFollowingNonMutable.add(following);
                    listFollowingMutable.postValue(listFollowingNonMutable);
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

    public LiveData<ArrayList<Following>> getFollowings(){
        return listFollowingMutable;
    }
}
