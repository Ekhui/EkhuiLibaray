package com.example.ekhuibaselibrary.utils

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * Created by Ekhui on 2020/4/23.
 */

/**
 * 获取安装App(支持7.0)的意图
 *
 * @param context
 * @param filePath
 * @return
 */
fun getInstallAppIntent(context: Context, filePath: String,applicationID:String): Intent? {
    //apk文件的本地路径
    val apkFile = File(filePath)
    if (!apkFile.exists()) {
        return null
    }
    val intent = Intent(Intent.ACTION_VIEW)
    val contentUri: Uri = getUriForFile(context, apkFile,applicationID)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
    return intent
}


/**
 * 通知android媒体库更新文件夹
 *
 * @param filePath FilePath 文件绝对路径，、/sda/aaa/jjj.jpg
 */
fun refreshMedia(context: Context?, filePath: String) {
    try {
        MediaScannerConnection.scanFile(
            context, arrayOf(filePath), null
        ) { path, uri ->
            fun onScanCompleted(path: String, uri: Uri) {

            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 将文件转换成uri(支持7.0)
 *
 * @param mContext
 * @param file
 * @return
 */
fun getUriForFile(mContext: Context?, file: File? ,applicationID:String): Uri {
    var fileUri: Uri? = null
    fileUri = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(
            mContext!!, "${applicationID}.fileprovider",
            file!!
        )
    } else {
        Uri.fromFile(file)
    }
    return fileUri
}



fun copyFile(
    fromFile: File,
    toFile: File,
    rewrite: Boolean
) {
    if (!fromFile.exists()) {
        return
    }
    if (!fromFile.isFile) {
        return
    }
    if (!fromFile.canRead()) {
        return
    }
    if (!toFile.parentFile.exists()) {
        toFile.parentFile.mkdirs()
    }
    if (toFile.exists() && rewrite) {
        toFile.delete()
    }

    //当文件不存时，canWrite一直返回的都是false
    //  if (!toFile.canWrite()) {
    //  MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());
    // Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);
    //   return ;
    //   }
    try {
        val fosfrom = FileInputStream(fromFile)
        val fosto = FileOutputStream(toFile)
        val bt = ByteArray(1024)
        var c: Int
        while (fosfrom.read(bt).also { c = it } > 0) {
            fosto.write(bt, 0, c) //将内容写到新文件当中
        }
        fosfrom.close()
        fosto.close()
//        Log.e("readfile", "success")
    } catch (ex: java.lang.Exception) {
//        Log.e("readfile", ex.message)
    }
}