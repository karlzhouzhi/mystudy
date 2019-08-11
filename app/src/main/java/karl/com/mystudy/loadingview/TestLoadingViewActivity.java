package karl.com.mystudy.loadingview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import karl.com.mystudy.R;

public class TestLoadingViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_loading);

//        init();
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            i++;
//            Log.w("karl", "i: " + i);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TestLoadingViewActivity.this, "timer i:" + String.valueOf(i), Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    int i= 0;
    private void init(){
//        Timer timer = new Timer();
//        timer.schedule(task, 1000, 1000);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TestLoadingViewActivity.this, "scheduleExecutorService i:" + String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        },1, 2, TimeUnit.SECONDS);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                mHandler = new Handler();
//                mHandler.postDelayed(runnable, 1000);
//                Looper.loop();
//            }
//        });
//        thread.start();


    }

    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory(){
        private AtomicInteger atoInteger = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("App-Thread" + atoInteger.getAndIncrement());
            return t;
        }
    });



    Handler mHandler;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            i++;
            Toast.makeText(TestLoadingViewActivity.this, "handler i:" +
                    String.valueOf(i) + " - " + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessage(0);
            mHandler.postDelayed(this, 1000);
        }
    };
}
