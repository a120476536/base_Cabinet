package com.linji.mylibrary.net;

import com.trello.rxlifecycle.LifecycleTransformer;

public interface IBaseView {


    /**
     * 显示网络错误
     */
    void showNetError();

//    /**
//     * 登出
//     */
//    void onLoginOut();
//
//
//    /**
//     * 登录失效
//     */
//    void goToReLogin();

    /**
     * 显示加载弹窗
     */
    void showLoading();

    void hideLoading();
    /**
     * 弹出toast提示
     *
     * @param toastStr
     */
    void showToast(String toastStr);


    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */

    <T> LifecycleTransformer<T> bindToLife();

}
