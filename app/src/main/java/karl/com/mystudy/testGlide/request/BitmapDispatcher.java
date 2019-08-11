package karl.com.mystudy.testGlide.request;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.concurrent.BlockingDeque;

public class BitmapDispatcher extends Thread{
    private final BlockingDeque<BitmapRequest> requestQueue;
    private Handler handler = new Handler(Looper.getMainLooper());

    public BitmapDispatcher(BlockingDeque<BitmapRequest> requestQueue){
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            // 阻塞队列
            try {
                final BitmapRequest request = requestQueue.take();
                // 子线程
                // 先展示loading
                showLoadingImage(request);

                // 加载图片url 请求网络
                final Bitmap bitmap = findBitmap(request);

                // 显示图片
                showImage(request, bitmap);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap findBitmap(BitmapRequest request) {
//        Bitmap bitmap = doubleLruCache.get(request);
//        if (bitmap!=null){
//            return bitmap;
//        }
//
//        // 网络下载
//        bitmap = downloadImage(request.getUrl());
//        if (bitmap!=null){
//            doubleLruCache.put(request, bitmap);
//        }
//
//        return bitmap;
        return null;
    }

    private void showImage(final BitmapRequest request, final Bitmap bitmap){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = request.getImageView();
                if (imageView!=null && bitmap!=null && imageView.getTag().equals(request.getUrlMD5())){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }

    /**
     * 显示加载loading id
     * @param request
     */
    private void showLoadingImage(BitmapRequest request){
        if (request.getLoadingResId() > 0 ){
            final ImageView imageView = request.getImageView();
            final int resId = request.getLoadingResId();
            if (imageView!=null){
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        // UI线程
                        imageView.setImageResource(resId);
                    }
                });
            }
        }
    }
}
