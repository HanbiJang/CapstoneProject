package main;
//어플 시작하면 도움말을 보여줌
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.view.WindowManager;
import android.widget.CheckBox;
import com.example.myregisterlogin.R;
import me.relex.circleindicator.CircleIndicator;
import androidx.fragment.app.FragmentPagerAdapter;


public class StartActivity extends AppCompatActivity {


    FragmentPagerAdapter adapterViewPager;

    private  CheckBox checkBox;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태창 숨기기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

        checkBox = (CheckBox)findViewById(R.id.checkBox);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

    }
}


