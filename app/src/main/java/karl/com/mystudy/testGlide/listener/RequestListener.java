package karl.com.mystudy.testGlide.listener;

import android.graphics.Bitmap;

public interface RequestListener {
    boolean onException();
    boolean onResourceReady(Bitmap resource);
}
