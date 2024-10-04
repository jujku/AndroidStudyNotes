package com.example.a1test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.example.a1test.view.ColorTrackTextView;
import com.example.a1test.view.QQStepView;

public class MainActivity extends AppCompatActivity {

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        QQStepView();
        Button btn = findViewById(R.id.start);
        Button btn2 = findViewById(R.id.end_start);
        btn.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: " + "点击");
            colorTraceTextViwe();
        });
        btn2.setOnClickListener(v->{
            colorTraceTextViwe2();
        });

    }
    private void colorTraceTextViwe() {
        ColorTrackTextView colorTrackTextView = findViewById(R.id.color_track_text_view);
        colorTrackTextView.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                colorTrackTextView.setCurrentProgress(currentProgress);
            }
        });
    valueAnimator.start();
    }
    private void colorTraceTextViwe2() {
        ColorTrackTextView colorTrackTextView = findViewById(R.id.color_track_text_view);
        colorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                colorTrackTextView.setCurrentProgress(currentProgress);
            }
        });
        valueAnimator.start();
    }

    private int currentSteps;
    private void QQStepView(){
        QQStepView stepView = findViewById(R.id.step_view);
        stepView.setMaxSteps(4000);

        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0,1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            currentSteps = (int) animation.getAnimatedValue();
            stepView.setCurrentSteps(currentSteps);
        });

        valueAnimator.start();
    }


}