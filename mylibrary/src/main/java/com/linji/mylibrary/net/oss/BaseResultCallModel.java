package com.linji.mylibrary.net.oss;


import com.linji.mylibrary.net.CallModel;

public class BaseResultCallModel<T> extends CallModel {


    protected T result;

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }


}
