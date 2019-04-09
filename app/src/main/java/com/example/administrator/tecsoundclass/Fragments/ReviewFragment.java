package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.Adapter.MyInteractAdapter;
import com.example.administrator.tecsoundclass.Adapter.MyReviewListAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Interaction;
import com.example.administrator.tecsoundclass.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.RecPointHandler;
import com.example.administrator.tecsoundclass.utils.Timer;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.iflytek.cloud.InitListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReviewFragment extends Fragment {
    private ImageView mIvBack;
    private RecyclerView mRvPoint;
    private Button mBtnClassBegin,mBtnClassOver,mBtnRecord;
    private Onclick onclick=new Onclick();
    private AlertDialog mRecordDialog;
    private TextView mTvResult;
    private Toast mToast;
    private MyReviewListAdapter adapter;
    private RecPointHandler recPointHandler;
    private List<Point> list;
    private List<Point> mPointList=new ArrayList<>();
    private String mAuthId;
    CourseMenuActivity mActivity;


    public ReviewFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        InitList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (CourseMenuActivity) getActivity();
    }

    private void init(View view){
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mIvBack=view.findViewById(R.id.im_back);
        mRvPoint=view.findViewById(R.id.recycler_view_point);
        mBtnClassBegin=view.findViewById(R.id.btn_class_begin);
    }
    private void SetListener(){
        mIvBack.setOnClickListener(onclick);
        mBtnClassBegin.setOnClickListener(onclick);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthId=mActivity.getmUser().getUser_id();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        SetListener();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRvPoint.setLayoutManager(layoutManager);

    }

    private List<Point> InitList(){
        list=new ArrayList<>();
        String url ="http://101.132.71.111:8080/TecSoundWebApp/GetPointListServlet";
        Map <String,String> params =new HashMap<>();
        params.put("course_id",mActivity.getmCourse().getCourse_id());
        VolleyCallback.getJSONObject(mActivity.getApplicationContext(), "GetPoints", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray points=r.getJSONArray("Points");

                    for (int i=0;i<points.length();i++){
                        JSONObject obj= (JSONObject) points.get(i);
                        Point point =new Point();
                        point.setPoint_id(obj.getString("point_id"));
                        point.setRelease_course_id(obj.getString("release_course_id"));
                        point.setPoint_time(obj.getString("point_time"));
                        point.setPoint_voice_src(obj.getString("point_voice_src"));
                        point.setPoint_content(obj.getString("point_content"));
                        list.add(point);
                    }
                    mPointList.clear();
                    mPointList.addAll(list);
                    adapter=new MyReviewListAdapter(mPointList);
                    adapter.notifyDataSetChanged();
                    mRvPoint.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.btn_class_begin:
                    showTip("状态:上课");
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View view =LayoutInflater.from(getActivity()).inflate(R.layout.layout_record_dialog,null);
                    mBtnClassOver=view.findViewById(R.id.btn_class_over);
                    mBtnRecord=view.findViewById(R.id.btn_record);
                    mTvResult=view.findViewById(R.id.tv_message);
                    mBtnRecord.setOnClickListener(onclick);
                    mBtnClassOver.setOnClickListener(onclick);
                    mRecordDialog=builder.setView(view).create();
                    mRecordDialog.setCancelable(false);
                    mRecordDialog.show();
                    break;
                case R.id.btn_record:
                    if (mBtnRecord.getText().equals("完成")){
                        showTip("[已记录]");
                        //存入数据库
                        Timer timer=new Timer();
                        Point point=new Point();
                        point.setPoint_time(timer.getmDate()+"  "+timer.getmTime());
                        point.setPoint_voice_src(recPointHandler.getMfilepath());

                        //刷新列表
                        mPointList.clear();
                        mPointList.addAll(InitList());
                        adapter.notifyDataSetChanged();
                        mTvResult.setText("");
                        mBtnRecord.setText("记录");
                    }else{
                        recPointHandler=new RecPointHandler(getActivity(),mTvResult,mBtnRecord);
                        recPointHandler.StartHandle(mAuthId+"_"+UUID.randomUUID().toString());
                    }
                    break;
                case R.id.btn_class_over:
                    mRecordDialog.setCancelable(true);
                    mRecordDialog.dismiss();
                    mRecordDialog.cancel();
                    showTip("状态:下课");
                    break;
            }
        }
    }
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
