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
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;

public class ClockViewCopy extends View {
    private Paint paint;

    private final int UPDATE_TIME_ANIMATION_TIME = 1500;
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

    float[] digitWidths = new float[4];

    EnumMap<AnglePurpose, Float> angles = new EnumMap<AnglePurpose, Float>(AnglePurpose.class);
    EnumMap<OldAngle, Float> oldAngles = new EnumMap<OldAngle, Float>(OldAngle.class);


    enum AnglePurpose {
        INDEX_0_1_TO_X,
        INDEX_0_X_TO_X,
        INDEX_0_7_TO_X,
        INDEX_0_9_TO_X,
        INDEX_1_1_TO_X,
        INDEX_1_X_TO_X,

        INDEX_2_1_TO_X,
        INDEX_2_X_TO_X,
        INDEX_2_7_TO_X,
        INDEX_2_9_TO_X,
        INDEX_3_1_TO_X,
        INDEX_3_X_TO_X,
    }

    enum OldAngle{
        INDEX_0,
        INDEX_1,
        INDEX_2,
        INDEX_3
    }

    private Handler timeUpdateHandler = new Handler();
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isFirstUpdate) {
                Log.d(TAG, "run: " + "执行了");
//                updateSystemTime(); // 调用updateSystemTime方法更新时间显示

        }
            Log.d(TAG, "run: " + isFirstUpdate);
            isFirstUpdate = false;
            Log.d(TAG, "run: " + isFirstUpdate);
            long now = System.currentTimeMillis();
            timeUpdateHandler.postDelayed(this, 60000 - (now % 60000)); // 每分钟更新一次，根据当前时间进行延迟}
    }};

    public ClockViewCopy(Context context) {
        this(context,null);
    }

    public ClockViewCopy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockViewCopy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initOldAngle();
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
    private boolean anglesInitialized = false;
    private void initAngles(){
        // 创建EnumMap以存储角度数据
//        if(!anglesInitialized) {
            angles.put(AnglePurpose.INDEX_0_1_TO_X, (float) Math.random() * -4 - 4);
            angles.put(AnglePurpose.INDEX_0_X_TO_X, (float) Math.random() * 5 - 2.5f);
            angles.put(AnglePurpose.INDEX_0_7_TO_X, (float) Math.random() * 4 + 2);
            angles.put(AnglePurpose.INDEX_0_9_TO_X, (float) Math.random() * 4 + 2);
            angles.put(AnglePurpose.INDEX_1_1_TO_X, (float) Math.random() * 7 + 2);
            angles.put(AnglePurpose.INDEX_1_X_TO_X, (float) Math.random() * 5 - 2.5f);

            angles.put(AnglePurpose.INDEX_2_1_TO_X, (float) Math.random() * -4 - 4);
            angles.put(AnglePurpose.INDEX_2_X_TO_X, (float) Math.random() * 5 - 2.5f);
            angles.put(AnglePurpose.INDEX_2_7_TO_X, (float) Math.random() * 4 + 2);
            angles.put(AnglePurpose.INDEX_2_9_TO_X, (float) Math.random() * 4 + 2);
            angles.put(AnglePurpose.INDEX_3_1_TO_X, (float) Math.random() * 7 + 2);
            angles.put(AnglePurpose.INDEX_3_X_TO_X, (float) Math.random() * 5 - 2.5f);

            anglesInitialized = true;
//        }
    }

