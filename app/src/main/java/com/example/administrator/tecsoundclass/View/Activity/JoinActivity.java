package com.example.administrator.tecsoundclass.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.model.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.Controller.zxing.android.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends BaseActivity {
    private TextView mTvCancle,mTvNext;
    private EditText mEtInput;
    private LinearLayout mLlScanJoin;
    private String ClassId="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mTvCancle=findViewById(R.id.tv_cancle);
        mTvNext=findViewById(R.id.tv_next);
        mLlScanJoin=findViewById(R.id.ll_scan_way);
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
                    ToastUtils.ShowMyToasts(JoinActivity.this,"请填写班级号", Gravity.CENTER);
                }else{
                    String Url="http://101.132.71.111:8080/TecSoundWebApp/QueryClassServlet";
                    Map<String, String> params=new HashMap<>();
                    params.put("course_id",mEtInput.getText().toString());
                    VolleyCallback.getJSONObject(getApplicationContext(), "GetCinfo", Url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try {
                                JSONArray courses=r.getJSONArray("Result");
                                if (courses.length()==0){
                                    ToastUtils.ShowMyToasts(JoinActivity.this,"查找的班级不存在",Gravity.CENTER);
                                }else {
                                    Intent intent=new Intent(JoinActivity.this,ClassResultActivity.class);
                                    Bundle bundle=new Bundle();
                                    Course course =new Course();
                                    course.setCourse_id(courses.getJSONObject(0).getString("course_id"));
                                    course.setCourse_class(courses.getJSONObject(0).getString("course_class"));
                                    course.setCourse_name(courses.getJSONObject(0).getString("course_name"));
                                    course.setTeacher_user_id(courses.getJSONObject(0).getString("teacher_user_id"));
                                    course.setCourse_time(courses.getJSONObject(0).getString("course_time"));
                                    course.setRegister_time(courses.getJSONObject(0).getString("register_time"));
                                    course.setCourse_request(courses.getJSONObject(0).getString("course_request"));
                                    course.setCourse_pic_src(courses.getJSONObject(0).getString("course_pic_src"));
                                    course.setUpdate_time(courses.getJSONObject(0).getString("update_time"));

                                    bundle.putSerializable("CourseInfo",course);
                                    bundle.putString("Stuid",getIntent().getExtras().getString("Stuid"));
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
        mLlScanJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(JoinActivity.this,new String[]{Manifest.permission.CAMERA},1);
                }else {
                    Intent intent = new Intent(JoinActivity.this, CaptureActivity.class);
                    startActivityForResult(intent,1);
                }

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(JoinActivity.this, CaptureActivity.class);
                    startActivityForResult(intent,1);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEtInput.setText(data.getStringExtra("codedContent"));
                            mTvNext.performLongClick();
                        }
                    });
                }
                break;
        }
    }
}
