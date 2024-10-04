package com.example.a1test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;



import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;

public class ClockView extends View {
    private Paint paint;

    private final int UPDATE_TIME_ANIMATION_TIME = 1200;
    private final float TEXT_SIZE_RATION_SCREEN_SIZE = 0.85f;
    private final int TIME_DIGITS_LENGTH = 4;

    private int[] currentTimeDigits = {0, 0, 0, 0}; // 13:32
    private int[] oldTimeDighits;
    private int[] tempTimeDighits;
    private float[] digitOffsets = new float[4]; // 垂直偏移，用于动画
    private float[] newDigitOffsets = new float[4]; // 垂直偏移，用于动画

    private float[] offset = new float[4];

    private float[] digitRotations = new float[4]; // 每个数字的旋转角度
    private float[] digitXOffsets = new float[4]; // 水平方向的偏移
    private float firstXOffset = 0f; //用于第一个数字的偏移

    private int screenWidth, screenHeight;
    private PorterDuffXfermode xfermodeADD,xfermodeLIGHTEN;
    private boolean isFirstDraw = true;
    private String TAG = "clocKvIEW";
    private boolean isFirstUpdate =true;

    private boolean isFirstOneIndex0 = true;

    private boolean isFirstOneIndex1 = true;

    float[] digitWidths = new float[4];

    private Handler timeUpdateHandler = new Handler();
    final int[] isClick = {3,5,1,1};

    EnumMap<Angle, Float> angles = new EnumMap<Angle, Float>(Angle.class);
    enum Angle{
        INDEX_0,
        INDEX_1,
        INDEX_2,
        INDEX_3
    }
    private void initDigitsAngle(){
        initAngle();
        for(int i = 0;i< 4;i++){
            startDigitAnimationRotate(i);
        }
    }
    private void initAngle(){
        angles.put(Angle.INDEX_0, (float) Math.random() * 6 - 3);
        angles.put(Angle.INDEX_1, (float) Math.random() * 6 - 3);
        angles.put(Angle.INDEX_2, (float) Math.random() * 6 - 3);
        angles.put(Angle.INDEX_3, (float) Math.random() * 6 - 3);

    }
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isFirstUpdate) {
                Log.d(TAG, "run: " + "执行了");
                updateSystemTime(); // 调用updateSystemTime方法更新时间显示

            }
            Log.d(TAG, "run: " + isFirstUpdate);
            isFirstUpdate = false;
            Log.d(TAG, "run: " + isFirstUpdate);
            long now = System.currentTimeMillis();


//            updateTime(new int[]{isClick[0],isClick[1],isClick[2],isClick[3]});
            isClick[0] += 1;
            if(isClick[0] > 9){
                isClick[1] += 1;
                isClick[0] = 0;
                if(isClick[1] > 4){
                    isClick[1] = 0;
                    isClick[2] += 1;
                    if(isClick[2] > 1){
                        isClick[2] = 0;
                        isClick[3] += 1;
                    }
                }
            }
            timeUpdateHandler.postDelayed(this, 60000 - (now % 60000)); // 每分钟更新一次，根据当前时间进行延迟}
//            timeUpdateHandler.postDelayed(this, 2000);
        }};

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initDigitsAngle();
        initTime();

        timeUpdateHandler.post(timeUpdateRunnable); // 启动定时任务

    }
    // 在ClockView类中添加一个方法，用来获取系统当前时间并调用updateTime方法更新时间显示
    public void updateSystemTime() {
        Log.d(TAG, "updateSystemTime: ");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 获取当前小时
        int minute = calendar.get(Calendar.MINUTE); // 获取当前分钟
        int[] newTimeDigits = new int[]{minute % 10, minute / 10, hour % 10, hour / 10};
        Log.d(TAG, "updateSystemTime: " + minute % 10+minute / 10+hour % 10+hour / 10);
        updateTime(newTimeDigits); // 调用updateTime方法更新时间显示
    }




    private void init() {

        ColorMatrix colorMatrix = new ColorMatrix();

// 实现灰度化效果
        colorMatrix.setSaturation(0); // 将彩色饱和度设置为0，即灰度化

// 实现反相色效果
        float[] invertColorMatrix = {
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        };
        colorMatrix.set(invertColorMatrix);

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(TEXT_SIZE_RATION_SCREEN_SIZE * getResources().getDisplayMetrics().heightPixels); // 设置字体大小为屏幕80%
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.WHITE); // 设置字体颜色
        paint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.roboto_black));
        xfermodeADD = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        paint.setXfermode(xfermodeADD);
        for (int i = 0; i < currentTimeDigits.length; i++) {
            Log.d(TAG, "init: " + Math.random());
            digitRotations[i] = (float) (Math.random() -0.5); // 随机旋转角度 -5 到 5 度
        }


