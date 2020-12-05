package com.example.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.beziercurve.BezierCurveInterpolator;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;

    private BezierCurveView bezierCurveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bezierCurveView = findViewById(R.id.bezier);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.iv);

//        PointF control1 = new PointF(0f, 0.5f);
//        PointF control2 = new PointF(1f, 0.5f);
//        PointF start = new PointF(0f, 0f);
//        PointF end = new PointF(1f, 1f);
//        Interpolator interpolator = new BezierCurveInterpolator(control1, control2, start, end);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0f, 1080f);
//        objectAnimator.setDuration(4000);
//        objectAnimator.setInterpolator(interpolator);
//        objectAnimator.start();

        final int width = getResources().getDisplayMetrics().widthPixels;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f,1f);
                BezierCurveInterpolator bezierCurveInterpolator = new BezierCurveInterpolator(bezierCurveView.getControlPoint1(),
                        bezierCurveView.getControlPoint2(), bezierCurveView.getStartPoint(), bezierCurveView.getEndPoint());
                valueAnimator.setInterpolator(bezierCurveInterpolator);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float)animation.getAnimatedValue();
                        Log.d("test", "onAnimationUpdate: value = " + value);

                        imageView.setTranslationX(value * (width - imageView.getMeasuredWidth()));
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        valueAnimator.removeAllUpdateListeners();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        valueAnimator.removeAllUpdateListeners();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        imageView.setTranslationX(0);
                    }
                });

                valueAnimator.setDuration(4000);
                valueAnimator.start();
            }
        });
    }
}
