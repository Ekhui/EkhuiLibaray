package com.example.ekhuibaselibrary.utils

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.*
import java.util.*


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
fun getInstallAppIntent(context: Context, filePath: String, applicationID: String): Intent? {
    //apk文件的本地路径
    val apkFile = File(filePath)
    if (!apkFile.exists()) {
        return null
    }

    return Intent(Intent.ACTION_VIEW).apply {

        val contentUri: Uri = getUriForFile(context, apkFile, applicationID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        setDataAndType(contentUri, "application/vnd.android.package-archive")

    }
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
fun getUriForFile(mContext: Context?, file: File?, applicationID: String): Uri {
    return if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(
            mContext!!, "${applicationID}.fileprovider",
            file!!
        )
    } else {
        Uri.fromFile(file)
    }
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
        val fileFrom = FileInputStream(fromFile)
        val fileTo = FileOutputStream(toFile)
        val bt = ByteArray(1024)
        var c: Int
        while (fileFrom.read(bt).also { c = it } > 0) {
            fileTo.write(bt, 0, c) //将内容写到新文件当中
        }
        fileFrom.close()
        fileTo.close()
//        Log.e("readfile", "success")
    } catch (ex: java.lang.Exception) {
//        Log.e("readfile", ex.message)
    }
}

/**
 * Created by Ekhui on 2020/4/29.
 * 作用：打开文件选择器 选择文件
 */

fun openAndChooseFile(activity: AppCompatActivity, requestCode: Int) {
    val DOC = "application/msword"
    val DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    val XLS = "application/vnd.ms-excel application/x-excel"
    val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val PPT = "application/vnd.ms-powerpoint"
    val PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    val PDF = "application/pdf"
    val TXT = "text/plain"
    val ZIP = "application/x-zip-compressed"


    val intent = Intent(Intent.ACTION_GET_CONTENT)
    val mimeTypes =
        arrayOf(DOC, DOCX, XLS, XLSX, PPT, PPTX, PDF, TXT, ZIP)
    intent.type = "application/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    activity.startActivityForResult(intent, requestCode)
}

/**
 * Created by Ekhui on 2020/4/29.
 * 作用：打开文件选择器 选择文件(带图片)
 */

fun openAndChooseFileWithImage(activity: AppCompatActivity, requestCode: Int) {
    val DOC = "application/msword"
    val DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    val XLS = "application/vnd.ms-excel application/x-excel"
    val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val PPT = "application/vnd.ms-powerpoint"
    val PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    val PDF = "application/pdf"
    val TXT = "text/plain"
    val ZIP = "application/x-zip-compressed"
    val JPEG = "image/jpeg"
    val PNG = "image/png"
    val JPG = "image/jpg"


    val intent = Intent(Intent.ACTION_GET_CONTENT)
    val mimeTypes =
        arrayOf(DOC, DOCX, XLS, XLSX, PPT, PPTX, PDF, TXT, ZIP, JPEG, PNG, JPG)
    intent.type = "application/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    activity.startActivityForResult(intent, requestCode)
}


/**
 * Created by Ekhui on 2020/4/29.
 * 作用：  获取文件后缀
 */

fun getFileType(fileName: String?): String? {
    return fileName?.substring(fileName.lastIndexOf(".") + 1)
}


/**
 * Created by Ekhui on 2020/4/29.
 * 作用：  判断文件是不是图片类型
 */

fun isImage(fileName: String?): Boolean {
    val name = fileName?.substring(fileName.lastIndexOf(".") + 1)
    return name == "jpg" || name == "jpeg" || name == "png"
}


/**
 * Created by Ekhui on 2020/4/29.
 * 作用：  获取文件名
 */

fun getFileName(fileName: String?): String? {
    val indexStart = fileName?.lastIndexOf("/") ?: 0
//    val indexEnd = fileName?.lastIndexOf(".")
//    return fileName?.substring(indexStart!! + 1, indexEnd!!)
    return fileName?.substring(indexStart + 1)

}


fun getUriFromFile(
    context: Context?,
    file: File?,
    applicationID: String
): Uri? {
    if (context == null || file == null) {
        throw NullPointerException()
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            context.applicationContext,
            "$applicationID.fileprovider",
            file
        )
    } else {
        Uri.fromFile(file)
    }
}

/**
 * 使用第三方qq文件管理器打开
 *
 * @param uri
 *
 * @return
 */
fun isQQMediaDocument(uri: Uri): Boolean {
    return "com.tencent.mtt.fileprovider" == uri.authority
}

/**
 * @param uri
 * The Uri to check.
 *
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context
 * The context.
 * @param uri
 * The Uri to query.
 * @param selection
 * (Optional) Filter used in the query.
 * @param selectionArgs
 * (Optional) Selection arguments used in the query.
 *
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String?>?
): String? {
    var cursor: Cursor? = null
    val column = MediaStore.MediaColumns.DATA
    val projection = arrayOf(column)
    try {
        cursor =
            context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
    return getDataColumn(context, contentUri, null, null)
}

/**
 * Created by Ekhui on 2020/4/30.
 * 作用：从内存选择
 */

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * Created by Ekhui on 2020/4/30.
 * 作用：从下载选择
 */

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * Created by Ekhui on 2020/4/30.
 * 作用：从媒体库选择
 */

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
 */
fun getFilePathFromUri(context: Context?, uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        //一些三方的文件浏览器会进入到这个方法中，例如ES
        //QQ文件管理器不在此列
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory()
                    .toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(context!!, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs =
                arrayOf<String?>(split[1])
            return getDataColumn(context!!, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(
            uri.scheme,
            ignoreCase = true
        )
    ) { // MediaStore (and general)
        // Return the remote address
        if (isGooglePhotosUri(uri)) return uri.lastPathSegment
        if (isQQMediaDocument(uri)) {
            val path = uri.path
            val fileDir = Environment.getExternalStorageDirectory()
            val file =
                File(fileDir, path!!.substring("/QQBrowser".length, path.length))
            return if (file.exists()) file.toString() else null
        }
        return getDataColumn(context!!, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) { // File
        return uri.path
    }
    return null
}

/**
 * 保存图片
 *
 * @param bm
 */
@Throws(IOException::class)
fun saveFile(bm: Bitmap, context: Context) {
    val dirFile =
        File(Environment.getExternalStorageDirectory().path)
    if (!dirFile.exists()) {
        dirFile.mkdir()
    }
    val fileName = UUID.randomUUID().toString() + ".jpg"
    val myCaptureFile = File(
        Environment.getExternalStorageDirectory().path + "/DCIM/Camera/" + fileName
    )
    val bos =
        BufferedOutputStream(FileOutputStream(myCaptureFile))
    bm.compress(Bitmap.CompressFormat.JPEG, 80, bos)
    bos.flush()
    bos.close()
    //把图片保存后声明这个广播事件通知系统相册有新图片到来
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val uri = Uri.fromFile(myCaptureFile)
    intent.data = uri
    context.sendBroadcast(intent)
}