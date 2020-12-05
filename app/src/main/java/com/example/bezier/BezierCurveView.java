package com.example.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.beziercurve.BezierCurveEvaluator;

public class BezierCurveView extends View {

    private PointF mControlPoint1;
    private PointF mControlPoint2;

    private PointF mStartPoint;
    private PointF mEndPoint;

    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    private RectF mDrawRect;

    private static final float STEP = 0.01f;

    private RectF mControlPointRect1;
    private RectF mControlPointRect2;

    private int mControlPointWidth = 20;
    private Path mPath;

    private boolean mTouchPoint1 = false;
    private boolean mTouchPoint2 = false;


    private BezierCurveEvaluator mBezierInterpolator;


    public BezierCurveView(Context context) {
        this(context, null);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5f);

        mControlPoint1 = new PointF();
        mControlPoint2 = new PointF();

        mControlPointRect1 = new RectF();
        mControlPointRect2 = new RectF();

        mStartPoint = new PointF();
        mEndPoint = new PointF();

        mDrawRect = new RectF();
        mPath = new Path();

        mBezierInterpolator = new BezierCurveEvaluator(mControlPoint1, mControlPoint2);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        int width = (mWidth - getPaddingLeft() - getPaddingRight()) / 2;
        int offsetWidth = (mWidth - width) / 2;
        int offsetHeight = (mHeight - width) / 2;
        int left = offsetWidth;
        int right = left + width;
        int top = offsetHeight;
        int bottom = top + width;
        mDrawRect.set(left, top, right, bottom);

        mStartPoint.set(mDrawRect.left, mDrawRect.bottom);
        mEndPoint.set(mDrawRect.right, mDrawRect.top);

        mControlPoint1.set(mDrawRect.centerX() / 2, mDrawRect.centerY() / 2);
        mControlPoint2.set(mDrawRect.centerX() * 3 / 4, mDrawRect.centerY() * 3 / 4);

        updateControlPointRect(mControlPointRect1, mControlPoint1);
        updateControlPointRect(mControlPointRect2, mControlPoint2);

        mBezierInterpolator.resetControlPoint(mControlPoint1, mControlPoint2);

        updatePath();

    }

    private void updateControlPointRect(RectF rectF, PointF controlPoint){
        float factor = 2f;
        rectF.set(controlPoint.x - mControlPointWidth * factor,
                controlPoint.y - mControlPointWidth * factor,
                controlPoint.x + mControlPointWidth * factor,
                controlPoint.y + mControlPointWidth * factor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mControlPoint1.x, mControlPoint1.y, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mControlPoint1.x, mControlPoint1.y, mControlPointWidth, mPaint);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(mEndPoint.x, mEndPoint.y, mControlPoint2.x, mControlPoint2.y, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mControlPoint2.x, mControlPoint2.y, mControlPointWidth, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);
        String text;
        text = "(0.0, 0.0)";
        canvas.drawText(text, mStartPoint.x, mStartPoint.y, mPaint);

        text = "(1.0, 1.0)";
        canvas.drawText(text, mEndPoint.x, mEndPoint.y, mPaint);

        float totalY = mEndPoint.y - mStartPoint.y;
        float totalX = mEndPoint.x - mStartPoint.x;
        float dx = (mControlPoint1.x - mStartPoint.x) / totalX;
        float dy = (mControlPoint1.y - mStartPoint.y) / totalY;
        text = String.format("(%.2f, %.2f)", dx, dy);
        canvas.drawText(text, mControlPoint1.x, mControlPoint1.y, mPaint);

        dx = (mControlPoint2.x - mStartPoint.x) / totalX;
        dy = (mControlPoint2.y - mStartPoint.y) / totalY;
        text = String.format("(%.2f, %.2f)", dx, dy);
        canvas.drawText(text, mControlPoint2.x, mControlPoint2.y, mPaint);


    }

    private void updatePath(){
        mPath.reset();

        mPath.moveTo(mDrawRect.left, mDrawRect.bottom);

        float i = 0;
        PointF pointF;
        while (i <= 1f){
            pointF = mBezierInterpolator.evaluate(i, mStartPoint, mEndPoint);
            mPath.lineTo(pointF.x, pointF.y);
            i += STEP;
        }


//        mPath.lineTo(mEndPoint.x, mEndPoint.y);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        boolean result = false;

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                if(mControlPointRect1.contains(x, y)){
                    mTouchPoint1 = true;
                    mTouchPoint2 = false;
                    result = true;
                } else if(mControlPointRect2.contains(x, y)){
                    mTouchPoint1 = false;
                    mTouchPoint2 = true;
                    result = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(mTouchPoint1){
                    mControlPoint1.set(x, y);
                    mBezierInterpolator.resetControlPoint(mControlPoint1, mControlPoint2);
                    updatePath();
                    invalidate();
                } else if(mTouchPoint2){
                    mControlPoint2.set(x, y);
                    mBezierInterpolator.resetControlPoint(mControlPoint1, mControlPoint2);
                    updatePath();
                    invalidate();
                }
                result = true;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                updateControlPointRect(mControlPointRect1, mControlPoint1);
                updateControlPointRect(mControlPointRect2, mControlPoint2);
                mTouchPoint1 = false;
                mTouchPoint2 = false;
                result = false;
                break;

        }

        return result;
    }

    public PointF getControlPoint1() {
        return mControlPoint1;
    }

    public PointF getControlPoint2() {
        return mControlPoint2;
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }
}
