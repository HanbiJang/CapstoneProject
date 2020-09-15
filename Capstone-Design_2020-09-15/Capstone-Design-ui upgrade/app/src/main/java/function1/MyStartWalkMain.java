package function1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myregisterlogin.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//꺾은선 그래프
//MPAndroidChart import
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.w3c.dom.Text;

import function1.walktimecalculater.UserInform;
import function1.walktimecalculater.WalkTimeCalculater;

public class MyStartWalkMain extends AppCompatActivity {

    private ImageButton btn_cancle;
    TextView tv_walktime;
    String userID =null ;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_start_walk_main2);

        //절대경로 관련
        File saveFile = new File(getFilesDir(), "/userData"); // 저장 경로

        //메인 화면에서 넘어온 아이디 정보 받기
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");


        //만족도 파일 경로
        final String fileName1 = "/popupResult"+userID+".txt";
        //산책시간 파일 경로
        final String fileName2 = "/walkTimeMinute" + userID + ".txt";


        //백버튼
        btn_cancle = (ImageButton) findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //조회 버튼

        //오늘 산책한 시간
        tv_walktime = (TextView) findViewById(R.id.tv_walktime);
        //오늘 산책한 시간 설정 (시,분,초 단위)
        //파일에서 시간을 읽어와서 설정
        getWalkMinute();


        //산책시작 버튼
        Button btn_startwalk = findViewById(R.id.btn_startwalk);
        btn_startwalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MyStartWalkMain.this)
                        .setTitle("산책 시작").setMessage("산책을 시작하시겠습니까?")
                        .setPositiveButton("산책 시작", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(MyStartWalkMain.this, MyStartWalk.class);
                                i.putExtra("userID", userID);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();

            }
        });

        //꺾은선 그래프
        LineChart lineChart;
        ArrayList<Entry> entry_chart = new ArrayList<>();
        lineChart = findViewById(R.id.chart);//layout의 id

        //****************오류*******************//

        //chartData하나에 linedataset이 set1, set2로 두개의 라인을 가진 그래프 : 기본설정
        ArrayList<Entry> entry1 = new ArrayList<>();
        ArrayList<Entry> entry2 = new ArrayList<>();

        ForGraph forGraph = new ForGraph();
        forGraph.makeGraph(lineChart,saveFile,fileName1,fileName2,userID); //그래프 갱신까지 다함


        //개 종류에 맞게 산책 시간 설정하기
        TextView tv_recowalktime = (TextView) findViewById(R.id.tv_recowalktime);

        UserInform userInform;


        //산책시간, 산책 횟수 조정하기
        Integer recoWalkTime = 0;
        Integer recoWalkMinute = 0;
        WalkTimeCalculater walkTimeCalculater;

        walkTimeCalculater = new WalkTimeCalculater();


        //폴더생성
        if (!saveFile.exists()) { // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }

        recoWalkTime = walkTimeCalculater.WalkTimeCalculater(userID, saveFile); //추천 산책횟수 계산
        recoWalkMinute = walkTimeCalculater.WalkMinuteCalculater(userID, saveFile); //추천 산책시간 계산

        //파일에 산책횟수, 산책시간 적기 (산책시간 : 조정하기에서 값 바꿀 것)
        //파일 초기화하기
        //파일에 산책횟수 산책시간 적기


        //산책시간 조정하기
        tv_recowalktime.setText("하루 " + recoWalkTime + "회/ " + recoWalkMinute + "분");


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getWalkMinute() {

        //날짜
        Date today;
        SimpleDateFormat format1;
        format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        today = new Date();

        //파일 이름
        final String fileName = "/walkTimeMinute" + userID + ".txt";

        // 먼저 파일을 읽고 오늘 날짜 정보가 파일에 써있지 않으면 이어쓰기 (파일의 마지막 날짜 != 시스템 날짜)
        // 파일을 읽고 오늘 날짜 정보가 파일에 써있으면 그 내용에 시간을 합산하여 이어쓰기 (시간과 횟수 모두 더함)

        //파일생성
        File saveFile = new File(getFilesDir(), "/userData"); // 저장 경로

        //파일 읽기
        ArrayList<WalkTimeMinuteResult> walkTimeMinuteResultsList = new ArrayList<>();
        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요

        //폴더생성
        if (!saveFile.exists()) { // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }


        try (
                FileReader rw = new FileReader(saveFile + fileName);
                BufferedReader br = new BufferedReader(rw);
        ) {


            String readLine = null;
            Integer i = 1;

            //MyStartWalkMain 창이 만들어질때, 파일을 읽고 사용자의 이전 산책 결과들을 뽑아내어 리스트에 저장함

            while ((readLine = br.readLine()) != null) { //파일 읽기
                //readLine 저장해야함
                // 1: 날짜 2: 1번 산책 횟수 3:  산책 시간

                WalkTimeMinuteResult buffer = new WalkTimeMinuteResult();

                buffer.setToday(readLine);
                buffer.setWalkTime(Integer.parseInt(br.readLine()));
                buffer.setWalkMinute(Integer.parseInt(br.readLine()));

                walkTimeMinuteResultsList.add(buffer);

                Log.d("123", "\n" + "\n" + walkTimeMinuteResultsList.get(i - 1).getToday() + "\n" + walkTimeMinuteResultsList.get(i - 1).getWalkTime() + "\n" + walkTimeMinuteResultsList.get(i - 1).getWalkMinute() + "\n" + "모든 요소");
                i++;


            }
            //끝

        } catch (IOException e) {
            System.out.println(e);
        }

        if(walkTimeMinuteResultsList.size() != 0){ //파일을 읽었지만 오늘의 이전 산책 내용이 있는 경우 (산책 처음 하는 경우) = 이어쓰기
            if (!(format1.format(today).toString().equals(walkTimeMinuteResultsList.get(walkTimeMinuteResultsList.size() -1 ).getToday()))) {
                //파일을 읽어왔는데, 오늘 날짜에 써진 파일이 없을 때 (=가장 최근에 파일에 써진 산책기록 날짜가 오늘이 아닐 때)
                tv_walktime.setText("00회 00시간 00분 00초");
            }
            else{
                //파일을 읽어왔는데, 오늘 날짜에 써진 파일이 있을때 (=> 그것을 텍스트뷰에 출력함)

                Integer sec = (walkTimeMinuteResultsList.get(walkTimeMinuteResultsList.size()-1).getWalkMinute()) % 60;
                Integer min = (walkTimeMinuteResultsList.get(walkTimeMinuteResultsList.size()-1).getWalkMinute()) / 60;
                Integer hour = (walkTimeMinuteResultsList.get(walkTimeMinuteResultsList.size()-1).getWalkMinute()) / 360;
                tv_walktime.setText(walkTimeMinuteResultsList.get(walkTimeMinuteResultsList.size()-1).getWalkTime() +"회 "+ hour+"시간 "+ min+"분 "+ sec+"초");

            }
        }
        else{
            //아예 전체 산책 기록이 없을때 //00회 00시간 00초로 설정함

            tv_walktime.setText("00회 00시간 00분 00초");

        }


    }

}







