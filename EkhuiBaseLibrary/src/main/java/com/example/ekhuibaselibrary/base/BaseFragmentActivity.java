package com.example.ekhuibaselibrary.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ekhuibaselibrary.custom.CustomDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;


/*
 * Created by Ekhui on 2019/11/20.
 */
public abstract class BaseFragmentActivity<VM extends BaseViewModel, VDB extends ViewDataBinding> extends FragmentActivity {
    protected abstract int initLayout();

    protected abstract void initLogic();

    protected VM mViewModel;
    protected VDB binding;

//    public final static String EVENT_SHOW_DIALOG = "显示弹窗";
//    public final static String EVENT_DIMISS_DIALOG = "关闭弹窗";


    //    @Override
//    public void onStop() {
//        super.onStop();
//        BusUtils.unregister(this);
//    }
//
    private CustomDialog customDialog;

    public void dismissDialog() {
        if (customDialog != null)
            customDialog.dismiss();
    }

    public void showDialog() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "");
        customDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog != null)
            customDialog = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, initLayout());
        binding.setLifecycleOwner(this);
        createViewModel();
        Log.i("当前Activity--> ", getClass().getName());

        initLogic();
        if (savedInstanceState != null) {
            resetData(savedInstanceState);
        }
    }

    //初始化状态栏
    public void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void resetData(Bundle savedInstanceState) {
    }

    public void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}