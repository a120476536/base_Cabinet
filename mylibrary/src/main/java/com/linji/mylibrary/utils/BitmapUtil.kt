package com.linji.mylibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.linji.mylibrary.model.Constants
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtil {

    /**
     * 单个文件复制
     */
    @JvmStatic
    fun copyFile(srcFile: File, destFile: File) {

        val fis = FileInputStream(srcFile)
        val fos = FileOutputStream(destFile)

        val bis = BufferedInputStream(fis)
        val bos = BufferedOutputStream(fos)

        val buf = ByteArray(1024)

        var len: Int
        while (true) {
            len = bis.read(buf)
            if (len == -1) break
            bos.write(buf, 0, len)
        }
        fis.close()
        fos.close()

        Log.e("TAG", "copy success")
    }

    @JvmStatic
    fun getBitmap(context: Context, path: String): Bitmap? {
        var bitmap: Bitmap? = null
        if (TextUtils.isEmpty(path)) {
            return bitmap
        }
        try {
            var file: File = File(path)
            if (file.exists()) {
                var opt = BitmapFactory.Options()
                opt.inPreferredConfig = Bitmap.Config.RGB_565
                opt.inPurgeable = true
                opt.inInputShareable = true
                bitmap = BitmapFactory.decodeFile(path, opt)
            }
        } catch (e: java.lang.Exception) {
            LogUtils.e(e.toString())
        }
        return bitmap

    }

    @JvmStatic
    fun saveBitmap(fileName: String, bitmap: Bitmap): String {
        val imgFile: File = File(FileUtil().sD_Path + Constants.New_Img_Path)
        if (!imgFile.exists()) {
            imgFile.mkdirs();
        }
        val file: File = File(FileUtil().sD_Path + Constants.New_Img_Path, fileName)
        if (bitmap == null) {
            null
        }
        if (file.exists()) {
            null
        }
        try {
            val opt = FileOutputStream(file)
            when {
                fileName.endsWith("jpg") -> bitmap.compress(Bitmap.CompressFormat.JPEG, 90, opt)
                fileName.endsWith("png") -> bitmap.compress(Bitmap.CompressFormat.PNG, 90, opt)
                else -> bitmap.compress(Bitmap.CompressFormat.WEBP, 90, opt)
            }
            opt.flush()
            opt.close()
            return file.path
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null.toString()
    }

}
