package myinformation;

//import androidx.appcompat.app.AlertDialog;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.myregisterlogin.R;

import function1.MyStartWalk;
import registerlogin.LoginActivity;
import shareddata.PreferenceManager;

public class MySettings extends AppCompatActivity {
    private ImageButton btn_back;
    private Button btn_logoff;
    private Switch sw_baddog;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        //20200915
        //산책시작메인 화면에서 넘어온 아이디 정보 받기
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        //백버튼
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //20200915
        //산책 기피견종 알림 설정
        sw_baddog = findViewById(R.id.sw_baddog);
        if((PreferenceManager.getString(MySettings.this,"badDogAlarm"+userID)).equals("true")){ //설정되어 있다면
            sw_baddog.setChecked(true);
        }
        else{
            sw_baddog.setChecked(false);
        }
        sw_baddog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    //활성화
                    PreferenceManager.setString(MySettings.this, "badDogAlarm"+userID,"true");
                }else{
                    //비활성화
                    PreferenceManager.setString(MySettings.this, "badDogAlarm"+userID,"false");
                }
            }
        });

        //로그오프 버튼
        //sharedPreference에
        btn_logoff = (Button) findViewById(R.id.btn_logoff);
        btn_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MySettings.this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(MySettings.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);

                                //지속 로그인 기능 -sharedPreference 기능 활용
                                PreferenceManager.setString(MySettings.this, "loginID","");
                                PreferenceManager.setString(MySettings.this, "loginPassword","");

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });


    }
}