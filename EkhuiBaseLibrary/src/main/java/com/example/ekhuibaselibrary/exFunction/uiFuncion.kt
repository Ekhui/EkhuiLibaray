package com.example.ekhuibaselibrary.exFunction

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils.runOnUiThread
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ekhuibaselibrary.R
import com.example.ekhuibaselibrary.itemDecoration.RecycleLineDivider
import com.example.ekhuibaselibrary.utils.AFClickListener
import com.scwang.smartrefresh.layout.api.RefreshLayout


/**
 * Created by Ekhui on 2020/2/28.
 */


fun String.toast() {
    ToastUtils.showShort(this)
}


fun String.log() {
    Log.i("zoo", this)
}

/**
 * Created by Ekhui on 2020/5/15.
 * 作用：规范输入内容：   最后一位是小数点时消除小数点 / 只有一个点变成0
 */
fun String?.filterInt(): String? {
    if (this == null || this.isEmpty())
        return this

    if (this.length == 1 && this == ".")
        return "0"
    if (this[this.length - 1].toString() == ".") {
        return this.substring(0, this.length - 1)
    }
    return this
}

fun String.toastAndLog() {
    ToastUtils.showShort(this@toastAndLog)
    Log.i("zoo", this)
}


/**
 * Created by Ekhui on 2020/3/24.
 * 作用：列表刷新成功
 */

fun RefreshLayout.onComplete(isNoMore: Boolean) {
    Thread(Runnable {
        runOnUiThread {
            if (state.isHeader)
                finishRefresh()
            else if (state.isFooter)
                finishLoadMore(0, true, isNoMore)
        }
    }).start()

}

/**
 * Created by Ekhui on 2020/3/24.
 * 作用：禁止输入框换行
 */
fun EditText.banEnter() {
    this.setOnEditorActionListener { _, _, keyEvent ->

        KeyboardUtils.hideSoftInput(this)
        if (keyEvent == null)
            return@setOnEditorActionListener true

        return@setOnEditorActionListener (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER);
    }
}

fun TextView.setOnceClick(function: () -> Unit) {
    this.setOnClickListener(object : AFClickListener() {
        override fun onSingleClick(v: View?) {
            function.invoke()
        }
    })
}

fun ImageView.setOnceClick(function: () -> Unit) {
    this.setOnClickListener(object : AFClickListener() {
        override fun onSingleClick(v: View?) {
            function.invoke()
        }
    })
}

fun LinearLayout.setOnceClick(function: () -> Unit) {
    this.setOnClickListener(object : AFClickListener() {
        override fun onSingleClick(v: View?) {
            function.invoke()
        }
    })
}

fun Button.setOnceClick(function: () -> Unit) {
    this.setOnClickListener(object : AFClickListener() {
        override fun onSingleClick(v: View?) {
            function.invoke()
        }
    })
}

fun View.setOnceClick(function: () -> Unit) {
    this.setOnClickListener(object : AFClickListener() {
        override fun onSingleClick(v: View?) {
            function.invoke()
        }
    })
}

fun ImageView.loadImage(path: Any) {
    Glide.with(this.context).load(path)
//        .override(100, 100)
//        .error(R.drawable.image_error)
        .into(this)
}

fun ImageView.preLoadImage(path: Any) {
    Glide.with(this.context).load(path)
//        .override(100, 100)
//        .error(R.drawable.image_error)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .preload()
}


fun resize(context: Context, image: Drawable): Drawable? {
    val b = (image as BitmapDrawable).bitmap
    val bitmapResized = Bitmap.createScaledBitmap(b, 50, 50, false)
    return BitmapDrawable(context.resources, bitmapResized)
}

/**
 * Created by Ekhui on 2020/3/24.
 * 作用：RecyclerView增加分割线
 */

fun RecyclerView.addLinearLine() {
    this.addItemDecoration(
        RecycleLineDivider(
            this.context, LinearLayoutManager.HORIZONTAL,
            2, ContextCompat.getColor(this.context, R.color.white9)
        )
    )
}

/**
 * Created by Ekhui on 2020/5/18.
 * 作用：获取系统Color资源
 */

fun Context.getColorForID(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

/**
 * Created by Ekhui on 2020/5/18.
 * 作用：获取系统String资源
 */

fun getStringForID(id: Int): String {
    return StringUtils.getString(id);
}


/**
 * Created by Ekhui on 2020/5/18.
 * 作用：共享元素 跳转
 */
fun View.getOptions(): Bundle? {
    return ActivityOptionsCompat
        .makeSceneTransitionAnimation(
            (this.context as Activity),
            this, getStringForID(R.string.transitionName_toolBarTitle)
        ).toBundle()
}