//        initAnimator();
        final int[] isClick = {1,0,0,0};
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                updateTime(new int[]{isClick[0],isClick[1],isClick[2],isClick[3]});
                isClick[0] += 1;
                if(isClick[0] > 5){
                    isClick[1] += 1;
                    isClick[0] = 0;
                    if(isClick[1] > 4){
                        isClick[1] = 0;
                        isClick[2] += 1;
                        if(isClick[2] > 1){
                            isClick[2] = 0;
                            isClick[3] += 1;
                        }
                    }
                }
            }
        });
    }


    private void initTime(){
        Calendar calendar = Calendar.getInstance(); // 获取系统日历对象
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 获取当前小时
        int minute = calendar.get(Calendar.MINUTE); // 获取当前分钟
        int[] newTimeDigits = new int[]{minute % 10, minute / 10, hour % 10, hour / 10}; // 转换成数组形式;
        currentTimeDigits = newTimeDigits;
    }

    private void startDigitAnimation(int index) {
        if(index >= 4){
            return;
        }
//        postDelayed(() -> {
        currentTimeDigits[index] = tempTimeDighits[index];
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(UPDATE_TIME_ANIMATION_TIME); // 动画持续时间为1秒
        animator.setInterpolator(new OvershootInterpolator(1.3f));
        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            // 旧数字从上滑走，新数字从下滑入
            digitOffsets[index] = progress * getHeight();  // 旧数字上滑
            newDigitOffsets[index] = (1 - progress) * getHeight(); // 新数字下滑
            invalidate(); // 触发重绘
        });
        animator.start();

        if (index + 1 < currentTimeDigits.length && oldTimeDighits[index + 1] != tempTimeDighits[index + 1]) {
            postDelayed(() -> startDigitAnimation(index + 1), 100); // 100ms 后启动下一个动画
            postDelayed(() -> startDigitAnimationRotate(index + 1), 0);
        }



