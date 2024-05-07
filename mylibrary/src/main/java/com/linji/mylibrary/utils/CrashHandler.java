package com.linji.mylibrary.utils;

import com.blankj.utilcode.util.AppUtils;


public class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        AppUtils.relaunchApp(true);
    }
}
