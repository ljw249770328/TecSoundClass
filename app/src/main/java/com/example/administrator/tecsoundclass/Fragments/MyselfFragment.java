package com.example.administrator.tecsoundclass.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.tecsoundclass.Activity.EditMyInfoActivity;
import com.example.administrator.tecsoundclass.Activity.FindPswActivity;
import com.example.administrator.tecsoundclass.Activity.LoginActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.Activity.SettingsActivity;
import com.example.administrator.tecsoundclass.Activity.StandDataActivity;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

public class MyselfFragment extends Fragment {
    private ImageView mIvMenu,mIvHead;
    private PopupWindow mPop;
    private TextView mTvStandards,mTvUsername,mTvUserId,mTvFriendNum,mTvCourseNum;
    private LinearLayout mEditInfo;
    MainMenuActivity activity;

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
    public void onResume() {
        super.onResume();
        SetInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (MainMenuActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);
        return view;
    }
    private void init(@NonNull View view){
        mIvMenu=view.findViewById(R.id.iv_menu_list);
        mTvStandards=view.findViewById(R.id.m_status_data);
        mEditInfo=view.findViewById(R.id.ll_2);
        mIvHead=view.findViewById(R.id.iv_user_head);
        mTvUsername=view.findViewById(R.id.user_name);
        mTvUserId=view.findViewById(R.id.user_id);
        mTvFriendNum=view.findViewById(R.id.fri_num);
        mTvCourseNum=view.findViewById(R.id.class_num);
    }
    private void SetOnclick(){
        OnClick onClick=new OnClick();
        mIvMenu.setOnClickListener(onClick);
        mTvStandards.setOnClickListener(onClick);
        mEditInfo.setOnClickListener(onClick);
    }
    private void initlist(){
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
    private void SetInfo(){
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", activity.getStudentID());
        VolleyCallback.getJSONObject(getActivity().getApplicationContext(), "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray users=r.getJSONArray("users");
                    JSONObject user= (JSONObject) users.get(0);
                    mTvUsername.setText(user.getString("user_name"));
                    mTvUserId.setText(user.getString("user_id"));
                    Glide.with(MyselfFragment.this).load(user.getString("user_pic_src")).into(mIvHead);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        SetOnclick();
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=null;
            MainMenuActivity activity=(MainMenuActivity)getActivity();
            switch (v.getId()){
                case R.id.iv_menu_list:
                    initlist();
                    break;
                case R.id.tv_send_zone:
                    mPop.dismiss();
                    Toast.makeText(getActivity(),"暂未开放,敬请期待",Toast.LENGTH_LONG).show();
                    break;
                case R.id.tv_reset:
                    mPop.dismiss();
                    intent=new Intent(getActivity(),FindPswActivity.class);
                    Bundle b =new Bundle();
                    b.putString("userid",activity.getStudentID());
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                case R.id.tv_settings:
                    mPop.dismiss();
                    intent=new Intent(getActivity(),SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_2:
                    intent=new Intent(getActivity(),EditMyInfoActivity.class);
                    Bundle bundle1 =new Bundle();
                    bundle1.putString("user_id",activity.getStudentID());
                    intent.putExtras(bundle1);
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
                    bundle.putString("StudentId",activity.getStudentID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
//            startActivity(intent);
        }
    }
}
