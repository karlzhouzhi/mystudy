package karl.com.mystudy.testpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import karl.com.mystudy.R;

public class GuaguaCard extends View {
    Paint mPaint;
    Path mPath;
    float mEventX, mEventY;
    Bitmap mResultBitmap;
    Bitmap mDest;
    Bitmap mSrc;
    int mHeight;
    int mWidth;


    public GuaguaCard(Context context) {
        this(context, null);
    }

    public GuaguaCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaguaCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(50);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mResultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.result);
        mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.eraser);
        mDest = Bitmap.createBitmap(mSrc.getWidth(), mSrc.getHeight(), Bitmap.Config.ARGB_8888);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mResultBitmap, null, new Rect(0, 0, mWidth, mResultBitmap.getHeight()), mPaint);

        int layoutId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);


        Canvas dstCanvas = new Canvas(mDest);
        dstCanvas.drawPath(mPath, mPaint);

        // 绘制目标图（下层图）
        canvas.drawBitmap(mDest, null, new Rect(0, 0, mWidth, mSrc.getHeight()), mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        // 绘制原图（上层）
        canvas.drawBitmap(mSrc, null, new Rect(0, 0, mWidth, mSrc.getHeight()), mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layoutId);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX, mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());

                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX, mEventY);

//                float endX = (event.getX() - mEventX) / 2 + mEventX;
//                float endY = (event.getY() - mEventY) / 2 + mEventY;
//                //画二阶贝塞尔曲线
//                mPath.quadTo(mEventX, mEventY, endX, endY);
//                mEventX = event.getX();
//                mEventY = event.getY();
                break;
        }
        invalidate();

        return true;
    }
}
