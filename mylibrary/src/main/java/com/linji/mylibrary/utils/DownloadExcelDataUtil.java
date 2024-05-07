package com.linji.mylibrary.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.linji.mylibrary.model.ExcelData;
import com.linji.mylibrary.widget.LoadingDialog;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class DownloadExcelDataUtil {
    /**
     * @param context
     * @param excelDataList excel数据
     * @param uri           导出的文件目录
     */

    public static void download(Context context, ArrayList<ExcelData> excelDataList, Uri uri, String xlsName) {
        Dialog loadingDialog = LoadingDialog.createLoadingDialog(context, "正在导入中...");
        loadingDialog.show();
        HSSFWorkbook wb = new HSSFWorkbook();
        //=================================定义表头属性===============================================
        HSSFFont font = wb.createFont(); // 生成字体格式设置对象
        font.setFontName("黑体"); // 设置字体黑体
        font.setBold(true); // 字体加粗
        font.setFontHeightInPoints((short) 16); // 设置字体大小
        font.setColor(HSSFFont.COLOR_NORMAL);//字体颜色
        //=================================定义内容属性===============================================
        HSSFFont txtContent = wb.createFont(); // 生成字体格式设置对象
        txtContent.setFontName("黑体"); // 设置字体黑体
        txtContent.setBold(false); // 字体加粗
        txtContent.setFontHeightInPoints((short) 12); // 设置字体大小
        txtContent.setColor(HSSFFont.COLOR_RED);//字体颜色
        String fileName = xlsName + getNowTime() + ".xls";
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri);
        DocumentFile file = documentFile.createFile("xls", fileName);
        //====================================写入数据===============================================
        for (int i = 0; i < excelDataList.size(); i++) {
            ExcelData excelData = excelDataList.get(i);
            HSSFSheet sheet = wb.createSheet(excelData.getSheetName());
            setSheetConfig(sheet, excelData.getHeadNameList().size());
            createExcelHead(sheet, excelData.getHeadNameList());
            createExcelCell(sheet, excelData.getHeadNameList(), excelData.getRowExcelData());
            try {
                OutputStream outputStream = context.getContentResolver().openOutputStream(file.getUri());
                wb.write(outputStream);
                outputStream.close();
                Toast.makeText(context, "数据已导出到 ：" + fileName, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loadingDialog.dismiss();
            }
        }
    }

    public static void download(Context context, ArrayList<ExcelData> excelDataList, String filePath, String xlsName, boolean UPan) {
        Dialog loadingDialog = LoadingDialog.createLoadingDialog(context, "正在导入中...");
        loadingDialog.show();
        HSSFWorkbook wb = new HSSFWorkbook();
        //=================================定义表头属性===============================================
        HSSFFont font = wb.createFont(); // 生成字体格式设置对象
        font.setFontName("黑体"); // 设置字体黑体
        font.setBold(true); // 字体加粗
        font.setFontHeightInPoints((short) 16); // 设置字体大小
        font.setColor(HSSFFont.COLOR_NORMAL);//字体颜色
        //=================================定义内容属性===============================================
        HSSFFont txtContent = wb.createFont(); // 生成字体格式设置对象
        txtContent.setFontName("黑体"); // 设置字体黑体
        txtContent.setBold(false); // 字体加粗
        txtContent.setFontHeightInPoints((short) 12); // 设置字体大小
        txtContent.setColor(HSSFFont.COLOR_RED);//字体颜色
        String fileName = xlsName + getNowTime() + ".xls";
        File file = new File(filePath, fileName);
        //====================================写入数据===============================================
        for (int i = 0; i < excelDataList.size(); i++) {
            ExcelData excelData = excelDataList.get(i);
            HSSFSheet sheet = wb.createSheet(excelData.getSheetName());
            setSheetConfig(sheet, excelData.getHeadNameList().size());
            createExcelHead(sheet, excelData.getHeadNameList());
            createExcelCell(sheet, excelData.getHeadNameList(), excelData.getRowExcelData());
            try {

                file.createNewFile();
                OutputStream outputStream = context.getContentResolver().openOutputStream(Uri.fromFile(file));
                wb.write(outputStream);
                outputStream.close();
                Toast.makeText(context, "数据已导出到" + (UPan ? "U盘" : "") + "：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loadingDialog.dismiss();
            }
        }
    }

    // 创建Excel标题行，第一行。
    public static void createExcelHead(HSSFSheet sheet, ArrayList<String> headNameList) {
        HSSFRow headRow = sheet.createRow(0);
        headRow.setHeight((short) 500);
        for (int i = 0; i < headNameList.size(); i++) {
            headRow.createCell(i).setCellValue(headNameList.get(i));
        }
    }

    // 创建Excel的一行数据。
    private static void createExcelCell(HSSFSheet sheet, ArrayList<String> headNameList, ArrayList<Map<String, String>> userDatas) {
        for (int i = 0; i < userDatas.size(); i++) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.setHeight((short) 500);
            Map<String, String> userData = userDatas.get(i);
            for (int j = 0; j < headNameList.size(); j++) {
                HSSFCell cell = dataRow.createCell(j);
                cell.setCellValue(userData.get(headNameList.get(j)));
            }

        }
    }

    public static String getNowTime() {
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(time);
    }

    public static void setSheetConfig(HSSFSheet sheet, int size) {
        //设置列宽
        for (int i = 0; i < size; i++) {
            sheet.setColumnWidth(i, 5000);
        }

    }
}

