package com.linji.mylibrary.mqtt;

import android.content.Context;
import android.util.Log;

import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.id2.Id2ItlsSdk;
import com.aliyun.alink.linksdk.tools.ALog;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SPUtils;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.utils.DeviceUtil;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MqttUtils {

    public static void initMqtt() {
        MqttConfigure.itlsLogLevel = Id2ItlsSdk.DEBUGLEVEL_NODEBUG;
        ALog.setLevel(ALog.LEVEL_INFO);
        // 设置心跳时间，默认65秒
        MqttConfigure.setKeepAliveInterval(65);
    }

    public static int sendMqtt(String topic, String message) throws MqttException {
        return pub(0, false, topic, message);
    }

    /**
     * 发布消息
     *
     * @param qos      0-至多1次、1-至少1次、2-一次
     * @param retained 是否保留：true-sub重新连接mqtt服务端时，总能拿到该主题的最新消息、false-sub重新连接mqtt服务端时，只能拿到连接后发布的消息
     * @param topic    话题
     * @param message  消息内容
     * @return
     */
    public static int pub(int qos, boolean retained, String topic, String message) throws MqttException {
        // 获取客户端实例
        MqttClient client = MqttFactory.getInstance();

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        // 此处必须指明编码方式，否则会出现订阅端中文乱码的情况
        mqttMessage.setPayload(message.getBytes(StandardCharsets.UTF_8));
        // 主题的目的地，用于发布/订阅信息
        MqttTopic mqttTopic = client.getTopic(topic);
        // 提供一种机制来跟踪消息的传递进度
        // 用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        // 将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
        // 一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
        token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
        return token.getMessageId();
    }

    /**
     * 订阅话题
     *
     * @param topic 话题
     * @param qos   0-至多1次、1-至少1次、2-一次
     */
    public static void sub(Context mContext, String topic, int qos) {
        try {
            // 获取客户端实例
            MqttClient client = MqttFactory.getInstance();
            String Topic = String.format(Locale.CHINA, "/%s/%s%s", SPStaticUtils.getString(Constants.PRODUCT_KEY), DeviceUtil.getIMEIDeviceId(mContext), topic);
            Log.e("topic", Topic);
            client.subscribe(Topic, qos);
            Log.e("subscribe", "success");
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e("subscribe", "failed" + e.getMessage());
        }
    }

    /**
     * 取消订阅
     */
    public static void cancelSub(Context mContext) {
        try {
            // 获取客户端实例
            MqttClient client = MqttFactory.getInstance();
            String Topic = String.format(Locale.CHINA, "/%s/%s%s", SPUtils.getInstance().getString(Constants.PRODUCT_KEY), DeviceUtil.getIMEIDeviceId(mContext), Constants.COMMON);
            Log.e("topic", Topic);
            client.unsubscribe(Topic);
            Log.e("subscribe", "success");
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e("subscribe", "failed" + e.getMessage());
        }
    }

    /**
     * 断开连接
     */
    public static void disConnect() {
        try {
            // 获取客户端实例
            MqttClient client = MqttFactory.getInstance();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}