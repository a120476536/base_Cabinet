package com.linji.mylibrary.net;


import com.linji.mylibrary.model.BindStateBean;
import com.linji.mylibrary.model.UpdateBean;
import com.linji.mylibrary.net.oss.OssUploadInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {
    /**
     * 获取oss配置
     */
    @POST(UrlConfig.GET_OSS_PATH_NAME)
    Observable<BaseCallModel<OssUploadInfo>> getOssConfigInfo();


    @Multipart      //文件上传类型头
    @POST(UrlConfig.UPLOAD_FILE)
    Observable<BaseCallModel<UploadInfo>> uploadFile(@Part("type") RequestBody key, @Part MultipartBody.Part file);

    /**
     * 更新接口
     */
    @GET(UrlConfig.UPDATE)
    Observable<BaseCallModel<UpdateBean>> update(@Path("deviceCode") String deviceCode);


    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);


    /**
     * 获取设备绑定状态
     *
     * @return
     */
    @GET(UrlConfig.GET_BIND_STATE)
    Observable<BaseCallModel<BindStateBean>> getBindState(@Path("deviceNo") String deviceCode);

}
