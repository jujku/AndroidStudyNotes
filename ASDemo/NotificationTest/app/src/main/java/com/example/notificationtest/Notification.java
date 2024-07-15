package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);


        // 定义渠道ID
        String channelId = "my_channel1_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 定义渠道名称
            CharSequence name = "My Channel1";
            // 定义渠道描述
            String description = "This is my channel1";
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
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        Button sendButton = (Button) findViewById(R.id.sendnote_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                android.app.Notification notification = new NotificationCompat.Builder(Notification.this,channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)

                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(remoteViews)
                        .setCustomHeadsUpContentView(remoteViews)
                        .setCustomBigContentView(remoteViews)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).build();

                notificationManager.notify(2, notification);
            }
        });
    }
    private static RemoteViews getCustomNotificationView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);


        return remoteViews;
    }
}