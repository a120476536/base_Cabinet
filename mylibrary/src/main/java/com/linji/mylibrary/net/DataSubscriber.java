package com.linji.mylibrary.net;


import com.blankj.utilcode.util.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import rx.Subscriber;

public abstract class DataSubscriber<T extends CallModel> extends Subscriber<T> {
    private static final String TAG = "DataSubscriber";
    private IBaseView mIBaseView;
    private Boolean isShowLoading = true;

    public DataSubscriber(IBaseView iBaseView) {
        this.mIBaseView = iBaseView;
    }

    public DataSubscriber(IBaseView iBaseView, Boolean isShowLoading) {
        this.mIBaseView = iBaseView;
        this.isShowLoading = isShowLoading;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowLoading) {
            mIBaseView.showLoading();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mIBaseView.hideLoading();
        if (e instanceof ConnectException) {
            mIBaseView.showNetError();
        } else if (e instanceof SocketTimeoutException) {
            mIBaseView.showNetError();
        } else if (e instanceof HttpException) {
            //当网络返回404
            if (e instanceof HttpException) {
                mIBaseView.showNetError();
            }
        } else {
            String message = e.getMessage();
            if (null != message)
                LogUtils.e(TAG, message);
        }
        onError();
    }

    @Override
    public void onNext(T t) {
        if (t.getCode() == 200 || t.getCode() == 1001) {
            if (isShowLoading) {
                mIBaseView.hideLoading();
            }
            onDataNext(t);
            if (t.getCode() == 1001) {
                mIBaseView.showToast(t.getMsg());
            }
        } else {
            mIBaseView.hideLoading();
            mIBaseView.showToast(t.getMsg());
            onError();
        }
    }

    protected abstract void onError();


    protected abstract void onDataNext(T t);
}
