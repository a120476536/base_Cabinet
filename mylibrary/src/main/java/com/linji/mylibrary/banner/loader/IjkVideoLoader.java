package com.linji.mylibrary.banner.loader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.linji.mylibrary.widget.MyJzvdStd;
import com.linji.mylibrary.banner.listener.OnVideoStateListener;

public class IjkVideoLoader extends VideoLoader {

    @Override
    public void displayView(Context context, Object path, View view, OnVideoStateListener listener) {
        MyJzvdStd jzVideo = (MyJzvdStd)view;
        jzVideo.setUp(path.toString(), "");
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .frame(1000)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        RequestBuilder<Drawable> requestManager = Glide.with(context).load(path).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true);
        Glide.with(context).load(path).thumbnail(requestManager).apply(requestOptions).into(jzVideo.thumbImageView);
        jzVideo.setOnVideoStateListener(listener);
        view.setTag("video");
    }
}
