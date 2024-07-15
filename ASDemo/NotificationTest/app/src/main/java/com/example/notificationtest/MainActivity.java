package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 定义渠道ID
        String channelId = "my_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 定义渠道名称
            CharSequence name = "My Channel";
            // 定义渠道描述
            String description = "This is my channel";
            // 定义渠道重要性
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // 创建渠道
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // 获取通知管理器
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            // 在系统中注册渠道
            notificationManager.createNotificationChannel(channel);
        }

        Button sendNotice = (Button) findViewById(R.id.notice_button);
        sendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(MainActivity.this,channelId)
                        .setContentTitle("这是一个标题")
                        .setContentText("这是一个内容文字")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
//                        .setAutoCancel(true)

//                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
//                        .setVibrate(new long[]{0,1000,1000,1000})
                        .build();
                notificationManager.notify(1,notification);
            }
        });
    }
}