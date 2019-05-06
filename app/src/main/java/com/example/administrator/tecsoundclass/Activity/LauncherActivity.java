package com.example.administrator.tecsoundclass.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.administrator.tecsoundclass.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import org.litepal.LitePal;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //加载数据库
        //LitePal.getDatabase();
        //初始化讯飞引擎
        SpeechUtility.createUtility(LauncherActivity.this, SpeechConstant.APPID +"=5be8469d");
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(LauncherActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        //初始化下载引擎
        FileDownloader.setupOnApplicationOnCreate(getApplication())
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
        handler.sendEmptyMessageDelayed(1,700);//延时发送，第二个参数跟的毫秒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}
