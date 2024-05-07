package com.linji.mylibrary.base;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.linji.mylibrary.net.BasePresenter;
import com.linji.mylibrary.net.oss.IOSSView;
import com.linji.mylibrary.net.oss.ImageItem;
import com.linji.mylibrary.net.oss.OssManager;
import com.linji.mylibrary.net.oss.ResourceInfo;
import com.linji.mylibrary.utils.ImageFileCompressEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public abstract class BaseUploadPicAct<T extends BasePresenter> extends BaseAct<T> implements IOSSView {
    protected int upLoadImageSize = 0;
    protected ArrayList<ResourceInfo> picNetPath = new ArrayList<>();


    public void selectPic(int selectModeConfig) {
        PictureSelector.create(mContext)
                .openSystemGallery(SelectMimeType.ofImage())
                .setCompressEngine(new ImageFileCompressEngine())
                .setSelectionMode(selectModeConfig)
                .forSystemResultActivity(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        dealSelectPic(result);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }
    private void dealSelectPic(ArrayList<LocalMedia> result) {
        ArrayList<ImageItem> images = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            LocalMedia localMedia = result.get(i);
            String path = StringUtils.isEmpty(localMedia.getCompressPath())?localMedia.getRealPath():localMedia.getCompressPath();
            File file = new File(path);
            ImageItem imageItem = new ImageItem();
            imageItem.name = file.getName();
            imageItem.path = path;
            images.add(imageItem);
        }
        upLoadImages(images);
    }

    protected void upLoadImages(ArrayList<ImageItem> images) {
        upLoadImageSize = images.size();
        showLoading();
        for (int i = 0; i < images.size(); i++) {
            ImageItem imageItem = images.get(i);
            upload(imageItem.path, imageItem.name, i);
        }
    }

    /**
     * OSS上传图片
     *
     * @param filePath 文件路径
     */
    public void upload(String filePath, String fileName, final int position) {
        picNetPath.clear();
        OssManager ossManager = OssManager.getInstance();
        ossManager.uploadPics(getMContext(), fileName, filePath, position, this);

    }

    @Override
    public void getOssPathNameSuccess(String fileName) {

    }

    @Override
    public void getOssPathNameFail() {
        upLoadImageSize = 0;
        showToast("上传失败，请您重新上传！");
        hideLoading();
    }

    @Override
    public void getOssPathNameSuccess(String uploadPath, int position) {
        if (uploadPath != null) {
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setPosition(position + 1);
            resourceInfo.setResourcePath(uploadPath);
            Log.i("===>", "onUpLoadListener:!!!! " + position);
            picNetPath.add(resourceInfo);
            Log.i("===>", "onUpLoadListener: " + upLoadImageSize + ":::" + picNetPath.size());
            if (upLoadImageSize == picNetPath.size()) {
                Collections.sort(picNetPath); // 按年龄排序
                upLoadImageSize = 0;
                submitPicData();
            }
        }
    }


    protected abstract void submitPicData();


}