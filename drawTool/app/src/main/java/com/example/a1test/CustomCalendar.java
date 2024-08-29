package com.example.a1test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomCalendar extends View {

    private Paint mPaint;
    private Handler handler = new Handler();
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate(); // 重新绘制视图
            handler.postDelayed(this, 60000); // 每分钟更新一次
        }
    };

    private Calendar calendar = Calendar.getInstance();

    public CustomCalendar(Context context) {
        this(context, null);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setColor(Color.BLACK); //文字颜色
        mPaint.setStyle(Paint.Style.FILL); //填充风格
        mPaint.setTextSize(40); //文字大小
        handler.post(updateRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE); // 背景色

        // 获取当前时间和日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 绘制标题（年-月）
        mPaint.setTextSize(60);
        mPaint.setColor(Color.RED);
        canvas.drawText(String.format(Locale.getDefault(), "%d月", year, month + 1), 50, 100, mPaint);

        // 绘制星期
        mPaint.setTextSize(40);
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        int startX = 50;
        int startY = 150;
        int cellSize = 100;

        for (int i = 0; i < 7; i++) {
            canvas.drawText(weekDays[i], startX + i * cellSize, startY, mPaint);
        }

        // 计算当前月的第一个日期是星期几
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

        // 计算当前月的天数
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 绘制日期
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startY += 50; // 增加行高

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseline = -fontMetrics.top; // 基线到顶部的距离

        for (int dayOfMonth = 1; dayOfMonth <= daysInMonth; dayOfMonth++) {
            int x = startX + (firstDayOfWeek + dayOfMonth - 1) % 7 * cellSize;
            int y = startY + (firstDayOfWeek + dayOfMonth - 1) / 7 * cellSize;



            // 高亮显示今天
            if (dayOfMonth == day) {
                Paint tmpPaint = new Paint(mPaint);
                tmpPaint.setColor(Color.RED);
                canvas.drawCircle(x,y + textHeight/2,40,tmpPaint);
            }
            canvas.drawText(String.valueOf(dayOfMonth), x-mPaint.measureText(String.valueOf(dayOfMonth)) / 2, y + textBaseline, mPaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.post(updateRunnable); // 启动定时更新
    }
}
