package karl.com.mystudy.testannotation;

import android.content.Context;
import android.util.Log;

public class TestAnnotation {
    public String getAnnotationStr(Context context){
        String ret = "getAnnotationStr 入参 Context";
        Log.w("karl", ret);

        return ret;
    }

    public String getAnnotationStr2(){
        String ret = "getAnnotationStr2 没有入参";
        Log.w("karl", ret);
        return ret;
    }

    public void setAnnotationStr(String str){
        Log.w("karl", "setAnnotationStr");
    }

    public void setAnnotationStr2(){
        Log.w("karl", "setAnnotationStr2");
    }
}
