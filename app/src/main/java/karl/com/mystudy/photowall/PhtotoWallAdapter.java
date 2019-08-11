package karl.com.mystudy.photowall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import karl.com.mystudy.R;
import karl.com.mystudy.testGlide.cache.MemeroyCache;

public class PhtotoWallAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private static final String TAG = PhtotoWallAdapter.class.getSimpleName();

    private Context mContext;

    private HashSet<ImageLoaderTask> mAllTasks = new HashSet<>();
    private AbsListView mPhotoView;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private boolean isFirstEnter = true;
    private String[] mUrls;

    private int itemCounts = 0;
    private int getviewCounts = 0;

    public PhtotoWallAdapter(Context context, AbsListView photoView, String[] urls){
        mContext = context;
        mPhotoView = photoView;
        mUrls = urls;
        mPhotoView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mUrls.length;
    }

    @Override
    public String getItem(int position) {
        return mUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        getviewCounts++;
//        Log.w(TAG, "getView getviewCounts: "  + getviewCounts);
        if (convertView==null){
            itemCounts++;
            convertView = LayoutInflater.from(mContext).inflate(R.layout.photowall_item, null);
            holder = new MyHolder();
            holder.photo = convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        }
        else {
            holder = (MyHolder) convertView.getTag();
        }

        Bitmap bitmap = MemeroyCache.getInstance().getBitmapFromMemoryCache(getItem(position));
        holder.photo.setTag(getItem(position));
        if (bitmap!=null){
            holder.photo.setImageBitmap(bitmap);
        }
        else {
            holder.photo.setImageResource(R.drawable.empty_photo);
        }

//        Log.w(TAG, "getView itemCounts: " + itemCounts);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            loadBitmap(mFirstVisibleItem, mVisibleItemCount);
        }
        else{
            cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;

        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
        // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount>0){
            loadBitmap(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    public void cancelAllTask() {
        if (mAllTasks==null){
            return;
        }
        for (ImageLoaderTask task : mAllTasks){
            task.cancel(false);
        }
    }

    private void loadBitmap(int firstVisibleItem, int visibleItemCount) {
        for (int i=firstVisibleItem; i< visibleItemCount+firstVisibleItem; i++){
            String url = mUrls[i];
            Bitmap bitmap = MemeroyCache.getInstance().getBitmapFromMemoryCache(url);
            if (bitmap==null){
                ImageLoaderTask task = new ImageLoaderTask(mAllTasks, mPhotoView);
                mAllTasks.add(task);
                task.execute(url);
            }
            else {
                ImageView imageView = mPhotoView.findViewWithTag(url);
                if (imageView!=null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private static class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

        /**
         * 图片的URL地址
         */
        private String imageUrl;

        private HashSet<ImageLoaderTask> mAllTasks;
        private WeakReference<AbsListView> mPhotoView;

        private ImageLoaderTask(HashSet<ImageLoaderTask> tasks, AbsListView photoView){
            mAllTasks = tasks;
            mPhotoView = new WeakReference<>(photoView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];

            Bitmap bitmap = null;
            HttpURLConnection urlConnection = null;
            InputStream is = null;
            try {
                URL httpUrl = new URL(imageUrl);
                urlConnection = (HttpURLConnection)httpUrl.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                is = urlConnection.getInputStream();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode==200){
                    bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap!=null) {
                        Log.w("KARL", "addBitmapToMemoryCache size: " + bitmap.getByteCount()/1024);
                        MemeroyCache.getInstance().addBitmapToMemoryCache(imageUrl, bitmap);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection!=null){
                    urlConnection.disconnect();
                }
                if (is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = mPhotoView.get().findViewWithTag(imageUrl);
            if (imageView!=null && bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
            mAllTasks.remove(this);
        }
    }

    private class MyHolder{
        ImageView photo;
    }
}
