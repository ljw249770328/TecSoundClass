package com.example.administrator.tecsoundclass.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.View.Adapter.MsgAdapter;
import com.example.administrator.tecsoundclass.model.JavaBean.Msg;
import com.example.administrator.tecsoundclass.model.JavaBean.MyApplication;
import com.example.administrator.tecsoundclass.model.JavaBean.User;
import com.example.administrator.tecsoundclass.R;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button Send;
    private ImageView back;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
   private TextView friend_name;
   private String comeMessage="";
    private User mFan;
    private User mMy;
    private Gson gson;
    private Receiver receiver;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    private void init(){
        inputText=(EditText)findViewById(R.id.input_text);
        Send = (Button)findViewById(R.id.bt_send);
        back = findViewById(R.id.chat_back);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        friend_name=findViewById(R.id.fri_name);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        Bundle bundle=getIntent().getExtras();
        mFan= (User) bundle.getSerializable("FanInfo");
        mMy = (User) bundle.getSerializable("MyInfo");
        if (bundle.containsKey("msg")){
            comeMessage=bundle.getString("msg");
        }
        adapter=new MsgAdapter(this,msgList,mFan,mMy);
        msgRecyclerView.setAdapter(adapter);
        gson=new Gson();
    }

    private void initMsgs(){

        Msg msg1=new Msg("以上是最新一条消息",Msg.TYPE_RECEIVED);
        Msg msg2=new Msg(comeMessage,Msg.TYPE_RECEIVED);
        msgList.clear();
        msgList.add(msg1);
        msgList.add(msg2);
        adapter.notifyItemChanged(msgList.size()-1);
    }

    private void SetListeners(){
        OnClick onClick =new OnClick();
        Send.setOnClickListener(onClick);
        back.setOnClickListener(onClick);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_send:
                    String content=inputText.getText().toString();
                    if(!"".equals(content)){
                        //发送
                        Map<String,String> obj =new HashMap<>();
                        obj.put("condition","ChatWith");
                        obj.put("message",content);
                        obj.put("SendUser",mFan.getUser_id());
                        try {
//                            WebSocketClientObject.getClient(getApplicationContext(),handler,null)
//                                    .send(URLEncoder.encode(gson.toJson(obj),"UTF-8"));
                            MyApplication.getmWebsocket().send(URLEncoder.encode(gson.toJson(obj),"UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        Msg msg=new Msg(content,Msg.TYPE_SENT);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        inputText.setText("");
                    }
                    break;
                case R.id.chat_back:
                    finish();
                    break;
//                case R.id.fri_name:
//                    friend_name.setText(mFan.getUser_name());

            }
        }
    }
    private void passName() {
       friend_name.setText(mFan.getUser_name());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        if (!comeMessage.equals("")){
            initMsgs();
        }

        SetListeners();
        passName();

        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("com.example.administrator.tecsoundclass.COME_CHAT");
        receiver=new Receiver();
        registerReceiver(receiver,intentFilter);
    }
    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "com.example.administrator.tecsoundclass.COME_CHAT":
                    String senderId = intent.getStringExtra("sender");
                    final String message = intent.getStringExtra("message");
                    if(mFan.getUser_id().equals(senderId)){
                        Msg msg1=new Msg(message,Msg.TYPE_RECEIVED);
                        msgList.add(msg1);
                        adapter.notifyItemInserted(msgList.size()-1);
                    }
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
