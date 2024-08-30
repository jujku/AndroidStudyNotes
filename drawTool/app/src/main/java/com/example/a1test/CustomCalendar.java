package com.example.a1test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

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

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取屏幕宽高
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 设置视图宽高为屏幕的一半
        int desiredWidth = screenWidth;
        int desiredHeight = screenHeight / 2;

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setColor(Color.WHITE); //文字颜色
        mPaint.setStyle(Paint.Style.FILL); //填充风格
        mPaint.setTextSize(40); //文字大小
        handler.post(updateRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        // 平移画布到中心点
        canvas.translate(width / 2, height / 2);
        // 旋转画布90度
        canvas.rotate(90);
        // 再次平移画布使其回到视图的左上角
        canvas.translate(-width / 2, -height / 2);

        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK); // 背景色
        // 获取当前时间和日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 绘制标题（年-月）
        mPaint.setTextSize(60);
        Paint tmpPaint = new Paint(mPaint);
        tmpPaint.setColor(Color.RED);
        canvas.drawText(String.format(Locale.getDefault(), "%d月", month + 1), 50, 100, tmpPaint);

        // 绘制星期
        mPaint.setTextSize(40);
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        int startX = 50;
        int startY = 300;
        int cellSize = getHeight()/7;

        for (int i = 0; i < 7; i++) {
            // 设置周日和周六的颜色为灰色
            if (i == 0 || i == 6) {
                mPaint.setColor(Color.GRAY);
            } else {
                mPaint.setColor(Color.WHITE);
            }

            float textWidth = mPaint.measureText(weekDays[i]);
            float x = startX + i * cellSize + (cellSize - textWidth) / 2;
            canvas.drawText(weekDays[i], x, startY, mPaint);
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

            if ((firstDayOfWeek + dayOfMonth - 1) % 7 == 0 || (firstDayOfWeek + dayOfMonth - 1) % 7 == 6) {
                mPaint.setColor(Color.GRAY);
            } else {
                mPaint.setColor(Color.WHITE);
            }
            int column = (firstDayOfWeek + dayOfMonth - 1) % 7;
            int row = (firstDayOfWeek + dayOfMonth - 1) / 7;

            int x = startX + column * cellSize;
            int y = startY + row * cellSize;

            // 计算文本的水平居中位置
            String dayString = String.valueOf(dayOfMonth);
            float textWidth = mPaint.measureText(dayString);
            float textX = x + (cellSize - textWidth) / 2;


            // 高亮显示今天
            if (dayOfMonth == day) {
                tmpPaint = new Paint(mPaint);
                tmpPaint.setColor(Color.RED);
                canvas.drawCircle(x + cellSize / 2,y + textHeight/2,40,tmpPaint);
                tmpPaint.setColor(Color.WHITE);
                canvas.drawText(dayString, textX, y + textBaseline, tmpPaint);
            }else {
                canvas.drawText(dayString, textX, y + textBaseline, mPaint);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.post(updateRunnable); // 启动定时更新
    }
}
