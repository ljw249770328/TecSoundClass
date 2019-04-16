package com.example.administrator.tecsoundclass.utils;

import android.os.Environment;

import com.liulishuo.filedownloader.FileDownloader;

public class FileDownloadUtil {

    private void initDownloader(){

    }
    public static void  DownLoadFile(String url){
        String [] temp=url.split("/");
        String Filename =temp[temp.length-1];
        FileDownloader.getImpl().create(url)
                .setPath(Environment.getExternalStorageDirectory().getAbsolutePath());
    }
}
//http://101.132.71.111:8080/images//storage/emulated/0/MyApplication/15180166098_2222222222_3284e1c9-d957-487b-95b3-a72f51baee96.wav