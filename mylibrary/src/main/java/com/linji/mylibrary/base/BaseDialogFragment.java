/*
 * project : Linji
 * author : maxinfeng
 * class : BaseDialogFragment.java
 * update:2020-06-03 18:21
 * last:2020-06-03 18:21
 * Copyright © 2020 临集网络技术有限公司
 */
package com.linji.mylibrary.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.R;
import com.linji.mylibrary.net.IBaseView;
import com.linji.mylibrary.utils.DeviceUtil;
import com.linji.mylibrary.widget.LoadingDialog;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseDialogFragment<T>  extends RxDialogFragment implements IBaseView {
    protected View rootView = null;
    protected int gravity = Gravity.CENTER;
    protected CommonClickListener listener;
    private Dialog mLoadingDialog;
    protected T mPresenter;
    private Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alert_dialog);
    }

    public void setListener(CommonClickListener listener) {
        this.listener = listener;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            rootView = inflater.inflate(getLayoutId(), null);
            unbinder = ButterKnife.bind(this, rootView);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mPresenter == null) {
            mPresenter = attachPresenter();
        }
        initView(savedInstanceState);
        initData(savedInstanceState);
        return rootView;
    }

    protected abstract int getLayoutId();
    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void initData(Bundle savedInstanceState);
    protected abstract T attachPresenter();
    protected <T extends View> T findViewById(@IdRes int id){
        return rootView.findViewById(id);
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity =gravity;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object myEvent) {

    }
    @Override
    public void showNetError() {
        if (DeviceUtil.isNetworkAvailable(getContext())) {
           showToast("网络异常，稍后重试");
        } else {
            showToast("网络不可用，请查看网络连接");
        }
    }


    long toastTime = 0;

    public void showToast(String toast) {
        if (System.currentTimeMillis() - toastTime > 2000) {
            ToastUtils.showShort(toast);
        }
        toastTime = System.currentTimeMillis();
    }

    /**
     * 网络加载弹窗，耗时加载弹窗
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.createLoadingDialog(getContext(), "正在加载中...");
            mLoadingDialog.show();
        } else {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        }
    }
    /**
     * 关闭加载弹窗
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
