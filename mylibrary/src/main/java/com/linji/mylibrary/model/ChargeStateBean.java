package com.linji.mylibrary.model;

import java.util.Objects;

public class ChargeStateBean {
    private String chargePosition;
    private String chargeState;

    public ChargeStateBean(String chargePosition, String chargeState) {
        this.chargePosition = chargePosition;
        this.chargeState = chargeState;
    }

    public String getChargePosition() {
        return chargePosition == null ? "" : chargePosition;
    }

    public void setChargePosition(String chargePosition) {
        this.chargePosition = chargePosition;
    }

    public String getChargeState() {
        return chargeState == null ? "" : chargeState;
    }

    public void setChargeState(String chargeState) {
        this.chargeState = chargeState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargeStateBean that = (ChargeStateBean) o;
        return Objects.equals(chargePosition, that.chargePosition) && Objects.equals(chargeState, that.chargeState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargePosition, chargeState);
    }

    @Override
    public String toString() {
        return "ChargeStateBean{" +
                "chargePosition='" + chargePosition + '\'' +
                ", chargeState='" + chargeState + '\'' +
                '}';
    }
}
