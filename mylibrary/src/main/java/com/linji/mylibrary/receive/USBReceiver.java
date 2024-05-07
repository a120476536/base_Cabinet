package com.linji.mylibrary.receive;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.blankj.utilcode.util.SPStaticUtils;
import com.linji.mylibrary.model.Constants;
import com.print.usbprint.util.USBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * USB 设备监听广播
 */

public class USBReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final static String ACTION = "android.hardware.usb.action.USB_STATE";
    private UsbManager mUsbManager;
    private HashMap<String, UsbDevice> deviceList;
    List<UsbDevice> usbDeviceList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_USB_PERMISSION.equals(action)) {
            // 获取权限结果的广播
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.e("USBReceiver", "获取权限成功：" + device.getDeviceName());
                        usbDeviceList.add(device);
                        Log.i("USBReceiver", String.valueOf(usbDeviceList));
                        getPrintDevices(context);
                    } else {
                        Log.e("USBReceiver", "获取权限失败：" + device.getDeviceName());
                    }

                }
            }
        } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            // 有新的设备插入了，在这里一般会判断这个设备是不是我们想要的，是的话就去请求权限
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Log.e("USBReceiver", "usb插入");
            getUsbPermission(device, context);

        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            // 有设备拔出了
            Log.e("USBReceiver", "usb拔出");
            getPrintDevices(context);
            if (usbDeviceList != null) {
                usbDeviceList.clear();
            }
        } else if (ACTION.equals(action)) {
            //已经有usb设备连接
            Log.i("USBReceiver", "已经有usb设备连接");
            try {
                getDetail(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPrintDevices(Context context) {
        List<UsbDevice> deviceList = USBUtil.getInstance().getDeviceList(context);
        if (deviceList != null) {
            UsbDevice usbDevice = USBUtil.getInstance().getUsbDevice(1155, 41061);
            if (usbDevice != null) {
                boolean isOpen = USBUtil.getInstance().IsOpen(usbDevice, context);
                if (isOpen) {
                    Log.e("USBReceiver", "打印机连接成功");
                    SPStaticUtils.put(Constants.PRINT_CONNECT, true);
                } else {
                    Log.e("USBReceiver", "打印机连接失败");
                    SPStaticUtils.put(Constants.PRINT_CONNECT, false);
                }
            }else {
                Log.e("USBReceiver", "打印机连接失败");
                SPStaticUtils.put(Constants.PRINT_CONNECT, false);
            }
        }
    }

    private void getUsbPermission(UsbDevice mUSBDevice, Context mContext) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        USBUtil.getInstance().init(mContext);
        mUsbManager = USBUtil.getInstance().getUsbManager();
        USBUtil.getInstance().setUsbManager(mUsbManager);
        mUsbManager.requestPermission(mUSBDevice, pendingIntent);
    }

    public void getDetail(Context context) throws IOException {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        USBUtil.getInstance().setUsbManager(manager);
        deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(device, pendingIntent);
        }
    }


}
