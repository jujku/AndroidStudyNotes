package com.example.servicetest2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {
    private static String TAG = "MyIntentService";
    public MyIntentService(){
        //调用父类的有参构造函数
        super("MyIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent){
        //打印当前线程的id
        Log.d(TAG, "Thread id si  " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}
