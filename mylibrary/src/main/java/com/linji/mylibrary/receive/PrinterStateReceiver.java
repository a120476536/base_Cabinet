package com.linji.mylibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.model.Constants;

import java.text.DecimalFormat;

public class PrinterStateReceiver extends BroadcastReceiver {
    public static final String PrinterState = "PrinterState";

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] byteResult = intent.getByteArrayExtra(PrinterState);
        String stringState = bytesToHexString(byteResult);
        LogUtils.e("打印机："+stringState);
        if (stringState == null) {
            stringState = "";
        }

        if (stringState.indexOf("FC4F4B") != -1) { //打印完成（FC 4F 4B）
//            Toast toast = Toast.makeText(context, "打印完成", Toast.LENGTH_SHORT);
//            toast.show();
        } else if (stringState.indexOf("FC6E6F") != -1) { // 打印未完成（FC 6E 6F）
//            Toast toast = Toast.makeText(context, "打印未完成", Toast.LENGTH_SHORT);
//            toast.show();
        } else if (stringState.indexOf("EF231A") != -1) { //缺纸 （EF 23 1A）
            LogUtils.e("缺纸");
            if (SPStaticUtils.getBoolean(Constants.USABLE_PRINT)) {
                ToastUtils.showShort("打印机缺纸");
            }
            SPStaticUtils.put(Constants.USABLE_PRINT, false);

        } else if (stringState.indexOf("FE2312") != -1) { //有纸（FE 23 12）
            LogUtils.e("有纸");
            SPStaticUtils.put(Constants.USABLE_PRINT, true);
        } else if (stringState.indexOf("FE2410") != -1) { //纸将尽（FE 24 10）
//            ToastUtils.showShort("打印机纸将尽");
        } else if (stringState.indexOf("FE2411") != -1) { // 纸还有（FE 24 11）

        } else if (stringState.indexOf("FE2510") != -1) { //温度正常（FE 25 10）

        } else if (stringState.indexOf("FE2511") != -1) { // 温度不正常（FE 25 11）
//            ToastUtils.showShort("温度不正常");
        } else if (stringState.indexOf("FE2610") != -1) { //切刀复位（FE 26 10）

        } else if (stringState.indexOf("FE2611") != -1) { // 切刀未复位（FE 26 11）
//            ToastUtils.showShort("切刀未复位");
        } else if (stringState.indexOf("FE2710") != -1) { //胶辊合上（FE 27 10）
//            ToastUtils.showShort("胶辊合上");
        } else if (stringState.indexOf("FE2711") != -1) { // 胶辊开着（FE 27 11）
//            ToastUtils.showShort("胶辊开着");
        } else if (stringState.indexOf("FE2810") != -1) { //纸张正常（FE 28 10）

        } else if (stringState.indexOf("FE2811") != -1) { // 卡纸（FE 28 11）
//            ToastUtils.showShort("卡纸");
        } else if (stringState.indexOf("FE2B10") != -1) { //电压正常（FE 2B 10）

        } else if (stringState.indexOf("FE2B11") != -1) { // 电压异常（FE 2B 11）
//            ToastUtils.showShort("电压异常");
        } else if (stringState.indexOf("FE2B10") != -1) { //蓝牙连接（FE 2C 10）
//            ToastUtils.showShort("蓝牙连接");
        } else if (stringState.indexOf("FE2B11") != -1) { // 蓝牙已断开（FE 2C 11）
//            ToastUtils.showShort("蓝牙已断开");
        } else if (stringState.indexOf("FE29") != -1) { //温度值：FE 29 xx xx (xx xx 温度值放大 100 倍 如返回25.2度 FE 29 D8 09  xx xx 低位在前高位在后)

            if (byteResult.length >= 4) {
                int low = byteResult[2] & 0xFF;
                int high = byteResult[3] & 0xFF;
                int result = (high << 8) | low;//先将高位左移，在与低位相与
                double value = result / 100.00;
                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                ToastUtils.showShort("温度:" + decimalFormat.format(value) + "°C");
            } else {
                ToastUtils.showShort("温度:未知");
            }

        } else if (stringState.indexOf("FE2A") != -1) { //电压值：FE 2A xx xx (xx xx 电压值放大 100 倍 如返回24.5V  FE 2A 92 09  xx xx 低位在前高位在后)

            if (byteResult.length >= 4) {
                int low = byteResult[2] & 0xFF;
                int high = byteResult[3] & 0xFF;
                int result = (high << 8) | low;//先将高位左移，在与低位相与
                double value = result / 100.00;
                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                ToastUtils.showShort("电压：" + decimalFormat.format(value) + "v");
            } else {
                ToastUtils.showShort("电压：未知");
            }
        } else if (stringState.indexOf("FEAA") != -1) { //纸张数：FE AA 01 00 (十六进制，低位在前，高位在后)
//            标签模式下连续打印，自动返回打印纸张的第几张数值
            if (byteResult.length >= 4) {
                int low = byteResult[2] & 0xFF;
                int high = byteResult[3] & 0xFF;
                int result = (high << 8) | low;//先将高位左移，在与低位相与

                Toast toast = Toast.makeText(context, "打印张数" + result, Toast.LENGTH_SHORT);
                toast.show();

                //返回了，打印完成
                if (byteResult.length == 7) {
                    Intent intentResult = new Intent();
                    byte[] bytes = new byte[3];
                    System.arraycopy(byteResult, 4, bytes, 0, bytes.length);
                    intentResult.setAction(PrinterState);
                    intentResult.putExtra(PrinterState, bytes);
                    this.onReceive(context, intentResult);
                }
            } else {
                Toast toast = Toast.makeText(context, "打印张数：", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    /**
     * Convert byte[] to hex string.将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string （大写）
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}
