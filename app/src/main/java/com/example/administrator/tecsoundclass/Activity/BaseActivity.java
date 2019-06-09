package com.example.administrator.tecsoundclass.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.InteractHandler;
import com.example.administrator.tecsoundclass.utils.ActivityCollector;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BaseActivity extends AppCompatActivity {

    private Receiver receiver;
    private Timer timer = null;
    private TimerTask task = null;
    private  AlertDialog dialog;
    private int i;
    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){

            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("com.example.administrator.tecsoundclass.FORCE_OFFLINE");
        intentFilter.addAction("com.example.administrator.tecsoundclass.COME_MESSAGE");
        receiver=new Receiver();
        registerReceiver(receiver,intentFilter);
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
                    View view = LayoutInflater.from(context).inflate(R.layout.layout_raceresp_dialog, null);
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
            }
        }
    }

}