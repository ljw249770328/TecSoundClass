package com.example.administrator.tecsoundclass.utils.baidu;

import android.util.Log;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONException;
import org.json.JSONObject;

public class BaiduApi {
    //设置APPID/AK/SK
    private static final String APP_ID = "18219016";
    private static final String API_KEY = "EvgEPlKetNCrM4iGstY15XfR";
    private static final String SECRET_KEY = "wMl7EX6BiCTdx5dQdjowLlpemhXMs95u";
    public static AipNlp client;

    public BaiduApi(){
        if (client==null){
            client=new AipNlp(APP_ID,API_KEY,SECRET_KEY);
        }
    }

    public static void TopicAnalysis(final String topic,final String content, final AiTextCallBack callBack){
        if (client==null){
            client=new AipNlp(APP_ID,API_KEY,SECRET_KEY);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject res = client.topic(topic,content, null);
                try {
                    Log.e("TA",res.toString(2));
                    callBack.TACallBack(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public static void SentimentAnalysis(final String text, final AiTextCallBack callBack){
        if (client==null){
            client=new AipNlp(APP_ID,API_KEY,SECRET_KEY);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
//                JSONObject res = client.lexer(text1, null);
                JSONObject res = client.sentimentClassify(text, null);

                try {
                    Log.e("STA",res.toString(2));
                    callBack.SACallBack(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public interface AiTextCallBack{
        public void TACallBack(JSONObject r);
        public void SACallBack(JSONObject r);
    }

}
//        final String text1 = "安卓（Android）是一种基于Linux的自由及开放源代码的操作系统。主要使用于移动设备，如智能手机和平板电脑，由Google公司和开放手机联盟领导及开发。Android操作系统最初由Andy Rubin开发，主要支持手机。2005年8月由Google收购注资。2007年11月，Google与84家硬件制造商、软件开发商及电信营运商组建开放手机联盟共同研发改良Android系统。随后Google以Apache开源许可证的授权方式，发布了Android的源代码。第一部Android智能手机发布于2008年10月。Android逐渐扩展到平板电脑及其他领域上，如电视、数码相机、游戏机、智能手表等。2011年第一季度，Android在全球的市场份额首次超过塞班系统，跃居全球第一。 2013年的第四季度，Android平台手机的全球市场份额已经达到78.1%。2013年09月24日谷歌开发的操作系统Android在迎来了5岁生日，全世界采用这款系统的设备数量已经达到10亿台。";
