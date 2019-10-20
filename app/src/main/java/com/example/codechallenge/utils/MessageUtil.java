package fr.carmoove.carmoove.utils;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.carmoove.carmoove.R;

public class MessageUtil {

    public static void showAlertMessageET_TV(Context context, TextView tv, EditText et, String alertMessage){
        tv.setTextColor(context.getResources().getColor(R.color.themeRed));
        et.setHintTextColor(context.getResources().getColor(R.color.themeRed));
        et.setBackground(context.getResources().getDrawable(R.drawable.et_background_alert_with_border));
        et.setHint(alertMessage);
    }

    public static void hideAlertMessageET_TV(Context context, TextView tv, EditText et){
        tv.setTextColor(context.getResources().getColor(R.color.themeTextColor));
        et.setHintTextColor(context.getResources().getColor(R.color.themeTextColor));
        et.setBackground(context.getResources().getDrawable(R.drawable.et_background_normal_with_border));
    }

    public static void showToastMessage(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbarMessage(View view, String str){
        Snackbar.make(view, str, Snackbar.LENGTH_SHORT).setDuration(1500).show();
    }
}
