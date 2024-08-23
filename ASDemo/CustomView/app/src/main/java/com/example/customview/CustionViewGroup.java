package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustionViewGroup extends ViewGroup {
    private int mWidth;
    private int mHeight;

    public CustionViewGroup(Context context) {
        this(context,null);
    }

    public CustionViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustionViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if(childCount == 0){
            mWidth = measureWidthAndHeight(widthMeasureSpec);
            mHeight = measureWidthAndHeight(heightMeasureSpec);
            setMeasuredDimension(mWidth,mHeight);
        }else{
            int childViewWidth = 0;
            int childViewHeight = 0;
            int childViewMarginTop = 0;
            int childViewMarginBottom = 0;
            for(int i = 0; i < childCount;i++){
                View childView = getChildAt(i);
                measureChild(childView,widthMeasureSpec,heightMeasureSpec);
                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                childViewWidth = Math.max(childViewWidth,childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                childViewHeight += childView.getMeasuredHeight();

                childViewMarginTop += lp.topMargin;
                childViewMarginBottom += lp.bottomMargin;
            }
            mWidth = childViewWidth + getPaddingLeft() + getPaddingRight();
            mHeight = childViewHeight + getPaddingTop() + getPaddingBottom() + childViewMarginBottom + childViewMarginTop;
            setMeasuredDimension(measureWidthAndHeight(widthMeasureSpec,mWidth),measureWidthAndHeight(heightMeasureSpec,mHeight));
        }


    }
    private int measureWidthAndHeight(int measureSpec,int size){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = size;
        }
        return result;
    }
    private int measureWidthAndHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = 0;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left,top,right,bottom;
        int childCount = getChildCount();
        int currentTop = getPaddingTop();
        for(int i = 0;i < childCount;i++){
            View view = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
            left = getPaddingLeft() + layoutParams.leftMargin;
            top = currentTop + layoutParams.topMargin;
            right = left + view.getMeasuredWidth();
            bottom = top + view.getMeasuredHeight();
            view.layout(left,top,right,bottom);
            currentTop += layoutParams.topMargin + layoutParams.bottomMargin + bottom - top;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
