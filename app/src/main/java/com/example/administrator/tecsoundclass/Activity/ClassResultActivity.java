package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;

public class ClassResultActivity extends AppCompatActivity {
    private TextView mTvBack,mTvCourseName,mTvClass,mTvTea,mTvCtime,mTvCreateT,mTvCreq;
    private Course course;
    private String StuId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_result);
        init();
        SetData();
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
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

}
