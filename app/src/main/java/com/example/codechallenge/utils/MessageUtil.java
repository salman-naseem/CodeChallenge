package com.example.codechallenge.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MessageUtil {

    public static void showToastMessage(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbarMessage(View view, String str){
        Snackbar.make(view, str, Snackbar.LENGTH_SHORT).setDuration(1500).show();
    }
}
