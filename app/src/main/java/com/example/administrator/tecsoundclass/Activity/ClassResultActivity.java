package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClassResultActivity extends AppCompatActivity {
    private TextView mTvBack,mTvCourseName,mTvClass,mTvTea,mTvCtime,mTvCreateT,mTvCreq;
    private Course course;
    private Button mBtnJoin;
    private String StuId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_result);
        init();
        SetData();
        SetOnClickListener();

    }
    private void init(){
        StuId=getIntent().getExtras().getString(";Stuid");
        course= (Course) getIntent().getExtras().getSerializable("CourseInfo");
        mTvBack=findViewById(R.id.tv_back);
        mTvCourseName=findViewById(R.id.tv_result_name);
        mTvClass=findViewById(R.id.tv_result_class);
        mTvTea=findViewById(R.id.tv_result_tea);
        mTvCtime=findViewById(R.id.tv_result_time);
        mTvCreateT=findViewById(R.id.tv_result_create);
        mBtnJoin=findViewById(R.id.btn_join);
        mTvCreq=findViewById(R.id.tv_result_req);
    }
    private void SetData(){
        mTvCourseName.setText(course.getCourse_name());
        mTvClass.setText(course.getCourse_class());
        mTvTea.setText(course.getTeacher_user_id());
        mTvCtime.setText(course.getCourse_time());
        mTvCreateT.setText(course.getRegister_time());
        mTvCreq.setText(course.getCourse_request());
    }
    private void SetOnClickListener(){
        OnClick onClick =new OnClick();
        mTvBack.setOnClickListener(onClick);
        mBtnJoin.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_back:
                    finish();
                    break;
                case R.id.btn_join:
                    String url ="";
                    Map<String,String> params=new HashMap<>();
                    params.put("Sid",StuId);
                    params.put("Cid",course.getCourse_id());
                    VolleyCallback.getJSONObject(getApplicationContext(), "select course", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            //这理
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
