package com.linji.mylibrary.serial

import android_serialport_api.Device
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.StringUtils
import com.linji.mylibrary.model.Constants


fun fingerprintDevice(): Device {
    val deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL)
    val device = Device()
    when (deviceModel) {
        "1" -> {//亮钻
            device.path = "/dev/ttyS0"
        }

        "2" -> {//卡奥斯
            device.path = "/dev/ttyS1"
        }

        "3" -> {//迈冲
            device.path = "/dev/ttyS2"
        }

        "5" -> {//德沃
//            device.path = "/dev/ttyS2"
            device.path = "/dev/ttyXRUSB3"
        }
        "6" -> {//润泽
            device.path = "/dev/ttyS0"
        }
    }
    device.speed = 57600
    return device
}

fun widgetDevice(): Device {
    val device = Device()
    device.path = "/dev/ttyS3"
    device.speed = 9600
    return device;
}


fun lockDevice(): Device {
    val deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL)
    val speed = SPStaticUtils.getString(Constants.LOCK_SPEED)
    val device = Device()
    if (StringUtils.isEmpty(speed)) {
        device.speed = 19200
    }else{
        device.speed = speed.toInt();
    }
    when (deviceModel) {
        "1" -> {//亮钻
            device.path = "/dev/ttyS2"
        }

        "2" -> {//卡奥斯
            device.path = "/dev/ttyS4"
        }

        "3" -> {//迈冲
            device.path = "/dev/ttyS3"
        }

        "5" -> {//德沃
            device.path = "/dev/ttyS3"
        }
        "6" -> {//润泽
            device.path = "/dev/ttyS2"
        }
    }
    return device
}