package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.PointItemViewHolder> {
    private List<Point> mPointList;
    private OnPointItemLongClickListener mListener;

    public class PointItemViewHolder extends RecyclerView.ViewHolder{
        View Pointview;
        View PopSound;
        TextView mDate,mVoiceSound,mPointContent;
        public View getPointview() {
            return Pointview;
        }
        public PointItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Pointview=itemView;
            PopSound=itemView.findViewById(R.id.v_pop_pointsound);
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
                    return true;
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
    public void onBindViewHolder(@NonNull PointItemViewHolder viewHolder, final int i) {
        Point point=mPointList.get(i);
        viewHolder.mDate.setText(point.getPoint_time());
        viewHolder.mPointContent.setText(point.getPoint_content());
        viewHolder.PopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file =new File(mPointList.get(i).getPoint_voice_src());
                    Log.d("File",file.getPath());
                    MediaPlayer mediaPlayer =new MediaPlayer();
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
