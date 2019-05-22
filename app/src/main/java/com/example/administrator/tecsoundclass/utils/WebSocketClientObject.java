package com.example.administrator.tecsoundclass.utils;

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
import java.util.logging.Handler;

public class WebSocketClientObject extends WebSocketClient {
    public static WebSocketClientObject client;
    public static Handler mHandler;
    public static String Uri ="";

    public WebSocketClientObject(URI serverUri, Draft draft, Map<String, String> header, int timeout) {
        super(serverUri, draft);
    }

    public static WebSocketClientObject getClient(Handler handler,@Nullable String connId){

        try {
            if (client==null){
                Log.e("开始连接：",Uri);
                Map<String,String> header =new HashMap<>();
                if(connId!=null){
                    header.put("id",connId);
                }
                client=new WebSocketClientObject(new URI(Uri),new Draft_17(),header,5000);
                client.close();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mHandler=handler;
        return  client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

        Log.e("mSocketconn","成功与服务器【"+getURI()+"】连接");
    }

    @Override
    public void onMessage(String message) {
        Log.e("mSocketMess","获取到服务器【"+getURI()+"】的消息"+message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("mSocketcls","与服务器【"+getURI()+"】断开连接，返回码："+code);
    }

    @Override
    public void onError(Exception ex) {
        Log.e("mSocketeError","与服务器【"+getURI()+"】连接异常，异常原因："+ex);
    }
}
