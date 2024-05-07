package com.linji.mylibrary.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.documentfile.provider.DocumentFile;

import com.linji.mylibrary.model.ExcelData;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xf
 * @version 1.0
 * @ description: excel读取数据工具类
 * @ date 2021/7/8 14:58
 */
public class ExcelHSSFReadUtil {

    /**
     * 选择需要导入的文件
     * @param activity
     * @param title
     */
    public static void selectExcelFile(Activity activity, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SystemUtil.showNavigationBar(activity);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.ms-excel");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            activity.startActivityForResult(Intent.createChooser(intent, title), 1);
        }
    }

    /**
     * @param excelFile 文件流
     * @ description:  读取excel数据
     * @ return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author xf
     * @ date: 2021/7/8 15:12
     */
    public static List<ExcelData> readExcel(Context context, DocumentFile excelFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = (FileInputStream) context.getContentResolver().openInputStream(excelFile.getUri());
//             fileInputStream = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileInputStream == null) {
            return null;
        }
        HSSFSheet sheet;
        List<ExcelData> resultList = new ArrayList<>();
        try {
            // 通过poi解析流中的workbook
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            // 获取所有workbook中的sheet工作表数量
            int number = workbook.getNumberOfSheets();
            for (int i = 0; i < number; i++) {
                // 获取到每一个sheet工作表
                sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                // 根据sheet获取数据
                ArrayList<Map<String, String>> data = getData(sheet);
                resultList.add(new ExcelData(sheetName, data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<Map<String, String>> getData(HSSFSheet sheet) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Map<String, String> content;
        HSSFRow row;
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        HSSFRow firstRow = sheet.getRow(0);
        //读取数据从1开始
        row = sheet.getRow(1);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第一行开始，下标从0开始
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            content = new HashMap<>();
            int j = 0;
            while (j < colNum) {
                content.put(getStringCellValue(firstRow.getCell(j)), getCellFormatValue(row.getCell((short) j)).trim());
                j++;
            }
            list.add(content);
        }
        return list;
    }

    /**
     * @param cell 单元格
     * @ description:  获取单元格数据内容为字符串类型的数据
     * @ return: java.lang.String
     * @ author xf
     * @ date: 2021/7/8 15:16
     */
    private static String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
}
