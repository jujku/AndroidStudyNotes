package com.example.a2test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.a2test.view.ShapeView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private Button test;
    private ShapeView shapeView;
    private Handler exchangeHandler = new Handler();
    private Runnable exchangeRunnable = new Runnable() {
        @Override
        public void run() {
            shapeView.exchange();
            exchangeHandler.postDelayed(this,1000);
        }
    };

    private void ShapeView(){
        test = findViewById(R.id.test);
        shapeView = findViewById(R.id.shape_view);

        test.setOnClickListener(v -> exchangeHandler.post(exchangeRunnable));
    }
}