//        }, 100);

    }
    //这个动画用于变化的数字的偏移
    private void startDigitAnimationXOffset(int index){
        if(index >= 4){
            return;
        }
        Log.d(TAG, "startDigitAnimationRotate: " + " 1");
        ValueAnimator animatorRotate = ValueAnimator.ofFloat(0, 1);
        animatorRotate.setDuration(UPDATE_TIME_ANIMATION_TIME);
        animatorRotate.setInterpolator(new OvershootInterpolator());
        animatorRotate.addUpdateListener(animationXOffsets ->{

            float progressXOffsets = (float) animationXOffsets.getAnimatedValue();
            switch (currentTimeDigits[index]) {
                case 1:
                    if(oldTimeDighits[index] != 1) {
                        digitXOffsets[index] = progressXOffsets * -digitWidths[index] * 0.20f;
                    }
                    break;
                default:
                    if(oldTimeDighits[index] == 1) {
                        digitXOffsets[index] = (1 - progressXOffsets) * -digitWidths[index] * 0.20f;
                    }
                    break;
            }
            if(index == 0) {
                if(tempTimeDighits[index + 1] == 2 && oldTimeDighits[index] ==5){
                    isFirstOneIndex0 = true;
                    digitXOffsets[index] = (1 - progressXOffsets) * -digitWidths[index] * 0.28f;
                }
                if (tempTimeDighits[index + 1] == 1) {
                    if(isFirstOneIndex0) {
                        if(progressXOffsets == 1.0){
                            isFirstOneIndex0 = false;
                        }
                        digitXOffsets[index] = progressXOffsets * -digitWidths[index] * 0.28f;

                    }else{
                        digitXOffsets[index] = -digitWidths[index] * 0.28f;
                    }
                }

            }
            if(index == 2) {
                if(tempTimeDighits[index + 1] == 2 && oldTimeDighits[index] ==5){
                    isFirstOneIndex1 = true;
                    digitXOffsets[index] = (1 - progressXOffsets) * -digitWidths[index] * 0.28f;
                }
                if (tempTimeDighits[index + 1] == 1) {
                    if(isFirstOneIndex1) {
                        if(progressXOffsets == 1.0) {
                            isFirstOneIndex1 = false;
                        }
                        digitXOffsets[index] = progressXOffsets * -digitWidths[index] * 0.28f;
                    }else{
                        digitXOffsets[index] = -digitWidths[index] * 0.28f;
                    }
                }

            }
        });

        animatorRotate.start();

        postDelayed(() -> startDigitAnimationXOffset(index + 1), 0);
    }

    private void startDigitAnimationRotate(int index){

        ValueAnimator animatorRotate = ValueAnimator.ofFloat(0, 1);
        animatorRotate.setDuration(UPDATE_TIME_ANIMATION_TIME);
        animatorRotate.setInterpolator(new OvershootInterpolator());
        animatorRotate.addUpdateListener(animationRotate ->{
            float progress = (float) animationRotate.getAnimatedValue();
            switch (index){
                case 0:
                    offset[0] = angles.get(Angle.INDEX_0);
                    break;
                case 1:
                    offset[1] = angles.get(Angle.INDEX_1);
                    break;
                case 2:
                    offset[2] = angles.get(Angle.INDEX_2);
                    break;
                case 3:
                    offset[3] = angles.get(Angle.INDEX_3);
                    break;
            }
//
        });

        animatorRotate.start();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: " + w + " " + h + " " + oldw +" " + oldh + " " + getWidth() + " " + getHeight());
        if(w>h) {
            paint.setTextSize(TEXT_SIZE_RATION_SCREEN_SIZE * h);
        }else{
            paint.setTextSize(TEXT_SIZE_RATION_SCREEN_SIZE * w);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);


        int width = getWidth();
        int height = getHeight();

        // 设置背景颜色为黑色
        canvas.drawColor(Color.BLACK);
        // 保存当前的 canvas 状态
        // 强制横屏显示
        if (height > width) {
            canvas.rotate(90, width / 2, height / 2);
        }

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        // 计算文本的总高度
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // 计算基线的 y 坐标，用于垂直居中
        float y = (height / 2) - (textHeight / 2) - fontMetrics.ascent;

        float textTop = (height / 2) - (textHeight / 2) + fontMetrics.top;

        float totalWidth = 0;

        // 先计算所有数字的总宽度，用于居中
        for (int i = 0; i < currentTimeDigits.length; i++) {
            digitWidths[i] = paint.measureText(String.valueOf(currentTimeDigits[i])) * 0.77f ;

            totalWidth += digitWidths[i] + digitXOffsets[i];
        }



        float colonWidth = paint.measureText(":")*0.65f;
        totalWidth += colonWidth;
        // 起始x坐标，用于居中
        float x = totalWidth + (width - totalWidth)/2;

        // 绘制数字
        for (int i = 0; i < TIME_DIGITS_LENGTH; i++) {
            canvas.save(); // 保存当前状态
            int saveCount = 0;
            // 设置不同位置的颜色
            if (i % 2 != 0) {
//                saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null);
                paint.setColor(getResources().getColor(R.color.purple)); // 偶数位用紫色
                paint.setAlpha(225);
            } else {
                paint.setColor(getResources().getColor(R.color.shallow_purple)); // 奇数位用浅紫色
                paint.setAlpha(255);
            }

            // 判断数字是否变化，如果变化则需要应用动画效果

            if (oldTimeDighits != null && oldTimeDighits[i] != currentTimeDigits[i]) {

                // 旧数字上滑出动画
                canvas.drawText(String.valueOf(oldTimeDighits[i]), x, y - digitOffsets[i], paint);
                // 新数字下滑入动画
                canvas.save();

                canvas.rotate(offset[i], x - digitWidths[i] / 2 , y+(fontMetrics.descent + fontMetrics.ascent) / 2);
                canvas.drawText(String.valueOf(currentTimeDigits[i]), x, y + newDigitOffsets[i], paint);
                Log.d(TAG, "onDraw: " + " index : " + i + " value: " + offset[i]);
                canvas.restore();
            } else {
                if (oldTimeDighits != null) {
//                    Log.d(TAG, "onDraw: " + "old不为空");
//                    if (oldTimeDighits[i] == 1 && currentTimeDigits[i] != 1) {
//                        Log.d(TAG, "onDraw: " + "1");
//                        canvas.rotate(offset[i], x + digitWidths[i] / 2, 0);
//                    } else if (oldTimeDighits[i] != 1 && currentTimeDigits[i] == 1) {
//                        Log.d(TAG, "onDraw: " + "搬动");
//                        canvas.rotate(offset[i], x + digitWidths[i] / 2, 0);
//                    }
                }
                canvas.save();
                canvas.rotate(offset[i], x + digitWidths[i] / 2, y+(fontMetrics.descent + fontMetrics.ascent) / 2);
                canvas.drawText(String.valueOf(currentTimeDigits[i]), x, y, paint);
                canvas.restore();
            }
//            if(saveCount !=0){
//                canvas.restoreToCount(saveCount);
//            }
            if(i ==2) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(getResources().getColor(R.color.pink));
                paint.setAlpha(240);
                paint.setXfermode(null);
//                if(currentTimeDigits[i] == 1){
//                    x -= digitWidths[i] *0.2f;
//                }
                canvas.drawText(":", x, y - textHeight * 0.05f, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setAlpha(255);
                paint.setXfermode(xfermodeADD);

            }
            // 更新下一个数字的x位置
            if(i == 1){
                x -= colonWidth/1.7;

            }
            x -= (digitWidths[i] + digitXOffsets[i]); // 重叠部分

            canvas.restore(); // 恢复画布状态
            isFirstDraw = false;
        }



    }

    // 模拟时间改变时启动动画
    public void updateTime(int[] newTimeDigits) {
        initAngle();

        oldTimeDighits = Arrays.copyOf(currentTimeDigits, currentTimeDigits.length);

        tempTimeDighits=newTimeDigits;

        if(oldTimeDighits[0] != newTimeDigits[0]){

            startDigitAnimation(0);
            startDigitAnimationXOffset(0);
            startDigitAnimationRotate(0);
//            startDigitAnimation3();
        }
    }
}
