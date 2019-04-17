package com.example.administrator.tecsoundclass.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.administrator.tecsoundclass.JavaBean.Course;
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

public class CourseFragment extends Fragment {
    private ImageView mIvMenu;
    private PopupWindow mPop;
    private RecyclerView mRvCourse;
    MainMenuActivity activity;
    private MyClassListAdapter adapter;
    private List<Course> CourseList = new ArrayList<>();
    private List<Course> list;
    private SwipeRefreshLayout swipeRefresh;
    private TextView mTvCreate,mTvJoin;

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
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        InitList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainMenuActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.layout_pop_add_course, null);
                mTvCreate = view.findViewById(R.id.tv_create);
                mTvJoin = view.findViewById(R.id.tv_add_course);
                if (activity.getmUser().getUser_identity().equals("老师")) {
                    mTvCreate.setVisibility(View.VISIBLE);
                }
                OnClick onclick = new OnClick();
                mTvCreate.setOnClickListener(onclick);
                mTvJoin.setOnClickListener(onclick);
                mPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPop.setOutsideTouchable(true);
                mPop.setFocusable(true);
                mPop.showAsDropDown(mIvMenu);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvCourse.setLayoutManager(layoutManager);


    }

    private List<Course> InitList() {
        list = new ArrayList<>();
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetCListServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", activity.getStudentID());
        VolleyCallback.getJSONObject(getActivity().getApplicationContext(), "GetCourseList", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray courses = r.getJSONArray("courses");
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject Cobj = (JSONObject) courses.get(i);
                        Course course = new Course();
                        course.setTeacher_user_id(Cobj.getString("teacher_user_id"));
                        course.setCourse_class(Cobj.getString("course_class"));
                        course.setCourse_name(Cobj.getString("course_name"));
                        course.setCourse_time(Cobj.getString("course_time"));
                        course.setCourse_request(Cobj.getString("course_request"));
                        course.setCourse_id(Cobj.getString("course_id"));

                        list.add(course);
                    }
                    CourseList.clear();
                    CourseList.addAll(list);
                    adapter = new MyClassListAdapter(CourseList);
                    adapter.notifyDataSetChanged();
                    mRvCourse.setAdapter(adapter);
                    adapter.setOnItemClickListener(new MyClassListAdapter.OnRecyclerItemClickListener() {
                        @Override
                        public void onItemClick(int position, List<Course> CourseList) {
                            String CourseId = CourseList.get(position).getCourse_id();
                            Intent intent = new Intent(getActivity(), CourseMenuActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", activity.getmUser());
                            bundle.putSerializable("course", CourseList.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mPop.dismiss();
            Intent intent ;
            switch (v.getId()) {
                case R.id.tv_create:
                    intent = new Intent(getActivity(), CreateClassActivity.class);
                    Bundle b = new Bundle();
                    b.putString("teaId", activity.getStudentID());
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                case R.id.tv_add_course:
                    intent = new Intent(getActivity(), JoinActivity.class);
                    ;
                    Bundle bundle = new Bundle();
                    bundle.putString("Stuid", activity.getmUser().getUser_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private void init(View view) {
        mIvMenu = view.findViewById(R.id.iv_more);
        mRvCourse = view.findViewById(R.id.rv_course);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.colorDarkGreen);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CourseList.clear();
                CourseList.addAll(InitList());
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }

        });
    }
}
