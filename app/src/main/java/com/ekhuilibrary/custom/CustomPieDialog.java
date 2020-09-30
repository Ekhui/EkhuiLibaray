package com.ekhuilibrary.custom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import  com.ekhuilibrary.R;


public class CustomPieDialog extends Dialog {
    private String message;
    private boolean canCancel;
    private TextView textView;
    private PieProgressView pie;
    private int progress;

    public CustomPieDialog(Context context, String message) {
        this(context, message, true);
    }

    public CustomPieDialog(Context context, String message, boolean canCancel) {
        super(context, R.style.LoadProgressDialog);
        this.message = message;
        this.canCancel = canCancel;
    }

    public void setMessage(String message) {
        this.message = message;
        handler.sendEmptyMessage(0);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                textView.setText(message);
            }
            if (msg.what == 1) {
                pie.setProgress(progress);
            }
        }
    };

    public void setProgress(int progress) {
        this.progress = progress;
        handler.sendEmptyMessage(1);
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_pie);
        textView = findViewById(R.id.tv_message);
        pie = findViewById(R.id.pie);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        pie.setProgress(1);
        textView.setText(message);

    }
}