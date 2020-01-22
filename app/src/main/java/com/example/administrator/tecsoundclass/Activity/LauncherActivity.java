package com.example.administrator.tecsoundclass.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.example.administrator.tecsoundclass.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.common.Constants;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.TransferMore;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LauncherActivity extends BaseActivity {
    private static final String TAG = "LauncherActivity";
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private Toast toast = null;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private FaceEngine faceEngine = new FaceEngine();
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
        //初始化虹软引擎
        initializeArcFace();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (pref.getString("userid","")!=""&&pref.getString("psw","")!=""){

                    String url = "http://101.132.71.111:8080/TecSoundWebApp/LoginServlet";
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",pref.getString("userid",""));  //注⑥
                    params.put("user_password",pref.getString("psw",""));
                    //登陆请求
                    VolleyCallback.getJSONObject(getApplicationContext(), "Login", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @SuppressLint("HandlerLeak")
                        @Override
                        public void onFinish(JSONObject r) {
                            String result = null;  //注④
                            try {
                                result = r.getString("Result");
                                if (result.equals("pass")) {
                                    //封装User对象
                                    TransferMore.GetUserById(getApplicationContext(),pref.getString("userid",""),new Handler(){
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what){
                                                case 1:
                                                   User mUser= (User) msg.obj;
                                                    //登录
                                                        Intent intent=new Intent(LauncherActivity.this,MainMenuActivity.class);
                                                        Bundle bundle=new Bundle();
                                                        bundle.putSerializable("user",mUser);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        finish();
                                                    break;
                                            }
                                        }
                                    });
                                } else if(result.equals("pswerror")) {
                                    ToastUtils.ShowMyToasts(LauncherActivity.this,"密码错误", Gravity.CENTER);
                                    Intent intent=new Intent(LauncherActivity.this,LoginActivity.class);
                                }else if(result.equals("notexists")) {
                                    ToastUtils.ShowMyToasts(LauncherActivity.this,"用户不存在,请先注册",Gravity.CENTER);
                                    Intent intent=new Intent(LauncherActivity.this,LoginActivity.class);

                                }
                            } catch (JSONException e) {
                                // 做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                Log.e("TAG", e.getMessage(), e);
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
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

    private void initializeArcFace(){
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(LauncherActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            Log.e(TAG,getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            Log.e(TAG,getString(R.string.already_activated));
                        } else {
                            Log.e(TAG,getString(R.string.active_failed, activeCode));
                        }

                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(LauncherActivity.this,activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }


}
