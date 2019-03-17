package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TPDialogFactory;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditMyInfoActivity extends AppCompatActivity {
    private ImageView mBack,mIvEditedHead;
    private TextView mTvMyname,mTvMyId,mTvMyBirthD,mTvMyIdentity,mTvSave;
    private RadioGroup mSexGroup;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String mSex="男";

    private void init(){
        mBack=findViewById(R.id.im_back);
        mIvEditedHead=findViewById(R.id.iv_edit_head);
        mTvMyname=findViewById(R.id.tv_my_name);
        mTvMyId=findViewById(R.id.tv_my_id);
        mTvMyIdentity=findViewById(R.id.tv_my_occupation);
        mTvMyBirthD=findViewById(R.id.tv_my_birth_date);
        mTvSave=findViewById(R.id.tv_save);
        mSexGroup=findViewById(R.id.rg_sex_group);
        rb_male=findViewById(R.id.rb_sex_male);
        rb_female=findViewById(R.id.rb_sex_female);
    }
    private void SetListeners(){
        OnClick onClick =new OnClick();
        mBack.setOnClickListener(onClick);
        mTvMyBirthD.setOnClickListener(onClick);
        mTvSave.setOnClickListener(onClick);
        mSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int count = mSexGroup.getChildCount();
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               for (int i=0;i<count;i++) {
                   RadioButton rb = (RadioButton) mSexGroup.getChildAt(i);
                   if (rb.isChecked()){
                       mSex=rb.getText().toString();
                   }
               }
            }
        });
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    finish();
                    break;
                case R.id.tv_my_birth_date:
                    TPDialogFactory factory =new TPDialogFactory();
                    factory.getDatePicker(EditMyInfoActivity.this,"出生日期",mTvMyBirthD).show(factory.getTime());
                    break;
                case R.id.tv_save:
                    String url="http://101.132.71.111:8080/TecSoundWebApp/AlterUInfoServlet";
                    Map<String,String> params=new HashMap<>();
                    params.put("user_id",mTvMyId.getText().toString());
                    params.put("user_age",mTvMyBirthD.getText().toString());
                    params.put("user_sex",mSex);
                    VolleyCallback.getJSONObject(EditMyInfoActivity.this, "EditMyinfo", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try {
                                String Result =r.getString("Result");
                                if(Result.equals("success")){
                                    Toast.makeText(EditMyInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(EditMyInfoActivity.this,"出现异常",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                default:
                    break;
            }
        }
    }
    private void SetData(){
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id",getIntent().getExtras().getString("user_id"));
        VolleyCallback.getJSONObject(EditMyInfoActivity.this, "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray users=r.getJSONArray("users");
                    JSONObject user= (JSONObject) users.get(0);
                    mTvMyname.setText(user.getString("user_name"));
                    mTvMyId.setText(user.getString("user_id"));
                    mTvMyBirthD.setText(user.getString("user_age"));
                    mTvMyIdentity.setText(user.getString("user_identity"));
                    if (user.getString("user_sex").equals("女"));{
                        rb_female.isChecked();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myinfo);
        init();
        SetListeners();
        SetData();
    }
}
