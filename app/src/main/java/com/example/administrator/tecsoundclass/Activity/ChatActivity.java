package com.example.administrator.tecsoundclass.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrator.tecsoundclass.Adapter.MsgAdapter;
import com.example.administrator.tecsoundclass.Fragments.SignFragment;
import com.example.administrator.tecsoundclass.JavaBean.Msg;
import com.example.administrator.tecsoundclass.JavaBean.User;
import com.example.administrator.tecsoundclass.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button Send;
    private ImageView back;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
   private TextView friend_name;
    private User mFan;
    private User mMy;


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
        adapter=new MsgAdapter(this,msgList,mFan,mMy);
        msgRecyclerView.setAdapter(adapter);
    }

    private void initMsgs(){
            Msg msg1=new Msg("hello gay.",Msg.TYPE_RECEIVED);
            msgList.add(msg1);
            Msg msg2=new Msg("hello,who is that?",Msg.TYPE_SENT);
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
                        Msg msg=new Msg(content,Msg.TYPE_SENT);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        inputText.setText("");
                    }
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
        initMsgs();

        SetListeners();
        passName();
    }

}
