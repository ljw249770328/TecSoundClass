package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.Activity.CourseMenuActivity;
import com.example.administrator.tecsoundclass.Adapter.KeyboardAdapter;

import com.example.administrator.tecsoundclass.Adapter.MyInteractAdapter;
import com.example.administrator.tecsoundclass.JavaBean.Interaction;

import com.example.administrator.tecsoundclass.R;
import com.example.administrator.tecsoundclass.iFlytec.InteractHandler;
import com.example.administrator.tecsoundclass.utils.VoiceManager;
import com.example.administrator.tecsoundclass.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class InteractFragment extends Fragment {
    private ImageView mIvBack;
    private RecyclerView mRvInteract;
    private TextView mTvTime, mTvgrade;
    private Button mBtncatch;
    private AlertDialog dialog;
    private Timer timer = null;
    private TimerTask task = null;
    private int i;
    private String mAuthId = "";
    private AlertDialog GradeDialog;
    private String grades = "", date = "";
    private Onclick onclick = new Onclick();
    private InteractHandler interactHandler;
    private MyInteractAdapter adapter;
    private List<Interaction> mInteractionList = new ArrayList<>();
    private List<Interaction> list;
    private PopupWindow mpop;
    CourseMenuActivity mActivity;

    public InteractFragment() {
    }

    public static Fragment newInstance() {
        Fragment fragment = new InteractFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthId = getActivity().getIntent().getExtras().getString("StudentId");
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interact, container, false);
    }
    private void init(View view){
        mIvBack = view.findViewById(R.id.im_back);
        mRvInteract = view.findViewById(R.id.recycler_view_interact);
    }
    private void SetListener(View view){
        mIvBack.setOnClickListener(onclick);
        view.findViewById(R.id.btn_race_resp).setOnClickListener(onclick);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        SetListener(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvInteract.setLayoutManager(layoutManager);
    }

    private List<Interaction> InitList() {
        list =new ArrayList<>();
        String url ="http://101.132.71.111:8080/TecSoundWebApp/GetInteractListServlet";
        Map <String,String> params=new HashMap<>();
        params.put("course_id",mActivity.getmCourse().getCourse_id());
        VolleyCallback.getJSONObject(getActivity().getApplicationContext(), "GetInteractList", url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                try {
                    JSONArray interacts=r.getJSONArray("Interacts");

                    for (int i=0;i<interacts.length();i++){
                        JSONObject interact= (JSONObject) interacts.get(i);
                        Interaction interaction =new Interaction();
                        interaction.setProblem_id(interact.getString("problem_id"));
                        interaction.setPropose_course_id(interact.getString("propose_course_id"));
                        interaction.setAnswer_user_id(interact.getString("answer_user_id"));
                        interaction.setAnswer_content(interact.getString("answer_content"));
                        interaction.setAnswer_content_src(interact.getString("answer_content_src"));
                        interaction.setAnswer_time(interact.getString("answer_time"));
                        interaction.setAnswer_grade(interact.getString("answer_grade"));
                        list.add(interaction);
                    }
                    mInteractionList.clear();
                    mInteractionList.addAll(list);
                    adapter = new MyInteractAdapter(mInteractionList);
                    setPopupWindow(adapter);
                    adapter.notifyDataSetChanged();
                    mRvInteract.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return list;
    }

    private void  setPopupWindow(MyInteractAdapter adapter){
        adapter.SetOnItemLongClickListener(new MyInteractAdapter.OnInteractItemLongClickListener() {
            @Override
            public void onItemLongClick(int pos, List<Interaction> interactionList) {
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
                RecyclerView.ViewHolder holder= mRvInteract.findViewHolderForAdapterPosition(pos);
                MyInteractAdapter.InteractItemViewHolder viewHolder = (MyInteractAdapter.InteractItemViewHolder) holder;
                int [] location =new int [2];
                View v =viewHolder.getInteractview();
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
                case R.id.tv_copy:
                    break;
                case R.id.tv_delete:
                    break;
                case R.id.tv_share:
                    break;
                case R.id.btn_race_resp:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_raceresp_dialog, null);
                    i = 10;
                    mBtncatch = view.findViewById(R.id.tv_getchance);
                    mTvTime = view.findViewById(R.id.tv_message);
                    mBtncatch.setOnClickListener(onclick);
                    mTvTime.setText(i + "");//这里不加""就会崩溃暂未找到原因怀疑是可能怕变量i不存在导致赋了空值
                    builder.setView(view);
                    dialog = builder.show();
                    StartTime();
                    break;
                case R.id.tv_getchance:
                    StopTime();
                    mTvTime.setText("抢到机会,点击开始回答");
                    mBtncatch.setText("开始");
                    mBtncatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interactHandler = new InteractHandler(getActivity(), mTvTime, mBtncatch);
                            interactHandler.StartHandle(mActivity.getmUser().getUser_id()+ "_" +mActivity.getmCourse().getCourse_id()+"_"+UUID.randomUUID().toString());
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (mBtncatch.getText().equals("答题完成")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_grade_dialog, null);
                                mTvgrade = view1.findViewById(R.id.tv_grade);
                                GridView mGvKeyboard = view1.findViewById(R.id.gv_keyboard);
                                mGvKeyboard.setAdapter(new KeyboardAdapter(getActivity()));
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
                                                    Toast.makeText(getActivity(), "您还未评分", Toast.LENGTH_SHORT).show();
                                                    break;
                                                } else {
                                                    GradeDialog.dismiss();
                                                    break;
                                                }
                                            default:
                                                break;
                                        }
                                        if (!mTvgrade.getText().toString().isEmpty()) {
                                            if (Integer.parseInt((String) mTvgrade.getText()) > 100) {
                                                mTvgrade.setText("100");
                                                grades = "100";
                                            }
                                        }
                                    }
                                });
                                GradeDialog = builder.setView(view1).create();
                                GradeDialog.setCancelable(false);
                                //弹窗消失,教师评分完成存入数据库并显示在recycview中
                                GradeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //上传音频
                                        String FileURL =VoiceManager.UploadFile(mActivity.getApplicationContext(),"InteractVoice",interactHandler.getMfilepath(),interactHandler.getMfilename(),"sound");
                                        //写入数据
                                        String url = "http://101.132.71.111:8080/TecSoundWebApp/AddInteractServlet";
                                        Map<String,String> params =new HashMap<>();
                                        params.put("propose_course_id",mActivity.getmCourse().getCourse_id());
                                        params.put("answer_user_id",mActivity.getmUser().getUser_id());
                                        params.put("answer_content",mTvTime.getText().toString());
                                        params.put("answer_content_src",FileURL);
                                        params.put("answer_grade",mTvgrade.getText().toString());
                                        VolleyCallback.getJSONObject(mActivity.getApplicationContext(), "insertInteract", url, params, new VolleyCallback.VolleyJsonCallback() {
                                            @Override
                                            public void onFinish(JSONObject r) {
                                                String result = null;
                                                try {
                                                    result = r.getString("Result");
                                                    Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                                                    InitList();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                });
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
            if (i == 0) {
                dialog.cancel();
            } else {
                StartTime();
            }

        }

        ;
    };
    public void StartTime() {
        timer = new Timer();
        task = new TimerTask() {
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

    public void StopTime() {
        timer.cancel();
    }
}
