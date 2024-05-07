package com.linji.mylibrary.utils;


import com.blankj.utilcode.util.LogUtils;

public class ButtonClickUtil {
    private static long lastClickTime;//记录最近一次点击时间
    private static long interval = 500;//间隔为1秒
    private static int lastButtonId;//存放最近一次传入的按钮id

    // 如果需要不同的间隔时间，直接调用这个方法设置所需间隔毫秒数即可
    public static void setInterval(long interval) {
        ButtonClickUtil.interval = interval;
    }

    // 不需要传入任何参数 直接在点击事件下调用此方法即可
    public static boolean isFastClick() {
        long time = System.currentTimeMillis() - lastClickTime;
        lastClickTime = System.currentTimeMillis();
        if (time < interval) {
            LogUtils.e("click", "快速点击");
            return true;
        }
        return false;
    }

    public static boolean isFastClick(long interval) {
        long time = System.currentTimeMillis() - lastClickTime;
        lastClickTime = System.currentTimeMillis();
        if (time < interval) {
            LogUtils.e("click", "快速点击");
            return true;
        }
        return false;
    }
}
