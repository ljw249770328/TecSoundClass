package com.example.administrator.tecsoundclass.Activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.tecsoundclass.Adapter.KeyboardAdapter;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.InteractHandler;
import com.example.administrator.tecsoundclass.utils.ActivityCollector;
import com.example.administrator.tecsoundclass.utils.FileUploadUtil;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;
import com.example.administrator.tecsoundclass.utils.WebSocketClientObject;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.HashMap;
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
    private static Notification notification;
    private Gson gson=new Gson();
    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 7:
                    Log.d("Test","Here");
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    break;
            }
            return false;
        }
    });

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
            statusBarView.setBackgroundResource(R.drawable.bg_toolbar);
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


    class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action =intent.getAction();
            AlertDialog.Builder builder;
            switch (action){
                case "com.example.administrator.tecsoundclass.FORCE_OFFLINE":
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
                                    //向服务器请求
                                    Map<String,String>param =new HashMap<>();
                                    param.put("condition","NoReply");
                                    param.put("question",mTvQuestion.getText().toString());
                                    param.put("Cid",Clsid);
                                    try {
                                        WebSocketClientObject.getClient(context,mHandler,null)
                                                .send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
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
                            WebSocketClientObject.getClient(context,mHandler,null)
                                    .send(URLEncoder.encode(gson.toJson(param),"UTF-8"));
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
                    break;
                case "com.example.administrator.tecsoundclass.ON_GRADE":
                    final HashMap <String,String> param = (HashMap<String, String>) intent.getSerializableExtra("params");

                    builder = new AlertDialog.Builder(context);
                    View view1 = LayoutInflater.from(context).inflate(R.layout.layout_grade_dialog, null);
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
                                        Toast.makeText(context, "您还未评分", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
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
                                            WebSocketClientObject.getClient(context,mHandler,null)
                                                    .send(URLEncoder.encode(gson.toJson(socketparams),"UTF-8"));
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
                    break;
                case "com.example.administrator.tecsoundclass.GRADE_ED":
                    Toast.makeText(context,"教师评分"+intent.getStringExtra("grade"),Toast.LENGTH_SHORT).show();
                    break;
                case "com.example.administrator.tecsoundclass.DIALOG_CANCEL":
                    if(dialog!=null){
                        dialog.dismiss();
                        timer.cancel();
                        task.cancel();
                    }
                    break;
                case "com.example.administrator.tecsoundclass.PICKED":
                    final String Cls=intent.getStringExtra("Cid");
                    builder = new AlertDialog.Builder(context);
                    view = LayoutInflater.from(context).inflate(R.layout.layout_raceresp_dialog, null);
                    i = 10;
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
                    break;
                case "com.example.administrator.tecsoundclass.COME_CHAT":

                    break;
            }
        }
    }

}