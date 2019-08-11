package karl.com.mystudy.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import karl.com.mystudy.R;

public class FourthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fourth_activity);
        int taskId = getTaskId();
        Log.w("mytest", "FourthActivity taskId: " + taskId);
        TextView fourth = findViewById(R.id.fourth);
        fourth.setText(fourth.getText().toString() + taskId);
    }

    public void startFirst(View view) {
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }
}
