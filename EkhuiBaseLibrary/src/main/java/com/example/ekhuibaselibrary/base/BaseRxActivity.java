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
import androidx.lifecycle.ViewModelProviders;

import com.example.ekhuibaselibrary.R;
import com.example.ekhuibaselibrary.custom.CustomDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;

/*
 * Created by Ekhui on 2019/11/20.
 */
public abstract class BaseRxActivity<VM extends BaseViewModel, VDB extends ViewDataBinding> extends RxAppCompatActivity {
    protected abstract int initLayout();

    protected abstract void initLogic(@Nullable Bundle savedInstanceState);

    protected VM mViewModel;
    protected VDB binding;

    private CustomDialog customDialog;

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

    protected void initBG() {
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);

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

        mViewModel.getSelectDialog().observe(this, a -> {
            onSelectDialogChange();
        });


        mViewModel.getDialog().observe(this, aBoolean -> {
            if (aBoolean) {
                if (customDialog == null)
                    customDialog = new CustomDialog(this, "");
                customDialog.show();
            } else {
                if (customDialog != null)
                    customDialog.dismiss();
            }
        });

        initLogic(savedInstanceState);

    }


    protected void onSelectDialogChange() {
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