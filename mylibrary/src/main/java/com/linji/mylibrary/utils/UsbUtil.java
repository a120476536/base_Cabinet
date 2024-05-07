package com.linji.mylibrary.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.model.DeviceInfo;
import com.linji.mylibrary.receive.PrinterStateReceiver;
import com.linji.mylibrary.receive.USBReceiver;
import com.print.usbprint.util.USBUtil;

import java.util.List;

public class UsbUtil {


    public static void init(Context mContext) {
        USBUtil.getInstance().init(mContext);
    }

    /**
     * 注册打印机广播
     * @param context
     * @param usbReceiver
     * @param printerStateReceiver
     */
    public static void initPrint(Context context, USBReceiver usbReceiver, PrinterStateReceiver printerStateReceiver) {
        init(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(USBReceiver.ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction("android.hardware.usb.action.USB_STATE"); //usb连接状态广播·
        context.registerReceiver(usbReceiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PrinterStateReceiver.PrinterState);
        context.registerReceiver(printerStateReceiver, intentFilter);
    }


    public static Boolean getPrintDevices(Context context) {
        if (getDeviceList(context) != null) {
            UsbDevice usbDevice = USBUtil.getInstance().getUsbDevice(DeviceInfo.vendorId, DeviceInfo.productId);
            if (usbDevice != null) {
                ToastUtils.showShort("打印机连接成功");
                USBUtil.getInstance().IsOpen(usbDevice, context);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<UsbDevice> getDeviceList(Context context) {
        List<UsbDevice> deviceList = USBUtil.getInstance().getDeviceList(context);
        return deviceList;
    }
}
