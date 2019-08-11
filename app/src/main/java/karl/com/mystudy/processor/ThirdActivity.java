package karl.com.mystudy.processor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import karl.com.mystudy.R;

public class ThirdActivity extends Activity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);
        Log.w("superkkone", "" + Constants.COUNT);
    }
}
