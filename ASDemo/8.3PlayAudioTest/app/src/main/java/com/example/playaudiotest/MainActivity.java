package com.example.playaudiotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play_button);
        Button pause = (Button) findViewById(R.id.pause_button);
        Button stop = (Button) findViewById(R.id.stop_button);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               mediaPlayer.reset();//停止播放
               initMediaPlayer();
           }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else {
            initMediaPlayer();
        }
    }
    private void initMediaPlayer(){
        try{
            File file = new File(Environment.getExternalStorageDirectory(),"Sounds/music.mp3");
            Log.d("File Path", file.getPath());
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}