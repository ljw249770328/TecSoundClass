package com.example.administrator.tecsoundclass.JavaBean;

import android.app.Application;

import java.util.HashMap;

import okhttp3.WebSocket;

public class MyApplication extends Application {
    private static HashMap<String, Object> map=new HashMap<String, Object>();

    public static WebSocket getmWebsocket() {
        return mWebsocket;
    }

    public static void setmWebsocket(WebSocket mWebsocket) {
        MyApplication.mWebsocket = mWebsocket;
    }

    public static WebSocket mWebsocket;

    public static User getmUser() {
        return mUser;
    }

    public static void setmUser(User mUser) {
        MyApplication.mUser = mUser;
    }

    private static User mUser;
    private static MyApplication application;
    public static MyApplication getApplication(){
        return application;
    }
    public static HashMap<String, Object> getMap(){
        return map;
    }
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //在Application创建时,读取Application
        application=this;
    }

}
