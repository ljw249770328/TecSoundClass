package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FriendResultActivity extends BaseActivity {
    private User mUser;
    private TextView mTvName,mTvId,mTvSex,mTvIdetity,mTvBirthdate,mTvCancel;
    private ImageView mIvHead;
    private Button mBtnFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_result);
        mUser= (User) getIntent().getExtras().getSerializable("Result");
        init();
    }

    private void init(){
        mTvId=findViewById(R.id.tv_result_id);
        mTvName=findViewById(R.id.tv_result_name);
        mTvSex=findViewById(R.id.tv_result_sex);
        mTvIdetity=findViewById(R.id.tv_result_identity);
        mTvBirthdate=findViewById(R.id.tv_result_time);
        mTvCancel=findViewById(R.id.tv_back);
        mIvHead=findViewById(R.id.iv_user_pic);
        mBtnFollow=findViewById(R.id.btn_follow);
        Setdata();
    }

    private void Setdata() {
        mTvId.setText(mUser.getUser_id());
        mTvName.setText(mUser.getUser_name());
        mTvBirthdate.setText(mUser.getUser_age());
        mTvIdetity.setText(mUser.getUser_identity());
        mTvSex.setText(mUser.getUser_sex());
        try {
            Glide.with(FriendResultActivity.this).load(new URL(mUser.getUser_pic_src())).signature(new ObjectKey(mUser.getUpdate_time())).encodeQuality(70).into(mIvHead);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mBtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUid=getIntent().getStringExtra("mUid");
                String url ="http://101.132.71.111:8080/TecSoundWebApp/FollowOthersServlet";
                Map<String,String> params=new HashMap<>();
                params.put("Uid",mUid );
                params.put("Fid",mTvId.getText().toString());
                VolleyCallback.getJSONObject(getApplicationContext(), "FollowOthers", url, params, new VolleyCallback.VolleyJsonCallback() {
                    @Override
                    public void onFinish(JSONObject r) {
                        try {
                            String result=r.getString("Result");
                            if (result.equals("exists")){
                                ToastUtils.ShowMyToasts(FriendResultActivity.this,"你已经关注Ta啦", Gravity.CENTER);
                            } else{
                                ToastUtils.ShowMyToasts(FriendResultActivity.this,result,Gravity.CENTER);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
