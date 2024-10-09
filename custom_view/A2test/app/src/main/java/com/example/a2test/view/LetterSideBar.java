package com.example.a2test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class LetterSideBar extends View {
    private Paint mPaint;
    private int mTextWidth;

    public static String[] mLetters = {"A","B","C","D","E","F","G","H","I"};
    private String TAG;
    private int mStartY;
    private int mItemHeight;
    private int mCurrentPostion = 0;

    public LetterSideBar(Context context) {
        this(context,null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
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
        mItemHeight = (getHeight() - getPaddingTop() -getPaddingBottom())/26;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

        mStartY = ((getHeight() -getPaddingTop() -getPaddingBottom()) - mItemHeight*mLetters.length)/2;

        int dy = (int) ((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);
        for(int i = 0;i <mLetters.length; i++){
            int letterCenterY = i * mItemHeight + mItemHeight/2 + getPaddingTop() + mStartY;
            int baseline = letterCenterY + dy;
            if(i == mCurrentPostion){
                mPaint.setColor(Color.RED);
            }else{
                mPaint.setColor(Color.BLUE);
            }
            canvas.drawText(mLetters[i],x,baseline,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getY();
                int touchPosition = (int) (currentY - mStartY )/mItemHeight;
                if(touchPosition < 0){
                    return true;
                }else if(touchPosition >= mLetters.length){
                    return true;
                }

                if(touchPosition != mCurrentPostion){
                    mCurrentPostion = touchPosition;
                    mListener.touch(mLetters[mCurrentPostion]);
                }
                invalidate();
        }
        return true;
    }
    private LetterTouchListener mListener;

    public void setOnLetterTouchListener(LetterTouchListener listener){
        this.mListener = listener;
    }

    public interface LetterTouchListener{
        void touch(CharSequence letter);
    }

    private int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }
}
