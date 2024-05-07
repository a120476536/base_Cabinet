package com.linji.mylibrary.net.oss;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.linji.mylibrary.net.BaseCallModel;
import com.linji.mylibrary.net.RetrofitHelper;

import rx.Subscriber;


/**
 * Created by Administrator on 2017/2/9.
 */

public class OssManager {

    private static OssManager ossManager;
    private static OSS oss;
    private static String bucketName;

    public static OssManager getInstance() {
        if (null == ossManager) {
            return new OssManager();
        }
        return ossManager;
    }

    public static void uploadPics(Context context, String fileName, final String filePath, final int position, final IOSSView iossView) {
        new Thread(() -> RetrofitHelper.getInstance().getService()
                .getOssConfigInfo()
                .subscribe(new Subscriber<BaseCallModel<OssUploadInfo>>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseCallModel<OssUploadInfo> ossUploadInfoBaseCallModel) {
                        if (ossUploadInfoBaseCallModel.getData() != null) {
                            OssUploadInfo ossUploadInfo = ossUploadInfoBaseCallModel.getData();
                            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossUploadInfo.getAccessKeyId(), ossUploadInfo.getAccessKeySecret(), ossUploadInfo.getSecurityToken());
                            ClientConfiguration configuration = new ClientConfiguration();
                            configuration.setConnectionTimeout(15 * 1000);
                            configuration.setSocketTimeout(15 * 1000);
                            configuration.setMaxConcurrentRequest(5);
                            configuration.setMaxErrorRetry(2);
                            bucketName = ossUploadInfo.getBucketName();
                            oss = new OSSClient(context, ossUploadInfo.getEndpoint(), credentialProvider);
                            putFile(bucketName, ossUploadInfo.getAccessUrl(), System.currentTimeMillis() + fileName, filePath, position, iossView);
                        }
                    }
                })).start();
    }

    public static void uploadPic(Context context, String fileName, final String filePath,final IOSSView iossView) {
        new Thread(() -> RetrofitHelper.getInstance().getService()
                .getOssConfigInfo()
                .subscribe(new Subscriber<BaseCallModel<OssUploadInfo>>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseCallModel<OssUploadInfo> ossUploadInfoBaseCallModel) {
                        if (ossUploadInfoBaseCallModel.getData() != null) {
                            OssUploadInfo ossUploadInfo = ossUploadInfoBaseCallModel.getData();
                            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossUploadInfo.getAccessKeyId(), ossUploadInfo.getAccessKeySecret(), ossUploadInfo.getSecurityToken());
                            ClientConfiguration configuration = new ClientConfiguration();
                            configuration.setConnectionTimeout(15 * 1000);
                            configuration.setSocketTimeout(15 * 1000);
                            configuration.setMaxConcurrentRequest(5);
                            configuration.setMaxErrorRetry(2);
                            bucketName = ossUploadInfo.getBucketName();
                            oss = new OSSClient(context, ossUploadInfo.getEndpoint(), credentialProvider);
                            putFile(bucketName, ossUploadInfo.getAccessUrl(), System.currentTimeMillis() + fileName, filePath, iossView);
                        }
                    }
                })).start();
    }
    /**
     * 上传文件
     */
    private static String putFile(String bucketName, String accessUrl,final String fileName, String filePath, final IOSSView iossView) {
        Log.e("===", "success：" + fileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, filePath);
        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                Log.e("===", "success：" + putObjectRequest.getObjectKey());
                iossView.getOssPathNameSuccess(accessUrl + "/" + fileName);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                Log.e("===", "error");
                iossView.getOssPathNameFail();

            }
        });
        return "";
    }

    /**
     * 上传文件
     */
    private static String putFile(String bucketName, String accessUrl, final String fileName, String filePath, final int position, final IOSSView iossView) {
        Log.e("===", "success：" + fileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, filePath);
        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                Log.e("===", "success：" + putObjectRequest.getObjectKey());
                iossView.getOssPathNameSuccess(accessUrl + "/" + fileName, position);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                Log.e("===", "error");
                iossView.getOssPathNameFail();

            }
        });
        return "";
    }
}
