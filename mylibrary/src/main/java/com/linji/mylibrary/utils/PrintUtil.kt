package com.linji.mylibrary.utils

import com.linji.mylibrary.model.TakeOutInfo
import com.linji.mylibrary.model.TicketBean
import com.print.usbprint.command.Esc
import com.print.usbprint.command.Label
import com.print.usbprint.util.USBUtil
import java.io.IOException

object PrintUtil {

    @JvmStatic
    fun printMsg(takeOutInfo: TakeOutInfo) {
        //52mm*30mm   416*240
        /** ———————
         * |            |   名称：xxx档案
         * |   qrCode   |   编号：123
         * |            |   位置：01-2
         * ———————
         */
        val label = Label()
        label.switchLabel()
        label.customPageStart(
            Integer.valueOf(500),
            Integer.valueOf(300),
            Integer.valueOf(0)
        )//页面尺寸/pageStart
        /**
         * x和y的取值范围为0-（page页页宽-1）\n" +
        "码块的取值范围为0-4\n" +
        "版本的取值范围为0-20\n" +
        "ECC的取值范围为0-4\n" +
        "旋转的值为0时不旋转，为1时旋转90°,为2时旋转180°，为3时旋转270°
         */

        /**
         * x和y的取值范围为0-（page页页宽-1）\n" + "高度的取值范围为16/24/32/48/64/80/96" + "加粗的值为0时表示不加粗，为1时表示加粗\n" +
        "下划线的值为0时表示不加下划线,为1时表示加下划线\n" + "反白的值0时表示不反白，为1时表示反白\n" + "删除线的值为0时表示不加删除线,为1时表示加删除线\n" +
        "旋转的值为0时不旋转，为1时旋转90°,为2时旋转180°，为3时旋转270°\n" + "倍高和倍宽的取值范围为0-15
         */
        //"text", "x", y, height, bold, underline, include, deleteline, rotate, widthtype, heighttype

        label.customPrintText("单位:${takeOutInfo.unit}", 0, 0, 24, 0, 0, 0, 0, 0, 0, 1)
        label.customPrintText("部门:${takeOutInfo.department}", 0, 45, 24, 0, 0, 0, 0, 0, 0, 1)
        label.customPrintText("职务:${takeOutInfo.position}", 0, 85, 24, 0, 0, 0, 0, 0, 0, 0)
        label.customPrintText("姓名:${takeOutInfo.name}", 0, 125, 24, 0, 0, 0, 0, 0, 0, 0)
        label.customPrintText(
            "领取时间:${takeOutInfo.takeOutTime}",
            0,
            165,
            24,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        )
        label.customPrintText("套餐类型:${takeOutInfo.mealType}", 0, 205, 24, 0, 0, 0, 0, 0, 0, 0)

        label.pageEnd()
        label.customPrintPage(1)
        try {
            USBUtil.getInstance().CommandLabel(label)
            label.clear()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun printNewBarCode(imeiDeviceId: String, ticket: TicketBean) {
        val label = Esc()
        label.reset()
        val deviceNo = imeiDeviceId.substring(imeiDeviceId.length - 3)
        val code = ticket.code
        val time = ticket.time
        label.align(1)
        label.barCodeSite(0)
        label.printBarCodeHeight(100)
        label.printBarCodeWidth(2)
        label.printBarCode(72, deviceNo + code)
        label.addArrayToCommand(byteArrayOf(15, 10, 13, 10))
        label.reset()
        label.align(1)
        label.printText(time)
        label.lineSpace(50)
        label.addArrayToCommand(byteArrayOf(25, 80, 0, 13, 10))
        try {
            USBUtil.getInstance().CommandEsc(label)
            label.clear()
            label.addArrayToCommand(byteArrayOf(13, 10, 13, 10))
            label.cutAll()
            USBUtil.getInstance().CommandEsc(label)
            val label = Esc()
            label.switchESC()
            label.paper()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun printBarCode(imeiDeviceId: String, ticket: TicketBean) {
        val label = Label()
        label.switchLabel()
        label.reset()
        val deviceNo = imeiDeviceId.substring(imeiDeviceId.length - 3)
        val code = ticket.code
        val time = ticket.time
        label.customPageStart(
            Integer.valueOf(420),
            Integer.valueOf(240),
            Integer.valueOf(0)
        )//页面尺寸/pageStart
        label.printBarcode(deviceNo+code, 30, 30, 8, 130, 2, 0)
        label.customPrintText(time, 80, 190, 20, 0, 0, 0, 0, 0, 1, 1)
        label.pageEnd()
        label.customPrintPage(1)
        try {
            USBUtil.getInstance().CommandLabel(label)
            label.clear()
            label.cutHalf()
            label.reset()
            USBUtil.getInstance().CommandLabel(label)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 半切刀
     */
    @JvmStatic
    fun labelCutHalf() {
        val label = Esc()
        label.addArrayToCommand(byteArrayOf(13, 10, 13, 10))
        label.cutAll()
        try {
            USBUtil.getInstance().CommandEsc(label)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}