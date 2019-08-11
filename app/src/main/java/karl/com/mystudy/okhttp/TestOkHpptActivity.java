package karl.com.mystudy.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import karl.com.mystudy.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOkHpptActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_okhttp);
    }

    public void sendRequest(View view) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.baidu.com/")
                .build();

        Call newCall = client.newCall(request);

        try {
            Response response = newCall.execute();
            assert response.body() != null;
            Log.w("karl", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                Log.w("karl", response.body().string());
            }
        });
    }
}
