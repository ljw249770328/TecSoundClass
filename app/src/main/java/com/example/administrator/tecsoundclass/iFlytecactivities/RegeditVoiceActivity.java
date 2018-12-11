package com.example.administrator.tecsoundclass.iFlytecactivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;

public class RegeditVoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PWD_TYPE_TEXT = 1;
    private int mPwdType=PWD_TYPE_TEXT;
    private ImageView mIvBack;
    private Toast mToast;
    private String mAuthId,mTextPwd;
    private TextView mResultText,mErrorResult;
    private SpeakerVerifier mSpeakerVerifier;
    private Button mBtnStartRecord;
    private ProgressDialog mProDialog;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit_voice);
        setUI();
        // 初始化SpeakerVerifier声纹识别引擎
        mSpeakerVerifier = SpeakerVerifier.createVerifier(this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (ErrorCode.SUCCESS == i) {
                    showTip("引擎初始化成功");
                } else {
                    showTip("引擎初始化失败，错误码：" + i);
                }
            }
        });


    }
    private void setUI(){
        Bundle bundle=getIntent().getExtras();
        mAuthId=bundle.getString("StudentId");
        mIvBack=findViewById(R.id.im_back);
        mResultText = findViewById(R.id.edt_result);
        mErrorResult = findViewById(R.id.error_result);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnStartRecord=findViewById(R.id.isv_reocrd);
        mBtnStartRecord.setOnClickListener(this);
        findViewById(R.id.isv_getpassword).setOnClickListener(this);
        findViewById(R.id.isv_delete).setOnClickListener(this);
        //设置加载框可关闭防止等待
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍候");
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != mSpeakerVerifier) {
                    mSpeakerVerifier.cancel();
                }
            }
        });
    }
    //确认声纹识别引擎是否成功初始化
    private boolean checkInstance() {
        if (null == mSpeakerVerifier) {
            // 创建单例失败，与 21001 错误为同样原因，
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        if (!checkInstance()) {
            return;
        }
        //防止重复点击造成崩溃
        mSpeakerVerifier.cancel();
        switch (v.getId()){
            case R.id.isv_getpassword:
                if(mTextPwd==null){
                    downloadPwd();
                }
                break;
            case R.id.isv_reocrd:

        }
    }
    //获得密码
    private SpeechListener mPwdListenter=new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
           /////////写到这里了!!
        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }
    };

    private void downloadPwd() {
        // 获取密码之前先终止之前的操作
        mSpeakerVerifier.cancel();
        // 下载密码时，按住说话触摸无效
        mBtnStartRecord.setClickable(false);

        mProDialog.setMessage("下载中...");
        mProDialog.show();
        // 清空参数
        mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mSpeakerVerifier.setParameter(SpeechConstant.MFV_SCENES, "ivp");
        // 当前声纹密码类型，1、2、3分别为文本、自由说和数字密码
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
        mSpeakerVerifier.getPasswordList(mPwdListenter);
    }
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
