package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class MyFriendListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public MyFriendListAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static  class ClassViewHolder{
        public ImageView mFriendPic;
        public TextView mFriendName,mFriendStatus,mTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_my_friends_item,null);
            holder=new ClassViewHolder();
            holder.mFriendPic=convertView.findViewById(R.id.iv_friend_pic);
            holder.mFriendName=convertView.findViewById(R.id.tv_friend_name);
            holder.mFriendStatus=convertView.findViewById(R.id.tv_friend_status);
            holder.mTime=convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder= (ClassViewHolder) convertView.getTag();
        }
        holder.mFriendName.setText("小明");
        holder.mFriendStatus.setText("正在上'java基础'");
        holder.mTime.setText("2:22");
        return convertView;
    }
}
