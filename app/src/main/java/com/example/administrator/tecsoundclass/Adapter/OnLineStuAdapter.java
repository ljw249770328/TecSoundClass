package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.CircleImageView;
import com.example.administrator.tecsoundclass.utils.TransferMore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OnLineStuAdapter extends RecyclerView.Adapter<OnLineStuAdapter.OlStuViewHolder> {


    private List<String> mResList;
    private Context mContext;
    private User user= new User();
    private OnRecyclerItemClickListener mListener;

    public OnLineStuAdapter(List<String> ResList, Context context){
        mResList=ResList;
        mContext=context;
    }

    public List<String> getmResList() {
        return mResList;
    }
    class OlStuViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCivHead;
        TextView mTvName;
        public OlStuViewHolder(@NonNull View itemView) {
            super(itemView);
            mCivHead=itemView.findViewById(R.id.cv_headpic);
            mTvName=itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void  SetOnItemClickListener(OnRecyclerItemClickListener listener){
        mListener=listener;
    }
    @NonNull
    @Override
    public OlStuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OlStuViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_list_online_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final OlStuViewHolder olStuViewHolder, int i) {
        String Sid=mResList.get(i);
        TransferMore.GetUserById(mContext,Sid,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        user= (User) msg.obj;
                        olStuViewHolder.mTvName.setText(user.getUser_name());
                        try {
                            Glide.with(mContext).load(new URL(user.getUser_pic_src())).signature(new ObjectKey(user.getUpdate_time())).encodeQuality(70).into(olStuViewHolder.mCivHead);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                return false;
            }
        }));
    }

    @Override
    public int getItemCount() {
        return mResList.size();
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(int posision);
    }

}
