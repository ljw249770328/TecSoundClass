package com.example.administrator.tecsoundclass.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Sign;
import com.example.administrator.tecsoundclass.R;

import java.util.List;

public class MySignListAdapter extends RecyclerView.Adapter<MySignListAdapter.ViewHolder> {
    private List<Sign> mSignList;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView mTime,mDate,mState;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTime=itemView.findViewById(R.id.tv_sign_time);
            mDate=itemView.findViewById(R.id.tv_sign_date);
            mState=itemView.findViewById(R.id.tv_sign_status);
        }
    }

    public MySignListAdapter(List<Sign> signlist){
        mSignList=signlist;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_sign_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Sign sign=mSignList.get(i);
        viewHolder.mTime.setText(sign.getSign_time());
        viewHolder.mDate.setText(sign.getSign_date());
        viewHolder.mState.setText(sign.getSign_state());
    }

    @Override
    public int getItemCount() {
        return mSignList.size();
    }


}
