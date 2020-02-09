package com.example.administrator.tecsoundclass.View.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.model.JavaBean.Msg;
import com.example.administrator.tecsoundclass.model.JavaBean.User;
import com.example.administrator.tecsoundclass.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;
    private String user_id="";
    private User mMy=null;
    private User mFans=null;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView mIvLeftPic,mIvRightPic;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);

            mIvLeftPic=view.findViewById(R.id.iv_friend_pic);
            mIvRightPic=view.findViewById(R.id.iv_my_pic);
        }
    }

    public MsgAdapter(Context context,List<Msg> msgList,User Friend,User My) {
        mMsgList = msgList;
        mMy=My;
        mFans=Friend;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        String content = msg.getContent();
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            try {
                Glide.with(mContext).load(new URL(mFans.getUser_pic_src())).signature(new ObjectKey(mFans.getUpdate_time())).encodeQuality(70).into(holder.mIvLeftPic);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (msg.getType() == Msg.TYPE_SENT) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            try {
                Glide.with(mContext).load(new URL(mMy.getUser_pic_src())).signature(new ObjectKey(mMy.getUpdate_time())).encodeQuality(70).into(holder.mIvRightPic);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}
