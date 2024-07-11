package com.example.servicetest;

import android.content.Context;
import android.widget.Toast;

public class MyThread extends  Thread{
    private Context mContext;
    public MyThread(Context context){
        mContext =context;
    }

    @Override
    public void run(){
//        Toast.makeText(mContext,"子线程",Toast.LENGTH_SHORT).show();
        //子线程中不能用

    }
}
