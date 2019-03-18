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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.administrator.tecsoundclass.Activity.LoginActivity;
import com.example.administrator.tecsoundclass.Adapter.MyClassListAdapter;
import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.Activity.CreateClassActivity;
import com.example.administrator.tecsoundclass.Activity.JoinActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CourseFragment extends Fragment {
    private ImageView mIvMenu;
    private PopupWindow mPop;
    private ListView mLv1;
    MainMenuActivity activity;
    String StuId="";
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
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (MainMenuActivity) getActivity();
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
//        mLv1.setAdapter(new MyClassListAdapter(getActivity()));///记得撤销
        mLv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(getActivity(),CourseMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("StudentId",activity.getStudentID());
                intent.putExtras(bundle);
               startActivity(intent);
            }
        });
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mPop.dismiss();
            switch (v.getId()){
                case R.id.tv_create:
                    String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", activity.getStudentID());
                    VolleyCallback.getJSONObject(getActivity().getApplicationContext(), "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try {
                                JSONArray users=r.getJSONArray("users");
                                JSONObject user= (JSONObject) users.get(0);
                                if(user.getString("user_identity").equals("老师")){
                                    Intent intent=new Intent(getActivity(),CreateClassActivity.class);
                                    Bundle b=new Bundle();
                                    b.putString("teaId",activity.getStudentID());
                                    intent.putExtras(b);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(),"您没有权限",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
                case R.id.tv_add_course:
                    Intent intent=null;
                    intent=new Intent(getActivity(),JoinActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }
}
