package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.JavaBean.Sign;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.TransferMore;

import java.util.IdentityHashMap;
import java.util.List;

public class MySignListAdapter extends RecyclerView.Adapter<MySignListAdapter.SignItemViewHolder> {
    private List<Sign> mSignList;
    private OnSignItemLongClickListener mListener;
    private Context mContext=null;
    private String mIdentity="";

     public class SignItemViewHolder extends  RecyclerView.ViewHolder{
        TextView mTime,mMan,mState;
        View mItemView;
         public View getmItemView() {
             return mItemView;
         }
        public SignItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTime=itemView.findViewById(R.id.tv_sign_time);
            mMan=itemView.findViewById(R.id.tv_sign_man);
            mState=itemView.findViewById(R.id.tv_sign_status);
            mMan.setVisibility(View.GONE);
            mItemView=itemView;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener!=null) {
                        mListener.onItemLongClick(getAdapterPosition(), mSignList);
                    }
                    return true;
                }
            });
        }
    }

    public MySignListAdapter(Context context,List<Sign> signlist,String identity){
        mContext=context;
        mSignList=signlist;
        mIdentity=identity;
    }

    @NonNull
    @Override
    public SignItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_sign_item,viewGroup,false);
        SignItemViewHolder holder=new SignItemViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SignItemViewHolder viewHolder, int i) {
        final Sign sign=mSignList.get(i);
        viewHolder.mTime.setText(sign.getSign_time());
        viewHolder.mState.setText(sign.getSign_state());
        if (mIdentity.equals("老师")){
            viewHolder.mMan.setVisibility(View.VISIBLE);
            TransferMore.GetUserById(mContext,sign.getSign_user_id(),new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            User user= (User) msg.obj;
                            viewHolder.mMan.setText(user.getUser_name());
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSignList.size();

    }
    public void setOnItemLongClickListener(OnSignItemLongClickListener listener){
        mListener= listener;
    }
    public interface OnSignItemLongClickListener{
        void onItemLongClick(int position, List<Sign> SignList);
    }


}
