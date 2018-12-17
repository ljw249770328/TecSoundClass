package com.example.administrator.tecsoundclass.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timer {
    private DateFormat dateformatter,timeformatter;
    public Timer(){
        dateformatter = new SimpleDateFormat("yyyy-MM-dd");
        timeformatter = new SimpleDateFormat("HH:mm:ss");
    }
    public String getmTime() {
        return timeformatter.format(Calendar.getInstance().getTime());
    }
    public String getmDate() {
        return dateformatter.format(Calendar.getInstance().getTime());
    }
}
