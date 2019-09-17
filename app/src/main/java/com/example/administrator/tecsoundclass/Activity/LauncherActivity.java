package com.example.administrator.tecsoundclass.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TransferMore;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LauncherActivity extends BaseActivity {
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        //加载数据库
        //LitePal.getDatabase();
        //初始化讯飞引擎
        SpeechUtility.createUtility(LauncherActivity.this, SpeechConstant.APPID +"=5be8469d");
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (pref.getString("userid","")!=""&&pref.getString("psw","")!=""){
                    Intent intent=new Intent(LauncherActivity.this,MainMenuActivity.class);
                    intent.setAction("ACTION_SAVED_LOGIN_AUTO");
                    startActivity(intent);
                    finish();
//                    String url = "http://101.132.71.111:8080/TecSoundWebApp/LoginServlet";
//                    Map<String, String> params = new HashMap<>();
//                    params.put("user_id", pref.getString("userid",""));  //注⑥
//                    params.put("user_password", pref.getString("psw",""));
//                    //登陆请求
//                    VolleyCallback.getJSONObject(getApplicationContext(), "Login", url, params, new VolleyCallback.VolleyJsonCallback() {
//                        @SuppressLint("HandlerLeak")
//                        @Override
//                        public void onFinish(JSONObject r) {
//                            String result = null;  //注④
//                            try {
//                                result = r.getString("Result");
//                                if (result.equals("pass")) {
//                                    //封装User对象
//                                    TransferMore.GetUserById(getApplicationContext(),pref.getString("userid",""),new Handler(){
//                                        @Override
//                                        public void handleMessage(Message msg) {
//                                            super.handleMessage(msg);
//                                            switch (msg.what){
//                                                case 1:
//                                                    mUser= (User) msg.obj;
//                                                    Map<String,String> header= new HashMap<>();
//                                                    try {
//                                                        header.put("id", URLEncoder.encode(pref.getString("userid",""),"UTF-8"));
//                                                        WebSocketClient client= WebSocketClientObject.getClient(getApplicationContext(),new Handler(){
//                                                            @Override
//                                                            public void handleMessage(Message msg) {
//                                                                super.handleMessage(msg);
//                                                                switch (msg.what){
//                                                                    case 0:
//                                                                        Intent intent=new Intent(LauncherActivity.this,MainMenuActivity.class);
//                                                                        Bundle bundle=new Bundle();
//                                                                        bundle.putSerializable("user",mUser);
//                                                                        intent.putExtras(bundle);
//                                                                        startActivity(intent);
//                                                                        finish();
//                                                                        break;
//                                                                }
//                                                            }
//                                                        },header);
//                                                        client.connect();
//                                                    } catch (UnsupportedEncodingException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    break;
//                                            }
//                                        }
//                                    });
//                                } else if(result.equals("pswerror")) {
//                                    Toast.makeText(LauncherActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
//                                }else if(result.equals("notexists")) {
//                                    Toast.makeText(LauncherActivity.this,"用户不存在,请先注册",Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                // 做自己的请求异常操作，如Toast提示（“无网络连接”等）
//                                Log.e("TAG", e.getMessage(), e);
//                            }
//
//                        }
//                    });
                }else {
                    Intent intent = new Intent(LauncherActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        //初始化下载引擎
        FileDownloader.setupOnApplicationOnCreate(getApplication())
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
        handler.sendEmptyMessageDelayed(1,500);//延时发送，第二个参数跟的毫秒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}
