package com.linji.mylibrary.banner.loader;

import android.content.Context;
import android.widget.LinearLayout;


public abstract class ImageLoader implements ViewLoaderInterface<LinearLayout> {

    @Override
    public LinearLayout createView(Context context) {
        LinearLayout imageView = new LinearLayout(context);
        return imageView;
    }

}
