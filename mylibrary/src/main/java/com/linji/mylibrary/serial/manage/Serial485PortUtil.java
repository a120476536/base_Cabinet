package com.linji.mylibrary.serial.manage;

import static com.linji.mylibrary.serial.SerialPortKt.lockDevice;

import android.util.Log;

import com.linji.mylibrary.serial.ByteUtilKt;

import android_serialport_api.SerialPortManager;

public class Serial485PortUtil {

    private static SerialPortManager serialPortManager;
    private static Serial485PortUtil instance;

    public static synchronized Serial485PortUtil getInstance() {
        if (instance == null) {
            instance = new Serial485PortUtil();
        }
        return instance;
    }

    private Serial485PortUtil() {
        serialPortManager = new SerialPortManager(lockDevice());
    }


    public void sendData(String sendStr) {
        if (serialPortManager != null) {
            String s = sendStr + ByteUtilKt.getXORCheck(sendStr);
            Log.e("TAG:    sendBytes====", s);
            byte[] bytes = hexString2Bytes(s);
            serialPortManager.sendPacket(bytes);
        } else {
            Log.e("TAG", "serialPortManager is null");
        }
    }

    public void sendData(String sendStr, SerialPortManager.OnDataReceiveListener listener) {
        if (serialPortManager != null) {
            String s = sendStr + ByteUtilKt.getXORCheck(sendStr);
            Log.e("TAG:    sendBytes====", s);
            byte[] bytes = hexString2Bytes(s);
            serialPortManager.sendPacket(bytes);
            serialPortManager.setOnDataReceiveListener(listener);
        } else {
            Log.e("TAG", "serialPortManager is null");
        }
    }

    public void sendData(String sendStr, Integer split, SerialPortManager.OnDataReceiveListener listener) {
        if (serialPortManager != null) {
            String s = sendStr + ByteUtilKt.getXORCheck(sendStr, split);
            Log.e("TAG:    sendBytes====", s);
            byte[] bytes = hexString2Bytes(s);
            serialPortManager.sendPacket(bytes);
            serialPortManager.setOnDataReceiveListener(listener);
        } else {
            Log.e("TAG", "serialPortManager is null");
        }
    }

    public void setOnReceiveListener(SerialPortManager.OnDataReceiveListener listener) {
        if (serialPortManager != null) {
            serialPortManager.setOnDataReceiveListener(listener);
        } else {
            Log.e("TAG", "serialPortManager is null");
        }
    }

    /**
     * 关闭串口
     */
    public static void closeSerialPort() {
        if (serialPortManager != null) {
            serialPortManager.closeSerialPort();
            serialPortManager.setOnDataReceiveListener(null);
        }
        serialPortManager = null;
        instance = null;
    }

    private byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }
}
