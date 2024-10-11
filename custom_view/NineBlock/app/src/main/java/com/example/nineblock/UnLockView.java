package com.example.nineblock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UnLockView extends View {
    int measuredWidth;
    private Paint paint;
    private float bigRadius;

    private float smallRadius;

    private static final int NUMBER = 3;

    private final ArrayList<ArrayList<UnLockBean>> unLockPoints = new ArrayList<>();
    private String TAG;

    public UnLockView(Context context) {
        this(context,null);
    }

    public UnLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UnLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.BEVEL);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int diameter = getWidth() / NUMBER;

        float ratio = NUMBER * 2f;
        int index = 1;

        for(int i = 0;i< NUMBER;i++){
            ArrayList<UnLockBean> list = new ArrayList<>();
            for(int j = 0;j <NUMBER;j++){
                list.add(new UnLockBean(
                        getWidth() / ratio + diameter * j,
                        getHeight() / ratio + diameter * i,
                        index++
                ));
            }
            unLockPoints.add(list);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,width);



        bigRadius = (width/(NUMBER * 2))*0.7f;

        smallRadius = bigRadius *0.2f;
    }


}
