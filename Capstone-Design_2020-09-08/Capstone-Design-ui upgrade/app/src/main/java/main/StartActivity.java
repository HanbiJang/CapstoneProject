package main;
//어플 시작하면 도움말을 보여줌
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import com.example.myregisterlogin.R;
import me.relex.circleindicator.CircleIndicator;
import registerlogin.LoginActivity;
import shareddata.PreferenceManager;

import androidx.fragment.app.FragmentPagerAdapter;


public class StartActivity extends AppCompatActivity {


    FragmentPagerAdapter adapterViewPager;

    private Context mContext;
    private  CheckBox cb_dontsee;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태창 숨기기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

        cb_dontsee = findViewById(R.id.cb_dontsee);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

        mContext = this;
        //sharedPreference에 있는 dontsee가 true 이면 바로 로그인 화면으로 직행함
        boolean dontsee = PreferenceManager.getBoolean(mContext,"dontsee");

        if(dontsee == true){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
        else{ //dontsee 에 값이 없거나 false라면
            //아무것도 안함
        }

    }
}


