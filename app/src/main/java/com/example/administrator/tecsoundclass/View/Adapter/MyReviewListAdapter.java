package com.example.administrator.tecsoundclass.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.tecsoundclass.model.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.FileDownloadUtil;
import com.example.administrator.tecsoundclass.utils.Tencent.TencentApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.PointItemViewHolder> {
    private List<Point> mPointList;
    private OnPointItemLongClickListener mListener;
    private Context mContext;
    private Activity mActivity;
    private MyKeyWordAdapter adapter;
    private List<String> mResultList=new ArrayList<>();


    public class PointItemViewHolder extends RecyclerView.ViewHolder{
        View Pointview;
        TextView PopSound,mTvNoKeyword;
        TextView mDate,mPointContent;
        RecyclerView mRvKeyWord;
        public View getPointview() {
            return Pointview;
        }
        public PointItemViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            Pointview=itemView;
            mDate=itemView.findViewById(R.id.tv_review_date);
            PopSound=itemView.findViewById(R.id.tv_voice_time);
            mPointContent=itemView.findViewById(R.id.tv_point_content);
            mRvKeyWord=itemView.findViewById(R.id.rv_prev_keyword);
            mTvNoKeyword=itemView.findViewById(R.id.tv_no_keyword);
            mRvKeyWord.setLayoutManager(new GridLayoutManager(context,3));
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
    public MyReviewListAdapter(Activity activity, Context context, List<Point> pointList){
        mPointList=pointList;
        mContext=context;
        mActivity=activity;
    }
    @NonNull
    @Override
    public PointItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_review_item,viewGroup,false);
        PointItemViewHolder holder=new PointItemViewHolder(mContext,view);

        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PointItemViewHolder viewHolder, final int i) {
        final Point point=mPointList.get(i);
        viewHolder.mDate.setText(point.getPoint_time());
        viewHolder.mPointContent.setText(point.getPoint_content());
        TencentApi.KeywordAnalysis(point.getPoint_content(), new TencentApi.TApiCallback() {
            @Override
            public void ResultCallback(final String resp) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        com.alibaba.fastjson.JSONObject object= JSON.parseObject(resp);
                        List<com.alibaba.fastjson.JSONObject> resultList= (List<JSONObject>) object.get("Keywords");
                        mResultList=new ArrayList<>();
                        if (resultList.size()!=0){
                            for (com.alibaba.fastjson.JSONObject obj:resultList){
                                if ((Float.valueOf(obj.get("Score").toString())>0.75)){
                                    mResultList.add((String) obj.get("Word"));
                                }
                            }
                            adapter=new MyKeyWordAdapter(mContext,mResultList);
                            adapter.notifyDataSetChanged();
                            viewHolder.mRvKeyWord.setAdapter(adapter);

                        }else {
                            viewHolder.mRvKeyWord.setVisibility(View.GONE);
                            viewHolder.mTvNoKeyword.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });


        viewHolder.PopSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FileDownloadUtil.mDownLoadFile(point.getPoint_voice_src(), new FileDownloadUtil.DownloadCallBack() {
                        @Override
                        public void mOncompleted(String FilePath) {
                            try {
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
