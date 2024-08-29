package com.example.a1test;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    private Paint mPaint;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setColor(getResources().getColor(R.color.purple_200)); //画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //画笔风格
        mPaint.setTextSize(36); //绘制文字大小,单位px
        mPaint.setStrokeWidth(5); //画笔粗细
    }
    //在这里绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.black)); //画布的颜色
//        画一个实心圆
        canvas.drawCircle(200,200,100,mPaint);
//        画矩形
        canvas.drawRect(0,0,200,100,mPaint);
//        绘制Bitmap
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.shawu),300,0,mPaint);
//        绘制弧形区域  true or false 表示是否失心？
        canvas.drawArc(new RectF(100,200,600,300),0,90,true,mPaint);
//        绘制圆角矩形
        canvas.drawRoundRect(new RectF(20,300,210,400),15,80,mPaint);
//        绘制椭圆
        canvas.drawOval(new RectF(20,420,200,720),mPaint);
//        绘制多边形
        Path path = new Path();
        path.moveTo(300,500);
        path.lineTo(400,500);
        path.lineTo(400,600);
        path.lineTo(700,550);
        path.close();
        canvas.drawPath(path,mPaint);
//        绘制文字
        canvas.drawText("和泉纱雾",700,100,mPaint);
    }
}
