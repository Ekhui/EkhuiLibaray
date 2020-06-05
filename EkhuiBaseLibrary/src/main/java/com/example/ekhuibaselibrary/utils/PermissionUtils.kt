package com.example.ekhuibaselibrary.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ekhuibaselibrary.R
import com.example.ekhuibaselibrary.exFunction.toastAndLog
import com.mylhyl.circledialog.CircleDialog


/**
 * Created by Ekhui on 2020/4/23.
 */
class PermissionUtils {

    companion object {
        private const val REQUEST_FILE_PERMISSION = 110
        private const val REQUEST_OPEN_SETTING = 9999


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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permissions[0]
                            )
                        ) {
                            "请开启文件读写权限".toastAndLog()
                            return false
                        }
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permissions[1]
                            )
                        ) {
                            "请开启文件读写权限".toastAndLog()
                            return false
                        }
                        if (needRequest)
                            activity.requestPermissions(permissions, REQUEST_FILE_PERMISSION)
                        return false
                    }
                }
            }
            return true
        }

        /**
         * Created by Ekhui on 2020/4/23.
         * 作用：文件读写权限
         * 参数： needRequest 判断当前权限后若无权限，是否需要请求权限
         */
        fun requestFileWriteReadPermission(
            activity: AppCompatActivity,
            needRequest: Boolean,
            needClose: Boolean = true
        ): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                for (str in permissions) {
                    if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                        勾选不在询问
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permissions[0]
                            )
                        ) {
                            openSetting(activity, needClose)
                            return false
                        }
//                        勾选不在询问
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permissions[1]
                            )
                        ) {
                            openSetting(activity, needClose)
                            return false
                        }
                        if (needRequest)
                            activity.requestPermissions(permissions, REQUEST_FILE_PERMISSION)
                        return false
                    }
                }
            }
            return true
        }

        /**
         * Created by Ekhui on 2020/4/23.
         * 作用：前往APP设置页面
         */
        private fun openSetting(activity: AppCompatActivity, needClose: Boolean = true) {
            CircleDialog.Builder().setNegative("关闭") {
                if (needClose)
                    activity.finish()

            }.setPositive("设置") {
                try {
                    val localIntent = Intent()
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts(
                        "package",
                        activity.packageName,
                        null
                    )
                    activity.startActivityForResult(localIntent, REQUEST_OPEN_SETTING)
                } catch (e: Exception) {
                }
            }.setTitle("设置").setText("\n您当前未开启存储读写权限 请前往设置\n")
                .setTextColor(ContextCompat.getColor(activity, R.color.black))
                .show(activity.supportFragmentManager)
        }

    }
}