package com.linji.mylibrary.banner.loader;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.linji.mylibrary.banner.listener.OnVideoStateListener;

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayView(Context context, Object path, LinearLayout imageView, OnVideoStateListener listener) {
        ImageView img = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setGravity(Gravity.CENTER);
        Glide.with(context).load(String.valueOf(path)).skipMemoryCache(true).into(img);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.addView(img);

    }
}