package karl.com.mystudy.processor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import karl.com.mystudy.R;

public class ProcessorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Constants.COUNT = 2;
        Log.w("superkkone", "" + Constants.COUNT);
    }

    public void startSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
