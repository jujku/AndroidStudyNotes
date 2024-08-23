package com.example.animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.text);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                textView.animate()
                        .translationY(100)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {
                                Log.d("", "onAnimationStart: " + "开始上");
                                button.setEnabled(false);
                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                textView.animate().translationY(100);
                                Log.d("", "onAnimationEnd: " + "上完");
                                button.setEnabled(true);
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        }
                );
        }
    }
}