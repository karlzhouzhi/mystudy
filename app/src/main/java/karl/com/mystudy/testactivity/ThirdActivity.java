package karl.com.mystudy.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import karl.com.mystudy.R;

public class ThirdActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.third_activity);
        int taskId = getTaskId();
        Log.w("mytest", "ThirdActivity taskId: " + taskId);
        TextView third = findViewById(R.id.third);
        third.setText(third.getText().toString() + taskId);
    }


    public void startFourth(View view) {
        Intent intent = new Intent(this, FourthActivity.class);
        startActivity(intent);
    }
}
