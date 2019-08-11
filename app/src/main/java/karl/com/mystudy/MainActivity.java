package karl.com.mystudy;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import karl.com.libzhi.AuthorityController;
import karl.com.libzhi.listener.IAuthorityListener;
import karl.com.mystudy.testhandler.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv_sample;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1000){
                tv_sample.setText(((User)msg.obj).toString());
            }
            super.handleMessage(msg);
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Example of a call to a native method
        tv_sample = findViewById(R.id.sample_text);
        tv_sample.setText(stringFromJNI());
//
//        TextView tv_test_handler = findViewById(R.id.tv_test_handler);
//        tv_test_handler.setOnClickListener(this);
//
//        ThreadLocal<String> stl = new ThreadLocal<>();
//        stl.set("1");
//        stl.set("2");
//        stl.set("3");
//
//        String ret = stl.get();
//
//        LinkedList f;

        AuthorityController.requestAuthority(this, new IAuthorityListener() {
            @Override
            public void onResult(boolean isSucess) {
                Log.w("mainactivity", "result: " + isSucess);
                tv_sample.setText("result: " + isSucess);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_test_handler:
                testHandler();
                break;
        }
    }

    private void testHandler(){
        new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                message.arg1 = 1000;
                User user = new User();
                user.setName("karl");
                user.setPwd("123456");
                message.obj = user;
                mHandler.sendMessage(message);
            }
        }.start();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
