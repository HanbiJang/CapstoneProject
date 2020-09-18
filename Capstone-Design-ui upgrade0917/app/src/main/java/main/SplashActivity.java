package main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import registerlogin.LoginActivity;
import shareddata.PreferenceManager;

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
        //20200915
        //sharedPreference에 있는 dontsee가 true 이면 바로 로그인 화면으로 직행함
        String dontsee = PreferenceManager.getString(SplashActivity.this,"dontsee");
        if(dontsee.equals("true")){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{ //dontsee이 false라면 설명화면
            //아무것도 안함
            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
