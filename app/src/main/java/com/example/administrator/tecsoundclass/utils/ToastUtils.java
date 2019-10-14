package com.example.administrator.tecsoundclass.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tecsoundclass.R;


public class ToastUtils {
    private static Toast toast;

    public static void ShowMyToasts(Context context,String Message,int Position){
        Toast toastCunstom=new Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_toast,null);
        toastCunstom.setView(view);
        TextView textView=view.findViewById(R.id.tv_toast);
        textView.setText(Message);
        toastCunstom.setGravity(Position,0,0);
        toastCunstom.show();
    }

}
