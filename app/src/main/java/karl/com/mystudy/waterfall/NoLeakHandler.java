package karl.com.mystudy.waterfall;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Set;

public class NoLeakHandler extends Handler {
    private static final String TAG = NoLeakHandler.class.getSimpleName();

    public static final int SCROLL_TO_START = 0x0001;

    public static final int SCROLL_TO_END = 0x0002;

    public static final int CHECK_SCROLL_VIEW_STATUS = 0X0003;

    private WeakReference<MyScrollView> myScrollViewRef;

    private Set<LoadImageTask> taskCollections;

    private int lastScrollY = -1;

    public NoLeakHandler(MyScrollView scrollView, Set<LoadImageTask> taskCollections){
        myScrollViewRef = new WeakReference<>(scrollView);
        this.taskCollections = taskCollections;
    }

    @Override
    public void handleMessage(Message msg) {
        MyScrollView scrollView = myScrollViewRef.get();
        if (scrollView==null){
            return;
        }
        /* 滑动scrollview在Y轴方向-上滑动的距离： scrollview顶部已经滑出屏幕的距离
           监听滑动到顶部：getScrollY()==0
           监听滑动到底部： scrollview.getChildAt(0).getMeasuredHeight() == scrollview.getScrollY() + scrollview.getHeight()
           其中getHeight() scrollview的可见高度
        */
        int scrollY = scrollView.getScrollY();
        int visibleHeight = scrollView.getHeight();
        int childHeight = scrollView.getChildAt(0).getMeasuredHeight();

        Log.w(TAG, "lastScrollY: " + lastScrollY + ", scrollY: " + scrollY + ", visibleHeight: " + visibleHeight
            + ", childHeight: " + childHeight);

        // 停止滑动
        if (lastScrollY == scrollY){
            if (scrollY == 0){
                Log.w(TAG, "scrollview滑到了顶部");
            }
            if (visibleHeight + scrollY >= childHeight && taskCollections.isEmpty()){
                Log.w(TAG, "scrollview滑到了底部");
                scrollView.loadMoreImages();
            }
            scrollView.checkVisibility();
        }
        else{
            lastScrollY = scrollY;
            Message message = Message.obtain();
            message.arg1 = CHECK_SCROLL_VIEW_STATUS;

            // 间隔5ms再次检查滚动的状态（停止；正在滚动)
            msg.getTarget().sendMessageDelayed(message, 5);
        }
    }
}
