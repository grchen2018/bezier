package com.beziercurve;

import android.graphics.PointF;

public class BezierCurveEvaluator {

    private PointF mControlP1;
    private PointF mControlP2;

    public BezierCurveEvaluator(PointF controlP1, PointF controlP2){
        this.mControlP1 = controlP1;
        this.mControlP2 = controlP2;
    }

    public PointF evaluate(float time, PointF start, PointF end) {

            float timeLeft = 1.0f - time;
            PointF point = new PointF();

            point.x = timeLeft * timeLeft * timeLeft * (start.x) + 3 * timeLeft * timeLeft * time *
                    (mControlP1.x) + 3 * timeLeft * time *
                    time * (mControlP2.x) + time * time * time * (end.x);

            point.y = timeLeft * timeLeft * timeLeft * (start.y) + 3 * timeLeft * timeLeft * time *
                    (mControlP1.y) + 3 * timeLeft * time *
                    time * (mControlP2.y) + time * time * time * (end.y);
            return point;
    }

    public void resetControlPoint(PointF controlP1, PointF controlP2){
        this.mControlP1 = controlP1;
        this.mControlP2 = controlP2;
    }
}
