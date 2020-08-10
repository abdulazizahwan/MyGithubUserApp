package com.abdulazizahwan.mygithubuserapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abdulazizahwan.mygithubuserapp.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserViewModel extends ViewModel {
    private ArrayList<User> listUsersNonMutable = new ArrayList<>();
    private MutableLiveData<ArrayList<User>> listUsersMutable = new MutableLiveData<>();

    public void getGithubUsers(final Context context) {
        String baseUrl = "https://api.github.com/users";

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
                        getUserDetail(username, context);
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

    public void getUserDetail(final String username, final Context context) {
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
                    User user = new User();
                    user.setUserName(jsonObject.getString("login"));
                    user.setName(jsonObject.getString("name"));
                    user.setAvatar(jsonObject.getString("avatar_url"));
                    user.setRepository(jsonObject.getInt("public_repos"));
                    user.setFollowers(jsonObject.getInt("followers"));
                    user.setFollowing(jsonObject.getInt("following"));
                    listUsersNonMutable.add(user);
                    listUsersMutable.postValue(listUsersNonMutable);
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

    public void getSearchUsers(String query, final Context context) {
        String baseSearchUsersUrl = "https://api.github.com/search/users?q=" + query;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token REPLACE_WITH_YOUR_OWN_GITHUB_TOKEN");
        client.addHeader("User-Agent", "request");
        client.get(baseSearchUsersUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("onSuccess", result);

                try {
                    listUsersNonMutable.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray items = jsonObject.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String username = itemObject.getString("login");
                        getUserDetail(username, context);
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

    public LiveData<ArrayList<User>> getUsers() {
        return listUsersMutable;
    }
}
