package com.example.administrator.tecsoundclass.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.model.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FollowActivity extends BaseActivity {
    private TextView mTvCancle,mTvNext;
    private EditText mEtInput;
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        final String mId=getIntent().getStringExtra("mUid");
        mTvCancle=findViewById(R.id.tv_cancle);
        mTvNext=findViewById(R.id.tv_next);
        mEtInput=findViewById(R.id.et_input);
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtInput.getText().toString().equals("")){
                    ToastUtils.ShowMyToasts(FollowActivity.this,"请填写想要关注人的账号", Gravity.CENTER);
                }else{
                    String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",mEtInput.getText().toString());
                    VolleyCallback.getJSONObject(getApplicationContext().getApplicationContext(), "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try {
                                JSONArray users=r.getJSONArray("users");
                                if (users.get(0).equals("")){
                                    ToastUtils.ShowMyToasts(FollowActivity.this,"账号不存在", Gravity.CENTER);
                                }else {
                                    JSONObject user= (JSONObject) users.get(0);
                                    mUser=new User();
                                    mUser.setUser_id(user.getString("user_id"));
                                    mUser.setUser_age(user.getString("user_age"));
                                    mUser.setUser_identity(user.getString("user_identity"));
                                    mUser.setUser_sex(user.getString("user_sex"));
                                    mUser.setUser_name(user.getString("user_name"));
                                    mUser.setUser_pic_src(user.getString("user_pic_src"));
                                    mUser.setUpdate_time(user.getString("update_time"));
                                    Intent intent=new Intent(FollowActivity.this,FriendResultActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("Result",mUser);
                                    intent.putExtra("mUid",mId);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
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

            }
        });

    }
}
