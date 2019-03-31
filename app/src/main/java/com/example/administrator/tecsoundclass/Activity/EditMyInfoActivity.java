package com.example.administrator.tecsoundclass.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.PicManager;
import com.example.administrator.tecsoundclass.utils.TPDialogFactory;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditMyInfoActivity extends AppCompatActivity {
    private ImageView mBack,mIvEditedHead;
    private TextView mTvMyname,mTvMyId,mTvMyBirthD,mTvMyIdentity,mTvSave,mTvChangeHead;
    private RadioGroup mSexGroup;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String mSex="男";
    private String picTurePath="";


    private void init(){
        mBack=findViewById(R.id.im_back);
        mIvEditedHead=findViewById(R.id.iv_edit_head);
        mTvMyname=findViewById(R.id.tv_my_name);
        mTvMyId=findViewById(R.id.tv_my_id);
        mTvMyIdentity=findViewById(R.id.tv_my_occupation);
        mTvMyBirthD=findViewById(R.id.tv_my_birth_date);
        mTvSave=findViewById(R.id.tv_save);
        mSexGroup=findViewById(R.id.rg_sex_group);
        rb_male=findViewById(R.id.rb_sex_male);
        rb_female=findViewById(R.id.rb_sex_female);
        mTvChangeHead=findViewById(R.id.tv_change_head);
    }
    private void SetListeners(){
        OnClick onClick =new OnClick();
        mBack.setOnClickListener(onClick);
        mTvMyBirthD.setOnClickListener(onClick);
        mTvSave.setOnClickListener(onClick);
        mTvChangeHead.setOnClickListener(onClick);
        mSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int count = mSexGroup.getChildCount();
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               for (int i=0;i<count;i++) {
                   RadioButton rb = (RadioButton) mSexGroup.getChildAt(i);
                   if (rb.isChecked()){
                       mSex=rb.getText().toString();
                   }
               }
            }
        });
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    finish();
                    break;
                case R.id.tv_my_birth_date:
                    TPDialogFactory factory =new TPDialogFactory();
                    factory.getDatePicker(EditMyInfoActivity.this,"出生日期",mTvMyBirthD).show(factory.getTime());
                    break;
                case R.id.tv_save:
                    String path=PicManager.UpLoadPic(getApplicationContext(),"upload",picTurePath,"headpic");
                    String url="http://101.132.71.111:8080/TecSoundWebApp/AlterUInfoServlet";
                    Map<String,String> params=new HashMap<>();
                    params.put("user_id",mTvMyId.getText().toString());
                    params.put("user_age",mTvMyBirthD.getText().toString());
                    params.put("user_sex",mSex);
                    params.put("user_pic_src",path);
                    VolleyCallback.getJSONObject(EditMyInfoActivity.this, "EditMyinfo", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try {
                                String Result =r.getString("Result");
                                if(Result.equals("success")){
                                    Toast.makeText(EditMyInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(EditMyInfoActivity.this,"出现异常"+Result,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    finish();
                    break;
                case R.id.tv_change_head:
                    Intent intent =new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);
                default:
                    break;
            }
        }
    }
    private void SetData(){
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetUInfoServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id",getIntent().getExtras().getString("user_id"));
        VolleyCallback.getJSONObject(EditMyInfoActivity.this, "GetUInfo", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray users=r.getJSONArray("users");
                    JSONObject user= (JSONObject) users.get(0);
                    mTvMyname.setText(user.getString("user_name"));
                    mTvMyId.setText(user.getString("user_id"));
                    mTvMyBirthD.setText(user.getString("user_age"));
                    mTvMyIdentity.setText(user.getString("user_identity"));
                    Glide.with(getApplicationContext()).load(user.getString("user_pic_src")).into(mIvEditedHead);
                    if (user.getString("user_sex").equals("女"));{
                        mSexGroup.check(R.id.rb_sex_female);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    picTurePath= cursor.getString(columIndex);
                    cursor.close();
                    mIvEditedHead.setImageBitmap(BitmapFactory.decodeFile(picTurePath));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myinfo);
        init();
        SetListeners();
        SetData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
