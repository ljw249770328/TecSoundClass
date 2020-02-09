package com.example.administrator.tecsoundclass.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;

public class StandDataActivity extends BaseActivity {
    private TextView mTvback,mTvVoiceData,mTvFaceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_data);
        final Bundle bundle=getIntent().getExtras();
        mTvback=findViewById(R.id.tv_back);
        mTvVoiceData=findViewById(R.id.tv_voice_data);
        mTvFaceData=findViewById(R.id.face);
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
        mTvFaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(StandDataActivity.this,RegeditFaceDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
