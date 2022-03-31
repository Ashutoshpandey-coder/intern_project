package com.example.intern_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.intern_project.R;
import com.example.intern_project.databinding.ActivitySplashBinding;
import com.example.intern_project.firebase.FirebaseClass;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideBar();
        String currentUserId = FirebaseClass.getCurrentUserId();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!currentUserId.equals("")){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
                }
                finish();
            }
        },2500);
    }
    private void hideBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        }else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

}