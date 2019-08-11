package karl.com.mystudy.testpaint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import karl.com.mystudy.ball.BangView;
import karl.com.mystudy.loadingview.LoadingView;
import karl.com.mystudy.testcanvas.TransfromView;

public class XfermodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new LoadingView(this));
    }
}
