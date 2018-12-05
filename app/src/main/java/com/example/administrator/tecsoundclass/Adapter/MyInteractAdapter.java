package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class MyInteractAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public MyInteractAdapter(Context context){
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
        public ImageView mInteractVoi;
        public TextView mInteractScore,InteractDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_my_interact_item,null);
            holder=new ClassViewHolder();
            holder.mInteractVoi=convertView.findViewById(R.id.iv_interact_voice);
            holder.mInteractScore=convertView.findViewById(R.id.tv_interact_score);
            holder.InteractDate=convertView.findViewById(R.id.tv_interact_date);
            convertView.setTag(holder);
        }else {
            holder= (ClassViewHolder) convertView.getTag();
        }
        holder.mInteractScore.setText("88");
        holder.InteractDate.setText("2018.12.5");
        holder.mInteractVoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
