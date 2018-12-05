package com.example.administrator.tecsoundclass;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private TextView mTvRegedit,mTvForget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin=findViewById(R.id.btn_login);
        mTvForget=findViewById(R.id.tv_forget);
        mTvRegedit=findViewById(R.id.tv_regedit);
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void setListeners(){
        OnClick onClick=new OnClick();
        mBtnLogin.setOnClickListener(onClick);
        mTvForget.setOnClickListener(onClick);
        mTvRegedit.setOnClickListener(onClick);

    }
    private class  OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent =null;
            switch (v.getId()){
                case R.id.btn_login:
                    intent=new Intent(LoginActivity.this,MainMenuActivity.class);
                    break;
                case R.id.tv_forget:
                    intent=new Intent(LoginActivity.this,ForgetpswActivity.class);
                    break;
                case R.id.tv_regedit:
                    intent=new Intent(LoginActivity.this,RegeditActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
