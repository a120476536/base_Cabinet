package com.linji.mylibrary.mqtt;



import static com.linji.mylibrary.model.EventMsg.MQTT_CONNECT_STATE;

import com.blankj.utilcode.util.LogUtils;
import com.linji.mylibrary.model.EventMsg;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

public class MqttFactory {

    private MqttProperties config;
 
    private static MqttFactory factory;
 
    private static MqttClient client;

    public boolean init(MqttProperties config) {
        factory = this;
        factory.config = config;
        try {
            MqttFactory.getInstance();
            LogUtils.e("[MQTT]初始化成功");
            EventBus.getDefault().post(new EventMsg<>(MQTT_CONNECT_STATE,"success"));
            return true;
        } catch (MqttException e) {
            LogUtils.e("[MQTT]初始化失败");
            EventBus.getDefault().post(new EventMsg<>(MQTT_CONNECT_STATE,"failed"));
            MqttFactory.reconnect();
            return false;
        }
    }
 
 
    /**
     * 获取客户端实例
     *      单例模式：存在即返回，不存在则初始化
     *
     * @return client
     * @throws MqttException 此处刻意抛出异常，否则无法执行断线重连
     */
    public static MqttClient getInstance() throws MqttException {
        if (client == null) {
            connect();
        }
        return client;
    }
 
 
    /**
     * 清空客户端实例
     *      当 mqtt 断开连接时，需清空 clientId，再执行断线重连
     */
    public static void clear() {
        client = null;
    }
 
 
    /**
     * 断线重连方法
     */
    public static void reconnect() {
        int count = 0;
        while (true) {
            clear();
            ++ count;
            try {
                LogUtils.e("----------------[MQTT]即将执行自动重连----------------");
                getInstance();
                LogUtils.e("----------------[MQTT]自动重连成功----------------");
                EventBus.getDefault().post(new EventMsg<>(MQTT_CONNECT_STATE,"reconnect_success"));
                break;
            } catch (MqttException e) {
                LogUtils.e("----------------[MQTT]自动重连失败，当前为第 {"+count+"}次尝试----------------");
                try {
                    TimeUnit.SECONDS.sleep(5 * count);
                } catch (InterruptedException ex) {
                    EventBus.getDefault().post(new EventMsg<>(MQTT_CONNECT_STATE,"reconnect_failed"));
                    LogUtils.e("----------------[MQTT]自动重连，休眠失败！----------------");
                }
            }
        }
    }
 
    /**
     * 客户端连接服务端
     *
     * @throws MqttException 此处刻意抛出异常，否则无法执行断线重连
     */
    private static void connect() throws MqttException {
        // 创建MQTT客户端对象
        LogUtils.e("[MQTT]config"+factory.config.toString());
        client = new MqttClient(factory.config.getUrl(), factory.config.getClientId(), new MemoryPersistence());
        // 连接设置
        MqttConnectOptions options = new MqttConnectOptions();
        // 是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
        // 设置为true表示每次连接服务器都是以新的身份
        options.setCleanSession(true);
        // 设置连接用户名
//        options.setUserName(factory.config.getUsername());
//         设置连接密码
//        options.setPassword(factory.config.getPassword().toCharArray());
        // 设置超时时间，单位为秒
        options.setConnectionTimeout(100);
        // 设置心跳时间 单位为秒，表示服务器每隔 20 秒的时间向客户端发送心跳判断客户端是否在线
        options.setKeepAliveInterval(20);
        // 设置回调
        client.setCallback(new MqttCallBack(factory.config.getContext()));
        client.connect(options);
    }
 
}