package com.example.notification.fragment;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class ToastMessage {
    public static void showToastMessage(Activity activity, String toastMessage, String Context) {
        try {
            activity.runOnUiThread(() -> Toast.makeText(
                    activity,
                    toastMessage,
                    Toast.LENGTH_SHORT
            ));
        } catch (NullPointerException e) {
            Log.e(Context, "Can not show toast message", e);
        }
    }
}
