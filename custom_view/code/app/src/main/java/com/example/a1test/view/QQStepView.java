package com.example.a1test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.a1test.R;

public class QQStepView extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20;
    private int mStepTextSize;
    private int mStepTextColor;
    private Paint mOuterPaint,mInnerPaint,mTextPaint;

    private float sweepAngle;

    private int currentSteps,maxSteps;
    private String TAG;

    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor,mOuterColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor,mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.QQStepView_QBorderWidth,mBorderWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize,mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor,mStepTextColor);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint = new Paint();
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //调用者在布局中可能 wrap——content  宽度高度不一致
        //获取模式AT——MOST 40dp
        //宽度高度不一致 取最小值 确保是一个正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width>height? height:width,width>height? height:width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(mBorderWidth/2,mBorderWidth/2,getWidth() - mBorderWidth/2,getHeight()-mBorderWidth/2);
        canvas.drawArc(rectF,135,270,false,mOuterPaint);
        if(maxSteps == 0) return;
        sweepAngle = 270 * (currentSteps/(float)maxSteps);
        Log.d(TAG, "onDraw: " + sweepAngle + " " + maxSteps + " " + (currentSteps/maxSteps) + " " + currentSteps);
        canvas.drawArc(rectF,135,sweepAngle,false,mInnerPaint);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline = getHeight()/2 + dy;
        canvas.drawText(currentSteps + "",getWidth()/2,baseline,mTextPaint);
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public void setCurrentSteps(int currentSteps) {
        this.currentSteps = currentSteps;
        invalidate();
    }
}
