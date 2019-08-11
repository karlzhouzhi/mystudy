package karl.com.mystudy.waterfall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import karl.com.mystudy.R;
import karl.com.mystudy.photowall.Images;
import karl.com.mystudy.testGlide.cache.MemeroyCache;

public class MyScrollView extends ScrollView implements View.OnTouchListener, LoadImageTask.LoadImageListener {
    private static final String TAG = MyScrollView.class.getSimpleName();

    public final int PAGE_SIZE = 15;  // 每一页图片数量

    private int currentPage = 0; // 当前是第几页

    private int columnWidth;   // 每一列的宽度

    private int firstcolumnHeight = 0; // 第一列的高度

    private int secondcolumnHeight = 0; // 第二列的高度

    private int thirdcolumnHeight = 0; // 第三列的高度

    private LinearLayout firstColumnLayout; // 第一列的布局

    private LinearLayout secondColumnLayout; // 第二列的布局

    private LinearLayout thirdColumnLayout; // 第三列的布局

    private int scrollViewHeight; // scroll view 的高度

    private View directChildLayout; // scroll view的直接子布局

    private MemeroyCache mMemoryCache;

    private Set<LoadImageTask> taskCollections; // 记录正在下载或者正在等待下载的任务

    private List<ImageView> imageViewList;

    private boolean isLoaded = false;

    private NoLeakHandler mHandler;

    private Context mContext;

    public MyScrollView(Context context){
        super(context);
        mContext = context;
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        mMemoryCache = MemeroyCache.getInstance();
        taskCollections = new HashSet<>();
        imageViewList = new ArrayList<>();
        mHandler = new NoLeakHandler(this, taskCollections);
        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !isLoaded) {
            scrollViewHeight = getHeight();
            directChildLayout = getChildAt(0);

            firstColumnLayout = findViewById(R.id.first_column_layout);
            secondColumnLayout = findViewById(R.id.second_column_layout);
            thirdColumnLayout = findViewById(R.id.third_column_layout);

            columnWidth = firstColumnLayout.getWidth();
            Log.w(TAG, "columnWidth: " + columnWidth);
            isLoaded = true;

            loadMoreImages();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            Message message = Message.obtain();
            message.arg1 = NoLeakHandler.CHECK_SCROLL_VIEW_STATUS;
            mHandler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    private boolean hasSdcard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 开始下载下一页的图片，每一张图片开启一个线程
     */
    public void loadMoreImages(){
        int startIndex = currentPage * PAGE_SIZE;
        int endIndex = (currentPage + 1) * PAGE_SIZE;

        if (hasSdcard()) {
            Log.w(TAG, "startIndex : " + startIndex + ", endIndex: " + endIndex);
            if (startIndex < Images.imageThumbUrls.length) {
                Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
                if (endIndex > Images.imageThumbUrls.length){
                    endIndex = Images.imageThumbUrls.length;
                }
                for (int i=startIndex; i<endIndex; i++){
                    LoadImageTask task = new LoadImageTask(this, mMemoryCache, columnWidth);
                    taskCollections.add(task);
                    task.execute(Images.imageThumbUrls[i]);
                }
                currentPage++;
            } else {
                Toast.makeText(getContext(), "没有更多的图片", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 如果ImageView已经不在屏幕中，则设置empty image
     * 如果在屏幕中，先检查缓存是否存在，不存在则创建线程下载。
     *
     */
    public void checkVisibility(){
        for (ImageView imageView : imageViewList) {
            int topImagePos = (Integer)imageView.getTag(R.id.tag_top_pos);   // ImageView的顶部位置
            int bottomImagePos = (Integer)imageView.getTag(R.id.tag_bottom_pos);  // ImageView的底部位置

            if (topImagePos < getScrollY() + scrollViewHeight &&  bottomImagePos > getScrollY()){
                String imageUrl = (String)imageView.getTag(R.id.tag_image_url);
                Bitmap bitmap = mMemoryCache.getBitmapFromMemoryCache(imageUrl);
                Log.w(TAG, "imageview 在屏幕中");
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }
                else if (!TextUtils.isEmpty(imageUrl)){
                    LoadImageTask task = new LoadImageTask(this, mMemoryCache, columnWidth, imageView);
                    task.execute(imageUrl);
                }
            }
            else{
                // ImageView 已经不在scrollview屏幕中显示了
                imageView.setImageResource(R.drawable.empty_photo);
            }

        }
    }

    @Override
    public void onPostExecute(Bitmap bitmap, LoadImageTask task, ImageView imageView, String url, int scaleHeight) {
        addImage(bitmap, columnWidth, scaleHeight, imageView, url);
        taskCollections.remove(task);
    }

    /**
     * 向ImageView添加一张图片
     * @param bitmap
     * @param width
     * @param height
     */
    private void addImage(Bitmap bitmap, int width, int height, ImageView imageView, String url) {
        if (bitmap==null || width < 0 || height < 0){
            return;
        }

        if (imageView!=null){
            imageView.setImageBitmap(bitmap);
        }
        else {
            Log.w(TAG, "addImage 创建一个ImageView，加入到一列中 with: " + width + ", height: " + height);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            ImageView view = new ImageView(getContext());
            view.setLayoutParams(params);
            view.setPadding(5, 5, 5, 5);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(R.id.tag_image_url, url);
            view.setImageBitmap(bitmap);

            findColumnToAdd(view, height).addView(view);

            imageViewList.add(view);
        }
    }

    /**
     * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列
     * @param imageView
     * @param imageHeight
     * @return
     */
    private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight){
        Log.w(TAG, "firstcolumnHeight: " + firstcolumnHeight + ", secondcolumnHeight: " + secondcolumnHeight
            + ", thirdcolumnHeight: " + thirdcolumnHeight + ", imageHeight: " + imageHeight
                + ", firstColumnLayout.getHeight: " + firstColumnLayout.getHeight()
                + ", secondColumnLayout.getHeight: " + secondColumnLayout.getHeight()
                + ", thirdColumnLayout.getHeight: " + thirdColumnLayout.getHeight());

        if (firstcolumnHeight < secondcolumnHeight){
            if (firstcolumnHeight < thirdcolumnHeight){
                imageView.setTag(R.id.tag_top_pos, firstcolumnHeight);
                firstcolumnHeight += imageHeight;
                imageView.setTag(R.id.tag_bottom_pos, firstcolumnHeight);
                Log.w(TAG, "添加到第一列");
                return firstColumnLayout;
            }
            else {
                imageView.setTag(R.id.tag_top_pos, thirdcolumnHeight);
                thirdcolumnHeight += imageHeight;
                imageView.setTag(R.id.tag_bottom_pos, thirdcolumnHeight);
                Log.w(TAG, "添加到第三列");
                return thirdColumnLayout;
            }
        }
        else {
            if (secondcolumnHeight < thirdcolumnHeight){
                imageView.setTag(R.id.tag_top_pos, secondcolumnHeight);
                secondcolumnHeight += imageHeight;
                imageView.setTag(R.id.tag_bottom_pos, secondcolumnHeight);
                Log.w(TAG, "添加到第二列");
                return secondColumnLayout;
            }
            else {
                imageView.setTag(R.id.tag_top_pos, thirdcolumnHeight);
                thirdcolumnHeight += imageHeight;
                imageView.setTag(R.id.tag_bottom_pos, thirdcolumnHeight);
                Log.w(TAG, "添加到第三列");
                return thirdColumnLayout;
            }
        }
    }

    public void onDestroy(){
        if (taskCollections!=null){
            for (LoadImageTask task : taskCollections){
                task.cancel(false);
            }
        }
    }
}
