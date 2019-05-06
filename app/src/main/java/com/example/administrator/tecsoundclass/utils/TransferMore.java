package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.tecsoundclass.JavaBean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransferMore {

    public static void GetUserById(final Context context, final String id, final Handler handler){
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
                Map<String, String> params = new HashMap<>();
                params.put("user_id",id);
                VolleyCallback.getJSONObject(context, "GetFollowuser", url, params, new VolleyCallback.VolleyJsonCallback() {
                    @Override
                    public void onFinish(JSONObject r) {
                        try {
                            JSONArray users=r.getJSONArray("users");
                            JSONObject user= (JSONObject) users.get(0);

                            User u=new User();
                            //封装user对象
                            u.setUser_id(user.getString("user_id"));
                            u.setUser_age(user.getString("user_age"));
                            u.setUser_identity(user.getString("user_identity"));
                            u.setUser_sex(user.getString("user_sex"));
                            u.setUser_name(user.getString("user_name"));
                            u.setUser_pic_src(user.getString("user_pic_src"));
                            u.setUpdate_time(user.getString("update_time"));
                            Message message =new Message();
                            message.what=1;
                            message.obj=u;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
}
