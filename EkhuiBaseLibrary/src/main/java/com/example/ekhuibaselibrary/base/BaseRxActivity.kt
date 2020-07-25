package com.example.ekhuibaselibrary.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ekhuibaselibrary.R
import com.example.ekhuibaselibrary.custom.CustomDialog
import java.lang.reflect.ParameterizedType

/*
 * Created by Ekhui on 2019/11/20.
 */
abstract class BaseRxActivity<VM : BaseViewModel, VDB : ViewDataBinding> : AppCompatActivity() {
    protected abstract fun initLayout(): Int
    protected abstract fun initLogic(savedInstanceState: Bundle?)
    protected lateinit var mViewModel: VM
    protected lateinit var binding: VDB
    private var customDialog: CustomDialog? = null

    //初始化状态栏
    fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
            }
    }

    protected fun initBG() {
        window.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onDestroy() {
        if (customDialog != null) customDialog = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<VDB>(this, initLayout())
        binding.lifecycleOwner = this
        createViewModel()
        Log.i("当前Activity--> ", javaClass.name)
        mViewModel.selectDialog.observe(
            this,
            Observer { a: Boolean? -> onSelectDialogChange() }
        )
        mViewModel.dialog.observe(
            this,
            Observer { aBoolean: Boolean ->
                if (aBoolean) {
                    if (customDialog == null) customDialog = CustomDialog(this, "")
                    customDialog!!.show()
                } else {
                    if (customDialog != null) customDialog!!.dismiss()
                }
            }
        )
        initLogic(savedInstanceState)
    }

    protected open fun onSelectDialogChange() {}

    private fun createViewModel() {
        val modelClass: Class<BaseViewModel> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<BaseViewModel>
        mViewModel = ViewModelProviders.of(this).get<BaseViewModel>(modelClass) as VM
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}