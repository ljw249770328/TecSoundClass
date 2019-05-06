package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Activity.LoginActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.Follow;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFriendListAdapter extends Adapter<MyFriendListAdapter.Viewholder> {
    private Context mContext;
    private List<Follow> mFollowList;
    private Handler handler=null;
    private String user_id="";
    private Viewholder holder=null;
    public MyFriendListAdapter(List<Follow> list,Context context){
        this.mContext=context;
        mFollowList=list;
    }
    class Viewholder extends RecyclerView.ViewHolder{
        ImageView mFriendPic;
        TextView mFriendName,mFriendStatus,mTime;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mFriendPic=itemView.findViewById(R.id.iv_friend_pic);
            mFriendName=itemView.findViewById(R.id.tv_friend_name);
            mFriendStatus=itemView.findViewById(R.id.tv_friend_status);
            mTime=itemView.findViewById(R.id.tv_time);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {
        View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_my_friends_item,null);
        Viewholder holder=new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        user_id=mFollowList.get(i).getFan_user_id();

        new Thread(){
            @Override
            public void run() {
                super.run();
                String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                VolleyCallback.getJSONObject(mContext, "GetFollowuser", url, params, new VolleyCallback.VolleyJsonCallback() {
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
        holder=viewholder;
        handler =new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                 switch (msg.what){
                     case 1:
                        User user = (User) msg.obj;
                        Toast.makeText(mContext,String.valueOf(holder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                         holder.mFriendName.setText(user.getUser_name());

                 }
            }
        };
        viewholder.mFriendStatus.setText("正在上'java基础'");
        viewholder.mTime.setText("2:22");
    }
    @Override
    public int getItemCount() {
        return mFollowList.size();
    }
}
