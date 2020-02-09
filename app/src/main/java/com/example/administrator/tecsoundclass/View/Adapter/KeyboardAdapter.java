package com.example.administrator.tecsoundclass.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.administrator.tecsoundclass.R;

public class KeyboardAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public KeyboardAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static  class  ViewHolder{
        public Button button;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_keyboard_item,null);
            holder=new ViewHolder();
            holder.button=convertView.findViewById(R.id.btn_num);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if(position==0){
            holder.button.setText("1");
        }else if (position==1){
            holder.button.setText("2");
        }else if (position==2){
            holder.button.setText("3");
        }else if (position==3){
            holder.button.setText("4");
        }else if (position==4){
            holder.button.setText("5");
        }else if (position==5){
            holder.button.setText("6");
        }else if (position==6){
            holder.button.setText("7");
        }else if (position==7){
            holder.button.setText("8");
        }else if (position==8){
            holder.button.setText("9");
        }else if (position==9){
            holder.button.setText("X");
        }else if (position==10){
            holder.button.setText("0");
        }else if (position==11){
            holder.button.setText("√");
        }

        return convertView;
    }
}
