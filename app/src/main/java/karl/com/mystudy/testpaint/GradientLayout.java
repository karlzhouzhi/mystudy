package karl.com.mystudy.testpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import karl.com.mystudy.R;

public class GradientLayout extends View {
    private Paint mPaint;
    private Shader mShader;
    private Bitmap mBitmap;

    public GradientLayout(Context context) {
        this(context, null);
    }

    public GradientLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientLayout(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GradientLayout(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty);

        mPaint = new Paint();
//        mPaint = new Paint();
//        mPaint.setColor(Color.RED);
//        mPaint.setARGB(255, 255, 255, 0);
//        mPaint.setAlpha(20);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(4);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeJoin(Paint.Join.MITER);
//        mPaint.setShader(new SweepGradient(200, 200, Color.BLUE, Color.RED));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
//        mPaint.setColorFilter(new LightingColorFilter(0x00ffff, 0x000000));
//        mPaint.setFilterBitmap(true);
//        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
//        mPaint.setTextScaleX(2);
//        mPaint.setTextSize(38);
//        mPaint.setTextAlign(Paint.Align.LEFT);
//        mPaint.setUnderlineText(true);
//
//        String str = "hello 世界";
//        Rect rect = new Rect();
//        mPaint.getTextBounds(str, 0, str.length(), rect);
//        mPaint.measureText(str);
//        mPaint.getFontMetrics();

//        mShader = new LinearGradient(0, 0, 500, 500, new int[]{Color.RED, Color.BLUE, Color.GREEN},
//                new float[]{0.5f, 0.7f,  1f}, Shader.TileMode.REPEAT);
//        mShader = new RadialGradient(250, 250, 250, new int[]{Color.RED, Color.BLUE, Color.GREEN},
//                new float[]{0.5f, 0.7f,  1f}, Shader.TileMode.CLAMP);
//        mShader = new SweepGradient(250, 250, Color.RED,Color.GREEN);
//        mShader = new RadialGradient(250, 250, 250, new int[]{Color.RED,Color.GREEN,Color.YELLOW},
//                null, Shader.TileMode.CLAMP);
//        mShader = new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        LinearGradient linearGradient = new LinearGradient(0, 0, 500, 500, new int[]{Color.RED, Color.BLUE, Color.GREEN},
                new float[]{0.5f, 0.7f,  1f}, Shader.TileMode.REPEAT);
        mShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);  // 图层混合
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(mShader);
        canvas.drawRect(0, 0, 1000, 1000, mPaint);
//        canvas.drawCircle(250, 250, 250, mPaint);
    }
}
