package com.example.administrator.tecsoundclass.iFlytec;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class InteractHandler {
    private RecognizerDialog mIatDialog;
    private TextView mTvSpeechResult;
    private String mfilepath,mfilename;
    private RecognizerDialogListener mRListener;
    private String result="";

    public InteractHandler(Context context, TextView tv, final Button button){
        mTvSpeechResult=tv;
        button.setText("录音中");
        mRListener = new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {
                String text = parseIatResult(results.getResultString());
                Log.d("text",text);
                result +=text;
                mTvSpeechResult.setText(result);
                if (isLast) {
                    result = "";
                    button.setText("答题完成");
                    button.setClickable(false);
                }
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        };
        mIatDialog = new RecognizerDialog(context, null);
        mIatDialog.setListener(mRListener);
    }
    public void StartHandle(String filename){
        mfilename=filename;
        mfilepath=Environment.getExternalStorageDirectory() + "/MyApplication/" + filename + ".wav";
        setIatParam(mfilename);
        mIatDialog.show();
    }

    private void setIatParam(String filename) {
        // 清空参数
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "2000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIatDialog.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIatDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, mfilepath);
    }
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
