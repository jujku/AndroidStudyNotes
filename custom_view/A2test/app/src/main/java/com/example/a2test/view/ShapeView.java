package com.example.a2test.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.security.PublicKey;

public class ShapeView extends View {

    private Shape mCurrentShape = Shape.Circle;
    private Paint mPaint;
    private String TAG;

    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: " + mCurrentShape);
        switch (mCurrentShape){
            case Circle:
                int center = getWidth()/2;
                mPaint.setColor(Color.YELLOW);
                canvas.drawCircle(center,center,center,mPaint);
                break;
            case Square:
                mPaint.setColor(Color.BLUE);
                canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                break;
            case Triangle:
                mPaint.setColor(Color.RED);
                Path path = new Path();
                path.moveTo(getWidth()/2,0);
                path.lineTo(0,(float) (getWidth()/2 * Math.sqrt(3)));
                path.lineTo(getWidth(),(float) (getWidth()/2 * Math.sqrt(3)));
                path.close();
                canvas.drawPath(path,mPaint);
                break;
        }
    }

    public void exchange(){
        Log.d(TAG, "exchange: " + mCurrentShape);
        switch (mCurrentShape){
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }

        invalidate();
    }


    public enum Shape{
        Circle,Square,Triangle
    }
}
