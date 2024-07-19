package com.example.servicetest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService = (Button) findViewById(R.id.start_button);
        Button stopService = (Button) findViewById(R.id.stop_button);

        Button bindService = (Button) findViewById(R.id.bind_button);
        Button unbindService = (Button) findViewById(R.id.unbind_button);

        Button startIntentService = (Button) findViewById(R.id.start_intent_service);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, MyService.class);
                startService(startIntent);
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopIntent = new Intent(MainActivity.this,MyService.class);
                stopService(stopIntent);

            }
        });

        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bindIntent = new Intent(MainActivity.this,MyService
                        .class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);//绑定服务
            }
        });

        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(connection); //解绑服务
            }
        });

        startIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Thread id is " + Thread.currentThread().getId());
                Intent intentService = new Intent(MainActivity.this,MyIntentService.class);
                startService(intentService);
            }
        });
    }
}