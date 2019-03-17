package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.utils.CustomDatePicker;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TPDialogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateClassActivity extends AppCompatActivity {
    private TextView mTvCancle,mTvNext,mTvClassTime,mTvClassOver;
    private EditText mEtClassNum,mEtClassName,mEtClassInfo;
    private ImageView mIvLoadPic;
    private TPDialogFactory factory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        init();
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SetListener();
    }
    private void  init(){
        mTvCancle=findViewById(R.id.tv_cancle);
        mTvNext=findViewById(R.id.tv_next);
        mEtClassInfo=findViewById(R.id.class_edt_moreinfo);
        mEtClassName=findViewById(R.id.class_edt_name);
        mEtClassNum=findViewById(R.id.class_edt_number);
        mIvLoadPic=findViewById(R.id.iv_load_class_pic);
        mTvClassTime=findViewById(R.id.tv_class_set_time);
        mTvClassOver=findViewById(R.id.tv_class_set__over_time);
        factory=new TPDialogFactory();
    }
    private void SetListener(){
        OnClick onClick=new OnClick();
        mTvCancle.setOnClickListener(onClick);
        mTvClassTime.setOnClickListener(onClick);
        mTvClassOver.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancle:
                    finish();
                    break;
                case R.id.tv_class_set_time:
                    mTvClassTime.setText("上课时间");
                    factory.getTimePicker(CreateClassActivity.this,"上课时间",mTvClassTime).show(factory.getTime());
                    break;
                case R.id.tv_class_set__over_time:
                    mTvClassOver.setText("下课时间");
                    factory.getTimePicker(CreateClassActivity.this,"下课时间",mTvClassOver).show(factory.getTime());
                    break;
            }
        }
    }

}
