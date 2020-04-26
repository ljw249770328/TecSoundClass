package com.example.administrator.tecsoundclass.View.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.View.Adapter.KeyboardAdapter;
import com.example.administrator.tecsoundclass.View.Adapter.OnLineStuAdapter;
import com.example.administrator.tecsoundclass.model.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.model.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.View.CustomScoreBar;
import com.example.administrator.tecsoundclass.utils.Tencent.TencentApi;
import com.example.administrator.tecsoundclass.utils.iFlytec.InteractHandler;
import com.example.administrator.tecsoundclass.Controller.service.BackService;
import com.example.administrator.tecsoundclass.utils.ActivityCollector;
import com.example.administrator.tecsoundclass.utils.ToastUtils;
import com.example.administrator.tecsoundclass.utils.TransferMore;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BaseActivity extends AppCompatActivity {

    private Receiver receiver;
    private Timer timer = null;
    private TimerTask task = null;
    private  AlertDialog dialog;
    private String grades="";
    private String question="";
    private int i;
    private View view,statusBarView;
    private  TextView mTvgrade;
    private RemoteViews mRview,mHeadView;;
    private static Notification notification;
    private static NotificationCompat.Builder mNfBuilder,mNfHeadBuilder;
    private static NotificationManager manager;
    private SharedPreferences myPref;
    public BackService.mBinder binder;
    private Gson gson=new Gson();

    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){

                case 0:
                    unbindService(connection);
                    ToastUtils.ShowMyToasts(BaseActivity.this,msg.obj.toString(),Gravity.CENTER);
                    break;
                case 1:
                    List<String> OlStu= new ArrayList<>();
                    Log.d("Testnow",OlStu.toString());
                    OlStu= (List<String>) msg.obj;
                    View view=dialog.getWindow().getDecorView();
                    RecyclerView mRvOnlineStu=view.findViewById(R.id.rv_connect_list);
                    LinearLayout mLlSelect =view.findViewById(R.id.ll_select);
                    final TextView mTvQuestion =view.findViewById(R.id.tv_question);
                    final TextView mTvCid=view.findViewById(R.id.tv_title);
                    mRvOnlineStu.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                    final OnLineStuAdapter adapter=new OnLineStuAdapter(OlStu,BaseActivity.this);
                    adapter.notifyDataSetChanged();
                    adapter.SetOnItemClickListener(new OnLineStuAdapter.OnRecyclerItemClickListener() {
                        @Override
                        public void onItemClick(int posision) {
                            unbindService(connection);
                            Map<String,String> param =new HashMap<>();
                            param.put("condition","TeaSelect");
                            param.put("question",mTvQuestion.getText().toString());
                            param.put("Cid",mTvCid.getText().toString().substring(0,10));
                            param.put("Cid",mTvCid.getText().toString().substring(0,10));
                            param.put("Sid",adapter.getmResList().get(posision));
                            try {
                                MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    mRvOnlineStu.setAdapter(adapter);
                    mLlSelect.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    break;
            }
            return false;
        }
    });


    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder= (BackService.mBinder) service;
            binder.onServiceMessage(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        statusBarView=getWindow().getDecorView().findViewById(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


    }
    private void initStatusBar() {
        if (statusBarView == null) {
            //利用反射机制修改状态栏背景
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);
        }
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(R.drawable.bg_status_bar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("com.example.administrator.tecsoundclass.FORCE_OFFLINE");
        intentFilter.addAction("com.example.administrator.tecsoundclass.COME_MESSAGE");
        intentFilter.addAction("com.example.administrator.tecsoundclass.ON_GRADE");
        intentFilter.addAction("com.example.administrator.tecsoundclass.GRADE_ED");
        intentFilter.addAction("com.example.administrator.tecsoundclass.DIALOG_CANCEL");
        intentFilter.addAction("com.example.administrator.tecsoundclass.PICKED");
        intentFilter.addAction("com.example.administrator.tecsoundclass.COME_CHAT");
        receiver=new Receiver();
        registerReceiver(receiver,intentFilter);

        //设置状态栏颜;
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initStatusBar();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }




    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action =intent.getAction();
            final AlertDialog.Builder builder;
            switch (action){
                case "com.example.administrator.tecsoundclass.FORCE_OFFLINE":
                    stopService(new Intent(BaseActivity.this,BackService.class));
                    SharedPreferences.Editor myEditor=getSharedPreferences("admin",Context.MODE_PRIVATE).edit();
                    myEditor.clear();
                    myEditor.commit();
                    builder =new AlertDialog.Builder(context);
                    builder.setTitle("下线通知");
                    builder.setMessage("与服务器断开连接");
                    builder.setCancelable(false);
                    builder.setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCollector.finishAll();
                            Intent intent=new Intent(context,LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    builder.show();
                    break;
                case "com.example.administrator.tecsoundclass.COME_MESSAGE":
                    myPref=getSharedPreferences("admin",MODE_PRIVATE);
                    if(myPref.getString("identity","").equals("学生")){
                        final String Clsid=intent.getStringExtra("Cid");
                        builder = new AlertDialog.Builder(context);
                        view = LayoutInflater.from(context).inflate(R.layout.layout_raceresp_dialog, null);
                        i = 10;
                        final Button mBtncatch = view.findViewById(R.id.tv_getchance);
                        final TextView mTvQuestion=view .findViewById(R.id.tv_question);
                        final TextView mTvTime = view.findViewById(R.id.tv_message);
                        TextView mTvTitle=view.findViewById(R.id.tv_title);
                        mTvQuestion.setText(intent.getStringExtra("question"));
                        mTvTitle.setText("来自"+Clsid+"课堂");
                        mTvTime.setText(i + "");//这里不加""就会崩溃暂未找到原因怀疑是可能变量i不存在时导致赋了空值
                        builder.setView(view);
                        dialog = builder.show();
                        dialog.setCancelable(false);
                        class Timediscounter{
                            private Handler TimeHandler = new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message msg) {
                                    mTvTime.setText(msg.arg1 + "");
                                    if (i == 0) {
                                        dialog.cancel();
//                                        //向服务器请求
//                                        Map<String,String>param =new HashMap<>();
//                                        param.put("condition","NoReply");
//                                        param.put("question",mTvQuestion.getText().toString());
//                                        param.put("Cid",Clsid);
//                                        try {
//                                            WebSocketClientObject.getClient(context,mHandler,null)
//                                                    .send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
//                                        } catch (UnsupportedEncodingException e) {
//                                            e.printStackTrace();
//                                        }
                                    } else {
                                        StartTime();
                                    }
                                    return false;
                                }
                            });
                            public  void StartTime() {
                                timer = new Timer();
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (i > 0) {
                                            i--;
                                            Message message = TimeHandler.obtainMessage();//获取实例
                                            message.arg1 = i;
                                            TimeHandler.sendMessage(message);
                                        }
                                    }
                                };
                                timer.schedule(task, 1000);
                            }

                            public void StopTime() {
                                timer.cancel();
                                task.cancel();
//
                            }
                        }
                        //学生端开始倒计时
                        new Timediscounter().StartTime();
                        mBtncatch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Timediscounter().StopTime();
                                mTvTime.setText("抢到机会,点击开始回答");
                                mBtncatch.setText("开始");
                                //取消其他人聊天框
                                Map<String,String> param =new HashMap<>();
                                param.put("condition","Caughted");
                                param.put("Cid",Clsid);
                                try {
//                                    WebSocketClientObject.getClient(context,mHandler,null)
//                                            .send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                    MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                mBtncatch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        InteractHandler interactHandler = new InteractHandler(context, mTvTime,mTvQuestion.getText().toString(),Clsid, mBtncatch,mHandler);
                                        interactHandler.StartHandle(UUID.randomUUID().toString());
                                        dialog.setCancelable(true);
                                    }
                                });
                            }
                        });
                    }else{
                        final String Clsid=intent.getStringExtra("Cid");
                        builder = new AlertDialog.Builder(context);
                        view = LayoutInflater.from(context).inflate(R.layout.layout_quesing_dialog, null);
                        i = 10;
                        final Button mBtnselect = view.findViewById(R.id.btn_select);
                        final Button mBtncancel = view.findViewById(R.id.btn_cancel);
                        final TextView mTvQuestion=view .findViewById(R.id.tv_question);
                        final TextView mTvTime = view.findViewById(R.id.tv_message);
                        ImageView mIvClose =view.findViewById(R.id.iv_close);
                        mIvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view.findViewById(R.id.ll_select).setVisibility(View.GONE);
                            }
                        });
                        mBtncancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                timer.cancel();
                                task.cancel();
                                Map<String,String> param =new HashMap<>();
                                param.put("condition","InteractCancel");
                                param.put("Cid",Clsid);
                                try {
                                    MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mBtnselect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String,String> param =new HashMap<>();
                                param.put("condition","GetStuList");
                                param.put("Cid",Clsid);
                                try {
                                    MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                    Intent intent=new Intent(BaseActivity.this,BackService.class);
                                    bindService(intent,connection,Context.BIND_AUTO_CREATE);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        TextView mTvTitle=view.findViewById(R.id.tv_title);
                        mTvQuestion.setText(intent.getStringExtra("question"));
                        mTvTitle.setText(Clsid+"\n当前问题");
                        mTvTime.setText(i + "");//这里不加""就会崩溃暂未找到原因怀疑是可能变量i不存在时导致赋了空值
                        builder.setView(view);
                        dialog = builder.show();
                        dialog.setCancelable(false);
                        class Timediscounter{
                            private Handler TimeHandler = new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message msg) {
                                    mTvTime.setText(msg.arg1 + "");
                                    if (i == 0) {
                                        dialog.cancel();
//                                        //向服务器请求
                                        Map<String,String>param =new HashMap<>();
                                        param.put("condition","NoReply");
                                        param.put("question",mTvQuestion.getText().toString());
                                        param.put("Cid",Clsid);
                                        try {
//                                            WebSocketClientObject.getClient(context,mHandler,null)
//                                                    .send(URLEncoder.encode(gson.toJson(param),"UTF-8"));

                                            MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        StartTime();
                                    }
                                    return false;
                                }
                            });
                            public  void StartTime() {
                                timer = new Timer();
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (i > 0) {
                                            i--;
                                            Message message = TimeHandler.obtainMessage();//获取实例
                                            message.arg1 = i;
                                            TimeHandler.sendMessage(message);
                                        }
                                    }
                                };
                                timer.schedule(task, 1000);
                            }

                            public void StopTime() {
                                timer.cancel();
                                task.cancel();
//
                            }
                        }
                        //开始倒计时
                        new Timediscounter().StartTime();
                    }
                    break;
                case "com.example.administrator.tecsoundclass.ON_GRADE":
                    final HashMap <String,String> param = (HashMap<String, String>) intent.getSerializableExtra("params");
                    final String Ques=param.get("question");
                    final String Answ=param.get("answer");
                    builder = new AlertDialog.Builder(context);
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    TencentApi.SentimentAnalyst(Answ, new TencentApi.TApiCallback() {
                        @Override
                        public void ResultCallback(String resp) {
                            Looper.prepare();
                            View view1 = LayoutInflater.from(context).inflate(R.layout.layout_grade_dialog, null);
                            CustomScoreBar scoreBar=view1.findViewById(R.id.scrollBar);
                            TextView mTvQuestion=view1.findViewById(R.id.tv_question);
                            TextView mTvAnswer=view1.findViewById(R.id.tv_answer);
                            TextView mTvSentiment=view1.findViewById(R.id.tv_sentiment);
                            com.alibaba.fastjson.JSONObject object= JSON.parseObject(resp);
                            Log.e("BaseOnGrade",object.toString());
                            float negative=  Float.valueOf(object.get("Negative").toString());
                            float positive= Float.valueOf(object.get("Positive").toString());
                            mTvQuestion.setText(Ques);
                            mTvAnswer.setText(Answ);
                            mTvSentiment.setText(object.get("Sentiment").toString());
                            scoreBar.setScores(Math.round(negative*100),Math.round(positive*100));
                            mTvgrade = view1.findViewById(R.id.tv_grade);
                            GridView mGvKeyboard = view1.findViewById(R.id.gv_keyboard);
                            mGvKeyboard.setAdapter(new KeyboardAdapter(context));
                            mGvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    switch (position) {
                                        case 0:
                                            grades += "1";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 1:
                                            grades += "2";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 2:
                                            grades += "3";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 3:
                                            grades += "4";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 4:
                                            grades += "5";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 5:
                                            grades += "6";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 6:
                                            grades += "7";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 7:
                                            grades += "8";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 8:
                                            grades += "9";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 9:
                                            grades = "";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 10:
                                            grades += "0";
                                            mTvgrade.setText(grades);
                                            break;
                                        case 11:
                                            if (mTvgrade.getText().toString().isEmpty()) {
                                                ToastUtils.ShowMyToasts(context, "您还未评分", Gravity.CENTER);
                                                break;
                                            } else {
                                                grades="";
                                                dialog.dismiss();
                                                //存储
                                                //写入数据
                                                String url = "http://101.132.71.111:8080/TecSoundWebApp/AddInteractServlet";
                                                Map<String,String> params =new HashMap<>();
                                                params.put("propose_course_id",param.get("Cid"));
                                                params.put("answer_user_id",param.get("Sid"));
                                                params.put("answer_content",param.get("answer"));
                                                params.put("answer_content_src",param.get("VoiceURL"));
                                                params.put("problem_content",param.get("question"));
                                                params.put("answer_grade",mTvgrade.getText().toString());
                                                VolleyCallback.getJSONObject(context, "insertInteract", url, params, new VolleyCallback.VolleyJsonCallback() {
                                                    @Override
                                                    public void onFinish(JSONObject r) {
                                                        String result = null;
                                                        try {
                                                            result = r.getString("Result");
                                                            ToastUtils.ShowMyToasts(context,result,Gravity.CENTER);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    @Override
                                                    public void onError(VolleyError error) {

                                                    }
                                                });
                                                //通信
                                                Map<String,String> socketparams =new HashMap<>();
                                                socketparams.put("condition","Graded");
                                                socketparams.put("Grade",mTvgrade.getText().toString());;
                                                socketparams.put("Sid",param.get("Sid"));
                                                socketparams.put("Cid",param.get("Cid"));
                                                try {
//                                            WebSocketClientObject.getClient(context,mHandler,null)
//                                                    .send(URLEncoder.encode(gson.toJson(socketparams),"UTF-8"));

                                                    MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            }
                                        default:
                                            break;
                                    }
                                    if (!mTvgrade.getText().toString().isEmpty()) {
                                        if (Integer.parseInt(mTvgrade.getText().toString()) > 100) {
                                            mTvgrade.setText("100");
                                            grades = "100";
                                        }
                                    }
                                }
                            });
                            dialog = builder.setView(view1).create();
                            dialog.setCancelable(false);
                            dialog.show();
                            Looper.loop();
                        }
                    });

                    break;
                case "com.example.administrator.tecsoundclass.GRADE_ED":
                    ToastUtils.ShowMyToasts(context,"教师评分"+intent.getStringExtra("grade"),Gravity.CENTER);
                    break;
                case "com.example.administrator.tecsoundclass.DIALOG_CANCEL":
                    if(dialog!=null&&dialog.isShowing()){
                        timer.cancel();
                        task.cancel();
                        if (myPref.getString("identity","").equals("学生")){
                            dialog.dismiss();
                        }else {
                            View view =dialog.getWindow().getDecorView();
                            TextView mTvMessage=view.findViewById(R.id.tv_message);
                            Button mBtnSelect=view.findViewById(R.id.btn_select);
                            mTvMessage.setText(intent.getStringExtra("Ca_Uid")+"回答");
                            mBtnSelect.setClickable(false);
                            View v =dialog.getWindow().getDecorView();
                            v.findViewById(R.id.ll_select).setVisibility(View.GONE);
                            v.findViewById(R.id.ll_question_btns).setVisibility(View.GONE);
                            v.findViewById(R.id.ll_waiting_view).setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case "com.example.administrator.tecsoundclass.PICKED":
                    if (myPref.getString("identity","").equals("学生")){
                        final String Cls=intent.getStringExtra("Cid");
                        builder = new AlertDialog.Builder(context);
                        view = LayoutInflater.from(context).inflate(R.layout.layout_raceresp_dialog, null);
                        final Button mBtncatch1 = view.findViewById(R.id.tv_getchance);
                        final TextView mTvQuestion1=view .findViewById(R.id.tv_question);
                        final TextView mTvTime1 = view.findViewById(R.id.tv_message);
                        TextView mTvTitle1=view.findViewById(R.id.tv_title);
                        mTvQuestion1.setText(intent.getStringExtra("question"));
                        mTvTitle1.setText("来自"+Cls+"课堂");
                        mTvTime1.setText("你被抽到啦，准备回答问题吧！");
                        mBtncatch1.setText("开始回答");
                        mBtncatch1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InteractHandler interactHandler = new InteractHandler(context, mTvTime1,mTvQuestion1.getText().toString(),Cls, mBtncatch1,mHandler);
                                interactHandler.StartHandle(UUID.randomUUID().toString());
                                dialog.setCancelable(true);
                            }
                        });
                        builder.setView(view);
                        dialog = builder.show();
                        dialog.setCancelable(false);
                    }else {
                        view = LayoutInflater.from(context).inflate(R.layout.layout_quesing_dialog, null);
                        final Button mBtnselect = view.findViewById(R.id.btn_select);
                        final Button mBtncancel = view.findViewById(R.id.btn_cancel);
                        final TextView mTvQuestion=view .findViewById(R.id.tv_question);
                        final TextView mTvTime = view.findViewById(R.id.tv_message);
                        TextView mTvTitle=view.findViewById(R.id.tv_title);
                        mTvQuestion.setText(intent.getStringExtra("question"));
                        mTvTitle.setText("当前问题（抽取）");
                        mTvTime.setText("请"+intent.getStringExtra("CaughtUid")+"回答");
                        mBtnselect.setVisibility(View.GONE);
                        mBtncancel.setVisibility(View.GONE);
                        view.findViewById(R.id.ll_waiting_view).setVisibility(View.VISIBLE);
                        builder = new AlertDialog.Builder(context);
                        builder.setView(view);
                        dialog = builder.show();
                    }

                    break;
                case "com.example.administrator.tecsoundclass.COME_CHAT":
                    String senderId=intent.getStringExtra("sender");
                    TransferMore.GetUserById(getApplicationContext(), senderId, new TransferMore.TransferCallBack() {
                        @TargetApi(Build.VERSION_CODES.O)
                        @Override
                        public void OnGetUserById(User u) {
                            manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mRview=new RemoteViews(context.getPackageName(),R.layout.layout_message_notify);
                            mRview.setTextViewText(R.id.tv_Sender_name,u.getUser_name());
                            mRview.setTextViewText(R.id.tv_sender_Message,intent.getStringExtra("message"));
                            NotificationChannel channel=new NotificationChannel("1","聊天弹窗",NotificationManager.IMPORTANCE_HIGH);
                            manager.createNotificationChannel(channel);
                            Intent intent1=new Intent(getApplicationContext(),ChatActivity.class);
                            Bundle bundle =new Bundle();
                            bundle.putSerializable("FanInfo",u);
                            bundle.putSerializable("MyInfo", MyApplication.getApplication().getmUser());
                            bundle.putString("msg",intent.getStringExtra("message"));
                            intent1.putExtras(bundle);
                            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);
                            mNfBuilder=new NotificationCompat.Builder(context,"1");
                            mNfBuilder.setSmallIcon(R.drawable.logo)
                                    .setTicker("消息通知")
                                    .setCustomContentView(mRview)
                                    .setAutoCancel(true).setContentIntent(pendingIntent);
                            notification=mNfBuilder.build();
                            manager.notify(1,notification);
                        }
                    });
                    break;
            }
        }


    }

}