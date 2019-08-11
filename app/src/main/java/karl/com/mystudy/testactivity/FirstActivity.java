package karl.com.mystudy.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import karl.com.mystudy.R;

import static android.view.Window.ID_ANDROID_CONTENT;

public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.first_activity);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

        FrameLayout decorView = (FrameLayout)getWindow().getDecorView();
        Log.w("mytest", "decorView child count: " + ((LinearLayout)decorView.getChildAt(0)).getChildCount());
        FrameLayout frameLayout = decorView.findViewById(ID_ANDROID_CONTENT);
        Log.w("mytest", "FrameLayout child count: " + frameLayout.getChildCount());


//        ((LinearLayout)decorView.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
        ((ViewStub)((LinearLayout)decorView.getChildAt(0)).getChildAt(0)).inflate();
        ((LinearLayout)decorView.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);

        int taskId = getTaskId();
        Log.w("mytest", "FirstActivity taskId: " + taskId);
        TextView first = findViewById(R.id.first);
        first.setText(first.getText().toString()+taskId);
    }

    public void startSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
