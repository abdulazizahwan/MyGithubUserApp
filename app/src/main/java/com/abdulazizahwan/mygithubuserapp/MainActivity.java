package com.abdulazizahwan.mygithubuserapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulazizahwan.mygithubuserapp.model.User;
import com.abdulazizahwan.mygithubuserapp.view.UserDetailActivity;
import com.abdulazizahwan.mygithubuserapp.viewmodel.UserAdapter;
import com.abdulazizahwan.mygithubuserapp.viewmodel.UserViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private RecyclerView recyclerViewGithubUser;

    private ProgressBar progressBar;

    private UserViewModel userViewModel;

    private ArrayList<User> listUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        showRecyclerView();

        runGetUsers();
        configMainViewModel();
    }

    // Inisiasi View
    private void initView(){
        recyclerViewGithubUser = findViewById(R.id.recyclerViewGithubUser);
        progressBar = findViewById(R.id.progressBar);
        userAdapter = new UserAdapter(this);

        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
    }

    // Menampilkan RecyclerView
    public void showRecyclerView() {
        recyclerViewGithubUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGithubUser.setAdapter(userAdapter);

        userAdapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                showSelectedUser(data);
            }
        });
    }

    // Menampilkan User yang dipilih => Berpindah ke Detail Activity
    private void showSelectedUser(User user) {
        String selectToast = getString(R.string.select_toast, user.getUserName());
        Toast.makeText(this, selectToast, Toast.LENGTH_SHORT).show();
        Intent userDetail = new Intent(MainActivity.this, UserDetailActivity.class);

        userDetail.putExtra("EXTRA_USER", user);
        startActivity(userDetail);
    }

    // Menampilkan random User di awal
    private void runGetUsers() {
        userViewModel.getGithubUsers(getApplicationContext());
        showLoading(true);
    }

    // Configurasi ViewModel
    private void configMainViewModel() {
        userViewModel.getUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    userAdapter.setUsers(users);
                    showLoading(false);
                }
            }
        });
    }

    // Menampilkan Loading
    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    // Menampilkan menu search dan locale
    // Menampilkan user sesuai query yang diinputkan
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String searchToast = getString(R.string.search_toast, query);
                    if (!(TextUtils.isEmpty(query))) {
                        listUsers.clear();
                        showLoading(true);
                        Toast.makeText(getApplicationContext(), searchToast, Toast.LENGTH_SHORT).show();
                        userViewModel.getSearchUsers(query, getApplicationContext());
                    } else {
                        showLoading(false);
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    // Intent ke pengaturan bahasa
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_change_settings){
            Intent changeLanguageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeLanguageIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
