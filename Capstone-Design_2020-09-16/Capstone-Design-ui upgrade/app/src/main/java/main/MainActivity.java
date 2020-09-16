package main;
//로그인을 해서 나오는 화면 (아이디와 로그인 띄워줘서 확인해주기)
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myregisterlogin.R;

import function1.MyStartWalkMain;
import function1.PopupActivity;
import map.MainMap;
import myinformation.MyInformCheck;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_password;

    private long backBtnTime  =0;
    private ImageButton btn_myinform, btn_help;
    private Button btn_startwalk;
    private Button btn_map;
    private Button btn_input;

    //뒤로버튼 두번 누르면 종료하기
        @Override
        public void onBackPressed(){
            long curTime = System.currentTimeMillis();
         //현재시간 가져와서 백버튼 누른시간을 빼줌
         long gapTime = curTime - backBtnTime;
         if (0 <= gapTime && 2000 >= gapTime) {
                super.onBackPressed();
                moveTaskToBack(true);
              finish();
             android.os.Process.killProcess(android.os.Process.myPid());
          } else {
             backBtnTime = curTime;
              Toast.makeText(this, "한번 더 누르면 종료됩니다", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_main);
        //로그인 화면에서 넘어온 아이디 정보 받기
        Intent intent = getIntent();
        final String userID= intent.getStringExtra("userID");
        String userPassword= intent.getStringExtra("userPassword");

        //내정보 기능
        btn_myinform = (ImageButton) findViewById(R.id.btn_myinform);

        btn_myinform.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, MyInformCheck.class);
                //MainActovity: 회원의 아이디 정보 넘기기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        //도움말 기능
        btn_help = (ImageButton) findViewById(R.id.btn_help);

        btn_help.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, SystemInform.class);
                //MainActovity: 회원의 아이디 정보 넘기기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        //1번 산책시작 기능
        btn_startwalk =  (Button) findViewById(R.id.btn_startwalk);
        btn_startwalk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, MyStartWalkMain.class);
                //MainActivity: 회원의 아이디 정보 넘기기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        //2번 경로 안내 기능 버튼
        btn_map = (Button)findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MainMap.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        //3번 만족도 기능
        //만족도 입력 버튼
        Button btn_input = findViewById(R.id.btn_input);

        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                //아이디 정보 넘기기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


    }
}
