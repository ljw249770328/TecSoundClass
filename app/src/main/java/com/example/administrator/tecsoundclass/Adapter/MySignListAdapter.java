package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class MySignListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public MySignListAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 2;
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
        public TextView mSignDate,mSigntime,mSignStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_my_sign_item,null);
            holder=new ClassViewHolder();
            holder.mSignDate=convertView.findViewById(R.id.tv_sign_date);
            holder.mSigntime=convertView.findViewById(R.id.tv_sign_time);
            holder.mSignStatus=convertView.findViewById(R.id.tv_sign_status);
            convertView.setTag(holder);
        }else {
            holder= (ClassViewHolder) convertView.getTag();
        }
        holder.mSignDate.setText("2018.12.5");
        holder.mSigntime.setText("11:14");
        holder.mSignStatus.setText("成功");
        return convertView;
    }

}
