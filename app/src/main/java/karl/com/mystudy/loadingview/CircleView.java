package karl.com.mystudy.loadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.security.acl.LastOwnerException;

public class CircleView extends View {

    private Paint mPaint;
    private int mRadius = 60;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width= MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode){
            case MeasureSpec.AT_MOST:
                Log.w("karl", "AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.w("karl", "EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.w("karl", "UNSPECIFIED");
                break;
        }

        Log.w("karl", "widthMode: " + widthMode + ", heightMode: " + heightMode
            + ", width: " + width + ", height: " + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.w("karl", "left: " + left + "top: " + top + ", right: " + right +
             ", bottom: " + bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       canvas.drawCircle(200, 200, mRadius, mPaint);
       canvas.drawRect(300, 300, 400, 400, mPaint);
    }

}
