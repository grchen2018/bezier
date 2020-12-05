package com.beziercurve;

import android.graphics.PointF;
import android.view.animation.Interpolator;

public class BezierCurveInterpolator implements Interpolator {

    private static final int SAMPLES_DEFAULT = 100;
    private PointF controlPoint1;
    private PointF controlPoint2;
    private PointF start;
    private PointF end;
    private float[] xValues;
    private float[] yValues;
    private int samples;

    public BezierCurveInterpolator(PointF controlPoint1, PointF controlPoint2){
        this(controlPoint1, controlPoint2, null, null, SAMPLES_DEFAULT);
    }

    public BezierCurveInterpolator(PointF controlPoint1, PointF controlPoint2, int samples){
        this(controlPoint1, controlPoint2, null, null, samples);
    }

    public BezierCurveInterpolator(PointF controlPoint1, PointF controlPoint2, PointF start, PointF end){
        this(controlPoint1, controlPoint2, start, end, SAMPLES_DEFAULT);
    }

    public BezierCurveInterpolator(PointF controlPoint1, PointF controlPoint2, PointF start, PointF end, int samples){
        this.controlPoint1 = controlPoint1;
        this.controlPoint2 = controlPoint2;
        this.samples = samples;
        if(start == null){
            this.start = new PointF(0f, 0f);
        }else {
            this.start = start;
        }
        if(end == null){
            this.end = new PointF(1f, 1f);
        }else {
            this.end = end;
        }
        initValues();
    }

    private void initValues(){
        xValues = new float[samples + 1];
        yValues = new float[samples + 1];

        for (int i = 0; i <= samples; i++){
            float input = i * 1f / samples;
            float timeLeft = 1.0f - input;

            xValues[i] = timeLeft * timeLeft * timeLeft * (start.x) + 3 * timeLeft * timeLeft * input *
                    (controlPoint1.x) + 3 * timeLeft * input *
                    input * (controlPoint2.x) + input * input * input * (end.x);
            yValues[i] = timeLeft * timeLeft * timeLeft * (start.y) + 3 * timeLeft * timeLeft * input *
                    (controlPoint1.y) + 3 * timeLeft * input *
                    input * (controlPoint2.y) + input * input * input * (end.y);
//            Log.d("TAG", "initValues: x: " + xValues[i] + " y: " + yValues[i]);
        }
    }


    @Override
    public float getInterpolation(float x) {
        //x值在 0f - 1f之间
//        Log.d("TAG", "getInterpolation: x: " + x);
        x = (1f - x) * start.x + x * end.x;
//        Log.d("TAG", "getInterpolation: x: " + x);
        float y = 0f;
        int size = xValues.length;
        if(x >= xValues[size- 1]){
            y = yValues[size - 1];
        }else if(x <= xValues[0]){
            y = yValues[0];
        }else {
            int index = -1;
            for (int i = 0; i < size- 1; i++){
                if(x >= xValues[i] && x < xValues[i + 1]){
                    index = i + 1;
                    break;
                }
            }

            if(index > 0){
//                Log.d("TAG", "getInterpolation: index: " + index + " up: " + xValues[index]);
                float ratio = Math.abs(x - xValues[index - 1]) / Math.abs(xValues[index] - xValues[index - 1]);
                y = (1f - ratio) * yValues[index - 1] + ratio * yValues[index];
            }

        }
        y = Math.abs(y - start.y) / Math.abs(end.y - start.y);
//        Log.d("TAG", "getInterpolation: y: " + y);
        return y;
    }


    public void resetControlPoint(PointF controlP1, PointF controlP2){
        this.controlPoint1 = controlP1;
        this.controlPoint2 = controlP2;
        initValues();
    }

    public void resetStartEndPoint(PointF start, PointF end){
        this.start = start;
        this.end = end;
        initValues();
    }


}
