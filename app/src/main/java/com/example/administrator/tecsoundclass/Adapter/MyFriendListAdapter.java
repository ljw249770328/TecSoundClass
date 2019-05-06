package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.JavaBean.Follow;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TransferMore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MyFriendListAdapter extends Adapter<MyFriendListAdapter.Viewholder> {
    private Context mContext;
    private List<Follow> mFollowList;

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
    public void onBindViewHolder(@NonNull final Viewholder viewholder, int i) {
        user_id=mFollowList.get(i).getFan_user_id();
        TransferMore.GetUserById(mContext,user_id,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        User user = (User) msg.obj;
                        viewholder.mFriendName.setText(user.getUser_name());
                        try {
                            if (!user.getUser_pic_src().equals("")){
                                Glide.with(mContext).load(new URL(user.getUser_pic_src())).signature(new ObjectKey(user.getUpdate_time())).encodeQuality(70).into(viewholder.mFriendPic);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
        viewholder.mFriendStatus.setText("正在上'java基础'");
        viewholder.mTime.setText("2:22");
    }
    @Override
    public int getItemCount() {
        return mFollowList.size();
    }
}
