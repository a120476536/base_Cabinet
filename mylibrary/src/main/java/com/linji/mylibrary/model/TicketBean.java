package com.linji.mylibrary.model;

public class TicketBean {
    private String code;
    private String time;

    public TicketBean(String code, String time) {
        this.code = code;
        this.time = time;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public String getTime() {
        return time == null ? "" : time;
    }
}
