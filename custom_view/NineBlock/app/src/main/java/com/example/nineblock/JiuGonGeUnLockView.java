package com.example.nineblock;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class JiuGonGeUnLockView extends View {

    private static int ORIGIN_COLOR = Color.parseColor("#D8D9D8");
    private static int DOWN_COLOR = Color.parseColor("#3AD94E");
    private static int UP_COLOR = Color.parseColor("#57D900");
    private static int ERROR_COLOR = Color.parseColor("#D9251E");

    private List<ArrayList<UnLockBean>> unLockPoints = new ArrayList<>();

    public List<Integer> password = new ArrayList<>();

    private static int NUMBER = 3;

    // 大圆半径
    private float bigRadius;
    // 小圆半径
    private float smallRadius;

    private Paint paint;

    private Type currentType = Type.ORIGIN;
    private Style currentStyle = Style.FILL;
    private String TAG;
    private boolean isDOWN;
    private boolean isEnableTouch = true;

    public enum Type {
        ORIGIN, DOWN, UP, ERROR
    }

    public enum Style {
        FILL, STROKE
    }
    private int getTypeColor(Type type) {
        switch (type) {
            case ORIGIN: return ORIGIN_COLOR;
            case DOWN: return DOWN_COLOR;
            case UP: return UP_COLOR;
            case ERROR: return ERROR_COLOR;
            default: return ORIGIN_COLOR;
        }
    }

    public JiuGonGeUnLockView(Context context) {
        this(context,null);
    }

    public JiuGonGeUnLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JiuGonGeUnLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.JiuGonGeUnLockView);
        ORIGIN_COLOR = array.getColor(R.styleable.JiuGonGeUnLockView_origin_color,ORIGIN_COLOR);
        DOWN_COLOR = array.getColor(R.styleable.JiuGonGeUnLockView_down_color,DOWN_COLOR);
        UP_COLOR = array.getColor(R.styleable.JiuGonGeUnLockView_up_color,UP_COLOR);
        ERROR_COLOR = array.getColor(R.styleable.JiuGonGeUnLockView_error_color,ERROR_COLOR);

        NUMBER = array.getInteger(R.styleable.JiuGonGeUnLockView_grid_size,3);



        array.recycle();


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.BEVEL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int diameter = getWidth() / NUMBER;

        float ratio = NUMBER * 2f;
        int index = 1;

        for(int i = 0;i< NUMBER;i++){
            ArrayList<UnLockBean> list = new ArrayList<>();
            for(int j = 0;j <NUMBER;j++){
                list.add(new UnLockBean(
                        getWidth() / ratio + diameter * j,
                        getHeight() / ratio + diameter * i,
                        index++
                ));
            }
            unLockPoints.add(list);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,width);

        bigRadius = (width/(NUMBER * 2))*0.7f;
        smallRadius = bigRadius *0.2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(ArrayList<UnLockBean> list : unLockPoints){
            for(UnLockBean data : list){
                paint.setColor(getTypeColor(data.getType()));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(dp(4));

                paint.setAlpha((int) (255 * 0.6));
                canvas.drawCircle(data.getX(),data.getY(),bigRadius,paint);
//                Log.d(TAG, "onDraw: " + " data.getX() " + data.getX() + " data.getY() " + data.getY() + " bigRadius " + bigRadius + " small" + smallRadius);

                paint.setAlpha(255);
                canvas.drawCircle(data.getX(),data.getY(),smallRadius,paint);
            }
        }

        paint.setColor(getTypeColor(currentType));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dp(4));
        canvas.drawPath(path,paint);

        paint.setStrokeCap(Paint.Cap.ROUND);
        if (line.first.x != 0f && line.second.x != 0f) {
            canvas.drawLine(line.first.x, line.first.y, line.second.x, line.second.y, paint);
        }

    }

    private ArrayList<UnLockBean> recordList = new ArrayList<>();
    private Path path = new Path();
    private final Pair<PointF, PointF> line = new Pair<>(new PointF(), new PointF());

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnableTouch){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                clear();
                UnLockBean pointF = isContains(event.getX(),event.getY());
                if(pointF != null){
                    pointF.setType(Type.DOWN);
                    isDOWN = true; // 表示按下
                    currentType = Type.DOWN;
                    recordList.add(pointF);

                    path.moveTo(pointF.getX(),pointF.getY());

                    line.first.set(pointF.getX(), pointF.getY());
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isDOWN) {
                    return super.onTouchEvent(event);
                }
                UnLockBean pointFMove = isContains(event.getX(),event.getY());
                if(pointFMove != null){
                    if (!recordList.contains(pointFMove)) {
                        recordList.add(pointFMove);

                        if(recordList.size() >= 2){
                            UnLockBean start = recordList.get(recordList.size() - 2);
                            UnLockBean end = recordList.get(recordList.size() - 1);

                            float d = distance(new PointF(start.getX(),start.getY()),new PointF(end.getX(),end.getY()));
                            float dx = (end.getX() - start.getX());
                            float dy = (end.getY() - start.getY());
                            float offsetX = dx * smallRadius / d;
                            float offsetY = dy * smallRadius / d;

                            float x1 = start.getX() + offsetX;
                            float y1 = start.getY() + offsetY;
                            path.moveTo(x1,y1);

                            float x2 = end.getX() - offsetX;
                            float y2 = end.getY() - offsetY;
                            path.lineTo(x2,y2);

                            line.first.set(pointFMove.getX() + offsetX,pointFMove.getY() + offsetY);
                        }

                    }
                    pointFMove.setType(Type.DOWN);
                }
                line.second.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: =====================");
                line.first.set(0f, 0f);
                line.second.set(0f, 0f);

                boolean isSuccess = recordList.size() == password.size() && IntStream.range(0, recordList.size())
                        .allMatch(i -> recordList.get(i).getIndex() == password.get(i));

                if (!isSuccess) {
                    recordList.forEach(it -> it.setType(Type.ERROR));
                    isEnableTouch = false;
                    postDelayed(() -> {
                        clear();
                        isEnableTouch = true;
                    }, 1000);
                }else{
                    recordList.forEach(it -> it.setType(Type.UP));
                }

                currentType = isSuccess ? Type.UP : Type.ERROR;

                List<Integer> indexList = new ArrayList<>();
                for (UnLockBean record : recordList) {
                    indexList.add(record.getIndex());
                }

                mListener.onPasswordEntered(indexList);
                break;
        }
        invalidate();
        return true;

    }

    public boolean contains(PointF a, PointF b, float bPadding){
        boolean isX = a.x <= b.x + bPadding && a.x >= b.x - bPadding;
        boolean isY = a.y <= b.y + bPadding && a.y >= b.y - bPadding;
        return isX && isY;
    }

    private UnLockBean isContains(float x,float y){
        for(List<UnLockBean> points : unLockPoints){
            for(UnLockBean data : points) {
                if (contains(new PointF(x, y), new PointF(data.getX(), data.getY()), bigRadius)) {
                    return data;
                }
            }
        }
        return null;
    }

    private void clear(){
        for(ArrayList<UnLockBean> list : unLockPoints){
            for(UnLockBean data : list){
                data.setType(Type.ORIGIN);
            }
        }
        recordList.clear();
        path.reset();
        isDOWN = false;
        line.first.set(0f, 0f);
        line.second.set(0f, 0f);
        invalidate();
    }

    public static float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float distance(PointF point1, PointF point2) {
        float dx = point1.x - point2.x;
        float dy = point1.y - point2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setOnPasswordListener(OnPasswordListener listener){
        mListener = listener;
    }

    private OnPasswordListener mListener;

    public interface OnPasswordListener{
        void onPasswordEntered(List<Integer> enteredPassword);
    }

    public void setPassword(List<Integer> password) {
        this.password = password;
    }

}
