package com.example.administrator.tecsoundclass.Activity;

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
import com.example.administrator.tecsoundclass.R;

import org.litepal.LitePal;

import java.util.List;

public class ForgetpswActivity extends AppCompatActivity {
    private ImageView mIvbackicon;
    private Button mBtnAlter;
    private EditText et_fgt_id;
    private  EditText et_fgt_realname;
    private String getPasswordById(String id){
        List<User> userlist=LitePal.select("user_name")
                .where("user_id=?",id)
                .find(User.class);
        User user=userlist.get(0);
        String realname=user.getUser_name();
        return realname;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsw);
        mIvbackicon=findViewById(R.id.im_back);
        mBtnAlter=findViewById(R.id.btn_check);
        et_fgt_id=findViewById(R.id.et_fgt_id);
        et_fgt_realname=findViewById(R.id.et_fgt_realname);

        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mBtnAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fgt_userid = et_fgt_id.getText().toString();
                String fgt_username = et_fgt_realname.getText().toString();
                List<User> persons =LitePal.findAll(User.class);
                boolean flag=false;
                //Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();

                for (User ps : persons) {

                    if(fgt_userid.equals(ps.getUser_id())){
                        if (fgt_username.equals(getPasswordById(fgt_userid)))
                            //匹配就设true
                            flag=true;
                    }
                }

                if (flag)
                {
                    Intent intent=new Intent(ForgetpswActivity.this,FindPswActivity.class);
                    intent.putExtra("username",et_fgt_realname.getText().toString());
                    intent.putExtra("user_id",et_fgt_id.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(ForgetpswActivity.this,"账号不存在或姓名不匹配",Toast.LENGTH_SHORT).show();
                }


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
