package com.linji.mylibrary.base;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linji.mylibrary.dia.CommonDialog;
import com.linji.mylibrary.faceHelp.model.SingleBaseConfig;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.net.BasePresenter;
import com.linji.mylibrary.net.IBaseView;
import com.linji.mylibrary.utils.ButtonClickUtil;
import com.linji.mylibrary.utils.DeviceUtil;
import com.linji.mylibrary.utils.HandlerUtil;
import com.linji.mylibrary.widget.LoadingDialog;
import com.linji.mylibrary.widget.TextWatcherAfter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * activity基类
 */

public abstract class BaseAct<T extends BasePresenter> extends RxAppCompatActivity implements IBaseView, View.OnClickListener {
    protected T mPresenter;
    public String TAG = this.getClass().getSimpleName();
    protected Dialog mLoadingDialog;
    private Unbinder unbinder;

    public Context mContext;
    protected int page = 1;
    protected int row = 10;

    protected boolean backHome = true;

    public void setBackHome(boolean backHome) {
        this.backHome = backHome;
    }

    public Context getMContext() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(initLayout());
        unbinder = ButterKnife.bind(this);
        ActivityUtils.addActivityLifecycleCallbacks(new Utils.ActivityLifecycleCallbacks());
        mPresenter = attachPresenter();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mContext = this;
        BarUtils.setStatusBarLightMode(this, true);
        initView();
        initData();
    }

    @LayoutRes
    protected abstract int initLayout();

    protected abstract T attachPresenter();

    protected abstract void initView();

    protected abstract void setKeepScreenOn();

    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setKeepScreenOn();
        refreshBackHomeTime();
    }

    //EditText输入后重置回首页时间
    protected void addEditListenerToBackMain(CommonDialog commonDialog, EditText editText) {
        editText.addTextChangedListener(new TextWatcherAfter() {
            @Override
            public void afterTextChange(@NonNull Editable s) {
                if (commonDialog != null) {
                    commonDialog.refreshTime();
                }
                refreshBackHomeTime();
            }
        });
    }

    //触摸重置回首页时间
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            refreshBackHomeTime();
        }
        return super.onTouchEvent(event);
    }

    //点击按钮重置回首页时间
    @Override
    public void onClick(View v) {
        if (ButtonClickUtil.isFastClick()) {
            return;
        }
        refreshBackHomeTime();
        onViewClick(v);
    }

    protected void onViewClick(View v) {

    }

    /**
     * 回到首页
     */
    public void refreshBackHomeTime() {
        HandlerUtil.getInstance().removeCallbacksAndMessages(null);
        if (backHome) {
            backHome();
        }
    }

    protected abstract void backHome();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object myEvent) {

    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void showLoading() {
        showLoading("正在加载中...");
    }

    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.createLoadingDialog(getMContext(), msg);
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void showNetError() {
        if (DeviceUtil.isNetworkAvailable(mContext)) {
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

    protected void setCameraOrientation() {
        String cameraManufacturer = SPStaticUtils.getString(Constants.CAMERA_MANUFACTURER);
        if (cameraManufacturer.contains("GH") || cameraManufacturer.contains("icSpring")) {
            SingleBaseConfig.getBaseConfig().setRgbVideoDirection(270);
            SingleBaseConfig.getBaseConfig().setRgbDetectDirection(270);
        } else if (cameraManufacturer.contains("Sonix") || cameraManufacturer.contains("Microphone")) {
            SingleBaseConfig.getBaseConfig().setRgbVideoDirection(0);
            SingleBaseConfig.getBaseConfig().setRgbDetectDirection(0);
        } else {
            SingleBaseConfig.getBaseConfig().setRgbVideoDirection(180);
            SingleBaseConfig.getBaseConfig().setRgbDetectDirection(180);
        }
    }

    /**
     * 刷新成功
     */
    protected void refreshSuccess(SmartRefreshLayout refreshLayout, RecyclerView recyclerView, List data, LinearLayout emptyLayout, BaseQuickAdapter adapter, int pageSize) {
        refreshLayout.finishRefresh();
        if (data == null || data.size() == 0) {
            if (recyclerView.getVisibility() == View.VISIBLE) recyclerView.setVisibility(View.GONE);
            if (emptyLayout.getVisibility() == View.GONE) emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (recyclerView.getVisibility() == View.GONE) recyclerView.setVisibility(View.VISIBLE);
        if (emptyLayout.getVisibility() == View.VISIBLE) emptyLayout.setVisibility(View.GONE);
        adapter.getData().clear();
        adapter.setNewData(data);
        if (data.size() < pageSize) {
            adapter.setEnableLoadMore(false);
        } else {
            adapter.setEnableLoadMore(true);
        }
    }

    protected void loadMoreSuccess(SmartRefreshLayout refreshLayout, List data, BaseQuickAdapter mAdapter, int pageSize) {
        mAdapter.loadMoreComplete();
        if (data == null) {
            mAdapter.loadMoreEnd();
            return;
        }
        refreshLayout.setEnableRefresh(true);
        mAdapter.addData(data);
        if (data.size() < pageSize) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.setEnableLoadMore(true);
        }
    }


    @Override
    protected void onDestroy() {
        hideLoading();
        ActivityUtils.removeActivityLifecycleCallbacks(this);
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
