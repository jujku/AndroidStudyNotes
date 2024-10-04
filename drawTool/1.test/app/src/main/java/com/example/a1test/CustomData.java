package com.example.a1test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomData extends View {
    private Bitmap bitmap;
    private Paint textPaint;
    private int desiredWidth;
    private int desiredHeight;
    private boolean isInitialized = false;
    private Handler handler = new Handler();
    private Runnable timeUpdater = new Runnable() {
        @Override
        public void run() {
            invalidate(); // 触发重绘
            handler.postDelayed(this, 1000); // 每秒更新一次
        }
    };

    public CustomData(Context context) {
        this(context, null);
    }

    public CustomData(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏幕宽高
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 设置视图宽高为屏幕的一半
//        int desiredWidth = screenWidth / 2;

        desiredWidth = screenWidth / 2;
        desiredHeight = screenHeight;

        setMeasuredDimension(desiredWidth, desiredHeight);
        if (!isInitialized) {
            init();
            isInitialized = true;
        }
    }
    private void initResource(){
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shan); // 替换为你的图片资源

        }
    }
    private void init() {
        initResource();
        // 初始化画笔
        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#FE8F00")); // 文字颜色

        textPaint.setTextAlign(Paint.Align.CENTER); // 文字居中对齐

        // 开始更新时间
        handler.post(timeUpdater);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        // 绘制图片在视图的中间
        float bitmapX = (canvasWidth - bitmap.getWidth()) / 2.0f;
        float bitmapY = (canvasHeight - bitmap.getHeight()) / 2.0f - desiredWidth/26; // 图片上移50像素，给文字留空间

        textPaint.setTextSize(desiredWidth/20); // 文字大小
        bitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth / 2, desiredWidth / 2, true);
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, null);
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dateText = new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date);
        String dayOfWeekText = getDayOfWeekText(calendar.get(Calendar.DAY_OF_WEEK));
        // 绘制日期文字在图片下方
        String fullText = dateText + "  " + dayOfWeekText;
        float textX = canvasWidth / 2.0f;
        float textY = bitmapY + bitmap.getHeight() + desiredWidth/26; // 文字位置在图片下方70像素
        canvas.drawText(fullText, textX, textY, textPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(timeUpdater); // 停止时间更新，防止内存泄漏
    }

    private String getDayOfWeekText(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }
}
