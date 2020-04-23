package com.example.ekhuibaselibrary.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 * Created by Ekhui on 2020/4/23.
 */
/**
 * 通知android媒体库更新文件夹
 *
 * @param filePath ilePath 文件绝对路径，、/sda/aaa/jjj.jpg
 */
fun scanFile(context: Context?, filePath: String) {
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