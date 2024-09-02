package com.example.a2demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyView extends View {
    private Paint mPaint; //绘制线条的Path
    private Path mPath; //记录用户绘制的Path
    private Canvas mCanvas; //内存中创建的Canvas
    private Bitmap mBitmap; //缓存绘制的内容

    private List<Path> mPaths; // 保存所有绘制的路径
    private List<Path> mUndonePaths; // 保存已撤销的路径

    private int mLastX;
    private int mLastY;
    private String TAG = "MyView";


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
        mPath = new Path();

        mPaths = new ArrayList<>();
        mUndonePaths = new ArrayList<>();

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND); //结合处为圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND); //设置转弯处为圆角
        mPaint.setStrokeWidth(20); //画笔宽度
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: " + "调用绘画");
        drawPath();
        canvas.drawBitmap(mBitmap,0,0,null);

    }

    private void drawPath(){
        mCanvas.drawColor(Color.WHITE); // 每次重绘时清空画布
        for (Path path : mPaths) {
            mCanvas.drawPath(path, mPaint); // 重新绘制所有保存的路径
        }
        mCanvas.drawPath(mPath, mPaint); // 绘制当前正在绘制的路径

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);

        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
//                int dx = Math.abs(x - mLastX);
//                int dy = Math.abs(y - mLastY);
//                if(dx > 1 || dy > 1)
                    mPath.lineTo(x,y);
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mPaths.add(new Path(mPath)); // 保存当前路径到路径列表
                mPath.reset(); // 重置当前路径
                break;
        }
        invalidate();
        return true;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE); // 清空画布
//        drawPath(); // 重新绘制路径
    }
    // 撤销操作
    public void undo() {
        Log.d(TAG, "undo: " + "");
        if (!mPaths.isEmpty()) {
            mUndonePaths.add(mPaths.remove(mPaths.size() - 1)); // 将最后一个路径移到撤销列表
            invalidate();
        }
    }

    // 重做操作
    public void redo() {
        if (!mUndonePaths.isEmpty()) {
            mPaths.add(mUndonePaths.remove(mUndonePaths.size() - 1)); // 从撤销列表中恢复路径
            invalidate();
        }
    }

//    @Nullable
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("superState",super.onSaveInstanceState());
//        Log.d(TAG, "onSaveInstanceState: " + "保存数据");
//        bundle.putSerializable("path",new ArrayList<>(mPaths));
//        bundle.putSerializable("undonePaths",new ArrayList<>(mUndonePaths));
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if(state instanceof Bundle){
//            Bundle bundle = (Bundle) state;
//            Log.d(TAG, "onRestoreInstanceState: " + "恢复数据");
//
//            mPaths = (ArrayList<Path>) bundle.getSerializable("paths");
//            mUndonePaths = (ArrayList<Path>) bundle.getSerializable("undonePaths");
//
//            if (mPaths == null) {
//                mPaths = new ArrayList<>();
//            }
//            if (mUndonePaths == null) {
//                mUndonePaths = new ArrayList<>();
//            }
//
//            state = bundle.getParcelable("superState");
//        }
//        super.onRestoreInstanceState(state);
//    }
}
