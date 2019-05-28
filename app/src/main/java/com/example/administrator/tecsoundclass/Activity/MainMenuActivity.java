package com.example.administrator.tecsoundclass.Activity;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.Fragments.CourseFragment;
import com.example.administrator.tecsoundclass.Fragments.FriendFragment;
import com.example.administrator.tecsoundclass.Fragments.MyselfFragment;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private  String StudentID="";
    private User mUser;
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
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mRgTab=findViewById(R.id.rg_main);

        mUser= (User) getIntent().getExtras().getSerializable("user");
        StudentID=mUser.getUser_id();
        //点击切换Fragment
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_course:
                        changeFragment(CourseFragment.class.getName());
                        WebSocketClientObject.client.send("点击发送");
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
    public interface UploadCallBack{
        void OnUploaded();
    }

}
