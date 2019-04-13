package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;

import java.util.List;

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.ViewHolder> {
   private List<Point> mPointList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mDate,mVoiceSound,mPointContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate=itemView.findViewById(R.id.tv_review_date);
            mVoiceSound=itemView.findViewById(R.id.tv_voice_time);
            mPointContent=itemView.findViewById(R.id.tv_point_content);
        }
    }
    public MyReviewListAdapter(List<Point> pointList){
        mPointList=pointList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_review_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Point point=mPointList.get(i);
        viewHolder.mDate.setText(point.getPoint_time());
        viewHolder.mPointContent.setText(point.getPoint_content());
        //填入录音时间
    }

    @Override
    public int getItemCount() {
        return mPointList.size();
    }


}
