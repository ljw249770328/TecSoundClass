package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
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
import com.example.administrator.tecsoundclass.utils.FileDownloadUtil;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.PointItemViewHolder> {
    private List<Point> mPointList;
    private OnPointItemLongClickListener mListener;

    public class PointItemViewHolder extends RecyclerView.ViewHolder{
        View Pointview;
        TextView PopSound;
        TextView mDate,mPointContent;
        public View getPointview() {
            return Pointview;
        }
        public PointItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Pointview=itemView;
            mDate=itemView.findViewById(R.id.tv_review_date);
            PopSound=itemView.findViewById(R.id.tv_voice_time);
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
                    FileDownloadUtil.mDownLoadFile(mPointList.get(i).getPoint_voice_src(), new FileDownloadUtil.DownloadCallBack() {
                        @Override
                        public void mOncompleted(String FilePath) {
                            try {
                                Log.d("Mfilepath",FilePath);
                                MediaPlayer mediaPlayer =new MediaPlayer();
                                mediaPlayer.setDataSource(FilePath);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void mOnError(Throwable e) {

                        }
                    });
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
