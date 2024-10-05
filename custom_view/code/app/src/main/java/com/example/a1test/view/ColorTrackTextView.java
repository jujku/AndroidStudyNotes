package com.example.a1test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.a1test.R;

public class ColorTrackTextView extends TextView {
    private Paint mOriginPaint;
    private Paint mChagnePaint;
    private float mCurrentProgress = 0.0f ;

    private Direction mDirection;
    private String TAG;

    public enum Direction{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context,attrs);
    }

    private void initPaint(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor = array.getColor(R.styleable.ColorTrackTextView_originColor,getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor,getTextColors().getDefaultColor());

         mOriginPaint = getPaintByColor(originColor);
         mChagnePaint = getPaintByColor(changeColor);

         array.recycle();
    }

    private Paint getPaintByColor(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        int middle = (int) (mCurrentProgress * getWidth());

        if(mDirection == Direction.LEFT_TO_RIGHT){
            drawText(canvas,mChagnePaint,0,middle);
            //绘制变色
            drawText(canvas,mOriginPaint,middle,getWidth());
        }else if(mDirection == Direction.RIGHT_TO_LEFT){
            drawText(canvas,mChagnePaint,middle,getWidth());
            //绘制变色
            drawText(canvas,mOriginPaint,0,middle);
        }else{
            drawText(canvas,mOriginPaint,middle,getWidth());
        }
    }

    private void drawText(Canvas canvas,Paint paint,int start,int end){
        canvas.save();
        //不变色的地方
        Rect rect = new Rect(start,0,end,getHeight());
        canvas.clipRect(rect);
        //我们自己来画
        String text = getText().toString();
        Rect bounds = new Rect();

        paint.getTextBounds(text,0,text.length(),bounds);
        int x = getWidth() / 2 - bounds.width() / 2;
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() /2 + dy;
        canvas.drawText(text,x,baseLine,paint);
        canvas.restore();
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    public void setCurrentProgress(float currentProgress){
        Log.d(TAG, "setCurrentProgress: " + "设置");
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setChangeColor(int changeColor){
        this.mChagnePaint.setColor(changeColor);
    }

    public void setOriginColor(int originColor){
        this.mOriginPaint.setColor(originColor);
    }
}
