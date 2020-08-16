package function1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myregisterlogin.R;

import main.MainActivity;

public class MyStartWalk extends AppCompatActivity {

    //버튼 정의
    private ImageButton btn_cancle;
    private ImageButton imgbtn_play;
    private ImageButton imgbtn_stop;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_start_walk);

        //산책시작메인 화면에서 넘어온 아이디 정보 받기
        Intent intent = getIntent();
        final String userID= intent.getStringExtra("userID");

        //버튼 찾기
        btn_cancle = (ImageButton) findViewById(R.id.btn_cancle);
        imgbtn_play = (ImageButton) findViewById(R.id.imgbtn_play);
        imgbtn_stop = (ImageButton) findViewById(R.id.imgbtn_stop);
        btn_finish = (Button) findViewById(R.id.btn_finish);

        //텍스트뷰
        TextView tv_walktime = (TextView) findViewById(R.id.tv_recowalktime);
        TextView tv_recowalktime = (TextView) findViewById(R.id.tv_recowalktime);

        //추천 산책시간 계산하여 tv_recowalktime에 반영하기
        set_tv_recowalktime();

        //산책 취소 버튼
        btn_cancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //산책 재생 버튼
        imgbtn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //산책한 산책시간 흘러가게 하기
                playWalkTime();
                //tv_walktime 에 산책시간 반영하기
                set_tv_walktime();

            }
        });
        //산책 일시정지 버튼
        imgbtn_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //산책한 산책시간 일시정지 시키기
                stopWalkTime();
                //tv_walktime 에 산책시간 반영하기
                set_tv_walktime();

            }
        });

        //산책 종료 버튼
        btn_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //산책한 시간 파일에 저장
                saveWalkTime();

                //메인화면으로 가기
                Intent intent =  new Intent(MyStartWalk.this, MainActivity.class);
                //MainActivity: 회원의 아이디 정보 넘기기
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });


    }

    //추천 산책시간 계산하여 tv_recowalktime에 반영하기
    void set_tv_recowalktime(){

    }

    //산책한 산책시간 흘러가게 하기
    void playWalkTime(){

    }

    //tv_walktime 에 산책시간 반영하기
    void set_tv_walktime(){

    }


    //산책한 산책시간 일시정지 시키기
    void stopWalkTime(){

    }

    //산책한 시간 파일에 저장
    void saveWalkTime(){

    }
}//클래스 괄호
