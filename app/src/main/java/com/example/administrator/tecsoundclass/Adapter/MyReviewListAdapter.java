package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;

import java.util.List;

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.PointItemViewHolder> {
    private List<Point> mPointList;
    private OnPointItemLongClickListener mListener;

    public class PointItemViewHolder extends RecyclerView.ViewHolder{
        View Pointview;
        TextView mDate,mVoiceSound,mPointContent;

        public View getPointview() {
            return Pointview;
        }
        public PointItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Pointview=itemView;
            mDate=itemView.findViewById(R.id.tv_review_date);
            mVoiceSound=itemView.findViewById(R.id.tv_voice_time);
            mPointContent=itemView.findViewById(R.id.tv_point_content);
            //设置点击事件
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener!=null){
                        mListener.onItemLongClick(getAdapterPosition(),mPointList);
                    }
                    return false;
                }
            });
        }
    }
    public MyReviewListAdapter(List<Point> pointList){
        mPointList=pointList;
    }
    @NonNull
    @Override
    public PointItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_review_item,viewGroup,false);
        PointItemViewHolder holder=new PointItemViewHolder(view);

        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointItemViewHolder viewHolder, int i) {
        Point point=mPointList.get(i);
        viewHolder.mDate.setText(point.getPoint_time());
        viewHolder.mPointContent.setText(point.getPoint_content());
        //填入录音时间
    }

    @Override
    public int getItemCount() {
        return mPointList.size();
    }

    public void setOnItemLongClickListener(OnPointItemLongClickListener listener){
        mListener=listener;
    }

    public interface OnPointItemLongClickListener{
        void onItemLongClick(int pos, List<Point> pointList);
    }

}
