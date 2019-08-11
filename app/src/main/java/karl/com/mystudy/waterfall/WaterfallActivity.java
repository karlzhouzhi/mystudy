package karl.com.mystudy.waterfall;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import karl.com.mystudy.R;

public class WaterfallActivity extends Activity {
    public static final int CODE_FOR_WRITE_PERMISSION = 1;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_fall_layout);
    }
}
