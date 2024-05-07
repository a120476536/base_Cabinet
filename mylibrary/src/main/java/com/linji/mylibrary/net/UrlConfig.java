
package com.linji.mylibrary.net;

public class UrlConfig {

    //域名
//    public static String BASE_URL = "http://39.105.99.153:8009";
    public static String BASE_URL = "https://cb.chuangjizn.cn";

    public static final String ACTIVATE_DEVICE = "http://device.test.linji-cloud.cn:80";

//    public static final String ACTIVATE_DEVICE = "https://device.chuangjizn.cn";

    //获取Oss配置
    public static final String GET_OSS_PATH_NAME = "/common/ossAssumeRole";

    //文件上传
    public static final String UPLOAD_FILE = "/common/upload";

    //版本更新接口
    public static final String UPDATE = "/equipment/lastVersion/{deviceCode}";


    //获取绑定设备状态
    public static final String GET_BIND_STATE = "/api/equipment/info/{deviceNo}";

}
