package com.linji.mylibrary.faceHelp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    public static Toast mToast = null;

    public static void toast(final Context context, final String text) {
        cancel();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                } else {
                    mToast.setText(text);
                }
                mToast.show();
            }
        });
    }

    public static void toast(final Context context, final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public static void cancel() {
        handler.removeCallbacksAndMessages(null);
    }
}
