package com.example.intern_project.firebase;

import android.app.Activity;
import android.util.Log;

import com.example.intern_project.utils.Constants;
import com.example.intern_project.activities.SignUpActivity;
import com.example.intern_project.models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class FirebaseClass {
    private static final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

    public static void registerUser(SignUpActivity activity, UserInfo userInfo){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener(v->{
                    activity.userRegisteredSuccess();
                })
                .addOnFailureListener(v->{
                    Log.e(activity.getClass().getName(),"Error occurred : " + v.getMessage());
                });
    }
    public static void fetchUserData(SignUpActivity activity){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener(v->{
                    UserInfo user = v.toObject(UserInfo.class);
                    activity.fillData(user);
                }).addOnFailureListener(v->{
            Log.e(activity.getClass().getName(),"Error occurred : " + v.getMessage());
        });

    }
    public static String getCurrentUserId(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = "";
        if (currentUser != null){
            currentUserId = currentUser.getUid();
        }
        return currentUserId;
//        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}
