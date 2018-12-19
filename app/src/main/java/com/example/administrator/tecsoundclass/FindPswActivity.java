package com.example.administrator.tecsoundclass;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.User;

public class FindPswActivity extends AppCompatActivity {
    private ImageView mIvbackicon;
    private Button mBtnAlter;
    private EditText et_fgt_id;
    private  EditText et_fgt_realname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        mIvbackicon=findViewById(R.id.im_back);
        mBtnAlter=findViewById(R.id.btn_alter);
        et_fgt_id=findViewById(R.id.et_mod_psw);
        et_fgt_realname=findViewById(R.id.et_cfg_psw);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent=getIntent();
        final String name=intent.getStringExtra("username");
        final String id=intent.getStringExtra("user_id");
        mBtnAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fgt_psw = et_fgt_id.getText().toString();
                String fgt_con_psw = et_fgt_realname.getText().toString();
                if(fgt_psw.trim().equals(fgt_con_psw.trim())){
                    User user=new User();
                    user.setUser_password(fgt_con_psw);
                    user.updateAll("user_name=? and user_id=?",name,id);
                    if(user.getUser_password().equals(fgt_con_psw)){
                        Toast.makeText(FindPswActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(FindPswActivity.this,LoginActivity.class);
                        startActivity(intent);}}
            }
        });
        mIvbackicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}