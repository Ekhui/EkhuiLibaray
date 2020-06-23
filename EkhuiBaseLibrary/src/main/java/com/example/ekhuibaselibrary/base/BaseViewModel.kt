package com.example.ekhuibaselibrary.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/*
 * Created by Ekhui on 2019/11/20.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

//    private val mContext = getApplication<Application>().applicationContext

    val mApplication: Application = application

    var mDialog = MutableLiveData<Boolean>()
    var dialog: LiveData<Boolean> = Transformations.map(mDialog) {
        it
    }

    var mSelectDialogTag = MutableLiveData<Int>()
    var mSelectDialog = MutableLiveData<Boolean>()
    var selectDialog: LiveData<Boolean> = Transformations.map(mSelectDialog) {
        it
    }

    override fun onCleared() {
        super.onCleared()
    }
}