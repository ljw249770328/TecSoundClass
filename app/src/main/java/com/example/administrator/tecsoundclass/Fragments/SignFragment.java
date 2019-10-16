package com.example.administrator.tecsoundclass.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.android.volley.VolleyError;
import com.arcsoft.face.FaceEngine;
import com.example.administrator.tecsoundclass.Activity.RecognizeFaceActivity;
import com.example.administrator.tecsoundclass.Adapter.MyReviewListAdapter;
import com.example.administrator.tecsoundclass.Adapter.MySignListAdapter;
import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.Adapter.MySignResultListAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Sign;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.Activity.RegeditVoiceActivity;
import com.example.administrator.tecsoundclass.utils.ConfigUtil;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignFragment extends Fragment {
    public static final int SIGN_STARTED =1;
    public static final int SIGN_STOPPED =3;
    public static final int SUC_SIGN =2;
    public static final int SIGN_DENYED=0;
    public static final int SIGN_ACCESSED=4;
    public static final int SIGN_ED=5;

    private ImageView mIvBack;
    private RecyclerView mRvSign;
    private Button mBtnSign;
    private AlertDialog mSignDialog;
    private AlertDialog mSignResDialog;
    private SpeakerVerifier mSpeakerVerifier;
    private Toast mToast;
    private String mAuthId , mTime = "", mDate = "", mStatus = "";
    private String mNumPwd = "";
    private final String TAG = CourseMenuActivity.class.getSimpleName();
    private static final int PWD_TYPE_NUM = 3;
    private TextView mResultText;
    private TextView mErrorResult;
    private String[] items;
    private MySignListAdapter adapter;
    private List<Sign> signList = new ArrayList<>();
    private List<Sign> list;
    private List<String> signStu= new ArrayList<>();
    private PopupWindow mpop;
    private Onclick onclick = new Onclick();
    private User user;
    private AlertDialog.Builder builder=null;
    private View view=null;
    CourseMenuActivity mActivity;


    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SIGN_STOPPED:
                    signStu= (List<String>) msg.obj;
                    builder=new AlertDialog.Builder(getActivity());
                    view=LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_result_dialog,null);
                    RecyclerView mRvSignRLst=view.findViewById(R.id.rv_sign_res_lst);
                    RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity());
                    mRvSignRLst.setLayoutManager(manager);
                    MySignResultListAdapter adapter=new MySignResultListAdapter(signStu,mActivity.getApplicationContext());
                    mRvSignRLst.setAdapter(adapter);
                    builder.setView(view);
                    mSignResDialog=builder.create();
                    mSignResDialog.show();
                    mSignResDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InitList();
                        }
                    });
                    mBtnSign.setText("开始签到");
                    break;
                case SIGN_STARTED:
                    mBtnSign.setText("结束签到");
                    break;
                case SUC_SIGN:
                    ToastUtils.ShowMyToasts(mActivity.getApplicationContext(),"签到完成，请认真上课",Gravity.CENTER);
                    mBtnSign.setText("已签到");
                    break;
                case SIGN_DENYED:
                    ToastUtils.ShowMyToasts(mActivity.getApplicationContext(),"教师未开放签到通道",Gravity.CENTER);
                    break;
                case SIGN_ED:
                    ToastUtils.ShowMyToasts(mActivity.getApplicationContext(),"您已参与本次签到",Gravity.CENTER);
                    mBtnSign.setText("已签到");
                    break;
                case SIGN_ACCESSED:
                    //进行人脸验证
                    ConfigUtil.setFtOrient(getActivity(), FaceEngine.ASF_OP_0_HIGHER_EXT);
                    startActivityForResult(new Intent(getActivity(), RecognizeFaceActivity.class),1);
                    break;

            }
            return false;
        }
    });
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
        mBtnSign = view.findViewById(R.id.btn_start_sign);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        user=mActivity.getmUser();
        mAuthId = user.getUser_id();
        mRvSign = view.findViewById(R.id.recycler_view_sign);
        if(mActivity.getmUser().getUser_identity().equals("学生")){
            mBtnSign.setText("签到");
        }
    }

    private void SetClickLIstener() {
        mIvBack.setOnClickListener(onclick);
        mBtnSign.setOnClickListener(onclick);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        SetClickLIstener();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvSign.setLayoutManager(layoutManager);
    }

    //刷新显示列表
    private List<Sign> InitList() {
        list = new ArrayList<>();
        String url = "http://101.132.71.111:8080/TecSoundWebApp/GetSignListServlet";
        Map<String, String> params = new HashMap<>();
        params.put("user_id", mActivity.getmUser().getUser_id());
        params.put("course_id", mActivity.getmCourse().getCourse_id());
        params.put("user_identity",mActivity.getmUser().getUser_identity());
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
                    adapter = new MySignListAdapter(mActivity.getApplicationContext(),signList,mActivity.getmUser().getUser_identity());
                    setPopupWindow(adapter);
                    adapter.notifyDataSetChanged();
                    mRvSign.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

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
                if (mActivity.getmUser().getUser_identity().equals("学生")){
                     mTbPopdelete.setVisibility(View.GONE);
                }
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
                case R.id.btn_start_sign:
                    Map<String,String>  msg =new HashMap<>();
                    Gson gson=new Gson();
                    if (mActivity.getmUser().getUser_identity().equals("老师")){

                        if(mBtnSign.getText().toString().equals("开始签到")){
                            msg.put("SignClass",mActivity.getmCourse().getCourse_id());
                            msg.put("condition","SignStart");
                            msg.put("ClsTea",mActivity.getmUser().getUser_id());
                            //开放签到通道
                            try {
                                WebSocketClientObject.getClient(mActivity.getApplicationContext(),mHandler,null)
                                        .send(URLEncoder.encode(gson.toJson(msg),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }else if (mBtnSign.getText().toString().equals("结束签到")){
                            msg.put("StopClass",mActivity.getmCourse().getCourse_id());
                            msg.put("condition","SignStop");
                            msg.put("ClsTea",mActivity.getmUser().getUser_id());
                            //关闭签到通道
                            try {
                                WebSocketClientObject.getClient(mActivity.getApplicationContext(),mHandler,null)
                                        .send(URLEncoder.encode(gson.toJson(msg),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        //检查签到通道开启以及当次签到状态
                        msg.put("SignClass",mActivity.getmCourse().getCourse_id());
                        msg.put("SignStu",mActivity.getmUser().getUser_id());
                        msg.put("condition","CheckSign");
                        try {
                            WebSocketClientObject.getClient(mActivity.getApplicationContext(),mHandler,null)
                                    .send(URLEncoder.encode(gson.toJson(msg),"UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

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
//        private void PrepareIdentify() {
//            // 清空参数
//            mSpeakerVerifier.setParameter(SpeechConstant.PARAMS, null);
//            //设置会话场景
//            mSpeakerVerifier.setParameter(SpeechConstant.MFV_SCENES, "ivp");
//            //设置下载密码类型为数字
//            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
//            SpeechListener mPwdListenter = new SpeechListener() {
//                @Override
//                public void onEvent(int i, Bundle bundle) {
//
//                }
//
//                @Override
//                public void onBufferReceived(byte[] bytes) {
//                    String result = new String(bytes);
//                    try {
//                        JSONObject object = new JSONObject(result);
//                        if (!object.has("txt_pwd")) {
//                            mResultText.setText("");
//                            return;
//                        }
//                        //JSONBOJECT.optjsonarray()方法比与getjsonarray()方法类似,前者不需要trycatch
//                        JSONArray pwdArray = object.optJSONArray("txt_pwd");
//                        items = new String[pwdArray.length()];
//                        for (int i = 0; i < pwdArray.length(); i++) {
//                            items[i] = pwdArray.getString(i);
//                        }
//                        mNumPwd = items[0];
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onCompleted(SpeechError speechError) {
//                    if (null != speechError && ErrorCode.SUCCESS != speechError.getErrorCode()) {
//                        showTip("获取失败：" + speechError.getErrorCode());
//                    }
//                    //在这里调用开始验证可以解决业务类型为验证时由于mTextPwd未获取到导致传参错误的问题
//                    StartIdentify();
//                }
//            };
//            mSpeakerVerifier.getPasswordList(mPwdListenter);
//        }

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
            String verifyPwd = mSpeakerVerifier.generatePassword(8);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
            // 设置auth_id
            mSpeakerVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
            mSpeakerVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + PWD_TYPE_NUM);
            // 开始验证
            if (verifyPwd == "") {
                showTip("mText未下载");
            } else {
                mResultText.setText("请读出：" + verifyPwd);
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

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                //socket对象分组
                Map<String,String> msg =new HashMap<>();
                Gson gson=new Gson();
                msg.put("condition","SignSuccess");
                msg.put("Sid",mActivity.getmUser().getUser_id());
                msg.put("Cid",mActivity.getmCourse().getCourse_id());
                try {
                    WebSocketClientObject.getClient(mActivity.getApplicationContext(),mHandler,null)
                            .send(URLEncoder.encode(gson.toJson(msg),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
                            Bundle bundle =new Bundle();
                            bundle.putSerializable("mUser",user);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==getActivity().RESULT_OK){
                    //进行声纹识别
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
                    //产生声纹签到弹窗
                    builder = new AlertDialog.Builder(getActivity());
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_dialog, null);
                    TextView mTvSigntitle=view.findViewById(R.id.tv_CTitle_from);
                    mTvSigntitle.setText("来自"+mActivity.getmCourse().getCourse_name()+"("+mActivity.getmCourse().getCourse_class()+")");
                    mResultText = view.findViewById(R.id.edt_result);
                    mErrorResult = view.findViewById(R.id.error_result);
                    builder.setView(view);
                    mSignDialog = builder.create();
                    mSignDialog.show();
                    //点击按钮开始验证
                    mErrorResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //声纹
                            VoiceIdentifier voiceIdentifier = new VoiceIdentifier();
                            voiceIdentifier.StartIdentify();
                        }
                    });
                }
        }
    }
}

