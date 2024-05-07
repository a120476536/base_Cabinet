package com.linji.mylibrary.model;

public class EventMsg<T> {

    public static final String FINGER_DEVICE_CAPACITY = "fingerDeviceCapacity";
    public static final String FINGER_TOTAL_NUM = "fingerTotalNum";
    public static final String FINGER_CLEAR_ALL_SUC = "fingerClearAllSuc";
    public static final String FINGER_DELETE_SUC = "fingerDeleteSuc";
    public static final String FINGER_DELETE_FAIL = "fingerDeleteFail";
    public static final String FINGER_REGISTER_SUC = "fingerRegisterSuc";
    public static final String FINGER_REGISTER_FAIL = "fingerRegisterFail";
    public static final String FINGER_VERIFY_SUC="fingerVerifySuc";
    public static final String FINGER_VERIFY_FAIL="fingerVerifyFail";
    public static final String SERIAL_FAIL = "serialFail";
    public static final String DELETE_USER_FINGER = "deleteUserFinger";
    public static final String CARD_RECOGNITION_SUC = "cardRecognitionSuc";
    public static final String UPDATE_APP = "updateApp";
    public static final String MQTT_CONNECT_STATE = "mqttConnectState";
    private String type;
    private T data;

    public EventMsg() {
    }

    public EventMsg(String type) {
        this.type = type;
    }

    public EventMsg(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
