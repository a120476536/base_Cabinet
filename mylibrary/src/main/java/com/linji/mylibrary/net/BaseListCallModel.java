package com.linji.mylibrary.net;

import java.util.ArrayList;
import java.util.List;

public class BaseListCallModel<T> extends CallModel {

    protected List<T> data;

    public List<T> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
