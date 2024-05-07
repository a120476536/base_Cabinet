/*
 * project : Linji
 * author : maxinfeng
 * class : CommonDialog.java
 * update:2020-06-12 17:35
 * last:2020-06-12 15:09
 * Copyright © 2020 临集网络技术有限公司
 */

package com.linji.mylibrary.dia;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.linji.mylibrary.R;
import com.linji.mylibrary.utils.ButtonClickUtil;
import com.linji.mylibrary.utils.DeviceUtil;
import com.linji.mylibrary.widget.BorderTextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CommonDialog extends Dialog implements View.OnClickListener {

    private DialogBuilder.DialogOptions options;
    private int count;
    private TextView mCountDownTime;
    private Timer timer;
    private Handler mHandler = new Handler(msg -> {
        if (options.countDownTime != -1) {
            if (msg.what == options.countDownTime) {
                if (timer != null) timer.cancel();
                count = options.countDownTime - 1;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(count--);
                    }
                }, 0, 1000);
            } else if (msg.what == 0) {
                timer.cancel();
                dismiss();
            } else {
                mCountDownTime.setText(String.format(Locale.CHINA, "%ds", msg.what));
            }
        }
        return false;
    });

    CommonDialog(DialogBuilder.DialogOptions options) {
        super(options.context, R.style.alert_dialog);
        this.options = options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dia_common);
        findViewById(R.id.close).setOnClickListener(v -> dismiss());
        TextView mTitle = findViewById(R.id.dialog_title);
        TextView mContent = findViewById(R.id.dialog_content);
        mCountDownTime = findViewById(R.id.count_down_time);
        BorderTextView leftBtn = findViewById(R.id.left_tv);
        BorderTextView rightBtn = findViewById(R.id.right_tv);
        LinearLayout container = findViewById(R.id.dialog_container);

        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        //设置dialog title

        if (options.title == null) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setText(options.title);
        }
        if (options.content == null) {
            mContent.setVisibility(View.GONE);
        } else {
            mContent.setText(options.content);
        }
        if (options.contentColor != 0) {
            mContent.setTextColor(options.contentColor);
        }
        if (options.leftStartColor != 0 && options.leftEndColor != 0) {
            leftBtn.setGradientColor(options.leftStartColor, options.leftEndColor, 0);
        }
        if (options.rightStartColor != 0 && options.rightEndColor != 0) {
            rightBtn.setGradientColor(options.rightStartColor, options.rightEndColor, 0);
        }
        if (options.view != null) {
            mContent.setVisibility(View.GONE);
            container.addView(options.view);
        }
        if (!StringUtils.isEmpty(options.left)){
            leftBtn.setText(options.left);
        }
        if (!StringUtils.isEmpty(options.right)){
            rightBtn.setText(options.right);
        }
        leftBtn.setVisibility(options.showLeftBt ? View.VISIBLE : View.GONE);

        rightBtn.setVisibility(options.showRightBt ? View.VISIBLE : View.GONE);

        if (options.countDownTime != -1) {
            mHandler.sendEmptyMessage(options.countDownTime);
        }
        mCountDownTime.setVisibility(options.isCountDownTime ? View.VISIBLE : View.GONE);
        if (options.dismissListener != null) {
            setOnDismissListener(dialog -> options.dismissListener.onClick(dialog));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (options.dialogWidth != 0) {
            params.width = options.dialogWidth;
        } else {
            params.width = DeviceUtil.dip2px(getContext(), 300);
        }

        if (options.dialogHeight != 0) {
            params.height = options.dialogHeight;
        }
        window.setGravity(Gravity.CENTER);
        window.setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        hideKeyboard();
        if (timer != null) {
            timer.cancel();
        }
        super.dismiss();
    }

    public void refreshTime() {
        if (options.countDownTime != -1) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessage(options.countDownTime);
        }
    }

    public void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                mInputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (ButtonClickUtil.isFastClick()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.left_tv) {
            if (options.btnListener != null) {
                options.btnListener.onLeftClick(this, v);
            }
        } else if (id == R.id.right_tv) {
            if (options.btnListener != null) {
                options.btnListener.onRightClick(this, v);
            }
            dismiss();
        }
    }
}
