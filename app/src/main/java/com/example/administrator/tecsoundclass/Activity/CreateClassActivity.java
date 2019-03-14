package com.example.administrator.tecsoundclass.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.utils.CustomDatePicker;
import com.example.administrator.tecsoundclass.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateClassActivity extends AppCompatActivity {
    private TextView mTvCancle,mTvNext,mTvClassTime;
    private EditText mEtClassNum,mEtClassName,mEtClassInfo;
    private ImageView mIvLoadPic;
    private CustomDatePicker datePicker,timePicker;
    private String time;
    private String date;
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
        initPicker();
    }
    private void  init(){
        mTvCancle=findViewById(R.id.tv_cancle);
        mTvNext=findViewById(R.id.tv_next);
        mEtClassInfo=findViewById(R.id.class_edt_moreinfo);
        mEtClassName=findViewById(R.id.class_edt_name);
        mEtClassNum=findViewById(R.id.class_edt_number);
        mIvLoadPic=findViewById(R.id.iv_load_class_pic);
        mTvClassTime=findViewById(R.id.tv_class_set_time);
    }
    private void SetListener(){
        OnClick onClick=new OnClick();
        mTvCancle.setOnClickListener(onClick);
        mTvClassTime.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancle:
                    finish();
                    break;
                case R.id.tv_class_set_time:
                    timePicker.show(time);
                    break;
            }
        }
    }
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置当前显示的时间
//        mTvClassTime.setText(time);

        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(this, "请选择日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                mTvClassTime.setText(time.split(" ")[0]);
            }
        }, "2007-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);

        timePicker = new CustomDatePicker(this, "请选择时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                mTvClassTime.setText(time);
            }
        }, "2007-01-01 00:00", "2027-12-31 23:59");//"2027-12-31 23:59"
        timePicker.showSpecificTime(true);
        timePicker.showSpecificDate(false);
        timePicker.setIsLoop(true);
    }
}
