package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.View.TextViewBorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MyKeyWordAdapter extends RecyclerView.Adapter<MyKeyWordAdapter.KeyWordViewHolder> {
    private Context mContext;
    private List<String> mKeywordList;
    private List<Integer> ColorList=new ArrayList<>();
    private int[] Color=new int[]{R.color.keyword_black,R.color.keyword_red,R.color.keyword_blue,R.color.keyword_orange,R.color.keyword_yellow,R.color.keyword_pink,
            R.color.keyword_purple,R.color.keyword_brown,R.color.keyword_green};

    public MyKeyWordAdapter(Context context,List<String> keywordList){
        mContext=context;
        mKeywordList=keywordList;
        Log.e("TestNow",mKeywordList.toString());
        for (int color:Color){
            ColorList.add(color);
        }
    }



    class KeyWordViewHolder extends RecyclerView.ViewHolder{
        private TextViewBorder mTvbKetword;
        public KeyWordViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvbKetword=itemView.findViewById(R.id.tvb_keyword);
        }
    }



    @NonNull
    @Override
    public KeyWordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new KeyWordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_keyword_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull KeyWordViewHolder keyWordViewHolder, int i) {
        keyWordViewHolder.mTvbKetword.setText(mKeywordList.get(i));Collections.shuffle(ColorList);
        Random random=new Random();
        int pos=random.nextInt(8);
        keyWordViewHolder.mTvbKetword.setTextColor(mContext.getResources().getColor(ColorList.get(pos)));
        keyWordViewHolder.mTvbKetword.setBorderColor(mContext.getResources().getColor(ColorList.get(pos)));

    }

    @Override
    public int getItemCount() {
        return mKeywordList.size();
    }




}
