package com.example.administrator.tecsoundclass;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.weeboos.permissionlib.PermissionRequest;
import com.example.weeboos.permissionlib.PermissionUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.LitePal;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity {

    final String [] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //加载数据库
        LitePal.getDatabase();
        //初始化引擎
        SpeechUtility.createUtility(LauncherActivity.this, SpeechConstant.APPID +"=5be8469d");
        //初始化权限
//        Requestpermission();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(LauncherActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(1,2000);//延时发送，第二个参数跟的毫秒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void Requestpermission(){
        PermissionRequest request = new PermissionRequest(this); // 这个this需要一个activity对象或者fragment对象
        request.requestPermission(new PermissionRequest.PermissionListener() {
            @Override
            public void permissionGranted() {
                //do Something when permission granted
                Toast.makeText(LauncherActivity.this,"权限获取成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionDenied(ArrayList<String> permissions) {
                //do Something when permission denied
                Toast.makeText(LauncherActivity.this,"获取拒绝",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionNeverAsk(ArrayList<String> permissions) {
                //do Something when permission never ask
                Toast.makeText(LauncherActivity.this,"不再询问",Toast.LENGTH_SHORT).show();
                PermissionUtils.showAlertDialog(LauncherActivity.this);
            }
        },permissions);
    }
}
