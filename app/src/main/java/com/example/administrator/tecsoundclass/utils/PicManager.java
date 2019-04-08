package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PicManager {
    private static Bitmap bitmap = null;
    private static ByteArrayOutputStream baos = null;
    private static String result = "";

    public static String bitmapToBase64(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.flush();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    public static String UpLoadPic(final Context context, String tag, String FilePath, String tagid, String kinds) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap = BitmapFactory.decodeStream(fis);

        //转换成Base64字符串编码
        String user_img = bitmapToBase64(bitmap);

        String url = "http://101.132.71.111:8080/TecSoundWebApp/Base642fileServlet";
        String path = tagid + "_" + kinds + ".jpeg";
        Map<String, String> params = new HashMap<>();
        params.put("user_img", user_img);
        params.put("path", "C:\\apache-tomcat-8.0.44\\webapps\\images\\" + path);
        VolleyCallback.getJSONObject(context, tag, url, params, new VolleyCallback.VolleyJsonCallback() {
            @Override
            public void onFinish(JSONObject r) {

                try {
                    String result = r.getString("Result");
                    Log.d("Tag", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return "http://101.132.71.111:8080/images/" + path;
    }
}
