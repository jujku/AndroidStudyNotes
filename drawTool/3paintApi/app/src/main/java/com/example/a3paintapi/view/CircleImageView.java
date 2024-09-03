package com.example.a3paintapi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.a3paintapi.R;

import java.lang.ref.WeakReference;

public class CircleImageView extends ImageView {
    private Paint mPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;
    private WeakReference<Bitmap> mWeakBitmap;
    //图片相关的属性
    private int type;                   //类型，圆形或者圆角
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 10; //圆角默认大小值
    private int mBorderRadius;  //圆角大小

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mBorderRadius = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_Radius,BODER_RADIUS_DEFAULT);
        type = typedArray.getInt(R.styleable.CircleImageView_type,TYPE_CIRCLE);
        typedArray.recycle();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE){
            int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
            setMeasuredDimension(width,width);  //设置当前view的大小
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Bitmap bitmap = mWeakBitmap == null? null : mWeakBitmap.get();
        if(bitmap == null || bitmap.isRecycled()){
            //获取图片宽高
            Drawable drawable = getDrawable();
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            if(drawable != null){
                bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
                Canvas drawCanvas = new Canvas(bitmap);
                float scale = 1.0f;
                if(type == TYPE_ROUND){
                    scale = Math.max(getWidth() * 1.0f /width,getHeight() * 1.0f/height);
                }else{
                    scale = getWidth() * 1.0F / Math.min(width,height);
                }
                //根据缩放比例 设置bounds 相当于缩放图片
                drawable.setBounds(0,0,(int) (scale * width),(int) (scale *height));
                drawable.draw(drawCanvas);

                if(mMaskBitmap == null || mMaskBitmap.isRecycled()){
                    mMaskBitmap = getBitmap();
                }

                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);

                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap,0,0,mPaint);

                //bitmap缓存起来,避免每次调用onDraw,分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);

                //绘制图片
                canvas.drawBitmap(bitmap,0,0,null);
                mPaint.setXfermode(null);

            }
        }
        if(bitmap != null){
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap,0.0f,0.0f,mPaint);
            return;
        }
    }
    //缓存Bitmap ，避免每次onDraw都重新分配内存与绘图
    public void invalidate(){
        mWeakBitmap = null;
        if(mWeakBitmap != null){
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }

    //定义一个绘制形状的方法
    private Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        paint.setColor(Color.BLACK);
        if(type == TYPE_ROUND){
            canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),mBorderRadius,mBorderRadius,paint);
        }else{
            canvas.drawCircle(getWidth() / 2,getWidth() /2,getWidth() / 2,paint);
        }
        return bitmap;
    }
}
