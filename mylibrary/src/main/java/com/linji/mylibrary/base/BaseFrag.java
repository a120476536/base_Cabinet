package com.linji.mylibrary.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linji.mylibrary.net.IBaseView;
import com.linji.mylibrary.utils.DeviceUtil;
import com.linji.mylibrary.widget.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFrag<T> extends RxFragment implements IBaseView {
    protected T mPresenter;
    protected View mRootView;
    private Dialog mLoadingDialog;
    private Unbinder unbinder;
    protected Context mContext;
    protected int page = 1;
    protected int row = 10;

    // Fragment被创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getMContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(attachLayout(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mPresenter == null) {
            mPresenter = attachPresenter();
        }
        initViews();
        initData();
        return mRootView;
    }

    protected void initData() {

    }

    @LayoutRes
    protected abstract int attachLayout();

    protected abstract void initViews();

    protected abstract T attachPresenter();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object myEvent) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }


    public Context getMContext() {
        return getContext();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.createLoadingDialog(getContext(), "正在加载中...");
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

    @SuppressLint("MissingPermission")
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
            toastTime = System.currentTimeMillis();
        }
    }

    /**
     * 刷新成功
     */
    protected void onRefreshSuccess(SmartRefreshLayout refreshLayout, RecyclerView recyclerView, List data, LinearLayout emptyLayout, BaseQuickAdapter adapter, int pageSize) {
        refreshLayout.finishRefresh();
        if (data == null || data.size() == 0) {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            }
            if (emptyLayout.getVisibility() == View.GONE) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
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

    protected void onLoadMoreSuccess(SmartRefreshLayout refreshLayout, List data, BaseQuickAdapter mAdapter, int pageSize) {
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
    public void onDestroy() {
        hideLoading();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

}
