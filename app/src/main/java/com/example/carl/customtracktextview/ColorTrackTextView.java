package com.example.carl.customtracktextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ColorTrackTextView extends TextView {
    private Paint mChangePaint;
    private Paint mOriginPaint;
    private float mCurrentProgress = (float) 0;
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    public enum Direction{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT;
    }
    public ColorTrackTextView(Context context) {
        //super(context);
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context,attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context,attrs);
    }
    private void initPaint(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MyTrackTextView);
        int originColor = array.getColor(R.styleable.MyTrackTextView_OriginColor,0x000000);
        int changeColor = array.getColor(R.styleable.MyTrackTextView_ChangeColor,0x000000);
        mChangePaint= getPaintByColor(changeColor);
        mOriginPaint= getPaintByColor(originColor);
        //回收
        array.recycle();
    }
    public void setChangeColor(int changeColor){
        this.mChangePaint.setColor(changeColor);
    }
    public void setOriginColor(int originColor){
        this.mOriginPaint.setColor(originColor);
    }


    private Paint getPaintByColor(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;

    }
    //利用clipRect API 可以裁剪
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int middle = (int)(mCurrentProgress*getWidth());
        //绘制不变色的部分
        String text = getText().toString();
        Rect rect = new Rect(0,0,middle,getHeight());


        if (mDirection == Direction.LEFT_TO_RIGHT){
            //绘制变色的部分
            drawText(canvas,mOriginPaint,middle,getWidth());
            drawText(canvas,mChangePaint,0,middle);
        }else {

            //绘制变色的部分
            drawText(canvas,mChangePaint,getWidth()-middle,getWidth());
            drawText(canvas,mOriginPaint,0,getWidth()-middle);

        }


    }

    private void drawText(Canvas canvas,Paint paint,int start,int end){
        canvas.save();
        //绘制不变色的部分
        String text = getText().toString();
        Rect rect = new Rect(start,0,end,getHeight());
        canvas.clipRect(rect);
        Rect bounds = new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        //top 是一个负值 bottom是一给正值 top bottom是baseLine到文字底部的距离(正值)
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2+dy;
        int x = getWidth()/2 - bounds.width()/2;
        canvas.drawText(text,x,baseLine,paint);
        canvas.restore();
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;

    }

    public void setCurrentProgress(float currentProgress){
        this.mCurrentProgress = currentProgress;
        // 重新刷新绘制 -> onDraw()
        invalidate();
    }


}
