package com.linji.mylibrary.mqtt;


import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.utils.SystemUtil;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MqttCallBack implements MqttCallback {
    private Context context;

    public MqttCallBack(Context context) {
        this.context = context;
    }

    /**
     * 与服务器断开的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
//        log.info("[MQTT]断开了与服务端的连接。考虑是否服务端掉线 or 回调参数解析报错 or 无默认sub");
        // 执行自动重连
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MqttFactory.reconnect();
        }
    }

    /**
     * 消息到达的回调
     *
     * @param topic       话题
     * @param mqttMessage 消息内容
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String msg = new String(mqttMessage.getPayload());
        LogUtils.e("[MQTT]已获取返回数据，当前数据为：\n" + topic + "\n" + msg);
        if (topic.endsWith(Constants.COMMON)) {
            try {
                MQTTMsgBean msgBean = GsonUtils.fromJson(msg, MQTTMsgBean.class);
                switch (msgBean.getCommandType()) {
                    case "reboot":
                        SystemUtil.softReboot(context);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消息发布成功的回调
     *
     * @param token token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        IMqttAsyncClient client = token.getClient();
        LogUtils.e("[MQTT]消息发布成功：" + client.getClientId());
    }

}