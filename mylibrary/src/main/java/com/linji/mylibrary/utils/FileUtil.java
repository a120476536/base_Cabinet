package com.linji.mylibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.blankj.utilcode.util.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * @author: qingf
 * @date: 2016/6/12.
 * @desc: 文件管理工具
 */
public class FileUtil {
    private String SD_Path;
    private boolean sdCardExist;

    public String getSD_Path() {
        return SD_Path;
    }

    public boolean isSdCardExist() {
        return sdCardExist;
    }

    public FileUtil() {
        sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        // 得到当前外部存储设备的目录
        if (sdCardExist) {
            SD_Path = Environment.getExternalStorageDirectory() + File.separator;
        } else {
            SD_Path = Environment.getRootDirectory().getAbsolutePath() + File.separator;
        }
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return File
     * @throws IOException
     */
    public File createSDFile(String dirName, String fileName) throws IOException {
        createSDDir(dirName);
        File file = new File(SD_Path + dirName + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return File
     */
    public File createSDDir(String dirName) {
        File dir = new File(SD_Path + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     *   * 删除某个文件
     *   
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 读取asset下json文件
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            LogUtils.e("获取文件大小--" + "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String toFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    //将bytes写入到SD卡中
    //将bytes写到path这个目录中的fileName文件上
    public File writeSDFromInput(String path, String fileName, byte[] bytes) {
        File file = null;
        FileOutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path, fileName);
            //FileOutputStream写入数据，写入到file这个文件上
            output = new FileOutputStream(file);
            output.write(bytes, 0, bytes.length);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


}
