package com.linji.mylibrary.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.cosmoplat.czapi.MyKAManager;
import com.dewod.sdk.DwHome;
import com.dewod.sdk.DwPower;
import com.dewod.sdk.DwSystemUi;
import com.linji.mylibrary.model.Constants;
import com.lztek.toolkit.Lztek;

import java.io.FileWriter;
import java.io.IOException;

public class SystemUtil {

    /**
     * 显示导航栏
     *
     * @param context
     */
    public static void showNavigationBar(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek lztek = Lztek.create(context);
                lztek.showNavigationBar();
                break;
            case "2"://卡奥斯
                MyKAManager myKAManager = MyKAManager.getInstance(context);
                myKAManager.hideNavBar(false);
                break;
            case "5"://德沃
                DwSystemUi dwSystemUi = DwSystemUi.getInstance(context);
                dwSystemUi.switchStatusBarAndNavigationOverwrite(true);
                break;
            case "6"://润泽
                context.sendBroadcast(new Intent("com.rz.action.show_nav_bar"));
                break;
        }
    }

    /**
     * 隐藏导航栏
     *
     * @param context
     */
    public static void hideNavigationBar(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek lztek = Lztek.create(context);
                lztek.hideNavigationBar();
                break;
            case "2"://卡奥斯
                MyKAManager myKAManager = MyKAManager.getInstance(context);
                myKAManager.hideNavBar(true);
                break;
            case "5"://德沃
                DwSystemUi dwSystemUi = DwSystemUi.getInstance(context);
                dwSystemUi.switchStatusBarAndNavigationOverwrite(false);
                break;
            case "6"://润泽
                context.sendBroadcast(new Intent("com.rz.action.hide_nav_bar"));
                context.sendBroadcast(new Intent("com.rz.action.hide_status_bar"));
                break;
        }
    }

    /**
     * 硬件开始重启
     *
     * @param context
     */
    public static void hardReboot(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek lztek = Lztek.create(context);
                lztek.hardReboot();
                break;
            case "2"://卡奥斯
                MyKAManager myKAManager = MyKAManager.getInstance(context);
                myKAManager.reboot();
                break;
            case "5"://德沃
                DwPower.getInstance(context).reboot();
                break;
            case "6"://润泽
                break;
        }
    }

    /**
     * 软件开始重启
     *
     * @param context
     */
    public static void softReboot(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek lztek = Lztek.create(context);
                lztek.softReboot();
                break;
            case "2"://卡奥斯
                MyKAManager myKAManager = MyKAManager.getInstance(context);
                myKAManager.reboot();
                break;
            case "5"://德沃
                DwPower.getInstance(context).reboot();
                break;
            case "6"://润泽
                context.sendBroadcast(new Intent("com.rz.reboot"));
                break;
        }
    }

    /**
     * 开机启动
     *
     * @param context
     */
    public static void bootSetup(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Intent intent = new Intent("com.lztek.tools.action.BOOT_SETUP");
                intent.putExtra("packageName", context.getPackageName());
                //intent.putExtra("delaySeconds", 5); // 开机启动完成 5 秒后运行指定 APK
                intent.setPackage("com.lztek.bootmaster.autoboot7"); // android 8以上必须
                context.sendBroadcast(intent);
                break;
            case "2"://卡奥斯
//                context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
                break;
            case "5"://德沃
//                DwHome.getInstance(context).setHomePackage(context.getPackageName());
                break;
            case "6"://润泽
                Intent it = new Intent();
                it.setAction("brc_start_on_boot");
                it.setPackage("com.android.launcher3");
                it.putExtra("pkg", context.getPackageName()); // 启动app的包名
                it.putExtra("cls", "StartAct"); // 启动app的类名
                context.sendBroadcast(it);
                break;
        }
    }

    /**
     * 开启应用守护
     *
     * @param context
     */
    public static void keepAliveSetup(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        Intent intent = new Intent();
        switch (deviceModel) {
            case "1"://亮钻
                intent.setAction("com.lztek.tools.action.KEEPALIVE_SETUP");
                intent.putExtra("packageName", context.getPackageName());
//        intent.putExtra("delaySeconds", 5); // 应用退出后 5 秒重新启动
//        intent.putExtra("foreground", true); // 应用可后台运行，进程退出后才重新打开
                intent.setPackage("com.lztek.bootmaster.autoboot7"); // android 8以上必须
                context.sendBroadcast(intent);
                break;
            case "2"://卡奥斯
                intent.setAction("ka.intent.system.KEEP_ALIVE");
                intent.putExtra("isOpen", true);
                intent.putExtra("packageName", context.getPackageName());
                context.sendBroadcast(intent);
                break;
            case "5"://德沃
                DwHome.getInstance(context).setHomePackage(context.getPackageName());
                break;

        }

    }

    /**
     * 关闭应用守护
     *
     * @param context
     */
    public static void closeAliveSetup(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        Intent intent = new Intent();
        switch (deviceModel) {
            case "1"://亮钻
                intent.setAction("com.lztek.tools.action.KEEPALIVE_UNSET_ALL");
                intent.putExtra("packageName", context.getPackageName());
//        intent.putExtra("delaySeconds", 5); // 应用退出后 5 秒重新启动
//        intent.putExtra("foreground", true); // 应用可后台运行，进程退出后才重新打开
                intent.setPackage("com.lztek.bootmaster.autoboot7"); // android 8以上必须
                intent.setPackage("com.lztek.bootmaster.poweralarm7");
                Log.d("[BOOT_MASTER]", "发送取消应用守护广播");
                context.sendBroadcast(intent);
                break;
            case "2"://卡奥斯
                intent.setAction("ka.intent.system.KEEP_ALIVE");
                intent.putExtra("isOpen", false);
                intent.putExtra("packageName", context.getPackageName());
                context.sendBroadcast(intent);
                break;
            case "5"://德沃
//                DwHome.getInstance(context).startRawLauncher();
                break;
        }
    }

    public static final int light_port1 = 36;
    public static final int light_port2 = 363;

    public static void openLight(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        if (deviceModel.equals("1")) {//亮钻
            Lztek lztek = Lztek.create(context);
            if (lztek.gpioEnable(light_port1)) {
                lztek.setGpioOutputMode(light_port1);
                lztek.setGpioValue(light_port1, 1);
            }
            if (lztek.gpioEnable(light_port2)) {
                lztek.setGpioOutputMode(light_port2);
                lztek.setGpioValue(light_port2, 1);
            }
        } else if (deviceModel.equals("6")) {
            FileWriter fw = null;
            final String filename = "/sys/devices/platform/gpio_ioctl.0/gpio_ioctl";
            try {
                fw = new FileWriter(filename, false);
                fw.write("0x131"); //nn=13代表GPIOB3;v=0对应GPIO输出低，v=1对应GPIO输出高
                fw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeLight(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        if (deviceModel.equals("1")) {//亮钻
            Lztek lztek = Lztek.create(context);
            if (lztek.gpioEnable(light_port1)) {
                lztek.setGpioOutputMode(light_port1);
                lztek.setGpioValue(light_port1, 0);
            }
            if (lztek.gpioEnable(light_port2)) {
                lztek.setGpioOutputMode(light_port2);
                lztek.setGpioValue(light_port2, 0);
            }
        } else if (deviceModel.equals("6")) {
            FileWriter fw = null;
            final String filename = "/sys/devices/platform/gpio_ioctl.0/gpio_ioctl";
            try {
                fw = new FileWriter(filename, false);
                fw.write("0x130"); //nn=13代表GPIOB3;v=0对应GPIO输出低，v=1对应GPIO输出高
                fw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void setResetScreenTime(Context context) {
        String resetScreenTime = SPStaticUtils.getString(Constants.RESTING_SCREEN_TIME, "永不");
        if (resetScreenTime.equals("永不")) {
            screenLight(context);
        } else {
            String time = resetScreenTime.split("分")[0];
            int resetTime = Integer.parseInt(time) * 60 * 1000;
            SystemUtil.resetScreen(context, resetTime);
        }
    }

    /**
     * 屏幕常亮
     *
     * @param context
     */
    public static void screenLight(Context context) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek.create(context).suExec("settings put global stay_on_while_plugged_in 1");
                Lztek.create(context).suExec("settings put system screen_off_timeout 0");
                break;
            case "2"://卡奥斯
                MyKAManager manager = MyKAManager.getInstance(context);
                manager.setDormantInterval(2147483647);
                break;
            case "5"://德沃
                DwPower.getInstance(context).setScreenTimeout(context, 2147483647);
                break;
            case "6"://润泽
                ShellUtils.execCmd("settings put system screen_off_timeout 2147483647", true);
                break;
        }
    }

    /**
     * 屏幕休眠时间
     *
     * @param context
     */
    public static void resetScreen(Context context, int time) {
        String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "0");
        switch (deviceModel) {
            case "1"://亮钻
                Lztek.create(context).suExec("settings put global stay_on_while_plugged_in 0");
                Lztek.create(context).suExec("settings put system screen_off_timeout " + time);
                break;
            case "2"://卡奥斯
                MyKAManager manager = MyKAManager.getInstance(context);
                manager.setDormantInterval(time);
                break;
            case "5"://德沃
                DwPower.getInstance(context).setScreenTimeout(context, time);
                break;
            case "6"://润泽
                ShellUtils.execCmd("settings put system screen_off_timeout " + time, true);
                break;
        }
    }
}
