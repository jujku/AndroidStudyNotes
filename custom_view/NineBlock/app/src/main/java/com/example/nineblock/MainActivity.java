package com.example.nineblock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JiuGonGeUnLockView view = findViewById(R.id.unlock);


        view.setOnPasswordListener(new JiuGonGeUnLockView.OnPasswordListener() {
            @Override
            public void onPasswordEntered(List<Integer> enteredPassword) {
                Log.d(TAG, "onPasswordEntered: " + view.password);
                if(view.password.size() == 0){
                    Log.d(TAG, "onPasswordEntered: " + view.password);
                    view.setPassword(enteredPassword);
                }
                Log.d(TAG, "onPasswordEntered: " + enteredPassword);
            }
        });
    }
}