# 三阶贝塞尔曲线插值器

<img src="https://github.com/grchen2018/bezier/blob/master/app/preview.gif" width="360">

## gradle
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.grchen2018:bezier:v1.0'
	}
  
  
## code
``` java
PointF control1 = new PointF(0f, 0.5f);
PointF control2 = new PointF(1f, 0.5f);
PointF start = new PointF(0f, 0f);
PointF end = new PointF(1f, 1f);
Interpolator interpolator = new BezierCurveInterpolator(control1, control2, start, end);
ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0f, 1080f);
objectAnimator.setDuration(4000);
objectAnimator.setInterpolator(interpolator);
objectAnimator.start();
```
