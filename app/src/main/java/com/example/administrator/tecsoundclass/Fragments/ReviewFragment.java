package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
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

import com.example.administrator.tecsoundclass.Adapter.MyInteractAdapter;
import com.example.administrator.tecsoundclass.Adapter.MyReviewListAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Interaction;
import com.example.administrator.tecsoundclass.JavaBean.Point;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.RecPointHandler;
import com.example.administrator.tecsoundclass.utils.Timer;
import com.iflytek.cloud.InitListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

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
    private List<Point> mPointList=new ArrayList<>();
    private String mAuthId;
    private Timer timer1;
    public ReviewFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new ReviewFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthId=getActivity().getIntent().getExtras().getString("StudentId");
        timer1=new com.example.administrator.tecsoundclass.utils.Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mIvBack=view.findViewById(R.id.im_back);
        mIvBack.setOnClickListener(onclick);
        mRvPoint=view.findViewById(R.id.recycler_view_point);
        mBtnClassBegin=view.findViewById(R.id.btn_class_begin);
        mBtnClassBegin.setOnClickListener(onclick);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRvPoint.setLayoutManager(layoutManager);
        mPointList=InitList();
        adapter=new MyReviewListAdapter(mPointList);
        mRvPoint.setAdapter(adapter);
    }

    private List<Point> InitList(){
        List<Point> list=new ArrayList<>();
        list=LitePal.select("point_time","point_voice_src").order("point_time").find(Point.class);
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
                        point.save();
                        //刷新列表
                        mPointList.clear();
                        mPointList.addAll(InitList());
                        adapter.notifyDataSetChanged();
                        mTvResult.setText("");
                        mBtnRecord.setText("记录");
                    }else{
                        recPointHandler=new RecPointHandler(getActivity(),mTvResult,mBtnRecord);
                        recPointHandler.StartHandle(mAuthId+"_"+timer1.getmDate()+timer1.getmTime());
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
