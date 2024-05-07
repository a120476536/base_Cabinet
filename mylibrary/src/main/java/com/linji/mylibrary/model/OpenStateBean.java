package com.linji.mylibrary.model;

import java.util.Objects;

public class OpenStateBean {
    private String  openState;
    private String  openPosition;

    public OpenStateBean(String openState, String openPosition) {
        this.openState = openState;
        this.openPosition = openPosition;
    }

    public String getOpenState() {
        return openState == null ? "" : openState;
    }

    public void setOpenState(String openState) {
        this.openState = openState;
    }

    public String getOpenPosition() {
        return openPosition == null ? "" : openPosition;
    }

    public void setOpenPosition(String openPosition) {
        this.openPosition = openPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenStateBean that = (OpenStateBean) o;
        return Objects.equals(openState, that.openState) && Objects.equals(openPosition, that.openPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openState, openPosition);
    }

    @Override
    public String toString() {
        return "OpenStateBean{" +
                "openState='" + openState + '\'' +
                ", openPosition='" + openPosition + '\'' +
                '}';
    }
}
