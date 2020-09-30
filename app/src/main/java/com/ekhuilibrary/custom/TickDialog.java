package com.ekhuilibrary.custom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import  com.ekhuilibrary.R;


public class TickDialog extends Dialog {

    public int time = 2000;

    public TickDialog(Context context) {
        super(context, R.style.LoadProgressDialog);
    }

    public TickDialog(Context context, int time) {
        super(context, R.style.LoadProgressDialog);
        this.time = time;
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_tick);
        setCancelable(false);
        setCanceledOnTouchOutside(false);


        new Handler().postDelayed(() -> {
            dismiss();
        }, time);
    }
}