package com.ekhuilibrary.custom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import  com.ekhuilibrary.R;

import cn.bingoogolapple.progressbar.BGAProgressBar;

/**
 * Created by Ekhui on 2020/8/4.
 */
public class UpdateDialog extends Dialog {
    private String toVersion;
    private String currVersion;
    private TextView toVersionTV;
    private BGAProgressBar progressBar;
    public int progress;


    public void setProgress(int progress) {
        this.progress = progress;
        handler.sendEmptyMessage(0);
    }

    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
        handler.sendEmptyMessage(1);
    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                progressBar.setProgress(progress);
            }
            if (msg.what == 1) {
                toVersionTV.setText("下载版本：" + toVersion);
            }
        }
    };

    public UpdateDialog(Context context, String currVersion) {
        super(context, R.style.LoadProgressDialog);
        this.currVersion = currVersion;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_update);
        progressBar = findViewById(R.id.progressBar);
        TextView currVersionTV = findViewById(R.id.currVersionTV);
        toVersionTV = findViewById(R.id.toVersionTV);
        currVersionTV.setText("当前版本：" + currVersion);


        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

}
