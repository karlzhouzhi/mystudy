package karl.com.mystudy.testGlide.request;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import karl.com.mystudy.testGlide.listener.RequestListener;
import karl.com.mystudy.testGlide.util.MD5Utils;

public class BitmapRequest {
    private String url;
    private WeakReference<ImageView> weakReference;
    private String urlMD5;

    // 正在等待的图片
    private int loadingResId;
    private Context context;

    private RequestListener requestListener;

    public BitmapRequest(Context context){
        this.context = context;
    }

    public BitmapRequest loading(int loadingResId){
        this.loadingResId = loadingResId;
        return this;
    }

    public BitmapRequest listener(RequestListener requestListener){
        this.requestListener = requestListener;
        return this;
    }

    public BitmapRequest load(String url){
        this.url = url;
        this.urlMD5 = MD5Utils.toMD5(url);
        return this;
    }

    public void into(ImageView imageView){
        this.weakReference = new WeakReference<>(imageView);
        imageView.setTag(urlMD5); //防止图片错位
    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return weakReference.get();
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public int getLoadingResId() {
        return loadingResId;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }
}
