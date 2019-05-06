package com.example.administrator.tecsoundclass.Adapter;


import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Interaction;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.FileDownloadUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyInteractAdapter extends RecyclerView.Adapter<MyInteractAdapter.InteractItemViewHolder>{
    private List<Interaction> mInteractList;
    private OnInteractItemLongClickListener mListener;

    public class InteractItemViewHolder extends RecyclerView.ViewHolder{
        TextView mScore,mDate,mTvQuestion,mTvAnswer;
        ImageView mVoice;
        View interactview ;
        public View getInteractview() {
            return interactview;
        }
        public InteractItemViewHolder(@NonNull View itemView) {
            super(itemView);
            interactview=itemView;
            mTvQuestion=itemView.findViewById(R.id.tv_question);
            mTvAnswer=itemView.findViewById(R.id.tv_answer);
            mScore=itemView.findViewById(R.id.tv_interact_score);
            mDate=itemView.findViewById(R.id.tv_interact_date);
            mVoice=itemView.findViewById(R.id.iv_interact_voice);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (mListener!=null){
                        mListener.onItemLongClick(getAdapterPosition(),mInteractList);
                    }
                    return true;
                }
            });
        }
    }
    public MyInteractAdapter(List<Interaction> interactionList){
        mInteractList=interactionList;
    }
    @NonNull
    @Override
    public InteractItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_interact_item,viewGroup,false);
        InteractItemViewHolder holder=new InteractItemViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InteractItemViewHolder viewHolder, int i) {
        final Interaction interaction=mInteractList.get(i);
        String[] fulldate= interaction.getAnswer_time().split(" ");
        String[] date =fulldate[0].split("-");
        String[] time = fulldate[1].split(":");
        String mDate =date[1]+"."+date[2]+" "+time[0]+":"+time[1];
        viewHolder.mScore.setText(interaction.getAnswer_grade()+"");
        viewHolder.mDate.setText(mDate);
        viewHolder.mTvQuestion.setText("面向对象的特征有哪些?");
        viewHolder.mTvAnswer.setText(interaction.getAnswer_content());
        viewHolder.mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloadUtil.mDownLoadFile(interaction.getAnswer_content_src(), new FileDownloadUtil.DownloadCallBack() {
                    @Override
                    public void mOncompleted(String FilePath) {
                        try {
                            MediaPlayer player =new MediaPlayer();
                            player.setDataSource(FilePath);
                            player.prepare();
                            player.start();
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
    }

    @Override
    public int getItemCount() {
       return mInteractList.size();
    }

    public void SetOnItemLongClickListener(OnInteractItemLongClickListener listener){
        mListener=listener;
    }
    public interface OnInteractItemLongClickListener{
        void onItemLongClick(int pos,List<Interaction> interactionList);
    }

}
