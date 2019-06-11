package com.example.administrator.tecsoundclass.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegeditVoiceActivity extends BaseActivity implements View.OnClickListener {
    private static final int PWD_TYPE_NUM = 3;
    private int mPwdType = PWD_TYPE_NUM;
    private ImageView mIvBack;
    private Toast mToast;
    private String mAuthId,mNumPwd ;
    private TextView mResultText,mErrorResult;
    private SpeakerVerifier mSpeakerVerifier;
    private TextView mBtnStartRecord,mTvDeleteModel;
    private ProgressDialog mProDialog;
    private String[] items;
    private String[] mNumPwdSegs;
    private AlertDialog mTextPwdSelectDialog;
    private boolean isStartWork = false; // 录音设备占用情况
    private static final String TAG = RegeditVoiceActivity.class.getSimpleName();

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
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        Bundle bundle=getIntent().getExtras();
        User user=(User)bundle.getSerializable("mUser");
        mAuthId=user.getUser_id();
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
        mTvDeleteModel=findViewById(R.id.isv_delete);
        mBtnStartRecord.setOnClickListener(this);
        findViewById(R.id.isv_getpassword).setOnClickListener(this);
        mTvDeleteModel.setOnClickListener(this);
        mBtnStartRecord.setVisibility(View.GONE);
        mTvDeleteModel.setVisibility(View.GONE);
        //设置加载框可关闭防止无网络等待
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
                if(mNumPwd==null){
                    downloadPwd();
                    mBtnStartRecord.setVisibility(View.VISIBLE);
                    mTvDeleteModel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.isv_reocrd:
                mErrorResult.setText("");
                vocalRegedit();
                break;
            case R.id.isv_delete:
                performModelOperation("del", mModelOperationListener);
                break;
        }
    }

    //下载密码
    private void downloadPwd() {
        // 获取密码之前先终止之前的操作
        mSpeakerVerifier.cancel();
        mBtnStartRecord.setClickable(false);
        mProDialog.setMessage("下载中...");
        mProDialog.show();
        // 清空引擎参数
        mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mSpeakerVerifier.setParameter(SpeechConstant.MFV_SCENES, "ivp");
        // 当前声纹密码类型为数字类型
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
        mSpeakerVerifier.getPasswordList(mPwdListenter);
    }

    //开始注册
    private void vocalRegedit() {
        mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
        mSpeakerVerifier.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test.wav");
        // 消除噪音,但是需要的音量很大
//        mSpeakerVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
        // 数字密码注册需要传入密码
        if (TextUtils.isEmpty(mNumPwd)) {
            showTip("请获取密码后进行操作");
            return;
        }
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("请点击“开始录音”按钮！\n");
        strBuffer.append("请读出：" +  mNumPwdSegs[0] + "\n");
        strBuffer.append("训练 第" + 1 + "遍，剩余4遍\n");
        mResultText.setText(strBuffer.toString());
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWD, mNumPwd);

        //业务参数设置
        mSpeakerVerifier.setParameter(SpeechConstant.AUTH_ID,mAuthId);
        // 设置业务类型为注册
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_SST, "train");
        // 设置声纹密码类型
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
        // 开始注册
        mSpeakerVerifier.startListening(mRegisterListener);
    }

    //对模型进行操作
    private void performModelOperation(String operation, SpeechListener listener) {
        // 清空参数
        mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
        //设置密码类型

        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
        if (TextUtils.isEmpty(mNumPwd)) {
            showTip("请获取密码后进行操作");
            return;
        }
        mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
        mSpeakerVerifier.sendRequest(operation, mAuthId, listener);

    }
    //设置模型监听
    private SpeechListener mModelOperationListener = new SpeechListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

            String result = new String(buffer);
            try {
                JSONObject object = new JSONObject(result);
                String cmd = object.getString("cmd");
                int ret = object.getInt("ret");
                if ("del".equals(cmd)) {
                    if (ret == ErrorCode.SUCCESS) {
                        showTip("删除成功");
                        mResultText.setText("");
                    } else if (ret == ErrorCode.MSP_ERROR_FAIL) {
                        showTip("删除失败，模型不存在");
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
                showTip("操作失败：" + error.getPlainDescription(true));
            }
        }
    };
    //获得密码
    private SpeechListener mPwdListenter=new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }
        @Override
        public void onBufferReceived(byte[] bytes) {
            mProDialog.dismiss();
            mBtnStartRecord.setClickable(true);
            String result = new String(bytes);


            StringBuffer numberString = new StringBuffer();
            try {
                JSONObject object = new JSONObject(result);
                if (!object.has("num_pwd")) {
                    mResultText.setText("");
                    return;
                }
                JSONArray pwdArray = object.optJSONArray("num_pwd");
                numberString.append(pwdArray.get(0));
                for (int i = 1; i < pwdArray.length(); i++) {
                    numberString.append("-" + pwdArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mNumPwd = numberString.toString();
            mNumPwdSegs = mNumPwd.split("-");
            mResultText.setText("注册密码 ：" + mNumPwd +
                    "\n点击“开始录音”开始建立基准声纹\n");
        }
        @Override
        public void onCompleted(SpeechError speechError) {
            if (null != speechError && ErrorCode.SUCCESS != speechError.getErrorCode()) {
                showTip("获取失败：" + speechError.getErrorCode());
            }
        }
    };
    //获取注册监听
    private VerifierListener mRegisterListener = new VerifierListener() {
        @Override
        public void onResult(VerifierResult verifierResult) {
            if (verifierResult.ret == ErrorCode.SUCCESS){
                switch (verifierResult.err){
                    case VerifierResult.MSS_ERROR_IVP_GENERAL:
                        mErrorResult.setText("内核异常");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
                        mErrorResult.setText("训练达到最大次数");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                        mErrorResult.setText("出现截幅");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                        mErrorResult.setText("太多噪音");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                        mErrorResult.setText("录音太短");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                        mErrorResult.setText("训练失败，您所读的文本不一致");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                        mErrorResult.setText("音量太低");
                        break;
                    case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                        mErrorResult.setText("音频长达不到自由说的要求");
                        break;
                    default:
                        showTip("");
                        break;
                }

                if (verifierResult.suc == verifierResult.rgn){
                    mResultText.setText("注册成功");
                    mErrorResult.setText("您的数字密码声纹ID：\n" + verifierResult.vid);
                    mBtnStartRecord.setClickable(false);
                    Log.e(TAG, "onResult: " + verifierResult.vid);
                    isStartWork = false;
                    if (mSpeakerVerifier != null) {
                        mSpeakerVerifier.stopListening();
                    }

                } else {
                    int nowTimes = verifierResult.suc + 1;
                    int leftTimes = verifierResult.rgn - nowTimes;
                    mErrorResult.setText("");
                    StringBuffer strBuffer = new StringBuffer();
                    strBuffer.append("请继续\n");
                    if (PWD_TYPE_NUM == mPwdType) {
                        strBuffer.append("读出：" + mNumPwdSegs[nowTimes - 1] + "\n");
                    }
                    strBuffer.append("训练 第" + nowTimes + "遍，剩余" + leftTimes + "遍");
                    mResultText.setText(strBuffer.toString());
                }

            } else {
                mResultText.setText("注册失败，请重新开始。");
            }
        }
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("当前正在说话，音量大小：" + i);
            Log.d(TAG, "返回音频数据：" + bytes.length);
        }

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
        }

        @Override
        public void onError(SpeechError speechError) {
            if (speechError.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
                showTip("模型已存在，如需重新注册，请先删除");
            } else {
                showTip("onError Code：" + speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}