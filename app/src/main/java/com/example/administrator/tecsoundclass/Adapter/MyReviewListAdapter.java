package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class MyReviewListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public MyReviewListAdapter(Context context){
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
        public TextView mReviewDate,mReviewTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_my_review_item,null);
            holder=new ClassViewHolder();
            holder.mReviewDate=convertView.findViewById(R.id.tv_review_date);
            holder.mReviewTime=convertView.findViewById(R.id.tv_review_time);
            convertView.setTag(holder);
        }else {
            holder= (ClassViewHolder) convertView.getTag();
        }
        holder.mReviewDate.setText("2018.12.5");
        holder.mReviewTime.setText("5'");
        return convertView;
    }
}
