package karl.com.mystudy.chat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class UrlDrawable extends BitmapDrawable {
    protected Bitmap bitmap;
    @Override
    public void draw(Canvas canvas) {
        // override the draw to facilitate refresh function later
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, getPaint());
        }
    }
}
