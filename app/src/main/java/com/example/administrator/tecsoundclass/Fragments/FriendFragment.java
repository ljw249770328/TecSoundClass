package com.example.administrator.tecsoundclass.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class FriendFragment extends Fragment {

    private ImageView mIvAFriends;
    private PopupWindow mPop;
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
            mIvAFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view =getActivity().getLayoutInflater().inflate(R.layout.layout_pop_add_friends,null);
                    TextView mTvAdd=view.findViewById(R.id.tv_add_friends);
                    mTvAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    mPop=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPop.setOutsideTouchable(true);
                    mPop.setFocusable(true);
                    mPop.showAsDropDown(mIvAFriends);
                }
            });
    }
}
