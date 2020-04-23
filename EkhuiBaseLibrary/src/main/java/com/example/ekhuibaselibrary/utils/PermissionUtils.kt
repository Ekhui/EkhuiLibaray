package com.example.ekhuibaselibrary.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by Ekhui on 2020/4/23.
 */
class PermissionUtils {

    companion object {
        private const val REQUEST_FILE_PERMISSION = 110


        /**
         * Created by Ekhui on 2020/4/23.
         * 作用：文件读写权限
         * 参数： needRequest 判断当前权限后若无权限，是否需要请求权限
         */

        fun requestFilePermission(activity: Activity, needRequest: Boolean): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                for (str in permissions) {
                    if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        if (needRequest)
                            activity.requestPermissions(permissions, REQUEST_FILE_PERMISSION)
                        return false
                    }
                }
            }
            return true
        }

    }
}