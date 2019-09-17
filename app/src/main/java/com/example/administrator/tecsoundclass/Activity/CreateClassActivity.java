package com.example.administrator.tecsoundclass.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.FileUploadUtil;
import com.example.administrator.tecsoundclass.utils.TPDialogFactory;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateClassActivity extends BaseActivity {
    private TextView mTvCancle,mTvNext,mTvClassTime,mTvClassOver;
    private EditText mEtClassNum,mEtClassName,mEtClassInfo;
    private ImageView mIvLoadPic;
    private TPDialogFactory factory;
    private  boolean k1=false,k2=false;
    private String CPicPath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        init();
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SetListener();
    }
    private void  init(){
        mTvCancle=findViewById(R.id.tv_cancle);
        mTvNext=findViewById(R.id.tv_next);
        mEtClassInfo=findViewById(R.id.class_edt_moreinfo);
        mEtClassName=findViewById(R.id.class_edt_name);
        mEtClassNum=findViewById(R.id.class_edt_number);
        mIvLoadPic=findViewById(R.id.iv_load_class_pic);
        mTvClassTime=findViewById(R.id.tv_class_set_time);
        mTvClassOver=findViewById(R.id.tv_class_set__over_time);
        factory=new TPDialogFactory();
    }
    private void SetListener(){
        OnClick onClick=new OnClick();
        mTvCancle.setOnClickListener(onClick);
        mTvClassTime.setOnClickListener(onClick);
        mTvClassOver.setOnClickListener(onClick);
        mTvNext.setOnClickListener(onClick);
        mIvLoadPic.setOnClickListener(onClick);
        mEtClassName.setOnFocusChangeListener(new OnFocusListener());
        mEtClassNum.setOnFocusChangeListener(new OnFocusListener());
        mTvClassTime.setOnFocusChangeListener(new OnFocusListener());
        mTvClassOver.setOnFocusChangeListener(new OnFocusListener());
        mTvClassOver.setClickable(false);
    }
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancle:
                    finish();
                    break;
                case R.id.tv_class_set_time:
                    mTvClassOver.setClickable(false);
                    factory.getTimePicker(CreateClassActivity.this,"上课时间",mTvClassTime,"1949-01-01 00:00").show(factory.getTime());
                    mTvClassOver.setClickable(true);
                    break;
                case R.id.tv_class_set__over_time:
                    factory.getTimePicker(CreateClassActivity.this,"下课时间",mTvClassOver,"1949-01-01 "+mTvClassTime.getText().toString()).show(factory.getTime());
                    break;
                case R.id.iv_load_class_pic:
                    Intent intent =new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);
                    break;
                case R.id.tv_next:
                    mEtClassName.clearFocus();
                    mEtClassNum.clearFocus();
                    if(k1&&k2&&!mTvClassTime.getText().toString().equals("上课时间")&&!mTvClassOver.getText().toString().equals("下课时间")){
                        String url="http://101.132.71.111:8080/TecSoundWebApp/ClassRandomNumServlet";
                        Map <String,String> params =new HashMap<>();
                        params.put("index","10");
                        VolleyCallback.getJSONObject(CreateClassActivity.this, "getCid", url, params, new VolleyCallback.VolleyJsonCallback() {
                            @Override
                            public void onFinish(JSONObject r) {
                                try {
                                    String Cid =r.getString("Result");
                                    String url="http://101.132.71.111:8080/TecSoundWebApp/CreateClassServlet";
                                    Map<String,String> params=new HashMap<>();
                                    params.put("course_id",Cid);
                                    params.put("teacher_user_id",getIntent().getExtras().getString("teaId"));
                                    params.put("course_class",mEtClassNum.getText().toString()+"班");
                                    params.put("course_time",mTvClassTime.getText().toString()+"-"+mTvClassOver.getText().toString());
                                    params.put("course_request",mEtClassInfo.getText().toString());
                                    params.put("course_name",mEtClassName.getText().toString());
                                    String FileUrl =FileUploadUtil.UploadFile(getApplicationContext(),"uploadCpic",CPicPath,Cid+".jpeg","ClassPic","course","course_id",null);
                                    params.put("course_pic_src",FileUrl);
                                    VolleyCallback.getJSONObject(CreateClassActivity.this, "CreateClass", url, params, new VolleyCallback.VolleyJsonCallback() {
                                        @Override
                                        public void onFinish(JSONObject r) {
                                            try {
                                                String result =r.getString("Result");
                                                if (result.length()==10){
                                                    Toast.makeText(CreateClassActivity.this,"创建成功,课堂Id为"+result,Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }else
                                                {
                                                    Toast.makeText(CreateClassActivity.this,"您已创建此课堂的相同班级",Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onError(VolleyError error) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });

                    }else {
                        Toast.makeText(CreateClassActivity.this,"请检查填写信息",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
    private class OnFocusListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                switch (v.getId()){
                    case R.id.class_edt_name:
                        k1=false;
                        if(mEtClassName.getText().toString().equals("")){
                            Toast.makeText(CreateClassActivity.this,"未填写课程名", Toast.LENGTH_SHORT).show();
                        }else if (mEtClassName.length()>15) {
                            mEtClassName.setText(mEtClassName.getText().toString().substring(0, 15));
                            mEtClassName.setSelection(15);
                            Toast.makeText(CreateClassActivity.this, "超出长度范围", Toast.LENGTH_SHORT).show();
                        }else{
                            k1=true;
                        }
                        break;
                    case R.id.class_edt_number:
                        k2=false;
                        if(mEtClassNum.getText().toString().equals("")){
                            Toast.makeText(CreateClassActivity.this,"未填写班级", Toast.LENGTH_SHORT).show();
                        }else if (mEtClassNum.length()>12) {
                            mEtClassNum.setText(mEtClassNum.getText().toString().substring(0, 12));
                            mEtClassNum.setSelection(12);
                            Toast.makeText(CreateClassActivity.this, "超出长度范围", Toast.LENGTH_SHORT).show();
                        }else{
                            k2=true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case 1:
                    Uri selectedImage =data.getData();
                    String[] filePathClumn ={MediaStore.Images.Media.DATA};
                    //获取照片的数据视图
                    Cursor cursor=getContentResolver().query(selectedImage,filePathClumn,null,null,null);
                    cursor.moveToFirst();
                    //获取已选择的图片路径
                    int columIndex =cursor.getColumnIndex(filePathClumn[0]);
                    CPicPath= cursor.getString(columIndex);
                    cursor.close();
                    mIvLoadPic.setImageBitmap(BitmapFactory.decodeFile(CPicPath));
                    break;
            }
        }
    }
}
