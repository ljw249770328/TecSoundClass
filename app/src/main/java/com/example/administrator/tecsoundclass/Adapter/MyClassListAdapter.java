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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyClassListAdapter extends RecyclerView.Adapter<MyClassListAdapter.ViewHolder> {
    private List<Course> mCourseList;
    private OnRecyclerItemClickListener mListener;
    private Context mContext;
     class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView mCoursePic;
        TextView mCourseName,mCourseid,mClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoursePic=itemView.findViewById(R.id.iv_course_pic);
            mCourseName=itemView.findViewById(R.id.tv_course_name);
            mCourseid=itemView.findViewById(R.id.tv_course_id);
            mClass=itemView.findViewById(R.id.tv_class_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                       mListener.onItemClick(getAdapterPosition(),mCourseList);
                    }
                }
            });
        }
    }
    public MyClassListAdapter(List<Course> courseList,Context context){
        mCourseList=courseList;
        mContext=context;
    }
    //提供Set方法调用
    public void setOnItemClickListener(OnRecyclerItemClickListener listener){
        mListener=listener;
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
        viewHolder.mClass.setText(course.getCourse_class());
        viewHolder.mCourseName.setText(course.getCourse_name());
        viewHolder.mCourseid.setText(course.getCourse_id());
        try {
            if (!course.getCourse_pic_src().equals("")){
                Glide.with(mContext).load(new URL(course.getCourse_pic_src())).signature(new ObjectKey(course.getUpdate_time())).encodeQuality(70).into(viewHolder.mCoursePic);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(int position,List<Course> CourseList);
    }
}
