package com.example.servicebestpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicebestpractice.enitiy.FileItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView selectedFile;
    private static String TAG = "MainActivity";
    RecyclerView recyclerView;

    private  DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFile = (TextView) findViewById(R.id.selected_file_view);

        recyclerView = findViewById(R.id.file_list);
        initFileData();
        //渲染文件列表

        Button refresh = (Button) findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFileData();
            }
        });


            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(permissionIntent);

        Button startDownload = (Button) findViewById(R.id.start_download_button);
        Button pauseDownload = (Button) findViewById(R.id.pause_download_button);
        Button cancelDownload = (Button) findViewById(R.id.cancel_download_button);

        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.42.228/"+selectedFile.getText().toString().substring(4);
                System.out.println(url);
                downloadBinder.startDownload(url);
            }
        });
        pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBinder.pauseDownload();
            }
        });
        cancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBinder.cancelDownload();
            }
        });

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent); //启动服务
        bindService(intent,connection,BIND_AUTO_CREATE);//绑定服务
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void initFileData(){
        new Thread(new Runnable(){
            List<FileItem> files = new ArrayList<>();
            @Override
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.42.228:3000/files")
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            List<FileItem> fileList = gson.fromJson(responseData,new TypeToken<List<FileItem>>(){}.getType());
                            for(FileItem item :fileList){
                                files.add(new FileItem(item.getName(),item.getSize()));
                            }
                            // 创建适配器并设置给RecyclerView
                            FileAdapter adapter = new FileAdapter(MainActivity.this, files,selectedFile);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                }
                            });
                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}