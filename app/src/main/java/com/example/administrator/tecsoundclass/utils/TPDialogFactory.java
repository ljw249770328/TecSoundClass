package com.example.administrator.tecsoundclass.utils;

import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TPDialogFactory {
    private String time,date;
    private CustomDatePicker fulldatepicker,datePicker,timePicker;
    public TPDialogFactory(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置当前显示的时间
//        mTvClassTime.setText(time);
    }
    public CustomDatePicker GetFullDatePicker(String Tag,final TimeCallback callback){
        /**
         * 设置年月日时分
         */
        fulldatepicker = new CustomDatePicker(this, Tag, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                callback.OnTimeSet(time);
            }
        }, "2007-01-01 00:00", time);

        fulldatepicker.showSpecificTime(true); //显示时和分
        fulldatepicker.showSpecificDate(true);
        fulldatepicker.setIsLoop(false);
        fulldatepicker.setDayIsLoop(true);
        return  fulldatepicker;
    }
    private void GetDatePicker(String Tag,final TimeCallback callback){
        /**
         * 设置年月日
         */
                datePicker = new CustomDatePicker(this, "请选择日期", new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        callback.OnTimeSet(time);
                    }
                }, "2007-01-01 00:00", time);
                Log.d("-------------selectet_time---------",time);
                datePicker.showSpecificTime(false); //显示时和分
                datePicker.showSpecificDate(true);
                datePicker.setIsLoop(false);
                datePicker.setDayIsLoop(true);
                datePicker.show(time);
    }
    private void GetTimePicker(String Tag,final TimeCallback callback){
        /**
         * 设置小时分钟
         */
        timePicker = new CustomDatePicker(this, "请选择时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                callback.OnTimeSet(time);
            }
        }, "2007-01-01 00:00", "2027-12-31 23:59");//"2027-12-31 23:59"
        timePicker.showSpecificTime(true);
        timePicker.showSpecificDate(false);
        timePicker.setIsLoop(true);
        timePicker.show(time);
    }
    public interface TimeCallback{
        void OnTimeSet(String time);
    }
}
