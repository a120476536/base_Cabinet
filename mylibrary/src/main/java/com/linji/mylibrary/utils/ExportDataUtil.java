package com.linji.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.StringUtils;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.model.ExcelData;

import java.util.ArrayList;

/**
 * 到处数据
 */
public class ExportDataUtil {
    public static void exportDataInfo(Activity context, ArrayList<ExcelData> excelData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SystemUtil.showNavigationBar(context);
            String deviceModel = SPStaticUtils.getString(Constants.DEVICE_MODEL, "1");
            if (deviceModel.equals("1")) {//亮钻可以支持选择文件导出
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                context.startActivityForResult(intent, 0);
            } else {//其他设备直接导出到U盘
                String uPanPath = DeviceUtil.getExternalPath(context, "U 盘");
                if (StringUtils.isEmpty(uPanPath)) {
                    String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    exportData(context, excelData, absolutePath, false);
                } else {
                    exportData(context, excelData, uPanPath, true);
                }
            }
        }
    }

    private static void exportData(Context context, ArrayList<ExcelData> excelData, String filePath, boolean UPan) {
        DownloadExcelDataUtil.download(context, excelData, filePath, "存包柜柜机用户数据", UPan);
    }
}
