package com.example.administrator.tecsoundclass.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.tecsoundclass.R;

public class SettingsActivity extends BaseActivity {
    private ImageView mIvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mIvBack=findViewById(R.id.im_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
