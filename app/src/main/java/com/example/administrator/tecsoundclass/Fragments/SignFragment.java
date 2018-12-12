package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Adapter.MySignListAdapter;
import com.example.administrator.tecsoundclass.CourseMenuActivity;
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

public class SignFragment extends Fragment {
    private ImageView mIvBack;
    private ListView mLv;
    private TextView mTvSign;
    private SpeakerVerifier mSpeakerVerifier;
    private Toast mToast;
    private String mAuthId;
    private String mTextPwd = "";
    private  final String TAG = CourseMenuActivity.class.getSimpleName();
    private static final int PWD_TYPE_TEXT = 1;
    private TextView mResultText,mErrorResult;
    private String[] items;

    public SignFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new SignFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_sign,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Onclick onclick=new Onclick();
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mIvBack=view.findViewById(R.id.im_back);
        mTvSign=view.findViewById(R.id.tv_start_sign);
        mAuthId=getActivity().getIntent().getExtras().getString("StudentId");
        mLv=view.findViewById(R.id.lv_1);
        mIvBack.setOnClickListener(onclick);
        mTvSign.setOnClickListener(onclick);
        mLv.setAdapter(new MySignListAdapter(getActivity()));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mSpeakerVerifier = SpeakerVerifier.createVerifier(getActivity(), new InitListener() {
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
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.tv_start_sign:
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View view =LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_dialog,null);
                    mResultText=view.findViewById(R.id.edt_result);
                    mErrorResult=view.findViewById(R.id.error_result);
                    builder.setView(view).show();
                    mErrorResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VoiceIdentifier voiceIdentifier=new VoiceIdentifier();
                            voiceIdentifier.StartIdentify();
                        }
                    });
            }
        }
    }
    private class VoiceIdentifier{
        private void StartIdentify(){
            //获取密码
            // 清空参数
            mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
            mSpeakerVerifier.setParameter(SpeechConstant.MFV_SCENES, "ivp");
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" +PWD_TYPE_TEXT);
            SpeechListener mPwdListenter = new SpeechListener() {
                @Override
                public void onEvent(int i, Bundle bundle) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {
                    String result = new String(bytes);
                    try {
                        JSONObject object = new JSONObject(result);
                        if (!object.has("txt_pwd")) {
                            mResultText.setText("");
                            return;
                        }
                        JSONArray pwdArray = object.optJSONArray("txt_pwd");
                        items = new String[pwdArray.length()];
                        for (int i = 0; i < pwdArray.length(); i++) {
                            items[i] = pwdArray.getString(i);
                        }
                        mTextPwd = items[0];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if (null != speechError && ErrorCode.SUCCESS != speechError.getErrorCode()) {
                        showTip("获取失败：" + speechError.getErrorCode());
                    }
                }
            };
            mSpeakerVerifier.getPasswordList(mPwdListenter);

            //验证
            mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
            mSpeakerVerifier = SpeakerVerifier.getVerifier();
            // 设置业务类型为验证
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
            // 消噪
//       mVerify.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
            mResultText.setText("请读出：" + mTextPwd);
            // 设置auth_id，不能设置为空
            mSpeakerVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_TEXT);
            // 开始验证
            mSpeakerVerifier.startListening(mVerifyListener);
        }

    }
        private void showTip(final String str) {
            mToast.setText(str);
            mToast.show();
        }
        private VerifierListener mVerifyListener = new VerifierListener() {

            @Override
            public void onVolumeChanged(int volume, byte[] data) {
                showTip("当前正在说话，音量大小：" + volume);
                Log.d(TAG, "返回音频数据：" + data.length);
            }

            @Override
            public void onResult(VerifierResult result) {
                mErrorResult.setText("签到完成");

                if (result.ret == 0) {
                    // 验证通过 这里就意味着通过了！！！
                    mResultText.setText("签到验证通过");
                } else {
                    // 验证不通过
                    switch (result.err) {
                        case VerifierResult.MSS_ERROR_IVP_GENERAL:
                            mErrorResult.setText("内核异常");
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
                            mErrorResult.setText("验证不通过，您所读的文本不一致");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                            mErrorResult.setText("音量太低");
                            break;
                        case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                            mErrorResult.setText("音频长达不到自由说的要求");
                            break;
                        default:
                            mErrorResult.setText("验证不通过,相似度:" + result.score + "%。");
                            break;
                    }
                }
            }


            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
            }

            @Override
            public void onError(SpeechError error) {

                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_NOT_FOUND:
                        mResultText.setText("模型不存在，请先注册");
                        break;
                    default:
                        showTip("onError Code：" + error.getPlainDescription(true));
                        break;
                }
            }

            @Override
            public void onEndOfSpeech() {
                // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
                showTip("结束说话");
            }

            @Override
            public void onBeginOfSpeech() {
                // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
                showTip("开始说话");
            }
        };
    }

