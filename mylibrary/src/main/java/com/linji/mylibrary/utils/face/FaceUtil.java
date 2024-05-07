package com.linji.mylibrary.utils.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.linji.mylibrary.faceHelp.api.FaceApi;
import com.linji.mylibrary.faceHelp.callback.FaceDetectCallBack;
import com.linji.mylibrary.faceHelp.camera.AutoTexturePreviewView;
import com.linji.mylibrary.faceHelp.camera.CameraPreviewManager;
import com.linji.mylibrary.faceHelp.listener.DBLoadListener;
import com.linji.mylibrary.faceHelp.listener.SdkInitListener;
import com.linji.mylibrary.faceHelp.manager.FaceSDKManager;
import com.linji.mylibrary.faceHelp.model.LivenessModel;
import com.linji.mylibrary.faceHelp.model.SingleBaseConfig;
import com.linji.mylibrary.faceHelp.model.User;
import com.linji.mylibrary.faceHelp.utils.BitmapUtils;
import com.linji.mylibrary.faceHelp.utils.FaceOnDrawTexturViewUtil;
import com.linji.mylibrary.faceHelp.view.PreviewTexture;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.utils.DeviceUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FaceUtil {
    private static final String TAG = "FaceUtil";
    // RGB摄像头图像宽和高
    private static final int PREFER_WIDTH = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int PERFER_HEIGHT = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
    // 包含适配屏幕后的人脸的x坐标，y坐标，和width
    private float[] mPointXY = new float[4];
    private byte[] mFeatures = new byte[512];
    private Bitmap mCropBitmap;
    private boolean mCollectSuccess = false;
    private TextView tip;
    private AutoTexturePreviewView mAutoTexturePreviewView;
    private Context mContext;
    private boolean addNewUser = false;
    private boolean verify = false;
    private boolean faceRecognition = false;

    private TextureView irPreviewView;
    private volatile byte[] rgbData;
    private volatile byte[] irData;
    private int mLiveType;

    public void setFaceRecognition(boolean faceRecognition) {
        this.faceRecognition = faceRecognition;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public void setAddNewUser(boolean addNewUser) {
        this.addNewUser = addNewUser;
    }


    public FaceUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void setTip(TextView tip) {
        this.tip = tip;
    }

    public void setIrPreviewView(TextureView irPreviewView) {
        this.irPreviewView = irPreviewView;
    }

    public void setAutoTexturePreviewView(AutoTexturePreviewView mAutoTexturePreviewView) {
        this.mAutoTexturePreviewView = mAutoTexturePreviewView;
    }

    public void initListener() {
        if (FaceSDKManager.initStatus != FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().initModel(mContext, new SdkInitListener() {
                @Override
                public void initStart() {
                }

                @Override
                public void initLicenseSuccess() {
                }

                @Override
                public void initLicenseFail(int errorCode, String msg) {
                }

                @Override
                public void initModelSuccess() {
                    FaceSDKManager.initModelSuccess = true;
                    LogUtils.e(TAG, "模型加载成功，欢迎使用");
                }

                @Override
                public void initModelFail(int errorCode, String msg) {
                    FaceSDKManager.initModelSuccess = false;
                    if (errorCode != -12) {
                        LogUtils.e(TAG, "模型加载失败，请尝试重启应用");
                    }
                    if (SPStaticUtils.getString(Constants.FACE_ACTIVATE).equals("1")) {
                        AppUtils.relaunchApp(true);
                    } else {
                        ToastUtils.showLong("人脸识别暂未开启，请联系运营商");
                    }
                }
            });
        }
    }
    public void initDbApi(Context context){
        Future future = Executors.newSingleThreadExecutor().submit(() -> FaceApi.getInstance().init(new DBLoadListener() {
            @Override
            public void onStart(int successCount) {
            }

            @Override
            public void onLoad(final int finishCount, final int successCount, final float progress) {
            }

            @Override
            public void onComplete(final List<User> users, final int successCount) {
                ThreadUtils.runOnUiThread(() -> {
                    FaceApi.getInstance().setUsers(users);
                    FaceSDKManager.getInstance().initDataBases(context);
                });

            }

            @Override
            public void onFail(final int finishCount, final int successCount, final List<User> users) {
                ThreadUtils.runOnUiThread(() -> {
                    FaceApi.getInstance().setUsers(users);
                    ToastUtils.showShort("人脸库加载失败,共" + successCount + "条数据, 已加载" + finishCount + "条数据");
                    FaceSDKManager.getInstance().initDataBases(mContext);
                });

            }
        }, mContext));
    }
    public void startCameraPreviewVerify() {
        long startTime = System.currentTimeMillis();
        addNewUser = false;
        verify = false;
        if (SingleBaseConfig.getBaseConfig().getRBGCameraId() != -1) {
            CameraPreviewManager.getInstance().setCameraFacing(SingleBaseConfig.getBaseConfig().getRBGCameraId());
        } else {
            CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        }
        mLiveType = SingleBaseConfig.getBaseConfig().getType();
        CameraPreviewManager.getInstance().openCamera();
        CameraPreviewManager.getInstance().startPreview(mContext, this.mAutoTexturePreviewView,
                PREFER_WIDTH, PERFER_HEIGHT, (data, camera, width, height) -> {
                    if (System.currentTimeMillis() - startTime < 1500) {//两秒后再进行人脸数据处理
                        return;
                    }
                    dealRgb(data);
                });
    }

    public void startCameraIrView(Camera[] mCamera, PreviewTexture[] mPreview, boolean isRegister) {
        if (SingleBaseConfig.getBaseConfig().getRBGCameraId() != -1) {
            mCamera[1] = Camera.open(Math.abs(SingleBaseConfig.getBaseConfig().getRBGCameraId() - 1));
        } else {
            mCamera[1] = Camera.open(0);
        }
        mCamera[1].startPreview();
        ViewGroup.LayoutParams layoutParams = irPreviewView.getLayoutParams();
        int w = layoutParams.width;
        int h = layoutParams.height;
        int cameraRotation = SingleBaseConfig.getBaseConfig().getNirVideoDirection();
        mCamera[1].setDisplayOrientation(cameraRotation);
        if (cameraRotation == 90 || cameraRotation == 270) {
            layoutParams.height = w;
            layoutParams.width = h;
            // 旋转90度或者270，需要调整宽高
        } else {
            layoutParams.height = h;
            layoutParams.width = w;
        }
        irPreviewView.setLayoutParams(layoutParams);
        mPreview[1].setCamera(mCamera[1], PREFER_WIDTH, PERFER_HEIGHT);
        mCamera[1].setPreviewCallback((data, camera) -> {
            dealIr(data);
        });
    }

    private void dealRgb(byte[] data) {
        rgbData = data;
        checkData();
    }

    private void dealIr(byte[] data) {
        SystemClock.sleep(30);
        irData = data;
        checkData();
    }

    private LivenessModel faceAdoptModel;
    private int verifyFailNum = 0;

    private void checkData() {
        if (rgbData != null) {
            FaceSDKManager.getInstance().onDetectCheck(rgbData, irData, null,
                    PERFER_HEIGHT, PREFER_WIDTH, mLiveType, new FaceDetectCallBack() {
                        @Override
                        public void onFaceDetectCallback(LivenessModel livenessModel) {
                            // 预览模式
                            if (faceAdoptModel == livenessModel) {
                                return;
                            }
                            faceAdoptModel = livenessModel;
                            if (livenessModel != null) {
                                if (faceRecognition) {
                                    checkLiveScore(livenessModel);
                                } else {
                                    checkFaceBound(livenessModel);
                                }
                            }
                        }

                        @Override
                        public void onTip(int code, String msg) {
//                            if (tip != null) {
//                                tip.setText(msg);
//                            }

                        }

                        @Override
                        public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
                        }
                    });

            rgbData = null;
            irData = null;
        }
    }

    private synchronized void verifyDate() {
        if (rgbData != null && irData != null) {
            FaceSDKManager.getInstance().onDetectCheck(rgbData, irData, null,
                    PERFER_HEIGHT, PREFER_WIDTH, 2, new FaceDetectCallBack() {

                        @Override
                        public void onFaceDetectCallback(LivenessModel livenessModel) {
                            if (faceVerifyResultListener != null) {
                                faceVerifyResultListener.onResult(livenessModel);
                            }
                        }

                        @Override
                        public void onTip(int code, String msg) {

                        }

                        @Override
                        public void onFaceDetectDarwCallback(LivenessModel livenessModel) {

                        }
                    });
        }
    }


    public void stopCamera() {
        CameraPreviewManager.getInstance().stopPreview();
        mCollectSuccess = false;
        if (mCropBitmap != null) {
            if (!mCropBitmap.isRecycled()) {
                mCropBitmap = null;
            }
        }
    }


    /**
     * 检查人脸边界
     *
     * @param livenessModel LivenessModel实体
     */
    private void checkFaceBound(final LivenessModel livenessModel) {
        // 当未检测到人脸UI显示
        ThreadUtils.runOnUiThread(() -> {

            if (mCollectSuccess) {
                return;
            }
            if (livenessModel == null || livenessModel.getFaceInfo() == null) {
                if (tip != null) {
                    tip.setText("请保持面部在取景框内");
                }
                return;
            }
            if (livenessModel.getFaceSize() > 1) {
                if (tip != null) {
                    tip.setText("请保证取景框内只有一个人脸");
                }
                return;

            }
            mPointXY[0] = livenessModel.getFaceInfo().centerX;   // 人脸X坐标
            mPointXY[1] = livenessModel.getFaceInfo().centerY;   // 人脸Y坐标
            mPointXY[2] = livenessModel.getFaceInfo().width;     // 人脸宽度
            mPointXY[3] = livenessModel.getFaceInfo().height;    // 人脸高度
            FaceOnDrawTexturViewUtil.converttPointXY(mPointXY, mAutoTexturePreviewView,
                    livenessModel.getBdFaceImageInstance(), livenessModel.getFaceInfo().width);
            float previewWidth = AutoTexturePreviewView.previewWidth;
            if (mPointXY[2] < 150 || mPointXY[3] < 150) {
                if (tip != null) {
                    tip.setText("请保证人脸区域清晰无遮挡");
                }
                // 释放内存
                destroyImageInstance(livenessModel.getBdFaceImageInstanceCrop());
                return;
            }

            if (mPointXY[2] > previewWidth || mPointXY[3] > previewWidth) {
                if (tip != null) {
                    tip.setText("请保证人脸区域清晰无遮挡");
                }
                // 释放内存
                destroyImageInstance(livenessModel.getBdFaceImageInstanceCrop());
                return;
            }

           /* if (roi(livenessModel.getFaceInfo())) {
                tip.setText("请保证人脸在识别区域");
                // 释放内存
                destroyImageInstance(livenessModel.getBdFaceImageInstanceCrop());
                return;
            }*/

            // 检验活体分值
            checkLiveScore(livenessModel);
        });
    }

    /*
     * faceDdInfo 检测到的人脸对象
     * l 可检测范围左侧位置
     * t 可检测范围顶部位置
     * r 可检测范围右侧位置
     * b 可检测范围底部位置
     * */
    private boolean roi(FaceInfo faceBdInfo) {
        float l = DeviceUtil.dip2px(mContext, 80);
        float t = DeviceUtil.dip2px(mContext, 50);
        float r = AutoTexturePreviewView.previewWidth - l;
        float b = AutoTexturePreviewView.previewHeight;
        float face_l = faceBdInfo.centerX - faceBdInfo.width / 2;
        float face_t = faceBdInfo.centerY - faceBdInfo.height / 2;
        float face_r = faceBdInfo.centerX + faceBdInfo.width / 2;
        float face_b = faceBdInfo.centerY + faceBdInfo.height / 2;
        if (face_l >= l && face_t >= t && face_r <= r && face_b <= b) {
            return false;
        }
        return true;
    }

    /**
     * 检验活体分值
     *
     * @param livenessModel LivenessModel实体
     */
    private void checkLiveScore(LivenessModel livenessModel) {
        if (livenessModel == null || livenessModel.getFaceInfo() == null) {
            if (tip != null) {
                tip.setText("请保持面部在取景框内");
            }
            return;
        }
        float rgbLivenessScore = livenessModel.getRgbLivenessScore();
        float rgbLiveScore = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
        float nirLivenessScore = livenessModel.getIrLivenessScore();
        float nirLiveScore = SingleBaseConfig.getBaseConfig().getNirLiveScore();
        Log.e(TAG, "rgbScore = " + rgbLivenessScore);
        Log.e(TAG, "nirScore = " + nirLivenessScore);
        if (!livenessModel.isQualityCheck()) {
            if (tip != null) {
                tip.setText("请正视摄像头");
            }
            return;
        }
        if (Camera.getNumberOfCameras() == 1) {
            if (rgbLivenessScore < rgbLiveScore) {
                if (tip != null) {
                    tip.setText("活体检测未通过");
                    return;
                }
            }
        } else {
            if (rgbLivenessScore < rgbLiveScore || nirLivenessScore < nirLiveScore) {
                if (tip != null) {
                    tip.setText("活体检测未通过");
                    return;
                }
            }
        }
        checkLivenessModel(livenessModel);

    }

    private void checkLivenessModel(LivenessModel livenessModel) {
        if (livenessModel.getUser() == null) {//查找用户失败
            verifyFailNum++;
            if (livenessModel.isMultiFrame() && verifyFailNum > 1) {
                if (tip != null) {
                    ThreadUtils.runOnUiThread(() -> {
                        tip.setText("正在识别");
                    });

                }
                if (addNewUser) {
                    return;
                }
                addNewUser = true;
                if (resultListener != null) {
                    resultListener.onResult(livenessModel.getFeature(), BitmapUtils.getInstaceBmp(livenessModel.getBdFaceImageInstance()));
                    verifyFailNum = 0;
                } else {
                    if (faceVerifyResultListener != null) {
                        if (livenessModel.getUser() == null) {
                            ThreadUtils.runOnUiThread(() -> {
                                tip.setText("请正视摄像头");
                            });
                        }
                        faceVerifyResultListener.onResult(livenessModel);
                        addNewUser = false;
                        verifyFailNum = 0;
                    }
                }
            }
        } else {//查找用户成功，且认证
            if (tip != null) {
                ThreadUtils.runOnUiThread(() -> {
                    tip.setText("正在识别");
                });
            }
            if (faceVerifyResultListener != null && !verify) {
                verify = true;
                faceVerifyResultListener.onResult(livenessModel);
                verifyFailNum = 0;
            }
        }
    }


    public interface ResultListener {
        void onResult(byte[] mFeatures, Bitmap mCropBitmap);
    }

    public interface FaceVerifyResultListener {
        void onResult(LivenessModel model);
    }

    private ResultListener resultListener;

    public FaceVerifyResultListener faceVerifyResultListener;

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setFaceVerifyResultListener(FaceVerifyResultListener faceVerifyResultListener) {
        this.faceVerifyResultListener = faceVerifyResultListener;
    }

    /**
     * 释放图像
     *
     * @param imageInstance
     */
    private void destroyImageInstance(BDFaceImageInstance imageInstance) {
        if (imageInstance != null) {
            imageInstance.destory();
        }
    }
}
