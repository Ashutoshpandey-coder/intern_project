package com.example.intern_project.utils;

import android.content.Context;
import android.widget.TextView;

import com.example.intern_project.R;

public class Dialog {
    private static android.app.Dialog mProgressDialog;


    public static void showProgressDialog(Context context){
        mProgressDialog = new android.app.Dialog(context);
        mProgressDialog.setContentView(R.layout.progress_dialog);
        mProgressDialog.show();
    }
    public static void hideDialog(){
        mProgressDialog.dismiss();
    }
}
