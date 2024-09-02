package com.example.a1test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private String TAG = "ManiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // 横屏
            setContentView(R.layout.activity_main);
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            setContentView(R.layout.activity_main_su);
        }



        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }  //隐藏系统自带的标题栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);  // 设置状态栏颜色为透明
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            setContentView(R.layout.activity_main);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            setContentView(R.layout.activity_main_su);
        }
    }
    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // 横屏
//            setContentView(R.layout.activity_main);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // 竖屏
//            setContentView(R.layout.activity_main_su);
//        }
//    }
}