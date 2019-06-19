package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketClientObject extends WebSocketClient {
    public static WebSocketClientObject client;
    public static Handler mHandler;
    public static Context mContext;
//    public static String Uri ="ws://172.20.10.10:8886";
    public static String Uri ="ws://101.132.71.111:8886";
    private Gson gson=new Gson();
    public WebSocketClientObject(URI serverUri, Draft draft,Map<String, String> header, int timeout) {
        super(serverUri, draft,header,timeout);
    }

    public static WebSocketClientObject getClient(Context context,Handler handler, @Nullable Map<String, String> header){

        try {
            if (client==null){
                Log.e("开始连接：",Uri);
                    client=new WebSocketClientObject(new URI(Uri),new Draft_17(),header,2000);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            client.close();
        }
        mContext=context;
        mHandler=handler;
        return  client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("mSocket","成功与服务器【"+getURI()+"】连接");
        Message msg =new Message();
        msg.what= 0;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onMessage(String message) {
        Log.e("mSocket","获取到服务器【"+getURI()+"】的消息"+message);
        Map<String,String> params =new HashMap<>();
        Intent intent;
        try {
            params=gson.fromJson(URLDecoder.decode(message,"UTF-8"), params.getClass());
            Message msg =new Message();
            switch (params.get("intent")){
                case "SIGN_STARTED":
                    msg.what=1;
                    mHandler.sendMessage(msg);
                    break;
                case "SIGN_STOPPED":
                    List<String> SignStu=new ArrayList<>();
                    SignStu=gson.fromJson(params.get("SignList"),SignStu.getClass());
                    msg.what=3;
                    msg.obj=SignStu;
                    mHandler.sendMessage(msg);
                    break;
                case "SIGN_DENYED":
                    msg.what=0;
                    mHandler.sendMessage(msg);
                    break;
                case "SIGN_ACCESSED":
                    msg.what=4;
                    mHandler.sendMessage(msg);
                    break;
                case "SIGN_ED":
                    msg.what=5;
                    mHandler.sendMessage(msg);
                    break;
                case "COME_QUESTION":
//                    msg.what=6;
//                    msg.obj=params.get("question");
//                    mHandler.sendMessage(msg);
                    Log.e("fragment", params.get("question")+params.get("CourseId"));
                    intent=new Intent("com.example.administrator.tecsoundclass.COME_MESSAGE");
                    intent.putExtra("question", params.get("question"));
                    intent.putExtra("Cid",params.get("CourseId"));
                    mContext.sendBroadcast(intent);
                    break;
                case "GRADE_DIALOG":
                    intent=new Intent("com.example.administrator.tecsoundclass.ON_GRADE");
                    intent.putExtra("params", (Serializable) params);
                    Log.e("beforebroadcast",params.toString());
                    mContext.sendBroadcast(intent);
                    break;
                case "GRADE_ED":
                    intent=new Intent("com.example.administrator.tecsoundclass.GRADE_ED");
                    intent.putExtra("grade",params.get("Grade"));
                    mContext.sendBroadcast(intent);
                    break;
                case "DIALOG_CANCLE":
                    msg.what=7;
                    mHandler.sendMessage(msg);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("mSocket","与服务器【"+getURI()+"】断开连接，返回码："+code);
        Intent intent=new Intent("com.example.administrator.tecsoundclass.FORCE_OFFLINE");
        mContext.sendBroadcast(intent);
        client=null;
    }

    @Override
    public void onError(Exception ex) {
        client=null;
        Log.e("mSocket","与服务器【"+getURI()+"】连接异常，异常原因："+ex);
    }
}
