package com.example.ekhuibaselibrary.api.exception.utils;

import android.view.View;

//防止按钮重复点击
public abstract class AFClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000L;

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
//            onFastClick(v);
        }
    }

    protected abstract void onSingleClick(View v);

}