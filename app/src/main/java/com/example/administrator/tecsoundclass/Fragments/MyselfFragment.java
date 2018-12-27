package com.example.administrator.tecsoundclass.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.EditMyInfoActivity;
import com.example.administrator.tecsoundclass.FindPswActivity;
import com.example.administrator.tecsoundclass.LoginActivity;
import com.example.administrator.tecsoundclass.MainMenuActivity;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.SettingsActivity;
import com.example.administrator.tecsoundclass.StandDataActivity;

public class MyselfFragment extends Fragment {
    private ImageView mIvMenu;
    private PopupWindow mPop;
    private TextView mTvStandards;
    private LinearLayout mEditInfo;
    public MyselfFragment() {

    }

    public static Fragment newInstance() {
        Fragment fragment = new MyselfFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIvMenu=view.findViewById(R.id.iv_menu_list);
        mTvStandards=view.findViewById(R.id.m_status_data);
        mEditInfo=view.findViewById(R.id.ll_2);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=getActivity().getLayoutInflater().inflate(R.layout.layout_pop_menu_my,null);
                TextView mTvSZone=view.findViewById(R.id.tv_send_zone);
                TextView mTvReset=view.findViewById(R.id.tv_reset);
                TextView mTvSettings=view.findViewById(R.id.tv_settings);
                TextView mTvExits=view.findViewById(R.id.tv_exit);
                OnClick onclick=new OnClick();
                mTvSZone.setOnClickListener(onclick);
                mTvReset.setOnClickListener(onclick);
                mTvSettings.setOnClickListener(onclick);
                mTvExits.setOnClickListener(onclick);
                mPop=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mPop.setOutsideTouchable(true);
                mPop.setFocusable(true);
                mPop.showAsDropDown(mIvMenu);
            }
        });
        mTvStandards.setOnClickListener(new OnClick());
        mEditInfo.setOnClickListener(new OnClick());
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.tv_send_zone:
                    mPop.dismiss();
                    Toast.makeText(getActivity(),"暂未开放,敬请期待",Toast.LENGTH_LONG).show();
                    break;
                case R.id.tv_reset:
                    mPop.dismiss();
                    intent=new Intent(getActivity(),FindPswActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_settings:
                    mPop.dismiss();
                    intent=new Intent(getActivity(),SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_2:
                    intent=new Intent(getActivity(),EditMyInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_exit:
                    mPop.dismiss();
                    intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case R.id.m_status_data:
                    intent=new Intent(getActivity(),StandDataActivity.class);
                    Bundle bundle=new Bundle();
                    MainMenuActivity activity= (MainMenuActivity) getActivity();
                    bundle.putString("StudentId",activity.getStudentID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
//            startActivity(intent);
        }
    }
}
