package com.example.administrator.tecsoundclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StandDataActivity extends AppCompatActivity {
    private TextView mTvback,mTvVoiceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_data);
        Bundle bundle=getIntent().getExtras();
        mTvback=findViewById(R.id.tv_back);
        mTvVoiceData=findViewById(R.id.tv_voice_data);
        mTvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
