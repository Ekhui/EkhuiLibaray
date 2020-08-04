package com.example.ekhuibaselibrary.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rxjava.rxlife.Scope
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/*
 * Created by Ekhui on 2019/11/20.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), Scope {

//    private val mContext = getApplication<Application>().applicationContext

    private var mDisposables: CompositeDisposable? = null

    override fun onScopeStart(d: Disposable) {
        addDisposable(d) //订阅事件时回调
    }

    override fun onScopeEnd() {
        //事件正常结束时回调
    }

    open fun addDisposable(disposable: Disposable) {
        var disposables = mDisposables
        if (disposables == null) {
            mDisposables = CompositeDisposable()
            disposables = mDisposables
        }
        disposables!!.add(disposable)
    }

    open fun dispose() {
        val disposables = mDisposables ?: return
        disposables.dispose()
    }

    override fun onCleared() {
        super.onCleared() //Activity/Fragment 销毁时回调
        dispose() //中断RxJava管道
    }


    val mApplication: Application = application

    //请求成功
    val fetchSuccess = MutableLiveData<Boolean>()
    var mFetchSuccess: LiveData<Boolean> = Transformations.map(fetchSuccess) {
        it
    }

    //    请求失败
    val fetchFail = MutableLiveData<Boolean>()
    var mFetchFail: LiveData<Boolean> = Transformations.map(fetchFail) {
        it
    }


    //    请求失败
    val mPopEvent = MutableLiveData<Boolean>()
    var popEvent: LiveData<Boolean> = Transformations.map(mPopEvent) {
        it
    }

    var mDialog = MutableLiveData<Boolean>()
    var dialog: LiveData<Boolean> = Transformations.map(mDialog) {
        it
    }

    var mSelectDialogTag = MutableLiveData<Int>()
    var mSelectDialog = MutableLiveData<Boolean>()
    var selectDialog: LiveData<Boolean> = Transformations.map(mSelectDialog) {
        it
    }


}