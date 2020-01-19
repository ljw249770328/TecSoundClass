package com.example.administrator.tecsoundclass.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketListener;
import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BackService extends Service {
    SharedPreferences pref;
    public BackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new InitSocketThread().start();


        Log.e("Service","服务已启动");
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
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 15 * 1000;//每隔15秒进行一次对长连接的心跳检测
    private WebSocketClient client;

    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
//                boolean isSuccess = client.send("");//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                if (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {//长连接已断开
                    mHandler.removeCallbacks(heartBeatRunnable);
                    client.close();//取消掉以前的长连接
                    new InitSocketThread().start();//创建一个新的连接
                } else {//长连接处于连接状态

                }

                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };



    // 初始化socket
    private void initSocket() throws UnknownHostException, IOException {
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,String> header= new HashMap<>();
        header.put("id", URLEncoder.encode(pref.getString("userid",""),"UTF-8"));
        client=WebSocketClientObject.getClient(getApplicationContext(),mHandler,header);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

}
