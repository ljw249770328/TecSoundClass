package com.example.administrator.tecsoundclass.utils;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.load.model.FileLoader;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class FileDownloadUtil {

    private void initDownloader(){

    }
    public static void  mDownLoadFile(String url, final DownloadCallBack callBack){
        String [] temp=url.split("/");
        String Filename =temp[temp.length-1];
        final String FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"sounds/"+Filename;
        FileDownloader.getImpl().create(url)
                .setPath(FilePath)
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    //等待
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("mDownloading","pending taskId:"+task.getId()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes);
                    }
                    //下载进度回调
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("mDownloading","progress taskId:"+task.getId()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes+",speed:"+task.getSpeed());
                    }
                    //完成下载
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d("mDownloading","completed taskId:"+task.getId()+",isReuse:"+task.reuse());
                        callBack.mOncompleted(FilePath);
                    }
                    //下载暂停
                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("mDownloading","paused taskId:"+task.getId()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes);
                    }
                    //下载出错
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d("mDownloading","error taskId:"+task.getId()+",e:"+e.getLocalizedMessage());
                        callBack.mOnError(e);
                    }
                    //存在相同下载
                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.d("mDownloading","warn taskId:"+task.getId());
                    }
                }).start();
    }

    public interface  DownloadCallBack{
        void mOncompleted(String FilePath);
        void mOnError(Throwable e);
    }
}