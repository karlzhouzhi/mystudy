package karl.com.mystudy.loadingview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import karl.com.mystudy.R;

public class LoadingView extends View {
    private static final String TAG = LoadingView.class.getSimpleName();

    private int mBackGroundColor = Color.WHITE;//0x0F000000;
    private int[] mCircleColors;
    private Paint mPaint;
    private Paint mHolePaint;
    private float mHoleRadius = 0f;
    private float mRotateAngle;
    private float mCurrentAngle = 0f;
    private float mCircleRadius = 8f;
    private float mCurrentRotateRadius = 50f;
    private float mCircleCenterX;
    private float mCircleCenterY;

    private float mMaxDistance;
    private int mDuration = 2000;

    private LoadingState mLoadingState;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mCircleColors = getResources().getIntArray(R.array.loading_view_colors);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHolePaint = new Paint();
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackGroundColor);

        mRotateAngle = (float)(2 * Math.PI / mCircleColors.length);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleCenterX = w * 1f / 2;
        mCircleCenterY = h * 1f / 2;
        mMaxDistance = (float) Math.hypot(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLoadingState == null){
            mLoadingState = new RotateState();
        }

        mLoadingState.drawState(canvas);
    }

    private abstract class LoadingState{
        abstract void drawState(Canvas canvas);
    }

    private void drawBackground(Canvas canvas){
        if (mHoleRadius > 0){
            float strokeWidth = mMaxDistance - mHoleRadius;
            float holeRadius = strokeWidth / 2 + mHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCircleCenterX, mCircleCenterY, holeRadius, mHolePaint);
        }
        else {
            canvas.drawColor(mBackGroundColor);
        }
    }

    private void drawCircle(Canvas canvas){
        for (int i = 0; i < mCircleColors.length; i++){
            float angle = i * mRotateAngle + mCurrentAngle;
            float cx = (float) (mCurrentRotateRadius * Math.cos(angle) + mCircleCenterX);
            float cy = (float) (mCurrentRotateRadius * Math.sin(angle) + mCircleCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    /**
     * 六个小球旋转
     */
    private class RotateState extends LoadingState{
        private ValueAnimator mValueAnimator;

        private RotateState(){
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI *2));
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(animation -> {
                        mCurrentAngle = (float) animation.getAnimatedValue();
                        invalidate();
                    }
            );
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new SpreadPolymerState();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            // 绘制背景
            drawBackground(canvas);
            // 绘制六个小球
            drawCircle(canvas);
        }
    }

    /**
     * 六个小球扩散聚合（收拢）
     */
    private class SpreadPolymerState extends LoadingState{
        private ValueAnimator mValueAnimator;

        private SpreadPolymerState(){
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mCurrentRotateRadius);
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f)); // 实现动画反向操作；向前甩一定值后再回到原来位置
            mValueAnimator.addUpdateListener(animation -> {
                mCurrentRotateRadius = (float)animation.getAnimatedValue();
                invalidate();
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new RippleState();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircle(canvas);
        }
    }

    /**
     * 水波纹
     */
    private class RippleState extends LoadingState{
        private ValueAnimator mValueAnimator;

        private RippleState(){
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mMaxDistance);
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(animation -> {
                mHoleRadius = (float)animation.getAnimatedValue();
                invalidate();
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }


}
