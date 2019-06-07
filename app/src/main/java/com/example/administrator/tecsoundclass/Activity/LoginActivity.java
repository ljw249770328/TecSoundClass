package com.example.administrator.tecsoundclass.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TransferMore;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.example.weeboos.permissionlib.PermissionRequest;
import com.example.weeboos.permissionlib.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends BaseActivity {
    private Button mBtnLogin;
    private TextView mTvRegedit,mTvForget;
    private EditText mEtaccount;
    private EditText mEtpassword;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private User mUser=null;
    private WebSocketClientObject client =null;
    final String [] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //安卓6.0以上需要动态获取权限
        //初始化权限
        Requestpermission();
        //加载控件
        init();
        //注册点击事件
        SetListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否有数据传递
        if(getIntent().getExtras()!=null){
            Toast.makeText(LoginActivity.this,"有传递",Toast.LENGTH_SHORT).show();
            if(!getIntent().getExtras().getString("userid").equals("")){
                Toast.makeText(LoginActivity.this,"已设置",Toast.LENGTH_SHORT).show();
                mEtaccount.setText(getIntent().getExtras().getString("userid"));
            }
            if(!getIntent().getExtras().getString("psw").equals("")){
                mEtpassword.setText(getIntent().getExtras().getString("psw"));
            }
        }
    }

    //获取返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    mEtaccount.setText(data.getStringExtra("id"));
                }
                break;
            case 2:
                if (resultCode==RESULT_OK){
                    mEtaccount.setText(data.getStringExtra("userid"));
                    mEtpassword.setText(data.getStringExtra("psw"));
                }
            default:
        }
    }
    //注册组件
    private void init(){
        mBtnLogin=findViewById(R.id.btn_login1);
        mTvForget=findViewById(R.id.tv_forget);
        mTvRegedit=findViewById(R.id.tv_regedit);
        mEtaccount=findViewById(R.id.et_username);
        mEtpassword=findViewById(R.id.et_password);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass=findViewById(R.id.remember_psw);
        boolean isRemember =pref.getBoolean("remember_password",false);
        if(isRemember){
            //设置已保存的帐号到文本框中
            mEtaccount.setText(pref.getString("userid",""));
            mEtpassword.setText(pref.getString("psw",""));
            rememberPass.setChecked(true);
        }

    }
    //设置监听
    private void SetListeners(){
        OnClick onClick=new OnClick();
        mBtnLogin.setOnClickListener(onClick);
        mTvForget.setOnClickListener(onClick);
        mTvRegedit.setOnClickListener(onClick);

    }
    //自定义点击事件内部类
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.btn_login1:
                    if (mEtaccount.getText().toString().equals("")||mEtpassword.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginActivity.this,"用户名或密码为空，登陆失败",Toast.LENGTH_SHORT).show();
                    }
                    else {
//                        LoginRequest(mEtaccount.getText().toString(),mEtpassword.getText().toString());
                        mBtnLogin.setText("登录中");
                        String url = "http://101.132.71.111:8080/TecSoundWebApp/LoginServlet";
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", mEtaccount.getText().toString());  //注⑥
                        params.put("user_password", mEtpassword.getText().toString());
                        //登陆请求
                        VolleyCallback.getJSONObject(getApplicationContext(), "Login", url, params, new VolleyCallback.VolleyJsonCallback() {
                            @SuppressLint("HandlerLeak")
                            @Override
                            public void onFinish(JSONObject r) {
                                String result = null;  //注④
                                try {
                                    result = r.getString("Result");
                                    if (result.equals("pass")) {
                                        editor=pref.edit();
                                        if (rememberPass.isChecked()){
                                            editor.putBoolean("remember_password",true);
                                            editor.putString("userid",mEtaccount.getText().toString());
                                            editor.putString("psw",mEtpassword.getText().toString());
                                        }else{
                                            editor.clear();
                                        }
                                        editor.apply();
                                        //封装User对象
                                        TransferMore.GetUserById(getApplicationContext(),mEtaccount.getText().toString(),new Handler(){
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                                switch (msg.what){
                                                    case 1:
                                                        mUser= (User) msg.obj;
                                                        Map<String,String> header= new HashMap<>();
                                                        try {
                                                            header.put("id", URLEncoder.encode(mEtaccount.getText().toString().trim(),"UTF-8"));
                                                            client=WebSocketClientObject.getClient(getApplicationContext(),new Handler(){
                                                                @Override
                                                                public void handleMessage(Message msg) {
                                                                    super.handleMessage(msg);
                                                                    switch (msg.what){
                                                                        case 0:
                                                                            Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
                                                                            Bundle bundle=new Bundle();
                                                                            bundle.putSerializable("user",mUser);
                                                                            intent.putExtras(bundle);
                                                                            startActivity(intent);
                                                                            mBtnLogin.setText("登录");
                                                                            finish();
                                                                            break;
                                                                    }
                                                                }
                                                            },header);
                                                            client.connect();
                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }
                                                    break;
                                                }
                                            }
                                        });
                                    } else if(result.equals("pswerror")) {
                                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                                        mBtnLogin.setText("登录");
                                    }else if(result.equals("notexists")) {
                                        Toast.makeText(LoginActivity.this,"用户不存在,请先注册",Toast.LENGTH_SHORT).show();
                                        mBtnLogin.setText("登录");
                                    }
                                } catch (JSONException e) {
                                   // 做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                     Log.e("TAG", e.getMessage(), e);
                                }

                            }
                        });
                    }
                    break;
                case R.id.tv_regedit:
                    intent=new Intent(LoginActivity.this,RegeditActivity.class) ;    //切换User Activity至RegeditActivity
                    startActivityForResult(intent,1);

                    break;
                case R.id.tv_forget:
                    intent=new Intent(LoginActivity.this,ForgetpswActivity.class) ;
                    if(!mEtaccount.getText().toString().equals("")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("userid", mEtaccount.getText().toString());
                        intent.putExtras(bundle);
                    }
                    startActivityForResult(intent,2);
                    break;
                default:
                    break;
            }
        }
    }
    //权限获取
    private void Requestpermission(){
        PermissionRequest request = new PermissionRequest(this); // 这个this需要一个activity对象或者fragment对象
        request.requestPermission(new PermissionRequest.PermissionListener() {
            @Override
            public void permissionGranted() {
                //do Something when permission granted
            }

            @Override
            public void permissionDenied(ArrayList<String> permissions) {
                //do Something when permission denied
            }

            @Override
            public void permissionNeverAsk(ArrayList<String> permissions) {
                //do Something when permission never ask
                PermissionUtils.showAlertDialog(LoginActivity.this);
            }
        },permissions);
    }
}




