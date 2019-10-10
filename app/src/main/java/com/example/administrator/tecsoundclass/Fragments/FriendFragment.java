package com.example.administrator.tecsoundclass.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.Activity.ChatActivity;
import com.example.administrator.tecsoundclass.Activity.MainMenuActivity;
import com.example.administrator.tecsoundclass.Adapter.MyFriendListAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Follow;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendFragment extends Fragment {

    private ImageView mIvAFriends;
    private PopupWindow mPop;
    private RecyclerView mRvFriends;
    private List<Follow> FollowList =new ArrayList<>();
    private List<Follow> list;
    MainMenuActivity activity;
    private MyFriendListAdapter adapter;
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
        init(view);
        SetListeners();
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getActivity());
        mRvFriends.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (MainMenuActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        InitList();
    }

    private void init(View view){
        mIvAFriends=view.findViewById(R.id.iv_add);
        mRvFriends=view.findViewById(R.id.lv_1);
    }
    private List<Follow> InitList(){
        list=new ArrayList<>();
        String url="http://101.132.71.111:8080/TecSoundWebApp/GetFListServlet";
        Map<String,String> params =new HashMap<>();
        params.put("user_id",activity.getmUser().getUser_id());
        VolleyCallback.getJSONObject(activity.getApplicationContext(), "GetFollowList", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray follows =r.getJSONArray("follows");
                    for (int i = 0; i < follows.length(); i++) {
                        JSONObject Fobj = (JSONObject) follows.get(i);
                        Follow follow =new Follow();
                        follow.setFollow_id(Fobj.getString("follow_id"));
                        follow.setFollower_user_id(Fobj.getString("follow_user_id"));
                        follow.setFan_user_id(Fobj.getString("fan_user_id"));
                        follow.setFollow_time(Fobj.getString("follow_time"));
                        list.add(follow);
                    }
                    FollowList.clear();
                    FollowList.addAll(list);
                    adapter=new MyFriendListAdapter(FollowList,activity.getApplicationContext());
                    adapter.SetOnItemClickListener(new MyFriendListAdapter.OnRecyclerItemClickListener() {
                        @Override
                        public void onItemClick(int posision, List<User> mFans) {
                            //跳转聊天,传递adapter中user
                            Intent intent=new Intent(getActivity(), ChatActivity.class);
                            Bundle bundle =new Bundle();
                            bundle.putSerializable("FanInfo",adapter.getmFans().get(posision));
                            bundle.putSerializable("MyInfo",activity.getmUser());
                            intent.putExtras(bundle);
                            startActivity(intent);
//                            Toast.makeText(getActivity(),"即将进入与"+mFans.get(posision).getUser_name()+"的聊天",Toast.LENGTH_SHORT).show();
                        }
                    });
                    mRvFriends.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        return list;
    }
    private void SetListeners(){
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
    }


}
