package karl.com.mystudy.testannotation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import karl.com.mystudy.R;

public class TestAnnotationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_annotation);

        testMyAop1("在TestAnnotationActivity中测试1");
        testMyAop2("在TestAnnotationActivity中测试2");

    }

    @Override
    protected void onResume() {
        super.onResume();

        TestAnnotation testAnnotation = new TestAnnotation();
        Log.w("karl", "testAnnotation.getAnnotationStr(this): " + testAnnotation.getAnnotationStr(this));
        testAnnotation.setAnnotationStr("hello");
        testAnnotation.setAnnotationStr2();
    }

    private boolean testMyAop1(String test){
        Log.w("KARL", "testMyAop1: " + test);
        testAop();
        return false;
    }

    private boolean testMyAop2(String test){
        Log.w("KARL", "testMyAop2: " + test);
        testAop();
        return false;
    }

    public void testAop(){
        Log.w("KARL", "testAop---------------");
    }

    public void downloadFile(){
        Log.w("karl", "文件下载");
    }
}
