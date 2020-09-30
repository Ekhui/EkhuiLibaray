package com.example.ekhuibaselibrary.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build.VERSION
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.SoftReference

/**
 * Created by Ekhui on 2018/4/12.
 */

class ScreenTools(private val _activity: SoftReference<AppCompatActivity>) {

    var imageName: String? = null   //照片保存的名字
    var imagePath: String? = null   //照片保存路径
    var isSaveImageEnable = true  //需不需要保存图片
    var requestWritePermission = true //申请文件保存权限

    private var _windowWidth = 0
    private var _windowHeight = 0
    private var _screenDensity = 0
    private var _virtualDisplay: VirtualDisplay? = null
    private var _imageReader: ImageReader? = null
    private var _mediaProjectionManager: MediaProjectionManager? = null
    private var _mediaProjection: MediaProjection? = null
    private var _resultCode = 0
    private var _resultData: Intent? = null
    private var _bitmap: Bitmap? = null
    private var _onScreenListener: OnScreenListener? = null //回调
    fun setCaptureListener(captureListener: OnScreenListener?) {
        _onScreenListener = captureListener
    }

    companion object {
        private val TAG = ScreenTools::class.java.name
        const val PERMISSION_CODE = 0x000123

        @JvmStatic
        fun getInstance(activity: AppCompatActivity): ScreenTools {
            return ScreenTools(SoftReference(activity))
        }
    }

    init {
        _activity.get()?.apply {
            val displayMetrics = DisplayMetrics()
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager).run {
                _windowWidth = this@run.defaultDisplay.width
                _windowHeight = this@run.defaultDisplay.height
                defaultDisplay.getMetrics(displayMetrics)
            }
            _screenDensity = displayMetrics.densityDpi
            imagePath = "$externalCacheDir/screen/"
            _imageReader = ImageReader.newInstance(_windowWidth, _windowHeight, 1, 2)
            _mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        }

    }

    /**
     * Created by Ekhui on 2018/4/12.
     * 作用：开始截屏
     */
    fun startScreen() {
        if (requestWritePermission && isSaveImageEnable)
            if (VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(_activity.get()!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == -1) {
                    _activity.get()?.requestPermissions(arrayOf("android.permission.WRITE_EXTERNAL_STORAGE"), PERMISSION_CODE)
                    return
                }
            }

        if (canStart()) {
            Handler().postDelayed({
                Log.d(TAG, "开始截图")
                startCapture()
            }, 200L)
        }
    }

    /**
     * Created by Ekhui on 2018/4/12.
     * 作用: 截屏
     */
    private fun startCapture() {
        if (imageName.isNullOrBlank())
            imageName = System.currentTimeMillis().toString() + ".png"


        _imageReader?.acquireLatestImage()?.apply {
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width
            _bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
            _bitmap?.copyPixelsFromBuffer(buffer)
            if (_bitmap != null)
                _bitmap = Bitmap.createBitmap(_bitmap!!, 0, 0, width, height)

            close()
            releaseVD()
            if (_bitmap != null) {
                Log.d(TAG, "Bitmap创建成功")
                if (isSaveImageEnable)
                    saveToFile()
                else
                    _onScreenListener?.onScreenSuccess(_bitmap)
            } else {
                Log.d(TAG, "Bitmap创建失败")
                _onScreenListener?.onScreenFailed("Bitmap创建失败")
            }

        }
    }

    /**
     * Created by Ekhui on 2018/4/12.
     * 作用：判断权限
     */
    private fun canStart(): Boolean {
        Log.d(TAG, "canStart")
        return if (_mediaProjection != null) {
            setUpVirtualDisplay()
            true
        } else if (_resultCode != 0 && _resultData != null) {
            setUpMediaProjection()
            setUpVirtualDisplay()
            true
        } else {
            Log.d(TAG, "需要点弹窗")
            _activity.get()?.startActivityForResult(_mediaProjectionManager!!.createScreenCaptureIntent(), 1)
            false
        }
    }

    private fun setUpVirtualDisplay() {
        _virtualDisplay = _mediaProjection?.createVirtualDisplay("ScreenTools", _windowWidth, _windowHeight, _screenDensity,
                16, _imageReader?.surface, null, null)
    }

    private fun setUpMediaProjection() {
        _mediaProjection = _mediaProjectionManager?.getMediaProjection(_resultCode, _resultData
                ?: return)
    }

    /**
     * Created by Ekhui on 2018/4/12.
     * 作用：保存
     */
    private fun saveToFile() {

        try {
            val fileFolder = File(imagePath)
            if (!fileFolder.exists()) {
                fileFolder.mkdirs()
            }
            val file = File(imagePath, imageName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val out = FileOutputStream(file)
            _bitmap?.compress(CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            _onScreenListener?.onScreenSuccess(_bitmap, file.absolutePath)


        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            e.printStackTrace()
        }
    }

    private fun releaseVD() {
        Log.i(TAG, "releaseVD: ")
        _virtualDisplay?.release()
        _virtualDisplay = null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult requestCode:$requestCode resultCode:$resultCode")
        if (requestCode == 1) {
            if (resultCode != -1) {
                return
            }
            _resultCode = resultCode
            _resultData = data
            startScreen()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == 0)
                    startScreen()
                else _onScreenListener?.onScreenFailed("没有保存权限")
            else -> {

            }
        }
    }

    fun clear() {
        _bitmap?.recycle()
        _bitmap = null
        releaseVD()
        _mediaProjection?.stop()
        _activity.clear()
    }


    interface OnScreenListener {
        fun onScreenSuccess(bitmap: Bitmap?, path: String? = "")
        fun onScreenFailed(msg: String?)
    }


}