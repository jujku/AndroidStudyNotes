package com.example.a3paintapi.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {

        public MyView(Context context) {
            this(context,null);
        }

        public MyView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs,0);
        }

        public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            BlurMaskFilter bmf = null;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(68);
            paint.setStrokeWidth(5);

            bmf = new BlurMaskFilter(10f,BlurMaskFilter.Blur.NORMAL);
            paint.setMaskFilter(bmf);
            canvas.drawText("你好！你好！",100,100,paint);
            bmf = new BlurMaskFilter(10f,BlurMaskFilter.Blur.OUTER);
            paint.setMaskFilter(bmf);
            canvas.drawText("你好！你好！",100,200,paint);
            bmf = new BlurMaskFilter(10f,BlurMaskFilter.Blur.INNER);
            paint.setMaskFilter(bmf);
            canvas.drawText("你好！你好！",100,300,paint);
            bmf = new BlurMaskFilter(10f,BlurMaskFilter.Blur.SOLID);
            paint.setMaskFilter(bmf);
            canvas.drawText("你好！你好！",100,400,paint);

            setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

    }