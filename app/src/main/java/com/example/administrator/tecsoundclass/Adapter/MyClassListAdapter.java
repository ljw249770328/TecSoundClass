package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class MyClassListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public MyClassListAdapter(Context context){
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
        public ImageView mCoursePic;
        public TextView mCourseName,mCourseTea,mClass;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_my_class_item,null);
            holder=new ClassViewHolder();
            holder.mCoursePic=convertView.findViewById(R.id.iv_course_pic);
            holder.mCourseName=convertView.findViewById(R.id.tv_class_name);
            holder.mCourseTea=convertView.findViewById(R.id.tv_tea_name);
            holder.mClass=convertView.findViewById(R.id.tv_class_name);
            convertView.setTag(holder);
        }else {
            holder= (ClassViewHolder) convertView.getTag();
        }
        holder.mCourseName.setText("java基础");
        holder.mCourseTea.setText("xx老师");
        holder.mClass.setText("16级软件工程2班");
        holder.mCoursePic.setImageResource(R.drawable.java);
        return convertView;
    }
}
