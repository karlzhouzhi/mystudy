package karl.com.mystudy.testGlide.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class MemeroyCache {
    private static final String TAG = MemeroyCache.class.getSimpleName();

    private LruCache<String, Bitmap> mMemoryCache;

    private static volatile MemeroyCache instance;

    private MemeroyCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.w(TAG, "maxMemory: " + maxMemory);

        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
//                return super.sizeOf(key, value);
//                Log.w("KARL", "bitmap size: " +  bitmap.getByteCount() / 1024);
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static MemeroyCache getInstance(){
        if (instance == null){
            synchronized (MemeroyCache.class){
                if (instance==null){
                    instance = new MemeroyCache();
                }
            }
        }

        return instance;
    }

    public Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
//        Log.w("KARL", "size cache: " + mMemoryCache.size() + ", createCount: " + mMemoryCache.createCount() + ", evictionCount: "
//        + mMemoryCache.evictionCount() + ", missCount: " + mMemoryCache.missCount() + ", putCount: " +mMemoryCache.putCount()
//        + ", hitCount: " +mMemoryCache.hitCount() + ", missCount: " + mMemoryCache.missCount() + ", maxSize: " + mMemoryCache.maxSize());
        if (getBitmapFromMemoryCache(key)==null){
            mMemoryCache.put(key, bitmap);
        }
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth){
        int sampleSize = 1;
        if (options == null){
            return sampleSize;
        }

        int outWidth = options.outWidth;

//        Log.w(TAG, "options.outWidth: " + options.outWidth  + "options.outHeight: " + options.outHeight + ", reqWidth: " + reqWidth);

        if (outWidth > reqWidth){
            sampleSize = Math.round(outWidth / (float)reqWidth);
        }

        return sampleSize;
    }

    public static Bitmap decodeFromFilepath(String bitmapPath, int reqWidth){
        if (TextUtils.isEmpty(bitmapPath)){
            return null;
        }
        File file = new File(bitmapPath);
        if (!file.exists() || !file.isFile()){
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options, reqWidth);
//        Log.w(TAG, "inSampleSize: " + options.inSampleSize);

        return BitmapFactory.decodeFile(file.getPath(), options);
    }
}
