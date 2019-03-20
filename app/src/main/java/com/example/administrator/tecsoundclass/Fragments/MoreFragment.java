package com.example.administrator.tecsoundclass.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;

public class MoreFragment extends Fragment {
    private ImageView mIvBack;
    private TextView mTvclassid,mTvCourseName,mTvClassName,mTvTeaInfo,mTvClasTime,mTvExit,mTvtitle;
    private CourseMenuActivity mActivity;
    private String Uid;
    private Course course;
    public MoreFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=(CourseMenuActivity)getActivity();
    }

    public static Fragment newInstance() {
        Fragment fragment = new MoreFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        setonclickListener();
        Uid=mActivity.getmUId();
        course=mActivity.getmCourse();
//        Toast.makeText(getActivity(),course.getCourse_id(),Toast.LENGTH_SHORT).show();
        SetData();
    }
    private void init(View view){
        mIvBack=view.findViewById(R.id.im_back);
        mTvclassid=view.findViewById(R.id.tv_class_id);
        mTvClassName=view.findViewById(R.id.tv_mclass_name);
        mTvCourseName=view.findViewById(R.id.tv_mcourse_name);
        mTvClasTime=view.findViewById(R.id.tv_Ctime);
        mTvTeaInfo=view.findViewById(R.id.tv_ctea_name);
        mTvtitle=view.findViewById(R.id.course_title);
        mTvExit=view.findViewById(R.id.tv_exit);
    }
    private void setonclickListener(){
        Onclick onclick=new Onclick();
        mIvBack.setOnClickListener(onclick);
        mTvExit.setOnClickListener(onclick);
    }
    private void SetData(){
        mTvclassid.setText("课堂id:"+course.getCourse_id());
        mTvCourseName.setText(course.getCourse_name());
        mTvtitle.setText(course.getCourse_name()+"("+course.getCourse_class()+")");
        mTvClassName.setText(course.getCourse_class());
        mTvTeaInfo.setText(course.getTeacher_user_id());
        mTvClasTime.setText(course.getCourse_time());
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.tv_exit:
                    break;
                default:
                    break;
            }
        }
    }
}
