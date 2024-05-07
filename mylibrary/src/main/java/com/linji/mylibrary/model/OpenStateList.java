package com.linji.mylibrary.model;

import java.util.ArrayList;

public class OpenStateList {
    private int position;
    private ArrayList<OpenStateBean> openStateBeans;

    public OpenStateList(int position, ArrayList<OpenStateBean> openStateBeans) {
        this.position = position;
        this.openStateBeans = openStateBeans;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<OpenStateBean> getOpenStateBeans() {
        if (openStateBeans == null) {
            return new ArrayList<>();
        }
        return openStateBeans;
    }

    public void setOpenStateBeans(ArrayList<OpenStateBean> openStateBeans) {
        this.openStateBeans = openStateBeans;
    }
}
