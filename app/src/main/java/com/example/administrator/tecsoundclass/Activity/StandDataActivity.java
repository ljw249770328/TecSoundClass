package com.example.administrator.tecsoundclass.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class StandDataActivity extends BaseActivity {
    private TextView mTvback,mTvVoiceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_data);
        final Bundle bundle=getIntent().getExtras();
        mTvback=findViewById(R.id.tv_back);
        mTvVoiceData=findViewById(R.id.tv_voice_data);
        mTvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvVoiceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(StandDataActivity.this,RegeditVoiceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
