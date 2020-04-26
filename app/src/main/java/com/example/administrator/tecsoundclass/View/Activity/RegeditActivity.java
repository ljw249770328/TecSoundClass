package com.example.administrator.tecsoundclass.View.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegeditActivity extends BaseActivity {
    private ImageView mIvbackicon;
    private Button mBtnRegedit;
    private RadioGroup radioGroup;
    private RadioButton rb_student;
    private RadioButton rb_teacher;
    private String mIdentity="学生";
    private EditText mEtregId;
    private EditText mEtPsw;
    private EditText mEtConPsw;
    private EditText mEtRealname;
    private  boolean k1=false,k2=false,k3=false,k4=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        init();
        setListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    //注册组件
    private void init(){
        mIvbackicon=findViewById(R.id.im_back);
        radioGroup=findViewById(R.id.rb_radiogroup);
        rb_student=findViewById(R.id.rb_student);
        rb_teacher=findViewById(R.id.rb_teacher);
        mBtnRegedit=findViewById(R.id.btn_regedit);
        mEtregId=findViewById(R.id.et_reg_id);
        mEtConPsw=findViewById(R.id.et_reg_configpsw);
        mEtPsw=findViewById(R.id.et_reg_psw);
        mEtRealname=findViewById(R.id.et_reg_realname);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mIvbackicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //设置监听
    private void setListener(){
        mEtregId.setOnFocusChangeListener(new mOnFocusListener(mEtregId));
        mEtPsw.setOnFocusChangeListener(new mOnFocusListener(mEtPsw));
        mEtConPsw.setOnFocusChangeListener(new mOnFocusListener(mEtConPsw));
        mEtRealname.setOnFocusChangeListener(new mOnFocusListener(mEtRealname));
        mBtnRegedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空焦点事件
                mEtregId.clearFocus();
                mEtPsw.clearFocus();
                mEtConPsw.clearFocus();
                mEtRealname.clearFocus();
                if(k1&&k2&&k3&&k4){
//                    RegeditRequest();
                    String url = "http://101.132.71.111:8080/TecSoundWebApp/RegeditServlet";
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id",mEtregId.getText().toString());  //注⑥
                    params.put("user_password",mEtConPsw.getText().toString());
                    params.put("user_identity",mIdentity);
                    params.put("user_name",mEtRealname.getText().toString());
                    VolleyCallback.getJSONObject(getApplicationContext(), "Regedit", url, params, new VolleyCallback.VolleyJsonCallback() {
                        @Override
                        public void onFinish(JSONObject r) {
                            try{
                            String result = r.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Intent intent =new Intent(RegeditActivity.this,LoginActivity.class);
                                intent.putExtra("id",mEtregId.getText().toString());
                                setResult(RESULT_OK,intent);
                                finish();
                            }else if(result.equals("0")){
                                ToastUtils.ShowMyToasts(RegeditActivity.this,"帐户已存在", Gravity.CENTER);
                            } else {
                                ToastUtils.ShowMyToasts(RegeditActivity.this,result,Gravity.CENTER);
                            }
                            } catch (JSONException e) {
                                //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                Log.e("TAG", e.getMessage(), e);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                }else {
                    ToastUtils.ShowMyToasts(RegeditActivity.this,"填写信息有误",Gravity.CENTER);
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int count=radioGroup.getChildCount();
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i = 0 ;i < count;i++){
                    RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                    if(rb.isChecked()){
                        if(rb.getText().toString().equals("我是学生")){
                            mIdentity="学生";
                        }else if(rb.getText().toString().equals("我是老师")){
                            mIdentity="老师";
                        }
                    }
                }
            }
        });
    }
    //自定义焦点丢失事件
    private class mOnFocusListener implements View.OnFocusChangeListener{
        private EditText e;
        private mOnFocusListener(EditText editText){
            e=editText;
        }
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                switch (v.getId()){
                    case R.id.et_reg_id:
                        k1=false;
                        if (e.getText().toString().equals("")){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"您还未填写注册Id", Gravity.CENTER);
                        }else if (e.length()>20){
                            e.setText(e.getText().toString().substring(0,20));
                            e.setSelection(20);
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"超出长度范围",Gravity.CENTER);
                        }else {
                            k1=true;
                        }
                        break;
                    case R.id.et_reg_psw:
                        k2=false;
                        if (e.getText().toString().equals("")){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"您还未填写账户密码", Gravity.CENTER);
                        }
                        else if (e.length()>=16){
                            e.setText(e.getText().toString().substring(0,16));
                            e.setSelection(16);
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"长度超出范围",Gravity.CENTER);
                        }else if(e.getTextSize()<6){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"长度不足6位",Gravity.CENTER);
                        }else {
                            k2=true;
                        }
                        break;
                    case R.id.et_reg_configpsw:
                        k3=false;
                        if (e.getText().toString().equals("")){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"您还未确认账户密码", Gravity.CENTER);
                        }
                        else if (e.length()>=16){
                            e.setText(e.getText().toString().substring(0,16));
                            e.setSelection(16);
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"长度超出范围",Gravity.CENTER);
                        }else if(e.length()<6){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"长度不足6位",Gravity.CENTER);
                        }else if (!e.getText().toString().equals(mEtPsw.getText().toString())){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"两次输入密码不一致",Gravity.CENTER);
                            e.setText("");
                        }else{
                            k3=true;
                        }
                        break;
                    case R.id.et_reg_realname:
                        k4=false;
                        if (e.getText().toString().equals("")){
                            ToastUtils.ShowMyToasts(RegeditActivity.this,"注册姓名为空",Gravity.CENTER);
                        }else{
                            k4=true;
                        }
                        break;
                    default:
                        break;
                }
            }

        }
    }
    //注册请求
//    private void RegeditRequest() {
//        //请求地址
//        String url = "http://101.132.71.111:8080/TecSoundWebApp/RegeditServlet";
//        String tag = "Regedit";
//        //取得请求队列
//        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        //防止重复请求，所以先取消tag标识的请求队列
//        requestQueue.cancelAll(tag);
//
//        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
//        final StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
//                            String result = jsonObject.getString("Result");  //注④
//                            if (result.equals("success")) {  //注⑤
//                                Intent intent =new Intent(RegeditActivity.this,LoginActivity.class);
//                                intent.putExtra("id",mEtregId.getText().toString());
//                                setResult(RESULT_OK,intent);
//                                finish();
//                            }else if(result.equals("0")){
//                                Toast.makeText(RegeditActivity.this,"帐户已存在", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(RegeditActivity.this,result,Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
//                            Log.e("TAG", e.getMessage(), e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
//                Log.e("TAG", error.getMessage(), error);
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("user_id",mEtregId.getText().toString());  //注⑥
//                params.put("user_password",mEtConPsw.getText().toString());
//                params.put("user_identity",mIdentity);
//                params.put("user_name",mEtRealname.getText().toString());
//                return params;
//            }
//        };
//
//        //设置Tag标签
//        request.setTag(tag);
//
//        //将请求添加到队列中
//        requestQueue.add(request);
//    }
}