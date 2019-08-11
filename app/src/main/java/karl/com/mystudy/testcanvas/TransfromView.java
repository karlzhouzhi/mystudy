package karl.com.mystudy.testcanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TransfromView extends View {
    Paint mPaint;

    public TransfromView(Context context) {
        this(context, null);
    }

    public TransfromView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransfromView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Log.w("TransfromView", ""+canvas.getSaveCount());
//        canvas.drawRect(0, 0, 500, 500, mPaint);
//        int state = canvas.save();
//        Log.w("TransfromView", ""+canvas.getSaveCount());
//        canvas.translate(100, 100);
////        canvas.scale(0.5f, 0.5f, 100, 100);
//
//        mPaint.setColor(Color.GREEN);
//        canvas.drawRect(0, 0, 500, 500, mPaint);
//
//        canvas.save();
//        Log.w("TransfromView", ""+canvas.getSaveCount());
//        canvas.translate(100, 100);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawLine(0, 0, 100, 100, mPaint);
//
//        canvas.restoreToCount(state);
//        mPaint.setColor(Color.BLACK);
//        canvas.drawRect(0, 0, 100, 100, mPaint);

        canvas.drawRect(300, 300, 500, 500, mPaint);

        int layerId = canvas.saveLayer(0, 0, 700, 700, mPaint);
        Matrix matrix = new Matrix();
        matrix.setTranslate(100, 100);
        canvas.setMatrix(matrix);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 300, 300, mPaint);

        mPaint.setColor(Color.BLACK);
        canvas.drawRect(50, 50, 700, 700, mPaint);

        canvas.restoreToCount(layerId);

        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, 300, 300, mPaint);

    }
}
