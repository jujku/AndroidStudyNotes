package com.example.a3paintapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.a3paintapi.util.ScreenUtil;

public class A2xfermodeView extends View {
    private PorterDuffXfermode porterDuffXfermode;
    private static PorterDuff.Mode PD_MODE = PorterDuff.Mode.CLEAR;
    //ADD 饱和度叠加 CLEAR 什么都没有 DARKEN取两层全部区域，交集部分颜色加深
    //DST 只保留目标图的alpha和color，所以绘制出来只有目标图
    //DST_ATOP  源图和目标图相交处绘制目标图，不相交的地方绘制源图
    //DST_IN 两者相交的地方绘制目标图，绘制的效果会受到原图处的透明度影响
    //DST_OUT  在不相交的地方绘制目标图
    //DST_OVER 目标图绘制在上方
    //LIGHTEN 取两层全部区域，点亮交集部分颜色
    //MULTIPLY 取两层交集部分后叠加颜色
    //OVERLAY 叠加
    //SCREEN  取两图层全部区域，交集部分变为透明色
    //讲DST换成SRC
    //XOR  不相交的地方按原样绘制源图和目标图
    private int screenW,screenH; //屏幕宽高
    private int width = 200; //绘制的图片宽高
    private int height = 200;
    private Bitmap srcBitmap,dstBitmap;  //上层SRC 下层DST
    public A2xfermodeView(Context context) {
        this(context,null);
    }

    public A2xfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        screenH = ScreenUtil.getScreenWidth(context);
        screenW = ScreenUtil.getScreenWidth(context);

        porterDuffXfermode = new PorterDuffXfermode(PD_MODE);

        srcBitmap = makeSrc(width,height);
        dstBitmap = makeDst(width,height);
    }

    public A2xfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Bitmap makeDst(int w,int h){
        Bitmap bm = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF26AAD1);
        c.drawOval(new RectF(0,0,w*3/4,h*3/4),p);
        return bm;
    }

    private Bitmap makeSrc(int w,int h){
        Bitmap bm = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCE43);
        c.drawRect(w/3,h/3,w*19/20,h*19/20,p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(srcBitmap,(screenW/3-width)/2,(screenH/2 - height) / 2,paint);
        canvas.drawBitmap(dstBitmap,(screenW / 3 - width)/2 + screenW / 3,(screenH/2 - height)/2,paint);

        int sc = canvas.saveLayer(0,0,screenW,screenH,null);
        canvas.drawBitmap(dstBitmap,(screenW/3 - width) / 2 + screenW/3 * 2,(screenH /2 - height) /2,paint); //绘制i
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(srcBitmap,(screenW/3 - width)/2 + screenW /3 * 2,(screenH /2 -height) /2,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }
}
