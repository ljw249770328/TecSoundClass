package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.example.administrator.tecsoundclass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileUploadUtil {

    private static String encodeBase64File(String filepath){
        String base64 ="";
        InputStream in = null;
        try {
            in = new FileInputStream(filepath);
            byte[] bytes =new  byte[in.available()];
            int length=in.read(bytes);
            base64=Base64.encodeToString(bytes,0,length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  base64;
    }

    public static String UploadFile(final  Context context, String tag, String filepath, String filename, String folder, @Nullable String table, @Nullable String tablekey){
        String base64file=encodeBase64File(filepath);
        String path = filename;
        String url = "http://101.132.71.111:8080/TecSoundWebApp/Base642fileServlet";
        Map<String,String> params =new HashMap<>();
        params.put("file",base64file);
        params.put("kinds",folder);
        params.put("path",path);
        if (!table.equals("")){
            params.put("table",table);
        }
       if (!tablekey.equals("")){
            params.put("tablekey",tablekey);
       }
        VolleyCallback.getJSONObject(context, tag, url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {
                String result = null;
                try {
                    result = r.getString("Result");
                    Log.d("Tag", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return "http://101.132.71.111:8080/"+folder+"/"+filename;
    }

}
