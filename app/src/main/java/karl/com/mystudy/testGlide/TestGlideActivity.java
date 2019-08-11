package karl.com.mystudy.testGlide;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import karl.com.mystudy.R;
import karl.com.mystudy.testGlide.core.Glide;
import karl.com.mystudy.testGlide.listener.RequestListener;

public class TestGlideActivity extends Activity {
    private static final String[] PERMISSION_STORAGE = {"android.permission.WRITE_EXTERNAL_STORAGE" , "android.permission.READ_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 0x001;
    LinearLayout scroll_line;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_glide);

        scroll_line = findViewById(R.id.scroll_line);
        verifyStoragePermission(this);
    }

    private void verifyStoragePermission(Activity activity) {
        try{
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadOnePic(View view) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Glide.with(this).load("").loading(R.drawable.ic_launcher_background).listener(new RequestListener() {
            @Override
            public boolean onException() {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource) {
                Toast.makeText(TestGlideActivity.this, "自定义处理图片", Toast.LENGTH_SHORT).show();
                return false;
            }
        }).into(imageView);
        scroll_line.addView(imageView);
    }

    public void loadMorePic(View view) {
    }
}
