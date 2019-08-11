package karl.com.mystudy.photowall;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import karl.com.mystudy.R;

public class TestPhotoWallActivity extends Activity {
    PhtotoWallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photowall);
        GridView photoView = findViewById(R.id.photo_wall);
//        ListView photoView = findViewById(R.id.photo_wall);

        adapter = new PhtotoWallAdapter(this, photoView, Images.imageThumbUrls);
        photoView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        adapter.cancelAllTask();
        super.onDestroy();
    }
}
