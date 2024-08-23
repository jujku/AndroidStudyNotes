package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomTextView extends View {

    private String text;
    private Paint mPaint;
    private Rect mTextBounds;

    private int width;
    private int height;

    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);
        text = ta.getString(R.styleable.CustomTextView_custom_text);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(50);

        mTextBounds = new Rect();
        mPaint.getTextBounds(text,0,text.length(),mTextBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("onMeasure模式跟数值: " +MeasureSpec.toString(widthMeasureSpec));

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            width = specWidth;
        } else if(specMode == MeasureSpec.AT_MOST){
            width = mTextBounds.width() +getPaddingLeft() + getPaddingRight();
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            height = specHeight;
        }else if(specMode == MeasureSpec.AT_MOST){
            height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawText(text,getPaddingLeft() + 0,mTextBounds.height() + getPaddingTop(),mPaint);
    }


}
