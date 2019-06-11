package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.Activity.ChatActivity;
import com.example.administrator.tecsoundclass.Activity.LoginActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.Follow;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TransferMore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyFriendListAdapter extends Adapter<MyFriendListAdapter.Viewholder> {
    private Context mContext;
    private List<Follow> mFollowList;
    private User user=null;



    private List<User> mFans=new ArrayList<>();
    private String user_id="";
    private Viewholder holder=null;
    private OnRecyclerItemClickListener mListener;
    public MyFriendListAdapter(List<Follow> list,Context context){
        this.mContext=context;
        mFollowList=list;
    }
    public List<User> getmFans() {
        return mFans;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onItemClick(getAdapterPosition(),mFans);
                    }
                }
            });
        }
    }

    public void  SetOnItemClickListener(OnRecyclerItemClickListener listener){
        mListener=listener;
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
        TransferMore.GetUserById(mContext,user_id,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        user = (User) msg.obj;
                        mFans.add(user);
                        viewholder.mFriendName.setText(user.getUser_name());
                        try {
                            if (!user.getUser_pic_src().equals("")){
                                Glide.with(mContext).load(new URL(user.getUser_pic_src())).signature(new ObjectKey(user.getUpdate_time())).encodeQuality(70).into(viewholder.mFriendPic);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                }
                return false;
            }
        }));
        viewholder.mFriendStatus.setText("正在上'java基础'");
        viewholder.mTime.setText("2:22");
    }
    @Override
    public int getItemCount() {
        return mFollowList.size();
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(int posision,List<User> mFans);
    }
}
