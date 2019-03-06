package com.example.administrator.tecsoundclass.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.w3c.dom.DOMImplementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgetpswActivity extends AppCompatActivity {
    private ImageView mIvbackicon;
    private Button mBtnCheck;
    private EditText et_fgt_id;
    private  EditText et_fgt_realname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsw);
        //注册组件
        init();
        //设置监听
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void init(){
        mIvbackicon=findViewById(R.id.im_back);
        mBtnCheck=findViewById(R.id.btn_check);
        et_fgt_id=findViewById(R.id.et_fgt_id);
        et_fgt_realname=findViewById(R.id.et_fgt_realname);

        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void setListeners(){
        Onclick onclick=new Onclick();
        mBtnCheck.setOnClickListener(onclick);
        mIvbackicon.setOnClickListener(onclick);
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_check:

                    break;
                case R.id.im_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
//    private void VerifyRequest(final String user_id, final String user_realname) {
//        //请求地址
//        String url = "http://101.132.71.111:8080/TecSoundWebApp/LoginServlet";
//        String tag = "Login";
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
//                            if (result.equals("pass")) {  //注⑤
//                                Intent intent=new Intent(ForgetpswActivity.this,FindPswActivity.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putString("LoginId",mEtaccount.getText().toString());
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                finish();
//                            } else if(result.equals("pswerror")) {
//                                Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
//                            }else if(result.equals("notexists")) {
//                                Toast.makeText(LoginActivity.this,"用户不存在,请先注册",Toast.LENGTH_SHORT).show();
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
//                params.put("user_id",user_id);  //注⑥
//                params.put("user_password",user_realname);
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
