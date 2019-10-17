package com.example.codechallenge.utils;

import android.content.Context;
import android.widget.ImageView;
import com.example.codechallenge.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class DialogBuilder {
    //Dialog Builder class which includes all basic dialogs designs
    private Context context;
    private KProgressHUD dialog;

    public DialogBuilder(Context context) {
        this.context = context;
        dialog = null;
    }

    public void loadingDialog(String str) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(str)
                .setCancellable(false);
        dialog.show();
    }

    public void failedDialog(String str) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_close);
        dialog = KProgressHUD.create(context)
                .setCustomView(imageView)
                .setLabel("   " + str + "   ");
        dialog.show();
    }

    public void successDialog(String str) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_tick);
        dialog = KProgressHUD.create(context)
                .setCustomView(imageView)
                .setLabel(str);
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isDialogShowing() {
        if (dialog != null)
            return dialog.isShowing();
        return false;
    }
}
