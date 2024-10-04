package com.example.a1test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class CustomGraphicsSu extends View {
    private Paint mPaint;
    private Bitmap clockFaceBitmap,hourHandBitmap,minuteHandBitmap,secondHandBitmap,rawClockFace;
    private Handler handler = new Handler();
    private Runnable rotateRunnable = new Runnable() {
        @Override
        public void run() {

//            rotationAngle += 6; // 递增角度
//            if (rotationAngle >= 360) {
//                rotationAngle = 0; // 如果角度大于等于360，重置角度
//            }
            invalidate(); // 重新绘制视图
            handler.postDelayed(this, 100); // 延迟调度下次更新
        }
    };
    private String TAG;
    private boolean isInitialized = false;


    public CustomGraphicsSu(Context context) {
        this(context, null);
    }

    public CustomGraphicsSu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGraphicsSu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    private void initResource(){
        clockFaceBitmap= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clock_face);

        hourHandBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hour_hand);

        minuteHandBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.minute_hand);

        secondHandBitmap= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.second_hand);

    }
    private void init() {
        initResource();
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
//        mPaint.setColor(getResources().getColor(R.color.purple_200)); //画笔颜色
//        mPaint.setStyle(Paint.Style.FILL);  //画笔风格
//        mPaint.setTextSize(100); //绘制文字大小,单位px
//        mPaint.setStrokeWidth(100); //画笔粗细
//        Bitmap rawClockFace= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clock_face);
//        Log.d("asdf", "init: " + desiredWidth);
//        clockFaceBitmap = scaleBitmap(rawClockFace, desiredWidth / 2, desiredWidth / 2); // 调整大小
//        float scale = (float) clockFaceBitmap.getHeight() /  (float) rawClockFace.getHeight();
//        Bitmap rawHourHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hour_hand);
//        hourHandBitmap = scaleBitmap(rawHourHand, (int) (rawHourHand.getWidth() * scale), (int)(rawHourHand.getWidth() *scale)); // 调整大小
//
//        Bitmap rawMinuteHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.minute_hand);
//        minuteHandBitmap = scaleBitmap(rawMinuteHand, (int)(rawMinuteHand.getWidth() * scale), (int)(rawMinuteHand.getWidth() * scale)); // 调整大小
//
//        Bitmap rawSecondHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.second_hand);
//        secondHandBitmap = scaleBitmap(rawSecondHand, (int)(rawSecondHand.getWidth() * scale), (int)(rawSecondHand.getWidth() * scale)); // 调整大小

//        rawClockFace= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clock_face);
//        Log.d("asdf", "init: " + desiredWidth);
//        clockFaceBitmap = scaleBitmap(rawClockFace, desiredWidth / 2, desiredWidth / 2); // 调整大小
//        float scale = (float) clockFaceBitmap.getHeight() /  (float) rawClockFace.getHeight();
//        Bitmap rawHourHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hour_hand);
//        hourHandBitmap = scaleBitmap(rawHourHand, (int) (rawHourHand.getWidth() * scale), (int)(rawHourHand.getWidth() *scale)); // 调整大小
//
//        Bitmap rawMinuteHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.minute_hand);
//        minuteHandBitmap = scaleBitmap(rawMinuteHand, (int)(rawMinuteHand.getWidth() * scale), (int)(rawMinuteHand.getWidth() * scale)); // 调整大小
//
//        Bitmap rawSecondHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.second_hand);
//        secondHandBitmap = scaleBitmap(rawSecondHand, (int)(rawSecondHand.getWidth() * scale), (int)(rawSecondHand.getWidth() * scale)); // 调整大小

        handler.post(rotateRunnable);
    }
    private int desiredWidth;
    private int desiredHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏幕宽高
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 设置视图宽高为屏幕的一半
//        int desiredWidth = screenWidth / 2;

        desiredWidth = screenWidth;
        desiredHeight = screenHeight/2;

        setMeasuredDimension(desiredWidth, desiredHeight);
        if (!isInitialized) {
            init();
            isInitialized = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        int width = getWidth();
        int height = getHeight();

        // 平移画布到中心点
        canvas.translate(width / 2, height / 2);
        // 旋转画布90度
        canvas.rotate(90);
        // 再次平移画布使其回到视图的左上角
        canvas.translate(-width / 2, -height / 2);
//        clockFaceBitmap = scaleBitmap(rawClockFace, desiredWidth / 2, desiredWidth / 2);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        // 将画布的原点移到画布的中心
//        Log.d(TAG, "onDraw: " + clockFaceBitmap.getWidth() + canvasWidth );

        // 绘制表盘
        canvas.save();
        canvas.scale(0.3f,0.3f);
        canvas.translate(clockFaceBitmap.getWidth() / 2, canvasHeight / 2 / 0.3f );
        canvas.drawBitmap(clockFaceBitmap, -clockFaceBitmap.getWidth() / 2, -clockFaceBitmap.getHeight() / 2, null);
//        Bitmap rawClockFace= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clock_face);
//        Log.d("asdf", "init: " + desiredWidth);
//        clockFaceBitmap = scaleBitmap(rawClockFace, desiredWidth / 2, desiredWidth / 2); // 调整大小
//        float scale = (float) clockFaceBitmap.getHeight() /  (float) rawClockFace.getHeight();
//        Bitmap rawHourHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hour_hand);
//        hourHandBitmap = scaleBitmap(rawHourHand, (int) (rawHourHand.getWidth() * scale), (int)(rawHourHand.getWidth() *scale)); // 调整大小
//
//        Bitmap rawMinuteHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.minute_hand);
//        minuteHandBitmap = scaleBitmap(rawMinuteHand, (int)(rawMinuteHand.getWidth() * scale), (int)(rawMinuteHand.getWidth() * scale)); // 调整大小
//
//        Bitmap rawSecondHand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.second_hand);
//        secondHandBitmap = scaleBitmap(rawSecondHand, (int)(rawSecondHand.getWidth() * scale), (int)(rawSecondHand.getWidth() * scale)); // 调整大小
//        clockFaceBitmap= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clock_face);


        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) / 60.0f;
        float minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) / 60.0f;
        float second = calendar.get(Calendar.SECOND);

        // 计算时针、分针、秒针的旋转角度
        float hourRotation = hour * 30; // 每小时30度
        float minuteRotation = minute * 6; // 每分钟6度
        float secondRotation = second * 6; // 每秒6度

//        // 绘制时针
        drawHand(canvas, hourHandBitmap, hourRotation);
//
//        // 绘制分针
        drawHand(canvas, minuteHandBitmap, minuteRotation);

        // 绘制秒针
        drawHand(canvas, secondHandBitmap, secondRotation);

        canvas.restore();

    }
    private void drawHand(Canvas canvas, Bitmap handBitmap, float rotationAngle) {
        // 创建一个矩阵来进行旋转和位置调整
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle, handBitmap.getWidth() / 2, handBitmap.getHeight() / 2);
        matrix.postTranslate(-handBitmap.getWidth() / 2, -handBitmap.getHeight() + clockFaceBitmap.getHeight()/2); // 调整位置，使旋转中心位于指针末端

        // 绘制指针
        canvas.drawBitmap(handBitmap, matrix, null);
    }
}