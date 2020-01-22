package com.example.administrator.tecsoundclass.utils.Tencent;

import android.util.Log;

import com.google.gson.Gson;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.KeywordsExtractionRequest;
import com.tencentcloudapi.nlp.v20190408.models.KeywordsExtractionResponse;
import com.tencentcloudapi.nlp.v20190408.models.SentimentAnalysisRequest;
import com.tencentcloudapi.nlp.v20190408.models.SentimentAnalysisResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TencentApi {
    private static final String SecretId= "";
    private static final String SecretKey = "";

    public static void KeywordAnalysis(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Credential cred=new Credential(SecretId,SecretKey);
                    HttpProfile httpProfile =new HttpProfile();
                    httpProfile.setEndpoint("nlp.tencentcloudapi.com");

                    ClientProfile clientProfile=new ClientProfile();
                    clientProfile.setHttpProfile(httpProfile);

                    NlpClient client=new NlpClient(cred,"ap-guangzhou",clientProfile);

                    Gson gson=new Gson();
                    Map<String,String> params=new HashMap<>();
                    params.put("Text",text);
                    KeywordsExtractionRequest req = KeywordsExtractionRequest.fromJsonString(gson.toJson(params), KeywordsExtractionRequest.class);

                    KeywordsExtractionResponse resp = null;

                    resp = client.KeywordsExtraction(req);
                    Log.e("TencentKeywordAnalysis",KeywordsExtractionRequest.toJsonString(resp));
                } catch (TencentCloudSDKException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }

    public static void SentimentAnalyst(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Credential cred=new Credential(SecretId,SecretKey);
                    HttpProfile httpProfile = new HttpProfile();
                    httpProfile.setEndpoint("nlp.tencentcloudapi.com");

                    ClientProfile clientProfile = new ClientProfile();
                    clientProfile.setHttpProfile(httpProfile);

                    NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

                    Gson gson=new Gson();
                    Map<String,String> params=new HashMap<>();
                    params.put("Text",text);
                    SentimentAnalysisRequest req = SentimentAnalysisRequest.fromJsonString(gson.toJson(params), SentimentAnalysisRequest.class);

                    SentimentAnalysisResponse resp = null;

                    resp = client.SentimentAnalysis(req);
                    Log.e("TencentSentimentAnalyst",SentimentAnalysisRequest.toJsonString(resp));
                } catch (TencentCloudSDKException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
}
