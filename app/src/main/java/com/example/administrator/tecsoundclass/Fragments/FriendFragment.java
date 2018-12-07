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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.Adapter.MyClassListAdapter;
import com.example.administrator.tecsoundclass.Adapter.MyFriendListAdapter;
import com.example.administrator.tecsoundclass.CourseMenuActivity;
import com.example.administrator.tecsoundclass.MainMenuActivity;
import com.example.administrator.tecsoundclass.R;

public class FriendFragment extends Fragment {

    private ImageView mIvAFriends;
    private PopupWindow mPop;
    private ListView mLv1;

    public FriendFragment() {

    }

    public static Fragment newInstance() {
        Fragment fragment = new FriendFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_friend,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIvAFriends=view.findViewById(R.id.iv_add);
        mLv1=view.findViewById(R.id.lv_1);
            mIvAFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view =getActivity().getLayoutInflater().inflate(R.layout.layout_pop_add_friends,null);
                    TextView mTvAdd=view.findViewById(R.id.tv_add_friends);
                    mTvAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPop.dismiss();
                        }
                    });
                    mPop=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPop.setOutsideTouchable(true);
                    mPop.setFocusable(true);
                    mPop.showAsDropDown(mIvAFriends);
                }
            });
        mLv1.setAdapter(new MyFriendListAdapter(getActivity()));
        mLv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

}
