package com.linji.mylibrary.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.LogUtils;
import com.linji.mylibrary.R;
import com.linji.mylibrary.dia.DialogBuilder;
import com.linji.mylibrary.model.Constants;
import com.linji.mylibrary.model.UpdateBean;

import java.io.File;

/**
 * Created by Administrator on 2018/5/24.
 */

public class UpdateUtil {
    private Context mContext;
    private UpdateBean mUpdateInfo;
    private AlertDialog mDownLoadDialog;
    private File files;
    private HttpServerUtil httpServerUtils;

    public UpdateUtil(Context mContext) {
        this.mContext = mContext;
    }

    private String checkLocalApk() {
        PackageManager pack;
        try {
            pack = mContext.getPackageManager();
            PackageInfo info = pack.getPackageArchiveInfo(apkPath(), PackageManager.GET_ACTIVITIES);
            return info.versionName;
        } catch (Exception e) {
            LogUtils.e("package error" + e.toString());
            return "0.0.0";
        }
    }

    public void checkUpdate(UpdateBean updateBean) {
        this.mUpdateInfo = updateBean;
        if (updateBean.getVersionNo().equals(getPackageInfo().versionName)) {
            isNewVersion();
            return;
        }
        File f = new File(apkPath());
        if (f.exists()) {
            LogUtils.e("文件");
            files = f;
            //0代表相等，1代表version1大于version2，-1代表version1小于version2
            if (compareVersion(getPackageInfo().versionName, checkLocalApk()) == -1) {
                setInstallDia();
            } else {
                f.delete();
                LogUtils.e("文件版本不相同，更新");
                if (updateBean.getForceUpdateFlag().equals("1")) {//强制下载
                    showDownApkDia();
                } else {
                    setUpdateDia(updateBean);
                }
            }
        } else {
            LogUtils.e("下载");
            if (updateBean.getForceUpdateFlag().equals("1")) {//强制下载
                showDownApkDia();
            } else {
                setUpdateDia(updateBean);
            }
        }
    }


    private void setInstallDia() {
        new DialogBuilder(mContext).setTitle("版本升级")
                .setCountDownTime(15)
                .setContent("文件已下载，是否安装最新版？")
                .setLeft("安装")
                .setRight("忽略")
                .setCommonDialogBtnListener(new DialogBuilder.CommonDialogBtnListener() {
                    @Override
                    public void onLeftClick(Dialog dia, View view) {
                        installAPK();
                    }

                    @Override
                    public void onRightClick(Dialog dia, View view) {
                        dia.dismiss();
                    }
                }).build().show();
    }

    public void isNewVersion() {
        new DialogBuilder(mContext).setCountDownTime(15).setTitle("检查更新")
                .setContent("\n您的软件已是最新版本\n")
                .setLeftColor(Color.parseColor("#2B87DB"))
                .setLeft("关闭")
                .showRightBtn(false)
                .setCommonDialogBtnListener(new DialogBuilder.CommonDialogBtnListener() {
                    @Override
                    public void onLeftClick(Dialog dia, View view) {
                        dia.dismiss();
                    }

                    @Override
                    public void onRightClick(Dialog dia, View view) {

                    }
                }).build().show();
    }

    public void setUpdateDia(UpdateBean updateBean) {
        View updateView = LayoutInflater.from(mContext).inflate(R.layout.update, null);
        TextView updateContentTv = updateView.findViewById(R.id.update_content_tv);
        updateContentTv.setText(updateBean.getVersionDescribe());
        new DialogBuilder(mContext).setTitle("检查更新")
                .setContentView(updateView)
                .setCountDownTime(15)
                .setCommonDialogBtnListener(new DialogBuilder.CommonDialogBtnListener() {
                    @Override
                    public void onLeftClick(Dialog dia, View view) {
                        showDownApkDia();
                        dia.dismiss();
                    }

                    @Override
                    public void onRightClick(Dialog dia, View view) {
                        dia.dismiss();
                    }
                }).build().show();
    }

    public void showDownApkDia() {
        httpServerUtils = new HttpServerUtil();
        httpServerUtils.setDownSuccess(file -> installAPK(file));
        downApk();
    }

    private void downApk() {
        httpServerUtils.downloadNewApk(mContext, mUpdateInfo.getFileAddress());

    }

    public void installAPK() {
        installAPK(files);
    }

    public void installAPK(File file) {

        if (!file.exists()) return;
        if (mDownLoadDialog != null) {
            mDownLoadDialog.dismiss();
            mDownLoadDialog = null;
        }
        Intent intent = new Intent();
        Uri uri;
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //当前设备系统版本在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.putExtra("IMPLUS_INSTALL", "SILENT_INSTALL");// 自动安装并在安装后自动执行startActivity(intent):
        mContext.startActivity(intent);
//        System.exit(0);
//        Lztek.create(mContext).installApplication(file.getAbsolutePath());
    }

    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    public void closeHttp() {
//        httpServerUtils.cancelDown();
        if (mDownLoadDialog != null) mDownLoadDialog.dismiss();
    }

    private static String apkPath() {
        return new FileUtil().getSD_Path() + Constants.New_Down_Path + File.separator + Constants.New_Apk_Name;
    }

    /**
     * 版本号比较
     *
     * @param version1 在线版本
     * @param version2 本地版本
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    private int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static void installAPK(File file, Context context) {
        if (!file.exists()) return;
        Intent intent = new Intent();
        Uri uri;
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //当前设备系统版本在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.putExtra("IMPLUS_INSTALL", "SILENT_INSTALL");// 自动安装并在安装后自动执行startActivity(intent):
        context.startActivity(intent);
    }
}
