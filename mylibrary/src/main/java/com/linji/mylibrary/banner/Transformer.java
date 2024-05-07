package com.linji.mylibrary.banner;


import androidx.viewpager.widget.ViewPager;

import com.linji.mylibrary.banner.transformer.AccordionTransformer;
import com.linji.mylibrary.banner.transformer.BackgroundToForegroundTransformer;
import com.linji.mylibrary.banner.transformer.CubeInTransformer;
import com.linji.mylibrary.banner.transformer.CubeOutTransformer;
import com.linji.mylibrary.banner.transformer.DefaultTransformer;
import com.linji.mylibrary.banner.transformer.DepthPageTransformer;
import com.linji.mylibrary.banner.transformer.FlipHorizontalTransformer;
import com.linji.mylibrary.banner.transformer.FlipVerticalTransformer;
import com.linji.mylibrary.banner.transformer.ForegroundToBackgroundTransformer;
import com.linji.mylibrary.banner.transformer.RotateDownTransformer;
import com.linji.mylibrary.banner.transformer.RotateUpTransformer;
import com.linji.mylibrary.banner.transformer.ScaleInOutTransformer;
import com.linji.mylibrary.banner.transformer.StackTransformer;
import com.linji.mylibrary.banner.transformer.TabletTransformer;
import com.linji.mylibrary.banner.transformer.ZoomInTransformer;
import com.linji.mylibrary.banner.transformer.ZoomOutSlideTransformer;
import com.linji.mylibrary.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
