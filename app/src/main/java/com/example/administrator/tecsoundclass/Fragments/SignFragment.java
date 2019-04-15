package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Adapter.MySignListAdapter;
import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.JavaBean.Sign;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.RegeditVoiceActivity;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignFragment extends Fragment {
    private ImageView mIvBack;
    private RecyclerView mRvSign;
    private TextView mTvSign;
    private AlertDialog mSignDialog;
    private SpeakerVerifier mSpeakerVerifier;
    private Toast mToast;
    private String mAuthId = "", mTime = "", mDate = "", mStatus = "";
    private String mTextPwd = "";
    private final String TAG = CourseMenuActivity.class.getSimpleName();
    private static final int PWD_TYPE_TEXT = 1;
    private TextView mResultText;
    private Button mErrorResult;
    private String[] items;
    private Bundle bundle;
    private MySignListAdapter adapter;
    private List<Sign> signList = new ArrayList<>();
    private List<Sign> list;
    private PopupWindow mpop;
    private Onclick onclick = new Onclick();
    CourseMenuActivity mActivity;

    public SignFragment() {

    }

    public static Fragment newInstance() {
        Fragment fragment = new SignFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        InitList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (CourseMenuActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        return view;
    }

    private void init(View view) {
        mIvBack = view.findViewById(R.id.im_back);
        mTvSign = view.findViewById(R.id.tv_start_sign);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        bundle = getActivity().getIntent().getExtras();
        mAuthId = bundle.getString("StudentId");
        mRvSign = view.findViewById(R.id.recycler_view_sign);
    }

    private void SetClickLIstener() {
        mIvBack.setOnClickListener(onclick);
        mTvSign.setOnClickListener(onclick);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        SetClickLIstener();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvSign.setLayoutManager(layoutManager);

    }

    //从数据库中获得显示的数据
    private List<Sign> InitList() {
        list = new ArrayList<>();
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetSignListServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", mActivity.getmUser().getUser_id());
        params.put("course_id", mActivity.getmCourse().getCourse_id());
        VolleyCallback.getJSONObject(mActivity.getApplicationContext(), "GetSignList", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray signs = r.getJSONArray("Signs");

                    for (int i = 0; i < signs.length(); i++) {
                        JSONObject obj = (JSONObject) signs.get(i);
                        Sign sign = new Sign();
                        sign.setSign_user_id(obj.getString("sign_user_id"));
                        sign.setSign_course(obj.getString("sign_course"));
                        sign.setSign_adress(obj.getString("sign_address"));
                        sign.setSign_time(obj.getString("sign_time"));
                        sign.setSign_state(obj.getString("sign_state"));
                        sign.setSign_voice_src(obj.getString("sign_voice_src"));
                        sign.setSign_pacepic_src(obj.getString("sign_facepic_src"));
                        list.add(sign);
                    }
                    signList.clear();
                    signList.addAll(list);
                    adapter = new MySignListAdapter(signList);
                    setPopupWindow(adapter);
                    adapter.notifyDataSetChanged();
                    mRvSign.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }

    private void setPopupWindow (MySignListAdapter adapter){
        //设置长按显示item操作菜单
        adapter.setOnItemLongClickListener(new MySignListAdapter.OnSignItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, List<Sign> SignList) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.layout_sign_popupwindow,null);
                TextView mTvPopcopy =view.findViewById(R.id.tv_copy);
                TextView mTbPopdelete =view.findViewById(R.id.tv_delete);
                TextView mTvPopshare =view.findViewById(R.id.tv_share);
                //设置选项事件
                mTvPopcopy.setOnClickListener(onclick);
                mTbPopdelete.setOnClickListener(onclick);
                mTvPopshare.setOnClickListener(onclick);
                //弹出窗口
                mpop=new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mpop.setOutsideTouchable(true);
                mpop.setFocusable(true);
                view .measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                RecyclerView.ViewHolder holder= mRvSign.findViewHolderForAdapterPosition(position);
                MySignListAdapter.SignItemViewHolder viewHolder = (MySignListAdapter.SignItemViewHolder) holder;
                int [] location =new int [2];
                View v =viewHolder.getmItemView();
                v.getLocationOnScreen(location);
                mpop.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - view.getMeasuredWidth() / 2, location[1] - v.getMeasuredHeight()+50);
            }
        });
    }
    private class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.tv_start_sign:
                    //初始化声纹识别引擎
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
                    //点击签到按钮产生签到弹窗
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_dialog, null);
                    mResultText = view.findViewById(R.id.edt_result);
                    mErrorResult = view.findViewById(R.id.error_result);
                    builder.setView(view);
                    mSignDialog = builder.create();
                    mSignDialog.show();
                    //点击按钮开始验证
                    mErrorResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VoiceIdentifier voiceIdentifier = new VoiceIdentifier();
                            voiceIdentifier.PrepareIdentify();
                        }
                    });
                    break;
                case R.id.tv_copy:
                    break;
                case R.id.tv_delete:
                    break;
                case R.id.tv_share:
                    break;
            }
        }
    }

    private class VoiceIdentifier {
        //获取密码
        private void PrepareIdentify() {
            // 清空参数
            mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
            //设置会话场景
            mSpeakerVerifier.setParameter(SpeechConstant.MFV_SCENES, "ivp");
            //设置下载密码类型为文本
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_TEXT);
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
                        //JSONBOJECT.optjsonarray()方法比与getjsonarray()方法类似,前者不需要trycatch
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
                    //在这里调用开始验证可以解决业务类型为验证时由于mTextPwd未获取到导致传参错误的问题
                    StartIdentify();
                }
            };
            mSpeakerVerifier.getPasswordList(mPwdListenter);
        }

        private void StartIdentify() {
            //验证
            mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
            mSpeakerVerifier = SpeakerVerifier.getVerifier();
            // 设置业务类型为验证
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
            // 消噪
            //mSpeakerVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
            //设置识别类型
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
            mResultText.setText("请读出：" + mTextPwd);
            mErrorResult.setText("识别中...");
            // 设置auth_id
            mSpeakerVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_TEXT);
            // 开始验证
            if (mTextPwd == "") {
                showTip("mText未下载");
            } else {
                mSpeakerVerifier.startListening(mVerifyListener);
            }

        }

    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    //验证监听
    private VerifierListener mVerifyListener = new VerifierListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onResult(final VerifierResult result) {

            if (result.ret == 0) {
                // 验证通过
                mResultText.setText("签到验证通过");
                mErrorResult.setText("签到完成");
                mStatus = "成功";
                //签到完成存入数据库
                String url = "http://101.132.71.111:8080/TecSoundWebApp/SignRecordServlet";
                Map<String, String> params = new HashMap<>();
                params.put("sign_user_id", mActivity.getmUser().getUser_id());
                params.put("sign_course", mActivity.getmCourse().getCourse_id());
                params.put("sign_address","");
                params.put("sign_state",mStatus);
                params.put("sign_voice_src","");
                params.put("sign_facepic_src","");
                VolleyCallback.getJSONObject(mActivity.getApplicationContext(), "Sign", url, params, new VolleyCallback.VolleyJsonCallback() {
                    @Override
                    public void onFinish(JSONObject r) {
                        try {
                            String Result=r.getString("Result");
                            Log.d("ERROR",Result);
                            Toast.makeText(getActivity(),Result,Toast.LENGTH_SHORT).show();
                            mErrorResult.setClickable(false);
                            //签到弹窗消失后刷新RecyclerView
                            mSignDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    InitList();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

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
                    mErrorResult.setText("注册");
                    mErrorResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), RegeditVoiceActivity.class);
                            intent.putExtras(bundle);
                            mSignDialog.dismiss();
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    showTip("错误码：" + error.getPlainDescription(true));
                    break;
            }
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
            mErrorResult.setText("签到");
        }

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");
        }
    };
}

