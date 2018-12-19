package com.example.administrator.tecsoundclass;

import android.Manifest;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.weeboos.permissionlib.PermissionRequest;
import com.example.weeboos.permissionlib.PermissionUtils;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private TextView mTvRegedit,mTvForget;
    private EditText accountEdit;
    private EditText passwordEdit;
    final String [] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE};

    //private CheckBox rememberPass;
    private String getPasswordById(String id){
        List<User> userlist=LitePal.select("user_password")
                .where("user_id=?",id)
                .find(User.class);
        User user=userlist.get(0);
        String psw=user.getUser_password();
        return psw;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //安卓6.0以上需要动态获取权限
        //初始化权限
        Requestpermission();
        mBtnLogin=findViewById(R.id.btn_login1);
        mTvForget=findViewById(R.id.tv_forget);
        mTvRegedit=findViewById(R.id.tv_regedit);
        accountEdit=findViewById(R.id.et_username);
        passwordEdit=findViewById(R.id.et_password);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                List<User>persons =LitePal.findAll(User.class);
                boolean flag=false;
                //Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();

                for (User ps : persons) {

                    if(username.equals(ps.getUser_id())){
                        if (password.equals(getPasswordById(username)))
                            //匹配就设true
                            flag=true;
                    }
                }

                if (flag)
                {
                    //如果匹配才进入主页面
                    Intent s = new Intent(LoginActivity.this,MainMenuActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("LoginId",accountEdit.getText().toString());
                    s.putExtras(bundle);
                    startActivity(s);
                }else{
                    Toast.makeText(LoginActivity.this,username+"  "+password,Toast.LENGTH_SHORT).show();
                }

            }
        });
        mTvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Login_to_Forget = new Intent(LoginActivity.this,ForgetpswActivity.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Login_to_Forget);
                finish();
            }
        });
        mTvRegedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Login_to_Register = new Intent(LoginActivity.this,RegeditActivity.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Login_to_Register);
                finish();
            }
        });
    }

    private void Requestpermission(){
        PermissionRequest request = new PermissionRequest(this); // 这个this需要一个activity对象或者fragment对象
        request.requestPermission(new PermissionRequest.PermissionListener() {
            @Override
            public void permissionGranted() {
                //do Something when permission granted
                Toast.makeText(LoginActivity.this,"权限获取成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionDenied(ArrayList<String> permissions) {
                //do Something when permission denied
                Toast.makeText(LoginActivity.this,"获取拒绝",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionNeverAsk(ArrayList<String> permissions) {
                //do Something when permission never ask
                Toast.makeText(LoginActivity.this,"不再询问",Toast.LENGTH_SHORT).show();
                PermissionUtils.showAlertDialog(LoginActivity.this);
            }
        },permissions);
    }
}




