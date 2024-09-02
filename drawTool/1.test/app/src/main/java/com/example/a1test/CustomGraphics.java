package com.example.a1test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomGraphics extends View {
    private Paint mPaint;
    private float rotationAngle = 0; // 初始旋转角度
    private Handler handler = new Handler();
    private Runnable rotateRunnable = new Runnable() {
        @Override
        public void run() {

            rotationAngle += 6; // 递增角度
            if (rotationAngle >= 360) {
                rotationAngle = 0; // 如果角度大于等于360，重置角度
            }
            invalidate(); // 重新绘制视图
            handler.postDelayed(this, 10); // 延迟调度下次更新
        }
    };

    private float minuteAngle = 0; // 初始分钟针角度
    private float hourAngle = 0;   // 初始时针角度

    private Runnable minuteRunnable = new Runnable() {
        @Override
        public void run() {
            minuteAngle += 6; // 每秒旋转6度
            if (minuteAngle >= 360) {
                minuteAngle = 0; // 重置角度
            }
            invalidate(); // 重新绘制视图
            handler.postDelayed(this, 600); // 每分钟更新一次
        }
    };

    private Runnable hourRunnable = new Runnable() {
        @Override
        public void run() {
            hourAngle += 6; // 每小时旋转0.5度
            if (hourAngle >= 360) {
                hourAngle = 0; // 重置角度
            }
            invalidate(); // 重新绘制视图
            handler.postDelayed(this, 36000); // 每小时更新一次
        }
    };

    public CustomGraphics(Context context) {
        this(context, null);
    }

    public CustomGraphics(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGraphics(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setColor(getResources().getColor(R.color.purple_200)); //画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);  //画笔风格
        mPaint.setTextSize(36); //绘制文字大小,单位px
        mPaint.setStrokeWidth(2); //画笔粗细

        handler.post(rotateRunnable);
        handler.post(minuteRunnable);
        handler.post(hourRunnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏幕宽高
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 设置视图宽高为屏幕的一半
        int desiredWidth = screenWidth / 2;
        int desiredHeight = screenHeight;

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);
        canvas.translate(canvas.getWidth() / 2, 400); //将位置移动画质的坐标点
        canvas.drawCircle(0, 0, 200, mPaint); //画圆圈

        //使用path绘制路径文字
        canvas.save();
        canvas.translate(-75, -75);
        Path path = new Path();
        path.addArc(new RectF(0, 0, 150, 150), -180, 180);
        Paint citePaint = new Paint(mPaint);
        citePaint.setTextSize(20);
        citePaint.setStrokeWidth(1);
        canvas.drawTextOnPath("绘制表盘", path, 28, 0, citePaint);
        canvas.restore();

        Paint tmpPaint = new Paint(mPaint); //小刻度画笔;
        tmpPaint.setStrokeWidth(2);
        tmpPaint.setTextSize(20);

        float y = 200;
        int count = 60;//总刻度数

        for (int i = 0; i < count; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(0f, y, 0, y + 24f, mPaint);
                canvas.drawText(String.valueOf(i / 5 + 1), -4f, y + 35f, tmpPaint);
            } else {
                canvas.drawLine(0f, y, 0f, y + 12f, tmpPaint);
            }
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }

        // 绘制秒针
        canvas.save();
        canvas.rotate(rotationAngle); // 应用秒针旋转
        tmpPaint.setColor(Color.GRAY);
        tmpPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 7, tmpPaint);
        tmpPaint.setStyle(Paint.Style.FILL);
        tmpPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 5, tmpPaint);
        canvas.drawLine(0, 20, 0, -165, mPaint);
        canvas.restore();

        // 绘制分针
        canvas.save();
        canvas.rotate(minuteAngle); // 应用分针旋转
        tmpPaint.setColor(Color.BLUE); // 分针颜色
        tmpPaint.setStrokeWidth(4);
        canvas.drawLine(0, 20, 0, -130, tmpPaint);
        canvas.restore();

        // 绘制时针
        canvas.save();
        canvas.rotate(hourAngle); // 应用时针旋转
        tmpPaint.setColor(Color.RED); // 时针颜色
        tmpPaint.setStrokeWidth(6);
        canvas.drawLine(0, 20, 0, -90, tmpPaint);
        canvas.restore();


    }
}