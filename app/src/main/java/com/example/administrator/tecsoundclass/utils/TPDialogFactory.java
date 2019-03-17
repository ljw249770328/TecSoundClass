package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TPDialogFactory {
    public String getTime() {
        return time;
    }

    private  String time,date;
    public TPDialogFactory(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
    }
    public  CustomDatePicker getFullDatePicker(Context con, String Tag, final TextView tv){
         CustomDatePicker fulldatepicker = new CustomDatePicker(con, Tag, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time);
            }
        }, "1949-01-01 00:00", time);
        fulldatepicker.showSpecificTime(true); //显示时和分
        fulldatepicker.showSpecificDate(true);
        fulldatepicker.setIsLoop(false);
        fulldatepicker.setDayIsLoop(true);
        return fulldatepicker;
    }
    public  CustomDatePicker getDatePicker(Context con, String Tag, final TextView tv){
        CustomDatePicker datepicker = new CustomDatePicker(con, Tag, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time.split(" ")[0]);
            }
        }, "1949-01-01 00:00", time);
        datepicker.showSpecificTime(false); //显示时和分
        datepicker.showSpecificDate(true);
        datepicker.setIsLoop(false);
        datepicker.setDayIsLoop(true);
        return datepicker;
    }
    public  CustomDatePicker getTimePicker(Context con, String Tag, final TextView tv){
        CustomDatePicker timepicker = new CustomDatePicker(con, Tag, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.append(time);
            }
        }, "1949-01-01 00:00", time);
        timepicker.showSpecificTime(true);
        timepicker.showSpecificDate(false);
        timepicker.setIsLoop(true);
        return timepicker;
    }

}
