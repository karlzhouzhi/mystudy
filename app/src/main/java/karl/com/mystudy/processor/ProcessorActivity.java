package karl.com.mystudy.processor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import karl.com.handlerlib.HandlerTest;
import karl.com.mystudy.R;

public class ProcessorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Constants.COUNT = 2;
        Log.w("superkkone", "" + Constants.COUNT);

        Toast.makeText(this, HandlerTest.getName(), Toast.LENGTH_LONG).show();
    }

    public void startSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
