package com.linji.mylibrary.base;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.linji.mylibrary.faceHelp.api.FaceApi;
import com.linji.mylibrary.faceHelp.camera.AutoTexturePreviewView;
import com.linji.mylibrary.faceHelp.manager.FaceSDKManager;
import com.linji.mylibrary.faceHelp.model.LivenessModel;
import com.linji.mylibrary.faceHelp.model.SingleBaseConfig;
import com.linji.mylibrary.faceHelp.utils.RegisterConfigUtils;
import com.linji.mylibrary.faceHelp.view.PreviewTexture;
import com.linji.mylibrary.net.BasePresenter;
import com.linji.mylibrary.utils.DeviceUtil;
import com.linji.mylibrary.utils.SystemUtil;
import com.linji.mylibrary.utils.face.FaceUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public abstract class BaseFaceAct extends BaseAct {

    protected TextView faceTipTv;
    protected TextureView irPreviewView;
    protected AutoTexturePreviewView mAutoTexturePreviewView;

    // RGB+IR 控件
    protected PreviewTexture[] mPreview;
    protected Camera[] mCamera;
    // 摄像头个数
    protected int mCameraNum;
    protected FaceUtil faceUtil = new FaceUtil(this);

    protected int count = 11;
    protected Timer timer;
    //识别系数，添加是0.8f，识别是1.0
    protected float faceConfig =1.0f;
    //质量控制，添加是true,识别是false
    protected boolean qualityControl = true;
    //是否识别添加人脸
    protected boolean addFace =false;
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (count > 1) {
                    count--;
                    runOnUiThread(() -> updateTime(count));
                } else {
                    count--;
                    finish();
                    if (count == -3) {
                        LogUtils.e("关闭拍照异常");
                        AppUtils.relaunchApp(true);
                    }
                }
            }
        }, 0, 1000);

        FaceSDKManager.getInstance().getFaceFeature().featurePush(new ArrayList<>());
        FaceSDKManager.getInstance().getFaceSearch().pushPersonFeatureList(FaceApi.getInstance().getAllUserList());
    }

    protected abstract void updateTime(int count);

    @Override
    protected void initView() {
        setBackHome(false);
        initFaceConfig();
    }

    private void initFaceConfig() {
        faceUtil.initListener();
        RegisterConfigUtils.initConfig();
        SystemUtil.openLight(getMContext());
        faceUtil.setTip(faceTipTv);
        startCamera();
        faceUtil.setFaceVerifyResultListener((model) -> {
            verifyResult(model);
        });
        if (addFace){
            faceUtil.setResultListener((mFeatures, mCropBitmap) -> {
                faceUtil.setResultListener(null);
                faceUtil.setFaceVerifyResultListener(null);
                recognitionResult(mFeatures, mCropBitmap);
            });
        }
    }


    /**
     * 验收到已有用户
     *
     * @param model
     */
    protected abstract void verifyResult(LivenessModel model);

    /**
     * 识别到新的用户人脸
     *
     * @param mFeatures
     * @param mCropBitmap
     */
    protected abstract void recognitionResult(byte[] mFeatures, Bitmap mCropBitmap);

    public void startCamera() {
        mCameraNum = Camera.getNumberOfCameras();
        if (!DeviceUtil.checkCamera(getMContext())) {
            return;
        }
        setFaceConfig();
        FaceSDKManager.getInstance().setActiveLog();
        if (SingleBaseConfig.getBaseConfig().getMirrorVideoNIR() == 1) {
            irPreviewView.setRotationY(180);
        }
        faceUtil.setAutoTexturePreviewView(mAutoTexturePreviewView);
        faceUtil.startCameraPreviewVerify();
        if (mCameraNum >= 2) {
            try {
                mPreview = new PreviewTexture[mCameraNum];
                mCamera = new Camera[mCameraNum];
                mPreview[1] = new PreviewTexture(getMContext(), irPreviewView);
                faceUtil.setIrPreviewView(irPreviewView);
                faceUtil.startCameraIrView(mCamera, mPreview, false);

            } catch (Exception e) {
               showToast("操作太快，稍后重试");
                e.printStackTrace();
            }
        }
    }



    private void setFaceConfig() {
        SingleBaseConfig.getBaseConfig().setQualityControl(qualityControl);
        SingleBaseConfig.getBaseConfig().setNose(faceConfig);
        SingleBaseConfig.getBaseConfig().setMouth(faceConfig);
        SingleBaseConfig.getBaseConfig().setLeftCheek(faceConfig);
        SingleBaseConfig.getBaseConfig().setRightCheek(faceConfig);
        SingleBaseConfig.getBaseConfig().setChinContour(faceConfig);
        if (mCameraNum == 1) {
            SingleBaseConfig.getBaseConfig().setType(1);
        } else if (mCameraNum == 2) {
            SingleBaseConfig.getBaseConfig().setType(2);
        }
        setCameraOrientation();
        RegisterConfigUtils.modityJson();
    }


    private void stopFace() {
        faceUtil.stopCamera();
        faceUtil.setResultListener(null);
        faceUtil.setFaceVerifyResultListener(null);
        faceUtil.setAddNewUser(false);
        closeCamera();
    }

    private void closeCamera() {
        Log.e("红外测试", "开始释放");
        if (mCameraNum >= 2) {
            for (int i = 0; i < mCameraNum; i++) {
                if (mCameraNum >= 2 && mCamera != null) {
                    if (mCamera[i] != null) {
                        mCamera[i].setPreviewCallback(null);
                        mCamera[i].stopPreview();
                        mPreview[i].release();
                        mCamera[i].release();
                        mCamera[i] = null;
                    }
                }
            }
        }
        Log.e("红外测试", "释放完成");
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        timer = null;
        stopFace();
        SystemUtil.closeLight(getMContext());
        super.onDestroy();

    }
}
