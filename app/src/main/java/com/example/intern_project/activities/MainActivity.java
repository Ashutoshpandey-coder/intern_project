package com.example.intern_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.intern_project.adapters.ListItemAdapter;
import com.example.intern_project.utils.Dialog;
import com.example.intern_project.utils.PaginationModelClass;
import com.example.intern_project.R;
import com.example.intern_project.apicall.RetrofitClient;
import com.example.intern_project.databinding.ActivityMainBinding;
import com.example.intern_project.models.ApiModelClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    ActivityMainBinding binding;
    List<ApiModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        makeApiCall();
        setUpActionBar();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void setUpActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        toolbar.setNavigationOnClickListener(v->toggleDrawer());

    }
    //toggle the drawer if open close it and if close open it
    private void toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_my_profile:
                startActivity(new Intent(MainActivity.this,SignUpActivity.class).putExtra("profile",true));
                break;
            case R.id.nav_sign_out:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Log out successfully !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                finish();
                break;
        }
        return false;
    }

    private void makeApiCall(){
        Dialog.showProgressDialog(this);
        Call<PaginationModelClass> call = RetrofitClient.getInstance().getMyApi().getApiList();
        call.enqueue(new Callback<PaginationModelClass>() {
            @Override
            public void onResponse(@NonNull Call<PaginationModelClass> call, @NonNull Response<PaginationModelClass> response) {
                if(response.isSuccessful()) {
                    Dialog.hideDialog();
                    Log.e("DATA list : ", new Gson().toJson(response.body()));
                    PaginationModelClass paginationModelClass = response.body();
                    if(paginationModelClass != null)
                        setUpRecyclerView(paginationModelClass.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaginationModelClass> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                Log.e("Error in GET call :", t.getMessage());
                Dialog.hideDialog();
            }

        });
    }
    private void setUpRecyclerView(List<ApiModelClass> data){
        list =data;
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        ListItemAdapter adapter = new ListItemAdapter(data, this, pos -> {
            data.remove(pos);
            setUpRecyclerView(data);
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}