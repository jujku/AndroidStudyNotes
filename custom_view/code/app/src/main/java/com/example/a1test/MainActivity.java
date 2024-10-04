package com.example.a1test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import com.example.a1test.view.QQStepView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        QQStepView();

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