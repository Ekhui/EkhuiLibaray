package com.example.ekhuibaselibrary.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Ekhui on 2019/11/20.
 */
public abstract class BaseFragment<VM extends BaseViewModel, VDB extends ViewDataBinding> extends Fragment {

    protected VM mViewModel;
    protected VDB binding;

    protected abstract int initLayout();

    protected abstract void initLogic();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        binding.setLifecycleOwner(this);
        createViewModel();
        initLogic();
        Log.i("当前Fragment--> ", getClass().getName());

        return binding.getRoot();
    }

    private void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (mViewModel == null) {
//            Class modelClass;
//            Type type = getClass().getGenericSuperclass();
//            if (type instanceof ParameterizedType) {
//                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
//            } else {
//                modelClass = BaseViewModel.class;
//            }
//            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
//        }
//    }

}
