package com.linji.mylibrary.serial.manage;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.model.EventMsg;
import com.linji.mylibrary.serial.ByteUtilKt;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import android_serialport_api.SerialPortManager;

public class SerialTTLManage {
    public String step;
    public String pressCount;
    private String callback = "";
    public boolean isFinger = false;
    public static boolean repeatFingerRegister = false;

    public static int recognitionNum = 3;
    private int id;
    public static SerialTtlPortUtil serialPort;

    public static TtlResultListener ttlResultListener;

    public void setTtlResultListener(TtlResultListener ttlResultListener) {
        this.ttlResultListener = ttlResultListener;
    }

    public SerialTTLManage() {
        if (serialPort == null) {
            serialPort = SerialTtlPortUtil.getInstance();
        }
    }

    private static class SingletonInstance {
        private static final SerialTTLManage INSTANCE = new SerialTTLManage();
    }


    public static SerialTTLManage getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public interface TtlResultListener {

        void serialFail();

        void fingerVerifySuc(String fingerId);

        void fingerVerifyFail();

        void clearAllFingerSuc();

        void fingerDeleteSuc(int id);

        void fingerDeleteFail();

        void fingerRegisterFail();

        void fingerRegisterSuc(int id);

    }

    public Handler getHandler() {
        return mHandler;
    }

    private final Handler mHandler = new Handler(msg -> {
        switch ((msg.what)) {
            case 10:
                fingerVerify();
                break;
            case 11:
                fingerLogin();
                break;
            case 20:
                fingerRegister();
                break;
            case 21:
                fingerCheckPress();
                break;
            case 30:
                verifyOrRegister();
                break;
            case 31:
                fingerVerifyOrRegister();
                break;
            case 22:
                register();
                break;

        }
        return false;
    });


    /**
     * 指纹头设备信息，主要用于查询容量
     */
    public void getFingerInfo() {
        step = "deviceInfo";
        serialPort.sendData("EF01FFFFFFFF0100030f", 6, dataReceiveListener);
    }


    /**
     * 查询有效指纹各个数
     */
    public void getFingerNum() {
        step = "fingerNum";
        serialPort.sendData("EF01FFFFFFFF0100031d", 6, dataReceiveListener);
    }

    /**
     * 指纹登录
     */
    public void fingerLogin() {
        step = "lgPress";
        serialPort.sendData("EF01FFFFFFFF01000301", 6, dataReceiveListener);
    }

    /**
     * 指纹验证
     */
    private void fingerVerify() {
        step = "login";
        serialPort.sendData("EF01FFFFFFFF0100083203FFFF0000", 6, dataReceiveListener);
    }

    public void fingerVerifyOrRegister() {
        step = "verifyRegisterPress";
        serialPort.sendData("EF01FFFFFFFF01000301", 6, dataReceiveListener);
    }

    /**
     * 指纹验证通过或者未通过添加
     */
    private void verifyOrRegister() {
        step = "verifyRegister";
        serialPort.sendData("EF01FFFFFFFF0100083203FFFF0000", 6, dataReceiveListener);
    }

    public static int fingerIndex = 0;

    public void fingerRegister() {
        step = "index";
        String sendStr = "EF01ffffffff0100041f0" + fingerIndex;
        serialPort.sendData(sendStr, 6, dataReceiveListener);
    }

    private void fingerCheckPress() {
        step = "checkPress";
        String sendStr = "EF01FFFFFFFF01000301";
        isFinger = true;
        serialPort.sendData(sendStr, 6, dataReceiveListener);
    }

    private void register() {
        Log.e("fingerId", "fingerId=" + id);
        String sendStr = "EF01ffffffff01000831" + String.format(Locale.CHINA, "%04x", id) + String.format(Locale.CHINA, "%02x", recognitionNum) + (repeatFingerRegister ? "0000" : "0010");
        step = "register";
        pressCount = "first";
        serialPort.sendData(sendStr, 6, dataReceiveListener);
    }

    public void cancel() {
        mHandler.removeCallbacksAndMessages(null);
        String sendStr = "EF01FFFFFFFF01000330";
        step = "cancel";
        serialPort.sendData(sendStr, 6, dataReceiveListener);
        ttlResultListener = null;
    }

