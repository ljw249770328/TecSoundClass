package com.example.administrator.tecsoundclass.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.administrator.tecsoundclass.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BackService extends Service {
    SharedPreferences pref;
    private static final String WEBSOCKET_HOST_AND_PORT = "ws://101.132.71.111:8886";//可替换为自己的主机名和端口号
    private static final long HEART_BEAT_RATE = 10* 1000;//每隔15秒进行一次对长连接的心跳检测
    private static WebSocket mWebSocket;
    public static Handler actHandler;
    public static Context mContext;
    public mBinder binder=new mBinder();
    private Gson gson=new Gson();
    private InitSocketThread thread;
    MyApplication mApplication=MyApplication.getApplication();


    private Handler mHandler = new Handler();
    public BackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new InitSocketThread().start();
        mContext=getApplicationContext();

        Log.e("Service","服务已启动");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private long sendTime = 0L;
    // 发送心跳包
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = mWebSocket.send("");//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                if (!isSuccess) {//长连接已断开
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mWebSocket.cancel();//取消掉以前的长连接
                   thread=new InitSocketThread();
                   thread.start();//创建一个新的连接
                } else {//长连接处于连接状态

                }

                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    /**
     * 心跳检测时间
     */


    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .header("id", URLEncoder.encode(pref.getString("userid",""),"UTF-8"))
                .url(WEBSOCKET_HOST_AND_PORT).build();
        okhttp3.WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.e("mSocket","成功与服务器【】连接"+response.toString());
                mWebSocket=webSocket;
                MyApplication.getApplication().setmWebsocket(webSocket);
            }

            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.e("mSocket","获取到服务器【】的消息"+text);
                Map<String,String> params =new HashMap<>();
                Intent intent;
                try {
                    params=gson.fromJson(URLDecoder.decode(text,"UTF-8"), params.getClass());
                    Message msg =new Message();
                    switch (params.get("intent")){
                        case "SIGN_STARTED":
                            msg.what=1;
                            actHandler.sendMessage(msg);
                            break;
                        case "SIGN_STOPPED":
                            List<String> SignStu=new ArrayList<>();
                            SignStu=gson.fromJson(params.get("SignList"),SignStu.getClass());
                            msg.what=3;
                            msg.obj=SignStu;
                            actHandler.sendMessage(msg);
                            break;
                        case "SIGN_DENYED":
                            msg.what=0;
                            actHandler.sendMessage(msg);
                            break;
                        case "SIGN_ACCESSED":
                            msg.what=4;
                            actHandler.sendMessage(msg);
                            break;
                        case "SIGN_ED":
                            msg.what=5;
                            actHandler.sendMessage(msg);
                            break;
                        case "COME_QUESTION":
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
                            intent =new Intent("com.example.administrator.tecsoundclass.DIALOG_CANCEL");
                            intent.putExtra("Ca_Uid",params.get("CaughtUid"));
                            mContext.sendBroadcast(intent);
                            break;
                        case "DRAW_ED":
                            Log.e("picked", params.get("question")+params.get("CourseId"));
                            intent=new Intent("com.example.administrator.tecsoundclass.PICKED");
                            intent.putExtra("question", params.get("question"));
                            intent.putExtra("Cid",params.get("CourseId"));
                            intent.putExtra("CaughtUid",params.get("CaughtUid"));

                            mContext.sendBroadcast(intent);

                            break;
                        case "INTERACT_REFLESH":
                            intent=new Intent("com.example.administrator.tecsoundclass.INTERACT_REFLESH");
                            mContext.sendBroadcast(intent);
                            break;
                        case "COME_CHAT":
                            intent=new Intent("com.example.administrator.tecsoundclass.COME_CHAT");
                            intent.putExtra("message",params.get("message"));
                            intent.putExtra("sender",params.get("Sender"));
                            mContext.sendBroadcast(intent);
                            break;
                        case "FORCE_OFFLINE":
                            intent=new Intent("com.example.administrator.tecsoundclass.FORCE_OFFLINE");
                            mContext.sendBroadcast(intent);
                            break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
                Log.e("mSocket","与服务器【】断开连接，返回码："+code);
            }

            @Override
            public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
                Log.e("mSocket","与服务器【】连接异常");
            }
        });
        client.dispatcher().executorService().shutdown();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    public class mBinder extends Binder{
        public void onServiceMessage(Handler handler){
            actHandler=handler;
        }
    }


//    // 初始化socket
//    private void initSocket() throws UnknownHostException, IOException {
//        pref= PreferenceManager.getDefaultSharedPreferences(this);
//        Map<String,String> header= new HashMap<>();
//        header.put("id", URLEncoder.encode(pref.getString("userid",""),"UTF-8"));
//        client=WebSocketClientObject.getClient(getApplicationContext(),mHandler,header);
//        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
//    }

}
