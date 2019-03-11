package com.example.administrator.tecsoundclass.Activity;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.tecsoundclass.Fragments.CourseFragment;
import com.example.administrator.tecsoundclass.Fragments.FriendFragment;
import com.example.administrator.tecsoundclass.Fragments.MyselfFragment;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private String StudentID="";
    private  User user=new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mRgTab=findViewById(R.id.rg_main);
        StudentID=getIntent().getExtras().getString("LoginId");
        //查询User信息
//        UInfoRequest(StudentID);
        Toast.makeText(MainMenuActivity.this,"欢迎 "+user.getUser_name(),Toast.LENGTH_SHORT).show();
        //点击切换Fragment
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_course:
                        changeFragment(CourseFragment.class.getName());
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
        if(savedInstanceState == null){
            changeFragment(CourseFragment.class.getName());
        }
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
//    private void UInfoRequest(final String accountNumber) {
//        //请求地址
//        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
//        String tag = "getinfo";
//        //取得请求队列
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        //防止重复请求，所以先取消tag标识的请求队列
//        requestQueue.cancelAll(tag);
//
//        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
//        final StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
//
//                            Toast.makeText(MainMenuActivity.this,jsonObject.toString(),Toast.LENGTH_SHORT).show();
//                            user.setUser_age(jsonObject.getString("user_age"));
//                            user.setUser_identity(jsonObject.getString("user_identity"));
//                            user.setUser_sex(jsonObject.getString("user_sex"));
//                            user.setUser_name(jsonObject.getString("user_name"));
//                            user.setUser_pic_src(jsonObject.getString("user_pic_src"));
//                        } catch (JSONException e) {
//                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
//                            Log.e("TAG", e.getMessage(), e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
//                Log.e("TAG", error.getMessage(), error);
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("user_id", accountNumber);  //注⑥
//                return params;
//            }
//        };
//
//        //设置Tag标签
//        request.setTag(tag);
//
//        //将请求添加到队列中
//        requestQueue.add(request);
//
//    }
}
