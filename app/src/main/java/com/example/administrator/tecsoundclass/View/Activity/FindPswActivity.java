package com.example.administrator.tecsoundclass.View.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindPswActivity extends BaseActivity {
    private ImageView mIvbackicon;
    private Button mBtnAlter;
    private EditText mEtNewPsw;
    private  EditText mEtConfPsw;
    private String userid=null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        //注册组件
        init();
        //设置监听
        SetListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    //注册组件
    private void init(){
        mIvbackicon=findViewById(R.id.im_back);
        mBtnAlter=findViewById(R.id.btn_alter);
        mEtNewPsw=findViewById(R.id.et_mod_psw);
        mEtConfPsw=findViewById(R.id.et_cfg_psw);
        userid=getIntent().getExtras().getString("userid");
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    //设置监听
    private void SetListener(){
        Onclick onclick =new Onclick();
        mBtnAlter.setOnClickListener(onclick);
        mIvbackicon.setOnClickListener(onclick);
        mEtNewPsw.setOnFocusChangeListener(new mOnFocusListener(mEtNewPsw));
        mEtConfPsw.setOnFocusChangeListener(new mOnFocusListener(mEtConfPsw));
    }
    //自定义点击事件内部类
    private class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_alter:
                    if(mEtNewPsw.getText().toString().equals("")){
                        ToastUtils.ShowMyToasts(FindPswActivity.this,"请输入新密码", Gravity.CENTER);
                    }else if(mEtConfPsw.getText().toString().equals("")){
                        ToastUtils.ShowMyToasts(FindPswActivity.this,"请确认密码",Gravity.CENTER);
                    }else if (!mEtConfPsw.getText().toString().equals(mEtNewPsw.getText().toString())){
                        ToastUtils.ShowMyToasts(FindPswActivity.this,"两次输入密码不一致,请重新输入",Gravity.CENTER);
                        mEtConfPsw.setText("");
                    }else {
//                        AlterRequest(userid,mEtNewPsw.getText().toString());
                        String url = "http://101.132.71.111:8080/TecSoundWebApp/AlterServlet";
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id",userid);
                        params.put("user_psw",mEtNewPsw.getText().toString());
                        VolleyCallback.getJSONObject(getApplicationContext(), "Alterpsw", url, params, new VolleyCallback.VolleyJsonCallback() {
                            @Override
                            public void onFinish(JSONObject r) {
                                try {
                                String result = r.getString("Result");  //注④
                                 if(result.equals("success")) {
                                     editor.putString("psw",mEtConfPsw.getText().toString());
                                     Intent intent=new Intent();
                                     intent.putExtra("userid",userid);
                                     intent.putExtra("psw",mEtConfPsw.getText().toString());
                                     setResult(RESULT_OK,intent);
                                     finish();
                                 }else {
                                     ToastUtils.ShowMyToasts(FindPswActivity.this,r.toString(),Gravity.CENTER);
                                 }
                                } catch (JSONException e) {
                                    Log.e("TAG", e.getMessage(), e);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                    break;
                case R.id.im_back:
                    finish();
                    break;
            }
        }
    }
    //自定义焦点丢失时间内部类
    private class mOnFocusListener implements View.OnFocusChangeListener {
        private EditText e;
        private mOnFocusListener(EditText editText) {
            e = editText;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.et_mod_psw:
                        if (e.length()>=16){
                            e.setText(e.getText().toString().substring(0,16));
                            e.setSelection(16);
                            ToastUtils.ShowMyToasts(FindPswActivity.this,"长度超出范围",Gravity.CENTER);
                        }else if(e.getTextSize()<6){
                            ToastUtils.ShowMyToasts(FindPswActivity.this,"长度不足6位",Gravity.CENTER);
                        }
                        break;
                    case R.id.et_cfg_psw:
                        if (e.length()>=16){
                            e.setText(e.getText().toString().substring(0,16));
                            e.setSelection(16);
                            ToastUtils.ShowMyToasts(FindPswActivity.this,"长度超出范围",Gravity.CENTER);
                        }else if(e.length()<6){
                            ToastUtils.ShowMyToasts(FindPswActivity.this,"长度不足6位",Gravity.CENTER);
                        }else if (!e.getText().toString().equals(mEtConfPsw.getText().toString())){
                            ToastUtils.ShowMyToasts(FindPswActivity.this,"两次输入密码不一致",Gravity.CENTER);
                            e.setText("");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
    //修改请求函数
//    private void AlterRequest(final String user_id, final String newpsw) {
//        //请求地址
//        String url = "http://101.132.71.111:8080/TecSoundWebApp/AlterServlet";
//        String tag = "alter";
//        //取得请求队列
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
//                             if(result.equals("success")) {
//                                 editor.putString("psw",mEtConfPsw.getText().toString());
//                                 Intent intent=new Intent();
//                                 intent.putExtra("userid",userid);
//                                 intent.putExtra("psw",mEtConfPsw.getText().toString());
//                                 setResult(RESULT_OK,intent);
//                                 finish();
//                            }else {
//                                Toast.makeText(FindPswActivity.this,response,Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
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
//                params.put("user_id",user_id);
//                params.put("user_psw",newpsw);
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