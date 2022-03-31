package com.example.intern_project.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intern_project.R;
import com.example.intern_project.activities.MainActivity;
import com.example.intern_project.databinding.ActivitySignInBinding;
import com.example.intern_project.utils.Dialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideBar();
        setUpActionBar();
        onClickMethod();
    }
    private void setUpActionBar(){
        setSupportActionBar(binding.toolbarSignInActivity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp);
        }
    }
    private void onClickMethod(){
        binding.btnSignIn.setOnClickListener(v->loginUser());
        binding.toolbarSignInActivity.setNavigationOnClickListener(v-> onBackPressed());
    }
    private void loginUser(){
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
        String pass = Objects.requireNonNull(binding.etPassword.getText()).toString();

        if(validateForm(email,pass)){
            Dialog.showProgressDialog(this);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(v->{
                if(v.isSuccessful()){
                    Toast.makeText(this, "Authentication successful !", Toast.LENGTH_SHORT).show();
                    Dialog.hideDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    Log.e("Sign In failed :", Objects.requireNonNull(v.getException()).toString());
                    Dialog.hideDialog();
                }
            });
        }
    }
    private boolean validateForm(String email, String pass){
        if(email.equals("")){
            binding.etEmail.setError("This field can't be empty !");
            return false;
        }else if(!email.contains("@")){
            binding.etEmail.setError("Email must contains @ !");
            return  false;
        }else if(pass.equals("")){
            binding.etPassword.setError("This field can't be empty !");
            return false;
        }
        return true;
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