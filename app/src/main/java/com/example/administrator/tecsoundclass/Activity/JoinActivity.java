package com.example.administrator.tecsoundclass.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends BaseActivity {
    private TextView mTvCancle,mTvNext;
    private EditText mEtInput;
    private String ClassId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
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
                    Toast.makeText(JoinActivity.this,"请填写班级号", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(JoinActivity.this,"查找的班级不存在", Toast.LENGTH_SHORT).show();
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
                    });

                }

            }
        });

    }
}
