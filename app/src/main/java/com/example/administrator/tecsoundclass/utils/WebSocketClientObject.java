package com.example.administrator.tecsoundclass.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClientObject extends WebSocketClient {
    public static WebSocketClientObject client;
    public static Handler mHandler;
    public static String Uri ="ws://192.168.1.104:8886";
//    public static String Uri ="ws://101.132.71.111:8886";

    public WebSocketClientObject(URI serverUri, Draft draft,Map<String, String> header, int timeout) {
        super(serverUri, draft,header,timeout);
    }

    public static WebSocketClientObject getClient(Handler handler,@Nullable Map<String, String> header){

        try {
            if (client==null){
                Log.e("开始连接：",Uri);
                    client=new WebSocketClientObject(new URI(Uri),new Draft_17(),header,2000);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            client.close();
        }
        mHandler=handler;
        return  client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("mSocket","成功与服务器【"+getURI()+"】连接");
        Message msg =new Message();
        msg.what= 1;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onMessage(String message) {
        Log.e("mSocket","获取到服务器【"+getURI()+"】的消息"+message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("mSocket","与服务器【"+getURI()+"】断开连接，返回码："+code);
        client=null;
    }

    @Override
    public void onError(Exception ex) {
        client=null;
        Log.e("mSocket","与服务器【"+getURI()+"】连接异常，异常原因："+ex);
    }
}
