package karl.com.mystudy.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import karl.com.mystudy.R;

public class ChatActivity extends Activity implements View.OnClickListener{
    TextView tv_teacher;

    ImageView iv_img;

    public boolean isAmplification=false;

    public Matrix matrix=new Matrix();
    public Bitmap bitmap;
    public float scaleWidth;
    public float scaleHeight;

    int height;
    int width;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height=dm.heightPixels;
        width=dm.widthPixels;

        String html = "下面是图片了\n: " + "<img src='http://www.qqpk.cn/Article/UploadFiles/201411/20141116135722282.jpg'/>" +
                "这也是图片\n:" +"<img src='http://h.hiphotos.baidu.com/image/pic/item/d000baa1cd11728b2027e428cafcc3cec3fd2cb5.jpg'/>" +
                "还有一张\n:"+  "<img src='http://img.61gequ.com/allimg/2011-4/201142614314278502.jpg' />";

        tv_teacher = findViewById(R.id.tv_teacher);
        iv_img = findViewById(R.id.iv_img);

        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.empty_photo)
                .load("http://www.qqpk.cn/Article/UploadFiles/201411/20141116135722282.jpg")
                .into(iv_img);

        iv_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_img){
            bitmap=((BitmapDrawable)iv_img.getDrawable()).getBitmap();//取imageview里面src 资源
            scaleWidth=width/bitmap.getWidth();//求倍率
            scaleHeight=height/bitmap.getHeight();
            if(!isAmplification)
            {
                /* 放大*/
                matrix.set(iv_img.getImageMatrix());
                matrix.postScale(scaleWidth,scaleHeight);
                Bitmap newbitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                iv_img.setImageBitmap(newbitmap);
                isAmplification=true;
            }
            else {
                /*缩小*/
                matrix.set(iv_img.getImageMatrix());
                matrix.postScale(1.0f,1.0f);
                Bitmap newbitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                iv_img.setImageBitmap(newbitmap);
                isAmplification=false;
            }
        }
    }
}