//    private float adjustAngle(float originalAngle, float minDelta, float maxDelta) {
//        // 生成一个在 [minDelta, maxDelta] 范围内的微小随机数
//        float delta = (float) (Math.random() * (maxDelta - minDelta) + minDelta);
//        return originalAngle + delta; // 调整角度
//    }
//    private final float minDelta = -1.0f;
//    private final float maxDelta = 1.0f;
//    private void updateAngles() {
//        // 对角度做细微调整
//        angles.put(AnglePurpose.INDEX_0_1_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_0_1_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_0_X_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_0_X_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_0_7_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_0_7_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_0_9_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_0_9_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_1_1_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_1_1_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_1_X_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_1_X_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_2_1_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_2_1_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_2_X_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_2_X_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_2_7_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_2_7_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_2_9_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_2_9_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_3_1_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_3_1_TO_X), minDelta,maxDelta));
//        angles.put(AnglePurpose.INDEX_3_X_TO_X, adjustAngle(angles.get(AnglePurpose.INDEX_3_X_TO_X), minDelta,maxDelta));
//    }

    private void init() {
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
        final int[] isClick = {1};
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initAngles();
                updateTime(new int[]{isClick[0],isClick[0],isClick[0],isClick[0]});
                isClick[0] += 1;
            }
        });
    }

    private void initOldAngle(){
        oldAngles.put(OldAngle.INDEX_0,0f);
        oldAngles.put(OldAngle.INDEX_1,0f);
        oldAngles.put(OldAngle.INDEX_2,0f);
        oldAngles.put(OldAngle.INDEX_3,0f);
    }
    private void initTime(){
        Calendar calendar = Calendar.getInstance(); // 获取系统日历对象
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 获取当前小时
        int minute = calendar.get(Calendar.MINUTE); // 获取当前分钟
        int[] newTimeDigits = new int[]{minute % 10, minute / 10, hour % 10, hour / 10}; // 转换成数组形式;
//        currentTimeDigits = newTimeDigits;
    }

    private void startDigitAnimation(int index) {
           if(index >= 4){
               return;
           }
//        postDelayed(() -> {
            currentTimeDigits[index] = tempTimeDighits[index];
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(UPDATE_TIME_ANIMATION_TIME); // 动画持续时间为1秒
            animator.setInterpolator(new OvershootInterpolator(1.5f));
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
                }else{
                    postDelayed(() -> startDigitAnimation2(index + 1), 0);
                }

