package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
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

import com.example.administrator.tecsoundclass.JavaBean.Follow;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import java.util.List;


public class MyFriendListAdapter extends Adapter<MyFriendListAdapter.Viewholder> {
    private Context mContext;
    private List<Follow> mFollowList;
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
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_my_friends_item,null);
        Viewholder holder=new Viewholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
//
        viewholder.mFriendName.setText("小明");
        viewholder.mFriendStatus.setText("正在上'java基础'");
        viewholder.mTime.setText("2:22");
    }
    @Override
    public int getItemCount() {
        Toast.makeText(mContext,String.valueOf(mFollowList.size()),Toast.LENGTH_SHORT).show();
        return mFollowList.size();
    }
}
