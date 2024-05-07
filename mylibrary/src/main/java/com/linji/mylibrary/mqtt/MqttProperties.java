package com.linji.mylibrary.mqtt;

import android.content.Context;

public class MqttProperties {

    private String url;
    private Context context;

    private String username;

    private String password;

    private String clientId;

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId + ":" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "MqttProperties{" +
                "url='" + url + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}