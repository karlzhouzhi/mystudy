package karl.com.mystudy.testGlide.request;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {
    private static RequestManager instance;

    private RequestManager(){}

    public static RequestManager getInstance(){
        if (instance == null){
            synchronized (RequestManager.class){
                if (instance == null){
                    instance = new RequestManager();
                }
            }
        }

        return instance;
    }

    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();


    public void addBitmapRequest(BitmapRequest request){
        if (requestQueue.contains(request)){
            requestQueue.add(request);
        }
        else {
            Log.i("err", "任务存在，不用添加");
        }
    }
}
