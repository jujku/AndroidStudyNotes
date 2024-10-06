package com.example.a2test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class LetterSideBar extends View {
    private Paint mPaint;
    private int mTextWidth;

    public static String[] mLetters = {"A","B","C","D","E","F","G","H"};

    public LetterSideBar(Context context) {
        super(context);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(sp2px(16));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mTextWidth = (int) mPaint.measureText("A");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mTextWidth + getPaddingLeft() + getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth()/2;
        int itemHeight = (getHeight() - getPaddingTop() -getBottom())/mLetters.length;
        for(int i = 0;i <mLetters.length; i++){
            int letterCenterY = i * itemHeight + itemHeight/2 + getPaddingTop();
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);
            int baseline = letterCenterY + dy;
            canvas.drawText(mLetters[i],x,baseline,mPaint);
        }
    }

    private int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }
}