//        }, 100);

    }
    //这个动画用于变化的数字的旋转
    private void startDigitAnimationRotate(int index){
        if(index >= 4){
            return;
        }
        Log.d(TAG, "startDigitAnimationRotate: " + " 1");
        ValueAnimator animatorRotate = ValueAnimator.ofFloat(0, 1);
        animatorRotate.setDuration(UPDATE_TIME_ANIMATION_TIME);
        animatorRotate.setInterpolator(new OvershootInterpolator());
        animatorRotate.addUpdateListener(animationXOffsets ->{

            float progressXOffsets = (float) animationXOffsets.getAnimatedValue();
            if(index == 0){
                switch (currentTimeDigits[index]) {
                    case 1:
                        offset[index] = oldAngles.get(OldAngle.INDEX_0) + progressXOffsets * (angles.get(AnglePurpose.INDEX_0_1_TO_X) - oldAngles.get(OldAngle.INDEX_0));
                        if(index != 0){
//                            digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.25f;
                        }

                        break;
                    case 7:
                        offset[index] = oldAngles.get(OldAngle.INDEX_0) + progressXOffsets * (angles.get(AnglePurpose.INDEX_0_7_TO_X) - oldAngles.get(OldAngle.INDEX_0));
                        break;
                    default:
                        offset[index] = oldAngles.get(OldAngle.INDEX_0) + progressXOffsets * (angles.get(AnglePurpose.INDEX_0_X_TO_X) - oldAngles.get(OldAngle.INDEX_0));
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.1f;
//                        if(oldTimeDighits[index] ==1){
//                            digitXOffsets[index] = -(digitWidths[index] * 0.25f - progressXOffsets*digitWidths[index] * 0.15f);
//                        }
                        break;

                }
//                oldAngles.put(OldAngle.INDEX_0,offset[index]);
            }else if(index == 1){
                switch (currentTimeDigits[index]){
                    case 1:
                        offset[index] = oldAngles.get(OldAngle.INDEX_1) + progressXOffsets * (angles.get(AnglePurpose.INDEX_1_1_TO_X) - oldAngles.get(OldAngle.INDEX_1));
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.2f;
                        Log.d(TAG, "startDigitAnimation: " + "123ds");
                        break;
                    default:
                        offset[index] =oldAngles.get(OldAngle.INDEX_1) + progressXOffsets * (angles.get(AnglePurpose.INDEX_1_X_TO_X) - oldAngles.get(OldAngle.INDEX_1));;
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.0f;
//                        if(oldTimeDighits[index] ==1){
//                            digitXOffsets[index] = -(digitWidths[index] * 0.25f - progressXOffsets*digitWidths[index] * 0.15f);
//                        }
                        break;
                }
//                oldAngles.put(OldAngle.INDEX_1,offset[index]);
            }else if(index == 2){
                switch (currentTimeDigits[index]) {
                    case 1:
                        offset[index] = oldAngles.get(OldAngle.INDEX_2) + progressXOffsets * (angles.get(AnglePurpose.INDEX_2_1_TO_X) - oldAngles.get(OldAngle.INDEX_2));
                        if(index != 0){
//                            digitXOffsets[index] = progressXOffsets - digitWidths[index]*0.2f;
                            Log.d(TAG, "startDigitAnimation: " + "123ds23f" + "digitXOffset["+index + "] " + digitXOffsets[index]);
                        }
                        break;
                    case 7:
                        offset[index] = oldAngles.get(OldAngle.INDEX_2) + progressXOffsets * (angles.get(AnglePurpose.INDEX_2_7_TO_X) - oldAngles.get(OldAngle.INDEX_2));
                        break;
                    default:
                        offset[index] =oldAngles.get(OldAngle.INDEX_2) + progressXOffsets * (angles.get(AnglePurpose.INDEX_2_X_TO_X) - oldAngles.get(OldAngle.INDEX_2));
                        break;
                }
//                oldAngles.put(OldAngle.INDEX_2,offset[index]);
            }else if(index == 3){
                switch (currentTimeDigits[index]){
                    case 1:
                        offset[index] = oldAngles.get(OldAngle.INDEX_3) + progressXOffsets * (angles.get(AnglePurpose.INDEX_3_1_TO_X) - oldAngles.get(OldAngle.INDEX_3));

//                        digitXOffsets[index - 1] = progressXOffsets * - digitWidths[index] * 0.35f;
                        break;
                    default:
                        offset[index] =oldAngles.get(OldAngle.INDEX_3) + progressXOffsets * (angles.get(AnglePurpose.INDEX_3_X_TO_X) - oldAngles.get(OldAngle.INDEX_3));
                        break;
                }
//                oldAngles.put(OldAngle.INDEX_3,offset[index]);
            }


        });

        animatorRotate.start();
        oldAngles.put(OldAngle.INDEX_0,offset[0]);
        oldAngles.put(OldAngle.INDEX_1,offset[1]);
        oldAngles.put(OldAngle.INDEX_2,offset[2]);
        oldAngles.put(OldAngle.INDEX_3,offset[3]);

        postDelayed(() -> startDigitAnimationRotate(index + 1), 0);
    }

    private void startDigitAnimation2(int index){

        ValueAnimator animatorXOffsets = ValueAnimator.ofFloat(0, 1);
        animatorXOffsets.setDuration(UPDATE_TIME_ANIMATION_TIME);
        animatorXOffsets.setInterpolator(new OvershootInterpolator());
        animatorXOffsets.addUpdateListener(animationXOffsets ->{
            float progressXOffsets = (float) animationXOffsets.getAnimatedValue();
//            if(index == 0){
//                switch (currentTimeDigits[index]) {
//                    case 1:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_0_1_TO_X);
//                        if(index != 0){
//                            digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.25f;
//                        }
//                        break;
//                    case 7:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_0_7_TO_X);
//                        break;
//                    default:
//                        offset[index] =progressXOffsets *  angles.get(AnglePurpose.INDEX_0_X_TO_X);
//                        if(oldTimeDighits[index] ==1){
//                            digitXOffsets[index] = -(digitWidths[index] * 0.25f - progressXOffsets*digitWidths[index] * 0.15f);
//                        }
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.1f;
//                        break;
//                }
//            }else if(index == 1){
//                switch (currentTimeDigits[index]){
//                    case 1:
//                        offset[index] =progressXOffsets *  angles.get(AnglePurpose.INDEX_1_1_TO_X);
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.2f;
//                        Log.d(TAG, "startDigitAnimation: " + "123ds");
//                        break;
//                    default:
//                        offset[index] =progressXOffsets * angles.get(AnglePurpose.INDEX_1_X_TO_X);
//                        if(oldTimeDighits[index] ==1){
//                            digitXOffsets[index] = -(digitWidths[index] * 0.25f - progressXOffsets*digitWidths[index] * 0.15f);
//                        }
//                        digitXOffsets[index] = progressXOffsets * - digitWidths[index] * 0.0f;
//                        break;
//                }
//            }else if(index == 2){
//                switch (currentTimeDigits[index]) {
//                    case 1:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_2_1_TO_X);
//                        if(index != 0){
////                            digitXOffsets[index] = progressXOffsets - digitWidths[index]*0.2f;
////                            Log.d(TAG, "startDigitAnimation: " + "123ds23f" + "digitXOffset["+index + "] " + digitXOffsets[index]);
//                        }
//                        break;
//                    case 7:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_2_7_TO_X);
//                        break;
//                    default:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_2_X_TO_X);
//                        break;
//                }
//            }else if(index == 3){
//                switch (currentTimeDigits[index]){
//                    case 1:
//                        offset[index] = progressXOffsets * angles.get(AnglePurpose.INDEX_3_1_TO_X);
//                        digitXOffsets[index - 1] = progressXOffsets * - digitWidths[index] * 0.35f;
//                        break;
//                    default:
//                        offset[index] =progressXOffsets * angles.get(AnglePurpose.INDEX_3_X_TO_X);
//                        break;
//                }
//            }

        });

        animatorXOffsets.start();

        postDelayed(() -> startDigitAnimation2(index + 1),0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        // 计算文本的总高度
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // 计算基线的 y 坐标，用于垂直居中
        float y = (screenHeight / 2) - (textHeight / 2) - fontMetrics.ascent;

        float textTop = (screenHeight / 2) - (textHeight / 2) + fontMetrics.top;

        float totalWidth = 0;
        
       // 先计算所有数字的总宽度，用于居中
        for (int i = 0; i < currentTimeDigits.length; i++) {
            digitWidths[i] = paint.measureText(String.valueOf(currentTimeDigits[i])) * 0.8f ;

            totalWidth += digitWidths[i] + digitXOffsets[i];
        }



        float colonWidth = paint.measureText(":")*0.65f;
        totalWidth += colonWidth;
        // 起始x坐标，用于居中
        float x = totalWidth + (screenWidth - totalWidth)/2;

        // 绘制数字
        for (int i = 0; i < TIME_DIGITS_LENGTH; i++) {
            canvas.save(); // 保存当前状态

            // 设置不同位置的颜色
            if (i % 2 != 0) {
                paint.setColor(getResources().getColor(R.color.purple)); // 偶数位用紫色
            } else {
                paint.setColor(getResources().getColor(R.color.shallow_purple)); // 奇数位用浅紫色
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
            if(i ==2) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(getResources().getColor(R.color.pink));
                paint.setXfermode(xfermodeLIGHTEN);
//                if(currentTimeDigits[i] == 1){
//                    x -= digitWidths[i] *0.2f;
//                }
                canvas.drawText(":", x, y - textHeight * 0.05f, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setXfermode(xfermodeADD);

            }
            // 更新下一个数字的x位置
            if(i == 1){
                x -= colonWidth/2;

            }
           x -= (digitWidths[i] + digitXOffsets[i]); // 重叠部分

            canvas.restore(); // 恢复画布状态
            isFirstDraw = false;
        }



    }

    // 模拟时间改变时启动动画
    public void updateTime(int[] newTimeDigits) {

        oldTimeDighits = Arrays.copyOf(currentTimeDigits, currentTimeDigits.length);

        tempTimeDighits=newTimeDigits;

        if(oldTimeDighits[0] != newTimeDigits[0]){

            startDigitAnimation(0);
            startDigitAnimationRotate(0);
//            startDigitAnimation3();
        }
    }
}
