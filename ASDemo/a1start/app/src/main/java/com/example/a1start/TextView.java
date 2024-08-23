package com.example.a1start;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Attr;

public class TextView extends View {
    private static final String TAG = "TextView";

    //构造函数会在代码里面new的时候调用
    //TextView tv = new TextView(this)
    public TextView(Context context) {
        this(context,null);
    }

    // 在布局layout中使用
    public TextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    private AttributeSet mAttrs;
    //在布局layout中使用，但是有style的
    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下
                break;
            case MotionEvent.ACTION_MOVE:
                //手指滑动
                Log.d(TAG, "onTouchEvent: " + event.getX() + "," + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起
                break;
        }
        return true;
    }
}
