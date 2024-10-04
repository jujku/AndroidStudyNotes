package com.example.a1test;


import android.animation.ValueAnimator;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SpecialCharView extends View {

    private Paint paint;
    private SimpleDateFormat timeFormat;
    private String currentTime;
    private Handler handler;
    private Runnable updateTimeRunnable;

    private float textSizeRatio = 0.8f; // 文字大小与视图高度的比例
    private int[] hourColors = new int[2]; // 小时每个数字的颜色
    private int[] minuteColors = new int[2]; // 分钟每个数字的颜色
    private int colonColor;

    public SpecialCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_black));

        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        currentTime = timeFormat.format(Calendar.getInstance().getTime());

        // 获取颜色
        hourColors[0] = ContextCompat.getColor(context, R.color.purple);
        hourColors[1] = ContextCompat.getColor(context, R.color.shallow_purple);

        minuteColors[0] = ContextCompat.getColor(context, R.color.purple);
        minuteColors[1] = ContextCompat.getColor(context, R.color.shallow_purple);
        colonColor = ContextCompat.getColor(context, R.color.pink);

        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                postInvalidate(); // 触发视图重绘
                handler.postDelayed(this, 600); // 计划下一次更新（每分钟一次）
            }
        };
        handler.postDelayed(updateTimeRunnable, 0); // 立即开始更新
        this.setOnClickListener(view -> playAnim());
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        Log.d("time",timeFormat.format(calendar.getTime()));
        currentTime = timeFormat.format(calendar.getTime());

    }

    private static final String TAG = "SpecialCharView";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 创建一个透明的画布层
        int saveLayer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        // 先绘制背景
        canvas.drawColor(Color.BLACK);

        float viewHeight = getHeight();
        float textSize = viewHeight * textSizeRatio;
        paint.setTextSize(textSize);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        String[] timeParts = currentTime.split(":");
        String hour = timeParts[0];
        String minute = timeParts[1];
        String[] hourgroup = hour.split("");
        String[] minutegroup = minute.split("");

        float hourWidth = paint.measureText(hourgroup[0]);
        float colonWidth = paint.measureText(":");
        float minuteWidth = paint.measureText(minutegroup[0]);

        float totalWidth = (hourWidth + colonWidth + minuteWidth) * 2;

        float centerX = (float) getWidth() / 2;
        float startX = (float) (centerX - totalWidth * 0.195);

        float[] hourRotateAngles = {-3, -8}; // 可以根据需要调整
        float[] minuteRotateAngles = {3, 3}; // 可以根据需要调整
        float colnorRotateAngles=-6;
        int number=0;
        // 绘制小时
        float hourX = startX;
        for (int i = 0; i < hourgroup.length; i++) {
            paint.setColor(hourColors[i]);

            float charWidth = paint.measureText(hourgroup[i]);
            float charHeight = fontMetrics.bottom - fontMetrics.top;
            float centerXForChar = hourX + charWidth / 2;
            float centerYForChar = getYPosition(paint) - (fontMetrics.ascent + fontMetrics.descent) / 2;

            canvas.save();
            canvas.rotate(hourRotateAngles[i], centerXForChar, centerYForChar);
            canvas.drawText(hourgroup[i], hourX, getYPosition(paint), paint);
            canvas.restore();

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
            if (number==0){
                hourX += charWidth * 0.72; // 可以调整字符间距
                number=1;
            }else {
                hourX += charWidth * 0.56; // 可以调整字符间距
            }
        }

        float savex=hourX;

        // 绘制分钟
        float minuteX = hourX;
        for (int i = 0; i < minutegroup.length; i++) {
            paint.setColor(minuteColors[i]);

            float charWidth = paint.measureText(minutegroup[i]);
            float charHeight = fontMetrics.bottom - fontMetrics.top;
            float centerXForChar = minuteX + charWidth / 2;
            float centerYForChar = getYPosition(paint) - (fontMetrics.ascent + fontMetrics.descent) / 2;

            canvas.save();
//            if(i==1) {
//                canvas.translate(0,-getHeight()*mProgress);
//                paint.setAlpha((int) (255*(1-mProgress)));
//            }
            canvas.rotate(minuteRotateAngles[i], centerXForChar, centerYForChar);
            canvas.drawText(minutegroup[i], minuteX, getYPosition(paint), paint);
//            if(i==1) {
//                canvas.translate(0,getHeight()*(1-mProgress));
//                paint.setAlpha((int) (255*mProgress));
//                canvas.rotate(minuteRotateAngles[i], centerXForChar, centerYForChar);
//                int ttt =Integer.valueOf(minutegroup[i])+1;
//                canvas.drawText(String.valueOf(ttt), minuteX, getYPosition(paint), paint);
//            }
            canvas.restore();
            Log.i(TAG, "onDraw: "+i+"  "+mProgress+"  getHeight() "+getHeight());

            // 设置混合模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
            minuteX += charWidth * 0.7; // 可以调整字符间距
        }



        // 清除混合模式
        paint.setXfermode(null);
        // 绘制冒号
        paint.setColor(colonColor);
        float charWidth1 = paint.measureText(":");
        float charHeight1 = fontMetrics.bottom - fontMetrics.top;
        float centerXForChar1 = savex + charWidth1 / 2;
        float centerYForChar1 = getYPosition(paint) - (fontMetrics.ascent + fontMetrics.descent) / 2;

        canvas.save();
        canvas.rotate(colnorRotateAngles, centerXForChar1, centerYForChar1);


        canvas.drawText(":", (float) (savex - (paint.measureText(hourgroup[1]) * 0.3)), (float) (getYPosition(paint) + (fontMetrics.ascent + fontMetrics.descent) * 0.1), paint);
        canvas.restore();
        // 进行混合操作后恢复画布状态
        canvas.restoreToCount(saveLayer);
    }


    private float getYPosition(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return getHeight() / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 在视图大小变化时更新文字大小
        // 不需要显式调用 postInvalidate()
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(updateTimeRunnable); // 视图被移除时取消定时更新
    }

    // 设置小时每个数字的颜色
    public void setHourColor(int position, int color) {
        if (position >= 0 && position < hourColors.length) {
            hourColors[position] = color;
            postInvalidate(); // 触发视图重绘
        }
    }

    // 设置分钟每个数字的颜色
    public void setMinuteColor(int position, int color) {
        if (position >= 0 && position < minuteColors.length) {
            minuteColors[position] = color;
            postInvalidate(); // 触发视图重绘
        }
    }
    float mProgress=0f;
    public void playAnim() {
        ValueAnimator ani = ValueAnimator.ofFloat(0, 1f);
        ani.addUpdateListener(valueAnimator -> {
             mProgress = (float) valueAnimator.getAnimatedValue();
            postInvalidate();
        });
        ani.setDuration(1000);
        ani.setInterpolator(new DecelerateInterpolator());
        ani.start();
    }

    // 设置冒号的颜色
    public void setColonColor(int color) {
        this.colonColor = color;
        postInvalidate(); // 触发视图重绘
    }
} 