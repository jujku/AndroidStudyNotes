package com.example.a2test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.a2test.R;

public class RaringBar extends View {
    private int mGrade = 5;
    private int mCurrentGrade = 0;
    private Bitmap unratedBitmap,rateBitmap;
    private Paint mPaint;
    int imageWidth;
    private String TAG;

    public RaringBar(Context context) {
        this(context,null);
    }

    public RaringBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RaringBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RaringBar);
        int unrated = array.getResourceId(R.styleable.RaringBar_rating_image,0);
        if(unrated == 0){
            throw new RuntimeException("请设置属性 ratingImage");
        }
        unratedBitmap = BitmapFactory.decodeResource(getResources(),unrated);
        int rated = array.getResourceId(R.styleable.RaringBar_rating_image_active,0);
        if(rated == 0){
            throw new RuntimeException("请设置属性 ratingImageActive");
        }
        rateBitmap = BitmapFactory.decodeResource(getResources(),rated);

        mGrade = array.getInt(R.styleable.RaringBar_grade_rating,mGrade);

        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        imageWidth = rateBitmap.getWidth()/5;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = imageWidth * mGrade;
        int height = rateBitmap.getHeight()/5;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: " + "画了");
        int x = 0;
        for(int i = 0;i < mGrade;i++){
            canvas.save();
            canvas.scale(0.2f,0.2f,x,0);
            if(mCurrentGrade > i){
                canvas.drawBitmap(rateBitmap,x,0,mPaint);
            }else{
                canvas.drawBitmap(unratedBitmap,x,0,mPaint);
            }
            x += rateBitmap.getWidth()/5;
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentGrade = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float moveX = event.getX();
                currentGrade = (int) (moveX/imageWidth + 1);

        }
        if (mCurrentGrade != currentGrade) {
            mCurrentGrade = currentGrade;
            invalidate();
        }
        return true;
    }
}
