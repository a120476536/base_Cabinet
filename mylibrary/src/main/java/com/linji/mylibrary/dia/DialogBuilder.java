/*
 * project : Linji
 * author : maxinfeng
 * class : DialogBuilder.java
 * update:2020-06-12 16:52
 * last:2020-06-12 16:52
 * Copyright © 2020 临集网络技术有限公司
 */

package com.linji.mylibrary.dia;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class DialogBuilder {

    private DialogOptions options;

    //Required
    public DialogBuilder(Context context) {
        options = new DialogOptions();
        options.context = context;
    }

    public DialogBuilder setTitle(CharSequence title) {
        options.title = title;
        return this;
    }

    public DialogBuilder setContent(CharSequence content) {
        options.content = content;
        return this;
    }

    public DialogBuilder setContentColor(int contentColor) {
        options.contentColor = contentColor;
        return this;
    }

    public DialogBuilder setContentView(View view) {
        options.view = view;
        return this;
    }

    public DialogBuilder setLeft(CharSequence left) {
        options.left = left;
        return this;
    }


    public DialogBuilder setRight(CharSequence right) {
        options.right = right;
        return this;
    }

    public DialogBuilder setLeftColor(int color) {
        options.leftStartColor = color;
        options.leftEndColor = color;
        return this;
    }

    public DialogBuilder setLeftColor(int leftStartColor, int leftEndColor) {
        options.leftStartColor = leftStartColor;
        options.leftEndColor = leftEndColor;
        return this;
    }

    public DialogBuilder setRightColor(int color) {
        options.rightStartColor = color;
        options.rightEndColor = color;
        return this;
    }

    public DialogBuilder setRightColor(int rightStartColor, int rightEndColor) {
        options.rightStartColor = rightStartColor;
        options.rightEndColor = rightEndColor;
        return this;
    }


    public DialogBuilder setCommonDialogBtnListener(CommonDialogBtnListener listener) {
        options.btnListener = listener;
        return this;
    }

    public DialogBuilder setDialogWidth(int dialogWidth) {
        options.dialogWidth = dialogWidth;
        return this;
    }

    public DialogBuilder setDialogHeight(int dialogHeight) {
        options.dialogHeight = dialogHeight;
        return this;
    }

    public DialogBuilder setCountDownTime(int countDownTime) {
        options.countDownTime = countDownTime;
        return this;
    }

    public DialogBuilder showLeftBtn(boolean show) {
        options.showLeftBt = show;
        return this;
    }

    public DialogBuilder showRightBtn(boolean show) {
        options.showRightBt = show;
        return this;
    }

    public DialogBuilder showCountDownTime(boolean isCountDownTime) {
        options.isCountDownTime = isCountDownTime;
        return this;
    }

    public DialogBuilder setDismissListener(CommonDialogDismissListener dismissListener) {
        this.options.dismissListener = dismissListener;
        return this;
    }

    public CommonDialog build() {
        return new CommonDialog(options);
    }

    class DialogOptions {
        Context context;
        CharSequence title, content, left, right;
        int contentColor, leftStartColor, leftEndColor, rightStartColor, rightEndColor;
        int dialogWidth, dialogHeight;
        int countDownTime = 60;
        boolean isCountDownTime;
        boolean showLeftBt = true;
        boolean showRightBt = true;
        View view;
        CommonDialogBtnListener btnListener;
        CommonDialogDismissListener dismissListener;
    }

    public interface CommonDialogBtnListener {
        void onLeftClick(Dialog dia, View view);

        void onRightClick(Dialog dia, View view);
    }

    public interface CommonDialogDismissListener {
        void onClick(DialogInterface flag);
    }
}
