package com.example.servicetest2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    private static String TAG = "MyService";
    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d(TAG, "startDownload: ");
        }
        public int getProgress(){
            Log.d(TAG, "getProgress: ");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        createNotificationChannel();

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Intent intent = new Intent(this,MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this,0,intent,f);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentText("this is content text")
                .setContentTitle("this is content title")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setCustomHeadsUpContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
//                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}