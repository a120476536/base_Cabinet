package com.linji.mylibrary.model;

public class TakeOutInfo {
    private String unit;
    private String department;
    private String position;
    private String name;
    private String takeOutTime;
    private String mealType;

    public TakeOutInfo(String unit, String department, String position, String name, String takeOutTime, String mealType) {
        this.unit = unit;
        this.department = department;
        this.position = position;
        this.name = name;
        this.takeOutTime = takeOutTime;
        this.mealType = mealType;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepartment() {
        return department == null ? "" : department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position == null ? "" : position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTakeOutTime() {
        return takeOutTime == null ? "" : takeOutTime;
    }

    public void setTakeOutTime(String takeOutTime) {
        this.takeOutTime = takeOutTime;
    }

    public String getMealType() {
        return mealType == null ? "" : mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
