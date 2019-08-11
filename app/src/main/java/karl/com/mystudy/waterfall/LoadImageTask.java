package karl.com.mystudy.waterfall;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import karl.com.mystudy.testGlide.cache.MemeroyCache;

class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    String TAG = LoadImageTask.class.getSimpleName();

    private WeakReference<ImageView> mImageView; // 可以重复使用的ImageView

    private LoadImageListener mListener;

    private MemeroyCache mMemeroyCache;

    private String imageUrl;

    private int columnWidth;

    private WeakReference<Activity> mActivity;

    public interface LoadImageListener{
        void onPostExecute(Bitmap bitmap, LoadImageTask task, ImageView imageView, String url, int scaleHeight);
    }
    public LoadImageTask(LoadImageListener listener, MemeroyCache memeroyCache, int columnWidth, ImageView imageView){
        mListener = listener;
        mMemeroyCache = memeroyCache;
        this.columnWidth = columnWidth;
        mImageView = new WeakReference<>(imageView);
    }

    public LoadImageTask(LoadImageListener listener, MemeroyCache memeroyCache, int columnWidth){
        mListener = listener;
        mMemeroyCache = memeroyCache;
        this.columnWidth = columnWidth;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        if (TextUtils.isEmpty(imageUrl) || mMemeroyCache==null){
            return null;
        }

        Bitmap bitmap = mMemeroyCache.getBitmapFromMemoryCache(imageUrl);
        if (bitmap==null){
            bitmap = loadImage();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mListener!=null && bitmap!=null){
            double ratio = bitmap.getWidth() / (columnWidth * 1.0);
            int scaledHeight = (int) (bitmap.getHeight() / ratio);
            Log.w(TAG, "width: " + bitmap.getWidth() + ", height:" + bitmap.getHeight() + ", scaledHeight: " + scaledHeight + ", ratio: " + ratio);
            if (mImageView==null){
                mListener.onPostExecute(bitmap, this, null, imageUrl, scaledHeight);
            }
            else if (mImageView.get()!=null){
                mListener.onPostExecute(bitmap, this, mImageView.get(), imageUrl, scaledHeight);
            }
        }
    }

    /**
     * 加载image
     */
    private Bitmap loadImage() {
        String imagePath = getImagePath();
        if (TextUtils.isEmpty(imagePath)){
            return null;
        }
        File imageFile = new File(imagePath);
        if (!imageFile.exists()){
            downloadImage(imageUrl);
        }

        Log.w("LoadImageTask", "imageFile: " + imageFile);

        if (imageUrl!=null && imageFile.exists()){
            Bitmap bitmap = MemeroyCache.decodeFromFilepath(imageFile.getPath(), columnWidth);
            if (bitmap!=null){
                mMemeroyCache.addBitmapToMemoryCache(imageUrl, bitmap);
                return bitmap;
            }
        }

        return null;
    }

    /**
     * 下载image
     * @param url
     * @return
     */
    private void downloadImage(String url){
        URL httpUrl;
        HttpsURLConnection connection;
        InputStream is = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File imageFile;

        try {
            httpUrl = new URL(url);
            connection = (HttpsURLConnection)httpUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            if(HttpsURLConnection.HTTP_OK == connection.getResponseCode()){
                is = connection.getInputStream();
                bis = new BufferedInputStream(is);

                imageFile = new File(getImagePath());
                fos = new FileOutputStream(imageFile);
                bos = new BufferedOutputStream(fos);

                byte[] bytes = new byte[1024];
                int len;
                while ((len=bis.read(bytes)) != -1){
                    bos.write(bytes, 0, len);
                    bos.flush();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos!=null){
                    bos.close();
                }
                if (fos!=null){
                    fos.close();
                }
                if (bis!=null){
                    bis.close();
                }
                if (is!=null){
                    is.close();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getImagePath(){
        if (TextUtils.isEmpty(imageUrl)){
            return null;
        }
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageName = imageUrl.substring(lastSlashIndex + 1);
        String imageDir = Environment.getExternalStorageDirectory().getPath() + "/WaterfallImages/";
        if (TextUtils.isEmpty(imageDir)){
            return null;
        }

        PermissionUtils.checkPermission(mActivity.get());

        File file = new File(imageDir);
        if (!file.exists()){
            boolean ret = file.mkdir();
            if (!ret){
                return null;
            }
        }
        else if (!file.isDirectory()){
            boolean ret  = file.delete();
            if (!ret){
                return null;
            }
            ret = file.mkdir();
            if (!ret){
                return null;
            }
        }

        return imageDir + imageName;
    }
}
