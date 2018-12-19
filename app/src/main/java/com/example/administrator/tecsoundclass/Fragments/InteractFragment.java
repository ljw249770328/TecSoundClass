package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Adapter.KeyboardAdapter;

import com.example.administrator.tecsoundclass.Adapter.MyInteractAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Interaction;

import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.InteractHandler;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InteractFragment extends Fragment {
    private ImageView mIvBack;
    private RecyclerView mRvInteract;
    private TextView mTvTime, mTvgrade;
    private Button mBtncatch;
    private AlertDialog dialog;
    private Timer timer=null;
    private TimerTask task=null;
    private int i ;
    private String mAuthId="";
    private AlertDialog GradeDialog;
    private String grades="",date="";
    private Onclick onclick=new Onclick();
    private InteractHandler interactHandler;
    private MyInteractAdapter adapter;
    private com.example.administrator.tecsoundclass.utils.Timer timer1;
    private List<Interaction> mInteractionList=new ArrayList<>();

    public InteractFragment(){
    }
    public static Fragment newInstance() {
        Fragment fragment = new InteractFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthId=getActivity().getIntent().getExtras().getString("StudentId");
        timer1=new com.example.administrator.tecsoundclass.utils.Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interact,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mIvBack=view.findViewById(R.id.im_back);
        mIvBack.setOnClickListener(onclick);
        mRvInteract=view.findViewById(R.id.recycler_view_interact);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRvInteract.setLayoutManager(layoutManager);
        mInteractionList=InitList();
        adapter=new MyInteractAdapter(mInteractionList);
        mRvInteract.setAdapter(adapter);
        view.findViewById(R.id.btn_race_resp).setOnClickListener(onclick);
    }
    private List<Interaction> InitList(){
        List<Interaction> list=new ArrayList<>();
        list=LitePal.select("answer_grace","answer_time","answer_content_src").where("answer_user_id=?",mAuthId).order("answer_time").find(Interaction.class);
        return list;
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
                    i=10;
                    mBtncatch=view.findViewById(R.id.tv_getchance);
                    mTvTime=view.findViewById(R.id.tv_message);
                    mBtncatch.setOnClickListener(onclick);
                    mTvTime.setText(i+"");//这里不加""就会崩溃暂未找到原因怀疑是可能怕变量i不存在导致赋了空值
                    builder.setView(view);
                    dialog=builder.show();
                    StartTime();
                    break;
                case R.id.tv_getchance:
                    StopTime();
                    mTvTime.setText("抢到机会,点击开始回答");
                    mBtncatch.setText("开始");
                    mBtncatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interactHandler =new InteractHandler(getActivity(),mTvTime,mBtncatch);
                            interactHandler.StartHandle(mAuthId+"_"+timer1.getmDate()+timer1.getmTime());
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (mBtncatch.getText().equals("答题完成")){
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                View view1=LayoutInflater.from(getActivity()).inflate(R.layout.layout_grade_dialog,null);
                                mTvgrade=view1.findViewById(R.id.tv_grade);
                                GridView mGvKeyboard=view1.findViewById(R.id.gv_keyboard);
                                mGvKeyboard.setAdapter(new KeyboardAdapter(getActivity()));
                                mGvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        switch (position){
                                            case 0:
                                                grades+="1";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 1:
                                                grades+="2";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 2:
                                                grades+="3";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 3:
                                                grades+="4";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 4:
                                                grades+="5";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 5:
                                                grades+="6";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 6:
                                                grades+="7";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 7:
                                                grades+="8";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 8:
                                                grades+="9";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 9:
                                                grades="";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 10:
                                                grades+="0";
                                                mTvgrade.setText(grades);
                                                break;
                                            case 11:
                                                GradeDialog.dismiss();
                                                Toast.makeText(getActivity(),"教师评分:"+Integer.parseInt((String) mTvgrade.getText()),Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                                break;
                                        }
                                        if(mTvgrade.getText()!=""){
                                            if(Integer.parseInt((String) mTvgrade.getText())>100){
                                                mTvgrade.setText("100");
                                                grades="100";
                                            }

                                        }
                                        //教师评分完成存入数据库并显示在recycview中
                                        GradeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                Interaction interaction=new Interaction();
                                                interaction.setAnswer_user_id(mAuthId);
                                                interaction.setAnswer_content_src(interactHandler.getMfilepath());
                                                interaction.setAnswer_time(timer1.getmDate());
                                                interaction.setAnswer_grace(Integer.parseInt((String) mTvgrade.getText()));
                                                interaction.save();
                                                //刷新列表
                                                mInteractionList.clear();
                                                mInteractionList.addAll(InitList());
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                                GradeDialog=builder.setView(view1).create();
                                GradeDialog.show();
                            }
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
