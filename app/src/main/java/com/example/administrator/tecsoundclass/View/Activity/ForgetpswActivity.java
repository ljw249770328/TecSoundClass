package com.example.administrator.tecsoundclass.View.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
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

public class ForgetpswActivity extends BaseActivity {
    private ImageView mIvbackicon;
    private Button mBtnCheck;
    private EditText mEtFgtId;
    private  EditText mEtRealname;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    Intent intent=new Intent();
                    intent.putExtra("userid",data.getStringExtra("userid"));
                    intent.putExtra("psw",data.getStringExtra("psw"));
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }

    private void init(){
        mIvbackicon=findViewById(R.id.im_back);
        mBtnCheck=findViewById(R.id.btn_check);
        mEtFgtId=findViewById(R.id.et_fgt_id);
        mEtRealname=findViewById(R.id.et_fgt_realname);
        if(getIntent().getExtras()!=null){
            mEtFgtId.setText(getIntent().getExtras().getString("userid"));
        }
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
                    if(mEtFgtId.getText().toString().equals("")){
                        ToastUtils.ShowMyToasts(ForgetpswActivity.this,"请输入需要找回的帐号Id", Gravity.CENTER);
                    }else if(mEtRealname.getText().toString().equals("")){
                        ToastUtils.ShowMyToasts(ForgetpswActivity.this,"请输入注册账户时填写的真实姓名",Gravity.CENTER);
                    }else{
//                        VerifyRequest(mEtFgtId.getText().toString(),mEtRealname.getText().toString());
                        String url = "http://101.132.71.111:8080/TecSoundWebApp/VerifyServlet";
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id",mEtFgtId.getText().toString());  //注⑥
                        params.put("user_realname",mEtRealname.getText().toString());
                        VolleyCallback.getJSONObject(getApplicationContext(), "Verify", url, params, new VolleyCallback.VolleyJsonCallback() {
                            @Override
                            public void onFinish(JSONObject r) {
                                try {
                                    String result = r.getString("Result");  //注④
                                    if(result.equals("notexists")){
                                        ToastUtils.ShowMyToasts(ForgetpswActivity.this,"账户不存在",Gravity.CENTER);
                                    }else if(result.equals("success")) {
                                        Intent intent=new Intent(ForgetpswActivity.this,FindPswActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("userid",mEtFgtId.getText().toString());
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent,1);
                                    }else if(result.equals("fail")){
                                        ToastUtils.ShowMyToasts(ForgetpswActivity.this,"姓名和用户id不匹配",Gravity.CENTER);
                                    }
                                } catch (JSONException e) {
                                    Log.e("TAG", e.getMessage(), e);
                                    ToastUtils.ShowMyToasts(ForgetpswActivity.this,e.toString() ,Gravity.CENTER);
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
                default:
                    break;
            }
        }
    }
//    private void VerifyRequest(final String user_id, final String user_realname) {
//        //请求地址
//        String url = "http://101.132.71.111:8080/TecSoundWebApp/VerifyServlet";
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
//                            if(result.equals("notexists")){
//                                Toast.makeText(ForgetpswActivity.this,"账户不存在",Toast.LENGTH_SHORT).show();
//                            }else if(result.equals("success")) {
//                                Intent intent=new Intent(ForgetpswActivity.this,FindPswActivity.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putString("userid",mEtFgtId.getText().toString());
//                                intent.putExtras(bundle);
//                                startActivityForResult(intent,1);
//                            }else if(result.equals("fail")){
//                                Toast.makeText(ForgetpswActivity.this,"姓名和用户id不匹配",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            Log.e("TAG", e.getMessage(), e);
//                            Toast.makeText(ForgetpswActivity.this,e.toString() ,Toast.LENGTH_SHORT).show();
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
//                params.put("user_realname",user_realname);
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
