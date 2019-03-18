package com.example.administrator.tecsoundclass.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;

import java.util.List;

public class MyClassListAdapter extends RecyclerView.Adapter<MyClassListAdapter.ViewHolder> {
    private List<Course> mCourseList;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView mCoursePic;
        TextView mCourseName,mCourseTea,mClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoursePic=itemView.findViewById(R.id.iv_course_pic);
            mCourseName=itemView.findViewById(R.id.tv_class_name);
            mCourseTea=itemView.findViewById(R.id.tv_tea_name);
            mClass=itemView.findViewById(R.id.tv_class_name);
        }
    }
    public MyClassListAdapter(List<Course> courseList){
        mCourseList=courseList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_list_my_course_item,viewGroup,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Course course =mCourseList.get(i);
        viewHolder.mClass.setText(course.getCourse_name());
//        写到这里 更改适配器,参照sign
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }
}
