package com.linji.mylibrary.serial.manage;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.linji.mylibrary.R;
import com.linji.mylibrary.model.ChargeStateBean;
import com.linji.mylibrary.model.ChargeStateList;
import com.linji.mylibrary.model.OpenStateBean;
import com.linji.mylibrary.model.OpenStateList;
import com.linji.mylibrary.serial.ByteUtilKt;
import com.linji.mylibrary.utils.ListUtil;
import com.linji.mylibrary.utils.SoundPoolUtil;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortManager;
import kotlin.text.StringsKt;

public class Serial485Manage {
    public static final String SWIPE_MODE = "swipeMode";
    private static Context context;

    public static String type;

    public static Serial485Result serial485Result;

    public void setSerial485Result(Serial485Result serial485Result) {
        this.serial485Result = serial485Result;
    }

    public Handler mHandler = new Handler(msg -> {
        if (msg.what == 12) {
            swipe();
        }
        return false;
    });

    public interface Serial485Result {
        void swipeCardResult(String cardNo);

        void chargeStateResult(ChargeStateList chargeStateList);

        void openStateResult(OpenStateList openStateList);
    }

    private Serial485Manage() {
    }

    private static class SingletonInstance {
        private static final Serial485Manage INSTANCE = new Serial485Manage();
    }

    public static Serial485Manage getInstance(Context context) {
        return getInstance(context, "");
    }

    public static Serial485Manage getInstance(Context context, String type) {
        Serial485Manage.context = context;
        Serial485Manage.type = type;
        return SingletonInstance.INSTANCE;
    }

    /**
     * 刷卡
     */
    public  void swipe() {
        if (type.equals(SWIPE_MODE)) {
            Message message = new Message();
            message.what = 12;
            mHandler.sendMessageDelayed(message, 1000);
        } else {
            mHandler.removeMessages(12);
            return;
        }
        Serial485PortUtil.getInstance().sendData("5502014000", dataReceiveListener);
    }

    public void cancelSwipe() {
        type = "";
        mHandler.removeMessages(12);
    }

    /**
     * 开锁
     */
    public void openLock(int lockAddress, int lockNO) {
        Serial485PortUtil.getInstance().sendData("55" + String.format("%02x", lockAddress) + "A1" + String.format("%02x", lockNO) + "00");
    }



    public static int stateNum = 0;

    /**
     * 查询充电状态
     *
     * @param boxNum
     */
    public  void checkChargeStates(int boxNum) {
        stateNum = 0;
        int checkNum = ListUtil.convertNum(boxNum);
        checkChargeState(checkNum);
    }

    private  void checkChargeState(int checkNum) {
        stateNum++;
        Serial485PortUtil.getInstance().sendData("5506" + (stateNum < 10 ? "0" + stateNum : stateNum) + "7000", dataReceiveListener);
        if (stateNum == checkNum) {
            stateNum = 0;
        } else {
            mHandler.postDelayed(() -> {
                checkChargeState(checkNum);
            }, 100);
        }

    }


    /**
     * 查询锁开关状态
     */
    public  void checkLockOpenStates(ArrayList<String> addressList) {
        for (int i = 0; i < addressList.size(); i++) {
            try {
                int addressNo = Integer.parseInt(addressList.get(i));
                Serial485PortUtil.getInstance().sendData("5501" + (addressNo < 10 ? "0" + addressNo : addressNo) + "200100", dataReceiveListener);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public SerialPortManager.OnDataReceiveListener dataReceiveListener = (bytes, i) -> {
        String s = ByteUtilKt.bytesToHexString(bytes, bytes.length);
        if (s.startsWith("55020140")) {//刷卡
            if (s.startsWith("55") && s.length() >= 20) {
                String confirm = s.substring(10, 12);
                if (confirm.equals("00")) {//有卡
                    Log.e("刷卡", s);
                    SoundPoolUtil.getInstance(context).playSoundWithRedId(R.raw.swip_card);
                    cancelSwipe();
                    String swipeCode = s.substring(12, 20);
                    if (serial485Result != null) {
                        serial485Result.swipeCardResult(swipeCode);
                    }
                }
            }
        }else if (s.startsWith("5506") && s.startsWith("70", 6) && s.length() >= 12 && s.startsWith("00", 10)) {//充电状态检测
            try {
                String result = s.substring(12, s.length() - 2);
                if (result.length() % 4 == 0) {
                    List<String> chargeStateList = StringsKt.chunked(result,4);
                    ArrayList<ChargeStateBean> chargeStates = new ArrayList<>();
                    for (String chargeState : chargeStateList) {
                        chargeStates.add(new ChargeStateBean(chargeState.substring(0, 2), chargeState.substring(2, 4)));
                    }
                    serial485Result.chargeStateResult(new ChargeStateList(Integer.parseInt(s.substring(4, 6)), chargeStates));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (s.startsWith("5501") && s.startsWith("20", 6) && s.length() >= 12 && s.startsWith("00", 10)) {//开锁状态检测
            try {
                int num = Integer.parseInt(s.substring(8, 10), 16);
                String result = s.substring(12, 10 + num * 2);
                Log.e("TAG", result);
                if (result.length() % 2 == 0) {
                    List<String> openStateList = StringsKt.chunked(result,2);
                    ArrayList<OpenStateBean> openStates = new ArrayList<>();
                    for (int j = 0; j < openStateList.size(); j++) {
                        openStates.add(new OpenStateBean(openStateList.get(j), (j + 1) + ""));
                    }
                    serial485Result.openStateResult( new OpenStateList(Integer.parseInt(s.substring(4, 6)), openStates));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
