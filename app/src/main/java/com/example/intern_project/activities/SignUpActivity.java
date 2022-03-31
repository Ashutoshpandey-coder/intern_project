package com.example.intern_project.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.intern_project.R;
import com.example.intern_project.models.UserInfo;
import com.example.intern_project.databinding.ActivitySignUpBinding;
import com.example.intern_project.firebase.FirebaseClass;
import com.example.intern_project.utils.Dialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    boolean isFromProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideBar();
        getData();
        setUpActionBar();
        clickMethods();
    }
    private void getData(){
        if(getIntent().hasExtra("profile")){
            isFromProfile = getIntent().getBooleanExtra("profile",false);
        }
        if(isFromProfile){
            binding.btnSignUp.setText("Update");
            Dialog.showProgressDialog(this);
            FirebaseClass.fetchUserData(this);
        }
    }
    private void setUpActionBar(){
        setSupportActionBar(binding.toolbarSignUpActivity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp);
        }
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
    private void clickMethods(){
        binding.btnSignUp.setOnClickListener(v->{
            if(!isFromProfile){
                registerUser();
            }else{
                Toast.makeText(this, "This part will be done later on!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.toolbarSignUpActivity.setNavigationOnClickListener(v-> onBackPressed());
    }
    public void fillData(UserInfo userInfo){
        Dialog.hideDialog();
        if(userInfo != null){
            binding.etConfirmPassword.setText(userInfo.getPassword());
            binding.etEmail.setText(userInfo.getEmail());
            binding.etPassword.setText(userInfo.getPassword());
            binding.etMobileNumber.setText(userInfo.getMobileNumber());
            binding.etAddress.setText(userInfo.getAddress());
            binding.etFirstName.setText(userInfo.getFirstName());
            binding.etLastName.setText(userInfo.getLastName());
        }
    }
    private void registerUser(){
        String firstName = Objects.requireNonNull(binding.etFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(binding.etLastName.getText()).toString();
        String mobile = Objects.requireNonNull(binding.etMobileNumber.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();
        String confirm = Objects.requireNonNull(binding.etConfirmPassword.getText()).toString();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
        String address =  Objects.requireNonNull(binding.etAddress.getText()).toString();
        if(validateForm(firstName,lastName,mobile,password,confirm,email,address)){
            Dialog.showProgressDialog(this);
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(v->{
                if(v.isSuccessful()){
                    FirebaseUser firebaseUser = v.getResult().getUser();
                    if(firebaseUser != null){
                        String registeredEmail = firebaseUser.getEmail();
                        Toast.makeText(this,"Registered successfully with us !",Toast.LENGTH_SHORT).show();
                        UserInfo userInfo = new UserInfo(firstName,lastName,email,password,mobile,address);
                        FirebaseClass.registerUser(this,userInfo);
                    }
                }else{
                    Dialog.hideDialog();
                    Toast.makeText(this,"Registration Failed !",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void userRegisteredSuccess(){
        Toast.makeText(this,"Registration Successful !",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SignInActivity.class));
    }
    private boolean validateForm(String first, String last, String mobile,String password,String confirmPass, String email, String address){
        if(first.equals("")){
            binding.etFirstName.setError("Enter first name !");
            return false;
        }else if(last.equals("")){
            binding.etLastName.setError("Enter last name !");
            return false;
        }else if(mobile.equals("") || mobile.length() != 10){
            binding.etMobileNumber.setError("Number must be of 10 digits !");
            return false;
        }else if(password.equals("")){
            binding.etPassword.setError("this field can't be empty !");
            return false;
        }else if(confirmPass.equals("")){
            binding.etConfirmPassword.setError("this field can't be empty !");
            return false;
        }else if(!confirmPass.equals(password)){
            binding.etConfirmPassword.setError("this field must be same with above pass !");
            return false;
        }
        else if(address.equals("")){
            binding.etAddress.setError("This field can't be empty !");
            return false;
        }
        return true;
    }
}