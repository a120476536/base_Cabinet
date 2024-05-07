package com.linji.mylibrary.net;

public class BaseCallModel<T> extends CallModel {


    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
