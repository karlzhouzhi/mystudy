package karl.com.mystudy.ball;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import karl.com.mystudy.R;

public class BangView extends View {
    String TAG = BangView.class.getSimpleName();

    Paint mPaint;
    Bitmap mBitmap;
    List<Ball> mBalls;
    float d = 3;
    private ValueAnimator mAnimator;

    public BangView(Context context) {
        this(context, null);
    }

    public BangView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);
        mBalls = new ArrayList<>();
        Log.i(TAG, "width: " + mBitmap.getWidth() + ", height: " + mBitmap.getHeight());
        for (int i = 0; i < mBitmap.getWidth(); i++){
            for (int j = 0; j < mBitmap.getHeight(); j++){
                Ball ball = new Ball();
                ball.color = mBitmap.getPixel(i, j);
                ball.r = d / 2;
                ball.x = i;
                ball.y = j;
                //速度(-20,20)
                ball.vX = rangInt(-20, 20);
                ball.vY = rangInt(-15, 35);

                //加速度
                ball.aX = 0f;
                ball.aY = 0.98f;

                mBalls.add(ball);
            }
        }

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            updateBall();
            invalidate();
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "start i: " + i);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "END i: " + i);
                i = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    int i = 0;
    private void updateBall() {

        Log.i(TAG, "UPDATE I: " + i);
        i++;
        for (Ball ball : mBalls) {
            ball.x += ball.vX;
            ball.y += ball.vY;
            ball.vX += ball.aX;
            ball.vY += ball.aY;
        }
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(500, 500);
        if (i>0) {
            canvas.scale(4, 4);
        }
        for (Ball ball : mBalls) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            mAnimator.start();
        }

        return super.onTouchEvent(event);
    }
}