    public void deleteFinger(Integer id) {
        this.id = id;
        String sendStr = "EF01FFFFFFFF0100070c" + String.format("%04x", id) + "0001";
        step = "delete";
        serialPort.sendData(sendStr, 6, dataReceiveListener);
        mHandler.postDelayed(() -> {
            if (ttlResultListener != null) {
                ttlResultListener.fingerDeleteFail();
            }
        }, 2000);
    }

    public void deleteAllFinger() {
        String sendStr = "EF01FFFFFFFF0100030d";
        step = "deleteAll";
        serialPort.sendData(sendStr, 6, dataReceiveListener);
        mHandler.postDelayed(() -> {
            if (ttlResultListener != null) {
                ttlResultListener.fingerDeleteFail();
            }
        }, 2000);
    }

    public SerialPortManager.OnDataReceiveListener dataReceiveListener = (bytes, i) -> {
        String s = ByteUtilKt.bytesToHexString(bytes, bytes.length);
        Log.e("TTL消息", step + ":" + s + "---length=" + s.length());
        if (s.equals("55") || s.equals("ff")) {
            if (ttlResultListener != null) {
                ttlResultListener.serialFail();
            }
            return;
        }
        if (s != null) {
            switch (step) {
                case "deviceInfo": {
                    if (s.startsWith("EF01")) {
                        String confirm = s.substring(18, 20);
                        if (confirm.equals("00") && s.length() > 32) {
                            EventBus.getDefault().post(new EventMsg<>(EventMsg.FINGER_DEVICE_CAPACITY, Integer.parseInt(s.substring(28, 32), 16)));
                        }
                    }
                }
                case "fingerNum": {
                    if (s.startsWith("EF01")) {
                        String confirm = s.substring(18, 20);
                        if (confirm.equals("00")) {
                            EventBus.getDefault().post(new EventMsg<>(EventMsg.FINGER_TOTAL_NUM, Integer.parseInt(s.substring(20, 24), 16)));
                        }
                    }
                }
                case "index":
                    if (s.startsWith("EF01")) {
                        mHandler.removeCallbacksAndMessages(null);
                        callback = s;
                    } else {
                        if (callback.equals("")) {
                            mHandler.sendEmptyMessageDelayed(20, 500);
                            return;
                        }
                        callback = callback + s;
                    }
                    if (callback.length() == 88) {
                        indexResult(callback);
                        callback = "";
                    }
                    break;
                case "checkPress":
                    callback = "";
                    if (s.length() > 20 && s.startsWith("EF01")) {
                        String confirm = s.substring(18, 20);
                        if (confirm.equals("00")) {
                            isFinger = false;
                            mHandler.sendEmptyMessage(22);
                        } else {
                            mHandler.sendEmptyMessageDelayed(21, 1000);
                        }
                    } else {
                        mHandler.sendEmptyMessageDelayed(21, 1000);
                    }
                    break;
                case "register":
                    if (s.startsWith("EF01") && s.length() > 20) {
                        String res = s.substring(18, 20).toUpperCase(Locale.ROOT);
                        String params1 = s.substring(20, 22).toUpperCase(Locale.ROOT);
                        String params2 = s.substring(22, 24).toUpperCase(Locale.ROOT);
                        Log.e("registerFinger", "res=" + res + "--------params1=" + params1 + "------params2=" + params2);
                        if (res.equals("00")) {
                            if (params1.equals("02") && pressCount.equals("first")) {//请按手指
                                pressCount = "second";
                                ToastUtils.showShort("请再次按压手指");
                            } else if (params1.equals("02") && pressCount.equals("second")) {//请再次按手指
                                pressCount = "third";
                                ToastUtils.showShort("请再次按压手指");
                            } else if (params1.equals("06") && params2.equals("F2")) {//录入成功
                                pressCount = "first";
                                if (ttlResultListener != null) {
                                    ttlResultListener.fingerRegisterSuc(id);
                                }
                            }
                        } else if (res.equals("26")) {
                            ToastUtils.showShort("指纹录入超时");
                            if (ttlResultListener != null) {
                                ttlResultListener.fingerRegisterFail();
                            }
                        } else if (res.equals("27")) {
                            ToastUtils.showShort("指纹已被录入");
                            if (ttlResultListener != null) {
                                ttlResultListener.fingerRegisterFail();
                            }
                        } else if (res.equals("07") || res.equals("0A")) {
                            ToastUtils.showShort("请抬起重按手指");
                        } else if (res.equals("01") || res.equals("22")) {
                            ToastUtils.showShort("指纹识别失败，请重新录入");
                            if (ttlResultListener != null) {
                                ttlResultListener.fingerRegisterFail();
                            }
                        } else if (res.equals("1F") || res.equals("0B")) {
                            ToastUtils.showShort("指纹库已满");
                            if (ttlResultListener != null) {
                                ttlResultListener.fingerRegisterFail();
                            }
                        }
                    }
                    break;
                case "lgPress": {
                    if (s.length() > 20 && s.startsWith("EF01")) {
                        String confirm = s.substring(18, 20);
                        Message message = new Message();
                        if (confirm.equals("00")) {
                            message.what = 10;
                        } else {
                            message.what = 11;
                        }
                        mHandler.sendMessageDelayed(message, 200);
                    }
                }
                case "login":
                case "verifyRegister":
                    fingerVerifyResult(s);
                    break;
                case "cancel":

                    break;
                case "verifyRegisterPress":
                    if (s.length() > 20 && s.startsWith("EF01")) {
                        String confirm = s.substring(18, 20);
                        Message message = new Message();
                        if (confirm.equals("00")) {
                            message.what = 30;
                        } else {
                            message.what = 31;
                        }
                        mHandler.sendMessageDelayed(message, 200);
                    }
                    break;
                case "delete":
                    mHandler.removeCallbacksAndMessages(null);
                    if (s.startsWith("EF01")) {
                        String resultCode = s.substring(18, 20);
                        if (resultCode.equals("00")) {
                            if (ttlResultListener != null) {
                                ttlResultListener.fingerDeleteSuc(id);
                            }
                        } else {
                            ToastUtils.showShort("删除指纹失败");
                        }
                    }
                    break;
                case "deleteAll":
                    mHandler.removeCallbacksAndMessages(null);
                    if (s.startsWith("EF01")) {
                        String resultCode = s.substring(18, 20);
                        if (resultCode.equals("00")) {
                            ToastUtils.showShort("指纹库已清空");
                            if (ttlResultListener != null) {
                                ttlResultListener.clearAllFingerSuc();
                            }
                        } else {
                            ToastUtils.showShort("指纹库清空失败");
                        }
                    }
                    break;
            }
        }
    };


