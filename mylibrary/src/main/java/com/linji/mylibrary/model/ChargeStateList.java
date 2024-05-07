package com.linji.mylibrary.model;

import java.util.ArrayList;

public class ChargeStateList {
    private int position;
    private ArrayList<ChargeStateBean> chargeStateBeans;

    public ChargeStateList(int position, ArrayList<ChargeStateBean> chargeStateBeans) {
        this.position = position;
        this.chargeStateBeans = chargeStateBeans;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<ChargeStateBean> getChargeStateBeans() {
        if (chargeStateBeans == null) {
            return new ArrayList<>();
        }
        return chargeStateBeans;
    }

    public void setChargeStateBeans(ArrayList<ChargeStateBean> chargeStateBeans) {
        this.chargeStateBeans = chargeStateBeans;
    }
}
