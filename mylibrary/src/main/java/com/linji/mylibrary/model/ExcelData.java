package com.linji.mylibrary.model;

import java.util.ArrayList;
import java.util.Map;

public class ExcelData {
    private String sheetName;
    private ArrayList<String> headNameList;
    private ArrayList<Map<String, String>> rowExcelData;

    public ExcelData(String sheetName, ArrayList<String> headNameList, ArrayList<Map<String, String>> rowExcelData) {
        this.sheetName = sheetName;
        this.headNameList = headNameList;
        this.rowExcelData = rowExcelData;
    }
    public ExcelData(String sheetName, ArrayList<Map<String, String>> rowExcelData) {
        this.sheetName = sheetName;
        this.rowExcelData = rowExcelData;
    }
    public String getSheetName() {
        return sheetName == null ? "" : sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public ArrayList<String> getHeadNameList() {
        if (headNameList == null) {
            return new ArrayList<>();
        }
        return headNameList;
    }

    public void setHeadNameList(ArrayList<String> headNameList) {
        this.headNameList = headNameList;
    }

    public ArrayList<Map<String, String>> getRowExcelData() {
        if (rowExcelData == null) {
            return new ArrayList<>();
        }
        return rowExcelData;
    }

    public void setRowExcelData(ArrayList<Map<String, String>> rowExcelData) {
        this.rowExcelData = rowExcelData;
    }
}
