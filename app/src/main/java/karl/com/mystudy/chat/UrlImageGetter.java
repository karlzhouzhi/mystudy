package karl.com.mystudy.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


public class UrlImageGetter implements Html.ImageGetter {
    Context mContext;
    TextView container;
    int width;
    UrlDrawable urlDrawable;


    public UrlImageGetter(TextView t, Context c) {
        this.mContext = c;
        this.container = t;
        width = c.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new UrlDrawable();
        Log.w("karl", "source: " + source);
        Glide.with( mContext)
                .asBitmap()
                .load(source)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap loadedImage, @Nullable Transition<? super Bitmap> transition) {
                        Log.w("karl", "getByteCount:" + loadedImage.getByteCount());
                        float scaleWidth = ((float) width) / loadedImage.getWidth();
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleWidth);
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0,
                                loadedImage.getWidth(), loadedImage.getHeight(),
                                matrix, true);
                        urlDrawable.bitmap = loadedImage;
                        urlDrawable.setBounds(0, 0, loadedImage.getWidth(),
                                loadedImage.getHeight());
                        container.invalidate();
                        Log.w("karl", "container.getText(): " + container.getText());
                        container.setText(container.getText());
                    }
                });

        return urlDrawable;
    }
}
