package com.example.playvediotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (VideoView) findViewById(R.id.video_view);
        Button play = (Button) findViewById(R.id.play_button);
        Button pause = (Button) findViewById(R.id.pause_button);
        Button replay = (Button) findViewById(R.id.replay_button);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("getpath", "file.getPath()");
                videoView.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();
            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.resume();
            }
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            initVideoPath(); //初始化
        }
    }
    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),"movie.mp4");
        Log.d("getpath", file.getPath());
        videoView.setVideoPath(file.getPath());//指定视频文件的路径
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initVideoPath();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null){
            videoView.suspend();
        }
    }
}