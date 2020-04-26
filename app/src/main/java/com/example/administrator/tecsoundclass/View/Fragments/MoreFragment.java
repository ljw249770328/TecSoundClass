package com.example.administrator.tecsoundclass.View.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.View.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.model.JavaBean.Course;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.FileUploadUtil;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.Controller.zxing.android.CreateCodeImg;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MoreFragment extends Fragment {
    private ImageView mIvBack,mIvClasssPic,mIvCodePic;
    private TextView mTvclassid,mTvCourseName,mTvClassName,mTvTeaInfo,mTvClasTime,mTvExit,mTvtitle,mTvClassRequest,mTvRequestText;
    private CourseMenuActivity mActivity;
    private String Uid;
    private Course course;
    private String picturePath="";
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
        Uid=mActivity.getmUser().getUser_id();
        course=mActivity.getmCourse();
//        Toast.makeText(getActivity(),course.getCourse_id(),Toast.LENGTH_SHORT).show();
        SetData();
        ViewTreeObserver observer= view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CreateCodeImg.createQRcodeImage(mIvCodePic,mTvclassid.getText().toString().substring(5,15));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case 1:
                    Uri selectedImage =data.getData();
                    String[] filePathClumn={MediaStore.Images.Media.DATA};
                    Cursor cursor =mActivity.getContentResolver().query(selectedImage,filePathClumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex =cursor.getColumnIndex(filePathClumn[0]);
                    picturePath=cursor.getString(columnIndex);
                    cursor.close();
                    mIvClasssPic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    FileUploadUtil.UploadFile(mActivity.getApplicationContext(),"changeCpic",picturePath,course.getCourse_id()+".jpeg","ClassPic","course",course.getCourse_id(),null);
                    break;
            }
        }
    }

    private void init(View view){
        mIvBack=view.findViewById(R.id.im_back);
        mTvclassid=view.findViewById(R.id.tv_class_id);
        mTvClassName=view.findViewById(R.id.tv_mclass_name);
        mTvCourseName=view.findViewById(R.id.tv_mcourse_name);
        mTvClasTime=view.findViewById(R.id.tv_Ctime);
        mIvClasssPic=view.findViewById(R.id.iv_change_cpic);
        mTvClassRequest=view.findViewById(R.id.tv_class_request);
        mTvTeaInfo=view.findViewById(R.id.tv_ctea_name);
        mTvtitle=view.findViewById(R.id.course_title);
        mTvRequestText=view.findViewById(R.id.tv_request_text);
        mTvExit=view.findViewById(R.id.tv_exit);
        mIvCodePic=view.findViewById(R.id.iv_code_img);
        if (mActivity.getmUser().getUser_identity().equals("老师")){
            mTvExit.setText("解散课堂");
        }

    }
    private void setonclickListener(){
        Onclick onclick=new Onclick();
        mIvBack.setOnClickListener(onclick);
        mTvExit.setOnClickListener(onclick);
        mIvClasssPic.setOnClickListener(onclick);
        mTvClassRequest.setOnClickListener(onclick);
    }
    private void SetData(){
        mTvclassid.setText("课堂id:"+course.getCourse_id());
        mTvCourseName.setText(course.getCourse_name());
        mTvtitle.setText(course.getCourse_name()+"("+course.getCourse_class()+")");
        mTvClassName.setText(course.getCourse_class());
        mTvTeaInfo.setText(course.getTeacher_user_id());
        mTvClasTime.setText(course.getCourse_time());
        mTvRequestText.setText(course.getCourse_request());
        try {
            Glide.with(mActivity.getApplicationContext()).load(new URL(course.getCourse_pic_src())).signature(new ObjectKey(course.getUpdate_time())).encodeQuality(70).into(mIvClasssPic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private void Deleteclass(String CourseId){
        String url="";
        Map<String ,String> params=new HashMap<>();
        if(mActivity.getmUser().getUser_identity().equals("老师")){
            url="http://101.132.71.111:8080/TecSoundWebApp/DeleteClassServlet";
            params.put("course_id",CourseId);
        }else{
            url="http://101.132.71.111:8080/TecSoundWebApp/ExitCourseServlet";
            params.put("Cid",course.getCourse_id());
            params.put("Sid",mActivity.getmUser().getUser_id());
        }
        VolleyCallback.getJSONObject(getActivity().getApplication(), "DeleteCourse", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    String result =r.getString("Result");
                    ToastUtils.ShowMyToasts(getActivity(),result, Gravity.CENTER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.tv_class_request:
                    if (mTvRequestText.getVisibility()==View.GONE){
                        Drawable nav_up=getResources().getDrawable(R.drawable.up,null);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());

                        mTvClassRequest.setCompoundDrawables(null, null, nav_up, null);
                        mTvRequestText.setVisibility(View.VISIBLE);
                    }
                    else {
                        Drawable nav_up=getResources().getDrawable(R.drawable.down,null);

                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());

                        mTvClassRequest.setCompoundDrawables(null, null, nav_up, null);
                        mTvRequestText.setVisibility(View.GONE);
                    }
                    break;
                case R.id.iv_change_cpic:
                    Intent intent =new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);
                    break;
                case R.id.tv_exit:
                    Deleteclass(course.getCourse_id());
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }
    }
}
