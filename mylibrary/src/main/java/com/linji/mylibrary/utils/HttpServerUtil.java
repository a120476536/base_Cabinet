package com.linji.mylibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.model.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpServerUtil {

    private Request request;

    public HttpServerUtil() {
    }

    private DownSuccess downSuccess;

    public void setDownSuccess(DownSuccess downSuccess) {
        this.downSuccess = downSuccess;
    }

    public interface DownSuccess {
        void installApk(File file);
    }

    public void downloadNewApk(Context context, String fileAddress) {
        //显示进度条
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向进度条, 可以显示进度
        dialog.setTitle("正在下载新版本...");
        dialog.setCancelable(false);
        dialog.show();
        //APK文件路径
        final String url = fileAddress;
        request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                String strFailure = "新版本APK下载失败";
                ToastUtils.showShort(strFailure);
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {

                    final ResponseBody responseBody = (ResponseBody) response.body();
                    if (response.isSuccessful() && responseBody != null) {
                        final long total = responseBody.contentLength();
                        final InputStream is = responseBody.byteStream();
                        final File file = new FileUtil().createSDFile(Constants.New_Down_Path, Constants.New_Apk_Name);
                        final FileOutputStream fos = new FileOutputStream(file);

                        int len;
                        final byte[] buf = new byte[2048];
                        long sum = 0L;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            float downloadProgress = (sum * 100F / total);
                            dialog.setProgress((int) downloadProgress);//下载中，更新进度
                        }
                        fos.flush();
                        responseBody.close();
                        is.close();
                        fos.close();
                        downSuccess.installApk(file);

                    } else {
                        String strFailure = "新版本APK获取失败";
                        ToastUtils.showShort(strFailure);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String strException = "新版本APK下载安装出现异常";
                    ToastUtils.showShort(strException);
                } finally {
                    /*正常应该在finally中进行关流操作，以避免异常情况时没有关闭IO流，导致内存泄露
                     *因为本场景下异常情况可能性较小，为了代码可读性直接在正常下载结束后关流
                     */
                    dialog.dismiss();//dialog消失
                }
            }
        });
    }
}
