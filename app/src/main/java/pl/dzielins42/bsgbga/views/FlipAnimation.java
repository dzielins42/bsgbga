package pl.dzielins42.bsgbga.views;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public abstract class FlipAnimation extends Animation {

    protected Camera camera;
    protected float centerX;
    protected float centerY;
    protected float scale;

    public FlipAnimation(Context context) {
        super();
        setDuration(800);
        int distance = 8000;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        scale = -Math.abs(displayMetrics.density * distance) / displayMetrics.densityDpi;
    }

    public FlipAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDuration(800);
        int distance = 8000;
        scale = context.getResources().getDisplayMetrics().density * distance;
    }

    public static final FlipAnimation leftOut(Context context) {
        return new LeftOut(context);
    }

    public static final FlipAnimation leftIn(Context context) {
        return new LeftIn(context);
    }

    public static final FlipAnimation rightOut(Context context) {
        return new RightOut(context);
    }

    public static final FlipAnimation rightIn(Context context) {
        return new RightIn(context);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        camera = new Camera();
        camera.setLocation(0, 0, scale);
    }

    protected abstract float getRotation(float interpolatedTime);

    protected abstract float getAlpha(float interpolatedTime);

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.setAlpha(getAlpha(interpolatedTime));
        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(getRotation(interpolatedTime));
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public static class LeftOut extends FlipAnimation {
        public LeftOut(Context context) {
            super(context);
        }

        public LeftOut(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected float getRotation(float interpolatedTime) {
            return 180.0f * interpolatedTime;
        }

        @Override
        protected float getAlpha(float interpolatedTime) {
            return interpolatedTime >= 0.5f ? 0.0f : 1.0f;
        }
    }

    public static class LeftIn extends FlipAnimation {
        public LeftIn(Context context) {
            super(context);
        }

        public LeftIn(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected float getRotation(float interpolatedTime) {
            return -180.0f + (180.0f * interpolatedTime);
        }

        @Override
        protected float getAlpha(float interpolatedTime) {
            return interpolatedTime >= 0.5f ? 1.0f : 0.0f;
        }
    }

    public static class RightOut extends FlipAnimation {
        public RightOut(Context context) {
            super(context);
        }

        public RightOut(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected float getRotation(float interpolatedTime) {
            return -180.0f * interpolatedTime;
        }

        @Override
        protected float getAlpha(float interpolatedTime) {
            return interpolatedTime >= 0.5f ? 0.0f : 1.0f;
        }
    }

    public static class RightIn extends FlipAnimation {
        public RightIn(Context context) {
            super(context);
        }

        public RightIn(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected float getRotation(float interpolatedTime) {
            return 180.0f - (180.0f * interpolatedTime);
        }

        @Override
        protected float getAlpha(float interpolatedTime) {
            return interpolatedTime >= 0.5f ? 1.0f : 0.0f;
        }
    }

}