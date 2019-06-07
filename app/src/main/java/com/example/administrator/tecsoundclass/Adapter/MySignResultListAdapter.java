package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.CircleImageView;
import com.example.administrator.tecsoundclass.utils.TransferMore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MySignResultListAdapter extends Adapter<MySignResultListAdapter.ViewHolder> {
    private List<String> mResList;
    private Context mContext;
    private User user= new User();
    public MySignResultListAdapter(List<String> ResList, Context context){
        mResList=ResList;
        mContext=context;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mCivHead;
        TextView mTvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCivHead=itemView.findViewById(R.id.cv_headpic);
            mTvName=itemView.findViewById(R.id.tv_name);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_signres_item,null);
        Log.e("msg","显示成功");
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String Sid=mResList.get(i);

        TransferMore.GetUserById(mContext,Sid,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        user= (User) msg.obj;
                        viewHolder.mTvName.setText(user.getUser_name());
                        try {
                            Glide.with(mContext).load(new URL(user.getUser_pic_src())).signature(new ObjectKey(user.getUpdate_time())).encodeQuality(70).into(viewHolder.mCivHead);
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
        Log.d("count",String.valueOf(mResList.size()));
        return mResList.size();
    }


}
