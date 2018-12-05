package com.example.administrator.tecsoundclass.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.Adapter.MyClassListAdapter;
import com.example.administrator.tecsoundclass.CourseMenuActivity;
import com.example.administrator.tecsoundclass.CreateClassActivity;
import com.example.administrator.tecsoundclass.JoinActivity;
import com.example.administrator.tecsoundclass.R;

public class CourseFragment extends Fragment {
    private ImageView mIvMenu;
    private PopupWindow mPop;
    private ListView mLv1;
    public CourseFragment() {

    }

    public static Fragment newInstance() {
        Fragment fragment = new CourseFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIvMenu=view.findViewById(R.id.iv_more);
        mLv1=view.findViewById(R.id.lv_1);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=getActivity().getLayoutInflater().inflate(R.layout.layout_pop_add_course,null);
                TextView mTvCreate=view.findViewById(R.id.tv_create);
                TextView mTvJoin=view.findViewById(R.id.tv_add_course);
                OnClick onclick=new OnClick();
                mTvCreate.setOnClickListener(onclick);
                mTvJoin.setOnClickListener(onclick);
                mPop=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mPop.setOutsideTouchable(true);
                mPop.setFocusable(true);
                mPop.showAsDropDown(mIvMenu);
            }
        });
        mLv1.setAdapter(new MyClassListAdapter(getActivity()));
        mLv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(getActivity(),CourseMenuActivity.class);
               startActivity(intent);
            }
        });
    }
    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.tv_create:
                    intent=new Intent(getActivity(),CreateClassActivity.class);
                    break;
                case R.id.tv_add_course:
                    intent=new Intent(getActivity(),JoinActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
