package com.linji.mylibrary.net.oss;


public interface IOSSView {

    /**
     * 获得oss上传地址成功
     * @param fileName
     */
    void getOssPathNameSuccess(String fileName);

    /**
     * 获得oss上传地址失败
     */
    void getOssPathNameFail();

    void getOssPathNameSuccess(String fileName, int position);
}
