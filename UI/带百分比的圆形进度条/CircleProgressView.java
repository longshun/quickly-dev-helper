package com.longshun.circleprogressapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by longShun on 2016/12/13.
 * desc圆形进度条
 */
public class CircleProgressView extends View {

    private static final String TAG = "CircleProgressView";
    /*画圆环的画笔*/
    private Paint circlePaint;

    /*圆环的颜色*/
    private int circleColor;

    /*画进度弧长的画笔*/
    private Paint arcProgressPaint;

    /*弧的颜色*/
    private int arcProgressColor;

    /*圆环和进度环的宽度*/
    private float circleWidth;

    /*当前进度值*/
    private int curProgress;

    /*当前进度最大值*/
    private static final int MAX_DEFAULT_PROGRESS = 100;
    private int maxProgress = MAX_DEFAULT_PROGRESS;

    /*进度文字*/
    private Paint textPaint;
    private int textSize;
    private int textColor;
    private boolean isNeedShowProgressText;

    private RectF oval;
    private Rect rect;

    public CircleProgressView(Context context) {
        this(context, null);
    }


    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        /*获取自定义属性集合*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        /*获取属性值*/
        circleWidth = typedArray.getInt(R.styleable.CircleProgressView_circleWidth, 15);

        circleColor = typedArray.getColor(R.styleable.CircleProgressView_circleColor, Color.GRAY);
        arcProgressColor = typedArray.getColor(R.styleable.CircleProgressView_circleProgressColor, Color.BLUE);

        textSize = typedArray.getInt(R.styleable.CircleProgressView_textSize,50);
        textColor = typedArray.getColor(R.styleable.CircleProgressView_textColor,Color.BLACK);
        isNeedShowProgressText = typedArray.getBoolean(R.styleable.CircleProgressView_textIsNeedShow,false);

        maxProgress = typedArray.getInteger(R.styleable.CircleProgressView_max,MAX_DEFAULT_PROGRESS);
        curProgress = typedArray.getInteger(R.styleable.CircleProgressView_progress,0);

        typedArray.recycle();

        /*圆的画笔*/
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);
        circlePaint.setColor(circleColor);

        /*进度圆的画笔*/
        arcProgressPaint = new Paint();
        arcProgressPaint.setAntiAlias(true);
        arcProgressPaint.setStyle(Paint.Style.STROKE);
        arcProgressPaint.setStrokeWidth(circleWidth);
        arcProgressPaint.setColor(arcProgressColor);

        /*文字的画笔*/
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.DEFAULT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆心
        int circleCenter = getWidth() / 2;
        float circleRadius = circleCenter - circleWidth / 2;
        /*画圆环*/
        canvas.drawCircle(circleCenter, circleCenter, circleRadius, circlePaint);
        Log.d(TAG, "onDraw: circleCenter=" + circleCenter + ";circleRadius=" + circleRadius);

        /*画进度文字*/
        /*获取进度*/
        if (isNeedShowProgressText){
            int percent = (int) ((curProgress*1f/maxProgress)*100);
            Log.d(TAG, "onDraw: percent="+percent+"%");
            //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            String text = percent + "%";
            if (rect == null) {
                rect = new Rect();
            }
            textPaint.getTextBounds(text, 0, text.length(), rect);
            int width = rect.width();//文本的宽度
            int height = rect.height();//文本的高度
            canvas.drawText(percent+"%",circleCenter-width/2,circleCenter+height/2,textPaint);
        }

        /*画进度圆*/
        if (maxProgress > 0) {
            //用于定义的圆弧的形状和大小的界限
            if (oval == null) {//复用对象
                oval = new RectF(circleCenter - circleRadius, circleCenter - circleRadius, circleCenter
                        + circleRadius, circleCenter + circleRadius);
            }
            canvas.drawArc(oval, -90, 360 * (curProgress * 1f / maxProgress), false, arcProgressPaint);
            Log.d(TAG, "onDraw: angle=" + (360 * (curProgress * 1f / maxProgress)));
        }
    }

    /*设置进度最大值*/
    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress <= 0) {
            maxProgress = MAX_DEFAULT_PROGRESS;
        }
        this.maxProgress = maxProgress;
    }


    /*
     * 设置当前进度值
    * 由于考虑多线的问题，需要同步
    * 刷新界面调用postInvalidate()能在非UI线程刷新
    */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > maxProgress) {
            progress = maxProgress;
        }
        this.curProgress = progress;
        postInvalidate();
    }

    public synchronized int getCurProgress() {
        return curProgress;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getArcProgressColor() {
        return arcProgressColor;
    }

    public void setArcProgressColor(int arcProgressColor) {
        this.arcProgressColor = arcProgressColor;
    }

    public float getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isNeedShowProgressText() {
        return isNeedShowProgressText;
    }

    public void setNeedShowProgressText(boolean needShowProgressText) {
        isNeedShowProgressText = needShowProgressText;
    }
}
