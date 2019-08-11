package karl.com.mystudy.testGlide.core;

import android.app.Activity;

import karl.com.mystudy.testGlide.request.BitmapRequest;

public class Glide {
    public static BitmapRequest with(Activity activity){
        return new BitmapRequest(activity);
    }
}