    private void indexResult(String callback) {
        if (callback.length() == 88) {
            String resultCode = callback.substring(18, 20);
            if (resultCode.equals("00")) {
                String result = callback.substring(20, 84);
                LogUtils.e("index:" + result);
                int[] ints = ByteUtilKt.spitString(result);
                for (id = 0; id < 256; id++) {
                    if (((ints[id / 8] >> (id % 8)) & 0x1) > 0) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (id == 256) {
                    fingerIndex++;
                    mHandler.sendEmptyMessage(20);
                } else {
                    id = fingerIndex * 256 + id;
                    mHandler.sendEmptyMessage(21);
                }
            }
        }
    }

    private void fingerVerifyResult(String s) {
        if (s.length() > 26 && s.startsWith("EF01")) {
            String confirm = s.substring(18, 20);
            if (confirm.equals("00")) {
                String param = s.substring(20, 22);
                if (param.equals("05")) {//已注册指纹比对
                    String fingerId = Integer.valueOf(s.substring(22, 26), 16).toString();
                    if (ttlResultListener != null) {
                        ttlResultListener.fingerVerifySuc(fingerId);
                    }
                }
            } else {
                if (step.equals("verifyRegister")) {
                    fingerIndex = 0;
                    fingerRegister();
                } else {
                    ToastUtils.showShort("指纹验证失败，请重新验证");
                    fingerLogin();
                }
            }
        }
    }

}