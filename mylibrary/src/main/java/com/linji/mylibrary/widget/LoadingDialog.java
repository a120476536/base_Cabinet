package com.linji.mylibrary.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.linji.mylibrary.R;


/**
 * @author qingf
 * @date: 2016/6/8.
 * @desc: 加载等待框
 */
public class LoadingDialog {

    /**
     * 构造对话框，准备等数据
     *
     * @param context 上下文对象
     * @param msg     // 提示信息
     * @return dialog
     */
    public static Dialog createLoadingDialog(Context context, String msg) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        LinearLayout layout = view.findViewById(R.id.dialog_view);
        ImageView img = view.findViewById(R.id.img);
        TextView tip = view.findViewById(R.id.tip);
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.dialog_img_rotate);
        img.startAnimation(animation);
        tip.setText(msg);
        try {
            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
            Window window = loadingDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            return loadingDialog;
        } catch (Throwable throwable) {
            LogUtils.e(throwable.getMessage());
            return null;
        }
    }

}
