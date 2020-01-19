package com.example.administrator.tecsoundclass.Activity;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.Fragments.CourseFragment;
import com.example.administrator.tecsoundclass.Fragments.FriendFragment;
import com.example.administrator.tecsoundclass.Fragments.MyselfFragment;
import com.example.administrator.tecsoundclass.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.service.BackService;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.TransferMore;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.example.administrator.tecsoundclass.utils.baidu.BaiduApi;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuActivity extends BaseActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private  String StudentID="";
    private User mUser;
    private SharedPreferences pref;
    private WebSocketClient client;// 连接客户端

    public User getmUser() {
        return mUser;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public void UpdatemUser(final UploadCallBack callBack){
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", mUser.getUser_id());
        VolleyCallback.getJSONObject(getApplicationContext().getApplicationContext(), "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray users=r.getJSONArray("users");
                    JSONObject user= (JSONObject) users.get(0);
                    mUser.setUser_id(user.getString("user_id"));
                    mUser.setUser_age(user.getString("user_age"));
                    mUser.setUser_identity(user.getString("user_identity"));
                    mUser.setUser_sex(user.getString("user_sex"));
                    mUser.setUser_name(user.getString("user_name"));
                    mUser.setUser_pic_src(user.getString("user_pic_src"));
                    mUser.setUpdate_time(user.getString("update_time"));
                    callBack.OnUploaded();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
//        BaiduApi.SentimentAnalysis();
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        if (getIntent().getAction()!=null&&getIntent().getAction().equals("ACTION_SAVED_LOGIN_AUTO")){
            Login(new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what){
                        case 0:
                            StudentID=mUser.getUser_id();
                            if(savedInstanceState == null){
                                changeFragment(CourseFragment.class.getName());
                            }
                            break;
                    }
                    return false;
                }
            }));
        }else {
            mUser= (User) getIntent().getExtras().getSerializable("user");
            MyApplication application=MyApplication.getApplication();
            application.setmUser(mUser);
            StudentID=mUser.getUser_id();
            if(savedInstanceState == null){
                changeFragment(CourseFragment.class.getName());
            }
        }
        startService(new Intent(this,BackService.class));
        mRgTab=findViewById(R.id.rg_main);
        //点击切换Fragment
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_course:
                        changeFragment(CourseFragment.class.getName());
//                        WebSocketClientObject.client.send("点击发送");
                        break;
                    case R.id.rb_friends:
                        changeFragment(FriendFragment.class.getName());
                        break;
                    case R.id.rb_myself:
                        changeFragment(MyselfFragment.class.getName());
                        break;
                }
            }
        });

    }
    public void changeFragment(String tag) {
        //先遍历隐藏Fragment
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(CourseFragment.class.getName())) {
                fragment = CourseFragment.newInstance();
            } else if (tag.equals(FriendFragment.class.getName())) {
                fragment = FriendFragment.newInstance();
            } else if (tag.equals(MyselfFragment.class.getName())) {
                fragment = MyselfFragment.newInstance();
            }
            //fragment栈管理
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }
    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }

    private  void Login(final Handler connectHandler){
                    String url = "http://101.132.71.111:8080/TecSoundWebApp/LoginServlet";
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", pref.getString("userid",""));  //注⑥
                    params.put("user_password", pref.getString("psw",""));
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
                                                    mUser= (User) msg.obj;
                                                    Map<String,String> header= new HashMap<>();
                                                    try {
                                                        header.put("id", URLEncoder.encode(pref.getString("userid",""),"UTF-8"));
                                                        WebSocketClient client= WebSocketClientObject.getClient(getApplicationContext(),connectHandler,header);
                                                        client.connect();

                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                } else if(result.equals("pswerror")) {
                                    ToastUtils.ShowMyToasts(MainMenuActivity.this,"密码错误,请重新登录", Gravity.CENTER);
                                    Intent intent=new Intent(MainMenuActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if(result.equals("notexists")) {
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
    }
    public interface UploadCallBack{
        void OnUploaded();
    }

}
