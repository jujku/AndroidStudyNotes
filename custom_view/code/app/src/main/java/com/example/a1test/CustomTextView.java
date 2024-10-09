package com.example.a1test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class CustomTextView extends LinearLayout {

    private String mText;
    private int mTextSize = 15;
    private int mTextColor = Color.BLACK;
    private Paint mPaint;
    private String TAG;

    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);

        mText = array.getString(R.styleable.CustomTextView_customText);
        mTextColor = array.getColor(R.styleable.CustomTextView_customTextColor,mTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.CustomTextView_customTextSize,sp2px(mTextSize));

        array.recycle();

        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        Log.d(TAG, "CustomTextView: " + mTextColor + " " + mText);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //EXACTLY
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText,0,mText.length(),bounds);
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
         }

        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            height = (int)(-fontMetrics.top +fontMetrics.bottom) + getPaddingTop() + getPaddingBottom();
        }
        //设置控件的宽高
        setMeasuredDimension(width,height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        Log.d(TAG, "onDraw: "+ fontMetrics.top + " " + fontMetrics.bottom + " " + fontMetrics.descent + " " + fontMetrics.ascent + " " + getHeight());
        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        int height = fontMetrics.bottom - fontMetrics.top;
        int baseLine = height/2 +getPaddingTop()+ dy;

        int x = getPaddingLeft();

        canvas.drawText(mText,x,baseLine,mPaint);
        //drawText的y是baseline

    }

    private int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }
}
