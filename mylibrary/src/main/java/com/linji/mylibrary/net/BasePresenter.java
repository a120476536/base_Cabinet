package com.linji.mylibrary.net;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 基类presenter方法
 */
public abstract class BasePresenter<I extends IBaseView> {

    protected I iBaseView;

    public BasePresenter(I iBaseView) {
        this.iBaseView = iBaseView;
    }

    /**
     * 获取数据后更新UI
     *
     * @param isRefresh 是否是刷新
     */
    public void getData(boolean isRefresh) {

    }

    /**
     * 获取更多数据
     */
    public void getMoreData() {

    }


    /**
     * 切换线程
     *
     * @param <T>
     * @return
     */
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(iBaseView.<T>bindToLife());
            }
        };
    }

    /**
     * 获取Apiservice
     *
     * @return
     */
    protected ApiService getApiService() {
        return RetrofitHelper.getInstance().getService();
    }

    protected ApiService getApiService(String url){
        return RetrofitHelper.getInstance(url).getService();
    }
}
