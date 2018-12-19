package com.example.administrator.tecsoundclass.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Interaction;
import com.example.administrator.tecsoundclass.R;

import java.util.List;

public class MyInteractAdapter extends RecyclerView.Adapter<MyInteractAdapter.ViewHolder>{
    private List<Interaction> mInteractList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mScore,mDate;
        ImageView mVoice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mScore=itemView.findViewById(R.id.tv_interact_score);
            mDate=itemView.findViewById(R.id.tv_interact_date);
            mVoice=itemView.findViewById(R.id.iv_interact_voice);
        }
    }
    public MyInteractAdapter(List<Interaction> interactionList){
        mInteractList=interactionList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_interact_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Interaction interaction=mInteractList.get(i);
        viewHolder.mScore.setText(interaction.getAnswer_grace()+"");
        viewHolder.mDate.setText(interaction.getAnswer_time());
//        viewHolder.mVoice.setText(sign.getSign_state());声音路径
    }

    @Override
    public int getItemCount() {
       return mInteractList.size();
    }


}
