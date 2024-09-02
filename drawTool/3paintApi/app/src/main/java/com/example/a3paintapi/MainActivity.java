package com.example.a3paintapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button maskFilter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maskFilter = (Button) findViewById(R.id.maskFilter);

        maskFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.maskFilter:
                intent = new Intent(MainActivity.this,A1MaskFilter.class);
                startActivity(intent);

        }
    }
}