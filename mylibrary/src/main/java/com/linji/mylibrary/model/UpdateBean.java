package com.linji.mylibrary.model;

public class UpdateBean {

    private String fileAddress;
    private String forceUpdateFlag;
    private String versionDescribe;
    private Integer versionId;
    private String versionNo;

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getForceUpdateFlag() {
        return forceUpdateFlag;
    }

    public void setForceUpdateFlag(String forceUpdateFlag) {
        this.forceUpdateFlag = forceUpdateFlag;
    }

    public String getVersionDescribe() {
        return versionDescribe;
    }

    public void setVersionDescribe(String versionDescribe) {
        this.versionDescribe = versionDescribe;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}
