package com.example.administrator.tecsoundclass.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.Activity.EditMyInfoActivity;
import com.example.administrator.tecsoundclass.Activity.FindPswActivity;
import com.example.administrator.tecsoundclass.Activity.LoginActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.Activity.SettingsActivity;
import com.example.administrator.tecsoundclass.Activity.StandDataActivity;
import com.example.administrator.tecsoundclass.service.BackService;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.TranslateFactory;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

public class MyselfFragment extends Fragment {
    private ImageView mIvMenu,mIvHead;
    private PopupWindow mPop;
    private TextView mTvStandards,mTvUsername,mTvUserId,mTvFriendNum,mTvCourseNum;
    private RelativeLayout mEditInfo;
    MainMenuActivity activity;
    private SharedPreferences.Editor myEditor;

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
        activity.UpdatemUser(new MainMenuActivity.UploadCallBack() {
            @Override
            public void OnUploaded() {
                SetInfo();
            }
        });

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
        mEditInfo=view.findViewById(R.id.rl_view_info);
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
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPop.showAsDropDown(mIvMenu, TranslateFactory.dip2px(getActivity(), -110), TranslateFactory.dip2px(getActivity(), 5));
    }
    private void SetInfo(){
        mTvUsername.setText(activity.getmUser().getUser_name());
        mTvUserId.setText(activity.getmUser().getUser_id());
        try {
            Glide.with(activity.getApplicationContext()).load(new URL(activity.getmUser().getUser_pic_src())).signature(new ObjectKey(activity.getmUser().getUpdate_time())).encodeQuality(70).into(mIvHead);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        myEditor=getActivity().getSharedPreferences("admin",Context.MODE_PRIVATE).edit();
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
                    ToastUtils.ShowMyToasts(getActivity(),"暂未开放,敬请期待", Gravity.CENTER);
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
                case R.id.rl_view_info:
                    intent=new Intent(getActivity(),EditMyInfoActivity.class);
                    Bundle bundle1 =new Bundle();
                    bundle1.putSerializable("mUser",activity.getmUser());
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    break;
                case R.id.tv_exit:
                    mPop.dismiss();
//                    WebSocketClientObject.client.close();
                    MyApplication.getmWebsocket().cancel();
                    intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    myEditor.clear();
                    myEditor.commit();
                    getActivity().stopService(new Intent(getActivity(), BackService.class));
                    getActivity().finish();
                    break;
                case R.id.m_status_data:
                    intent=new Intent(getActivity(),StandDataActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("mUser",activity.getmUser());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
//            startActivity(intent);
        }
    }
}
