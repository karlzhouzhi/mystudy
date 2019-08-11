package karl.com.mystudy.testpaint;

import android.app.Activity;
import android.os.Bundle;

public class TestPaintActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new GradientLayout(this));
    }
}
