package com.example.administrator.tecsoundclass.Adapter;


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

import java.util.List;

public class MyInteractAdapter extends RecyclerView.Adapter<MyInteractAdapter.InteractItemViewHolder>{
    private List<Interaction> mInteractList;
    private OnInteractItemLongClickListener mListener;

    public class InteractItemViewHolder extends RecyclerView.ViewHolder{
        TextView mScore,mDate;
        ImageView mVoice;
        View interactview ;
        public View getInteractview() {
            return interactview;
        }
        public InteractItemViewHolder(@NonNull View itemView) {
            super(itemView);
            interactview=itemView;
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
        Interaction interaction=mInteractList.get(i);
        viewHolder.mScore.setText(interaction.getAnswer_grade()+"");
        viewHolder.mDate.setText(interaction.getAnswer_time());
//        viewHolder.mVoice.setText(sign.getSign_state());声音路径
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
