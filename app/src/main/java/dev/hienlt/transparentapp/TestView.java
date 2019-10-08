package dev.hienlt.transparentapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TestView extends View {

    private Paint paint;
    private Paint pRect;
    private Paint pAngel;
    private ValueAnimator valueAnimator;
    private float currentAngel;
    private PointF pointTopLeft;
    private PointF pointTopRight;
    private PointF pointBottomLeft;
    private PointF pointBottomRight;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        pointTopLeft.set(0, 0);
    }

    private void init() {
        pointTopLeft = new PointF();
        pointTopRight = new PointF();
        pointBottomLeft = new PointF();
        pointBottomRight = new PointF();


        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        pRect = new Paint();
        pRect.setStyle(Paint.Style.FILL);
        pRect.setColor(Color.BLACK);

        pAngel = new Paint();
        pAngel.setStyle(Paint.Style.STROKE);
        pAngel.setColor(Color.BLUE);
        pAngel.setStrokeWidth(70);


        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(4000);
        valueAnimator.setFloatValues(0, 360);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngel = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.YELLOW);
        canvas.drawRect(0,0, getWidth(), getHeight(), paint);

        //Draw top left
//        canvas.drawRect(-50, -50,  + 50, 0 + 50, pRect);
        canvas.drawCircle(0,0, 50, pRect);

        //Draw top right
        canvas.drawRect(getWidth() - 100, 0, getWidth(), 100, pRect);

        //Draw bottom left;
        canvas.drawRect(0, getHeight() - 100, 100, getHeight(), pRect);

        //Draw bottom right
        canvas.drawRect(getWidth() - 100, getHeight() - 100, getWidth(), getHeight(), pRect);

        //Draw center
        float startX = (getWidth() - 200) / 2f;
        float startY = (getHeight() - 200) / 2f;
        canvas.drawRect(startX, startY, startX + 200, startY + 200, pRect);

        //Draw circle
        canvas.drawArc(40, 40, getWidth() - 40, getHeight() - 40, 270, currentAngel, false, pAngel);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }
}
