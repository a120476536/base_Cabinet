package com.linji.mylibrary.net;


import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.google.gson.Gson;
import com.linji.mylibrary.model.Constants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Retrofit retrofit;

    private RetrofitHelper(String baseUrl) {
        createRetrofit(baseUrl);
    }

    public static RetrofitHelper getInstance() {
        return new RetrofitHelper(SPStaticUtils.getString(Constants.BASE_URL, UrlConfig.BASE_URL));
    }

    public static RetrofitHelper getInstance(String baseUrl) {
        return new RetrofitHelper(baseUrl);
    }

    private Retrofit createRetrofit(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public ApiService getService() {
        return retrofit.create(ApiService.class);
    }


    private OkHttpClient createOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //添加请求头
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", SPStaticUtils.getString(Constants.USER_TOKEN))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(sLoggingInterceptor)
                .build();

        return okHttpClient;
    }

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                LogUtils.d("request.body() == null");
            }
            LogUtils.w(request.url().toString()); //打印url信息
            LogUtils.w((request.body() != null ? parseParams(request.body(), requestBuffer) : "")); //打印url信息
            final Response response = chain.proceed(request);
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                LogUtils.w(headers.name(i) + ": " + headers.value(i));//打印请求头
            }
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (contentLength != 0) {
                LogUtils.w(buffer.clone().readString(charset));//打印返回的数据
            }
            return response;
        }
    };

    @NonNull
    private static String parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }
}
