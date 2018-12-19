package com.example.administrator.tecsoundclass;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.User;

import org.litepal.LitePal;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class RegeditActivity extends AppCompatActivity {
    private ImageView mIvbackicon;
    private Button mBtnRegedit;
    private RadioGroup radioGroup;
    private RadioButton rb_student;
    private RadioButton rb_teacher;
    private EditText et_reg_id;
    private EditText con_passwordEdit;
    private EditText passwordEdit;
    private EditText realName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        mIvbackicon=findViewById(R.id.im_back);
        radioGroup=findViewById(R.id.rb_radiogroup);
        rb_student=findViewById(R.id.rb_student);
        rb_teacher=findViewById(R.id.rb_teacher);
        mBtnRegedit=findViewById(R.id.btn_login);
        et_reg_id=findViewById(R.id.et_reg_id);
        con_passwordEdit=findViewById(R.id.et_reg_configpsw);
        passwordEdit=findViewById(R.id.et_reg_psw);
        realName=findViewById(R.id.et_reg_realname);
        //身份确认未完成
//        radioGroup.setOnCheckedChangeListener(new View.OnClickListener(){
//
//        });
        mIvbackicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnRegedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = realName.getText().toString();
                final String password = passwordEdit.getText().toString();
                final String con_password = con_passwordEdit.getText().toString();
                final String reg_id = et_reg_id.getText().toString();
                boolean flag=false;
                List<User> persons =LitePal.findAll(User.class);
                //Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();

                for (User ps : persons) {

                    if(reg_id.trim().equals(ps.getUser_id()))
                    {
                        //匹配就设true
                        flag=true;
                    }
                }
                if(flag){
                    Toast.makeText(RegeditActivity.this, "该用户已经存在", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(con_password)||TextUtils.isEmpty(reg_id)) {
                        Toast.makeText(RegeditActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    } else {
                        if (reg_id.length()!=11) {
                            Toast.makeText(RegeditActivity.this, "请输入正确的11位手机号或学号", Toast.LENGTH_SHORT).show();
                        } else {
                            if(!(password.equals(con_password))){
                                Toast.makeText(RegeditActivity.this, "两次输入的密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (password.length()<6||password.length()>11){
                                    Toast.makeText(RegeditActivity.this, "请输入6位或者6位以上11位以下的密码", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (rb_student.isChecked()) {
                                        User user = new User();
                                        user.setUser_id(reg_id);
                                        user.setUser_identity("student");
                                        user.setUser_name(username);
                                        user.setUser_password(password);
                                        user.save();
                                        Toast.makeText(RegeditActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        Intent intent_Register_to_Login = new Intent(RegeditActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                                        startActivity(intent_Register_to_Login);
                                        finish();
                                    } else {
                                        User user = new User();
                                        user.setUser_id(reg_id);
                                        user.setUser_identity("teacher");
                                        user.setUser_name(username);
                                        user.setUser_password(password);
                                        user.save();
                                        Toast.makeText(RegeditActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        Intent intent_Register_to_Login = new Intent(RegeditActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                                        startActivity(intent_Register_to_Login);
                                        finish();
                                    }
                                }
                            }
                        }
                    }
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

}
