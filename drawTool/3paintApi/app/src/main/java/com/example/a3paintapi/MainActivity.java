package com.example.a3paintapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button maskFilter,porterDiffuXferMode,circleImageView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maskFilter = (Button) findViewById(R.id.maskFilter);
        porterDiffuXferMode = (Button) findViewById(R.id.a2xfermodeView);
        circleImageView = (Button) findViewById(R.id.a3CircleImageView);

        maskFilter.setOnClickListener(this);
        porterDiffuXferMode.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.maskFilter:
                intent = new Intent(MainActivity.this,A1MaskFilter.class);
                startActivity(intent);
                break;
            case R.id.a2xfermodeView:
                intent = new Intent(MainActivity.this,A2xfermode.class);
                startActivity(intent);
                break;
            case R.id.a3CircleImageView:
                intent = new Intent(MainActivity.this,A3CircleImageView.class);
                startActivity(intent);
                break;

        }
    }
}