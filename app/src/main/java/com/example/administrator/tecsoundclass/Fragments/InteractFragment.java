package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.Adapter.MyInteractAdapter;
import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.SpeechHandler;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class InteractFragment extends Fragment {
    private ImageView mIvBack;
    private ListView mLv;
    private TextView mTvTime;
    private Button mTvcatch;
    private AlertDialog dialog;
    private Timer timer=null;
    private TimerTask task=null;
    private int i=5 ;
    private Onclick onclick=new Onclick();

    public InteractFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new InteractFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interact,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mIvBack=view.findViewById(R.id.im_back);

        mIvBack.setOnClickListener(onclick);
        mLv=view.findViewById(R.id.lv_1);
        mLv.setAdapter(new MyInteractAdapter(getActivity()));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        view.findViewById(R.id.btn_race_resp).setOnClickListener(onclick);
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case  R.id.btn_race_resp:
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View view=LayoutInflater.from(getActivity()).inflate(R.layout.layout_raceresp_dialog,null);
                    mTvcatch=view.findViewById(R.id.tv_getchance);
                    mTvTime=view.findViewById(R.id.tv_message);
                    mTvcatch.setOnClickListener(onclick);
                    mTvTime.setText(i+"");//这里不加""就会崩溃暂未找到原因怀疑是可能怕变量i不存在导致赋了空值
                    builder.setView(view);
                    dialog=builder.show();
                    StartTime();
                    break;
                case R.id.tv_getchance:
                    StopTime();
                    mTvTime.setText("抢到机会,点击开始回答");
                    mTvcatch.setText("开始");
                    mTvcatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpeechHandler speechHandler=new SpeechHandler(getActivity(),mTvTime,mTvcatch);
                            speechHandler.StartHandle("test");
                        }
                    });

                    break;
            }
        }
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mTvTime.setText(msg.arg1 + "");
            if (i==0){
                dialog.cancel();
            }else {
                StartTime();
            }

        };
    };
        public void  StartTime(){
            timer=new Timer();
            task=new TimerTask() {
                @Override
                public void run() {
                    if (i > 0) {
                        i--;
                        Message message = mHandler.obtainMessage();//获取实例
                        message.arg1 = i;
                        mHandler.sendMessage(message);
                    }
                }
            };
            timer.schedule(task, 1000);
        }

        public void StopTime(){
            timer.cancel();
        }
}
