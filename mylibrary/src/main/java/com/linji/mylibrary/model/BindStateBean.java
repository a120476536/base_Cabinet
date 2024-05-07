package com.linji.mylibrary.model;

public class BindStateBean {
    public String baiduFaceActivateStatus;
    public String speechSynthesisStatus;
    public String speechSynthesisKey;
    public String deviceModel;
    public String deviceNo;
    public String deviceType;
    public String deviceVersion;



    public String getSpeechSynthesisKey() {
        return speechSynthesisKey == null ? "" : speechSynthesisKey;
    }

    public void setSpeechSynthesisKey(String speechSynthesisKey) {
        this.speechSynthesisKey = speechSynthesisKey;
    }

    public String getSpeechSynthesisStatus() {
        return speechSynthesisStatus == null ? "" : speechSynthesisStatus;
    }

    public void setSpeechSynthesisStatus(String speechSynthesisStatus) {
        this.speechSynthesisStatus = speechSynthesisStatus;
    }

    public String getBaiduFaceActivateStatus() {
        return baiduFaceActivateStatus == null ? "" : baiduFaceActivateStatus;
    }

    public void setBaiduFaceActivateStatus(String baiduFaceActivateStatus) {
        this.baiduFaceActivateStatus = baiduFaceActivateStatus;
    }

    public String getDeviceModel() {
        return deviceModel == null ? "" : deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceNo() {
        return deviceNo == null ? "" : deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceType() {
        return deviceType == null ? "" : deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceVersion() {
        return deviceVersion == null ? "" : deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
