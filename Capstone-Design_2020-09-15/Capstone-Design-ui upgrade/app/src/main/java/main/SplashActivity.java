package main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2500); //스플래시 액티비티 보이는 시간
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
