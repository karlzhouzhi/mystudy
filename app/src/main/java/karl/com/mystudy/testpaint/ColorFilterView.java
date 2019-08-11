package karl.com.mystudy.testpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import karl.com.mystudy.R;

public class ColorFilterView extends View {
    Paint mPaint;
    Bitmap mBitmap;
    LightingColorFilter lightingColorFilter;
    private int mWidth;
    private int mHeight;

    public ColorFilterView(Context context) {
        this(context, null);
    }

    public ColorFilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty);
//        mBitmap.setWidth(500);
//        mBitmap.setHeight(500);
//        lightingColorFilter = new LightingColorFilter(0x00ffff, 0x000000);
        lightingColorFilter = new LightingColorFilter(0xffffff, 0x003000);
        mPaint.setColorFilter(lightingColorFilter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.w("karl", "width: " + mWidth + ", height: " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
        mPaint.setColorFilter(porterDuffColorFilter);
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        canvas.drawBitmap(mBitmap, null, rect, mPaint);
    }
}
