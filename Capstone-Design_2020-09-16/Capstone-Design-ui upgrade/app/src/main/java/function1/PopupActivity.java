package function1;

import androidx.annotation.RequiresApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myregisterlogin.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.MainActivity;

import static android.provider.Telephony.Mms.Part.FILENAME;




public class PopupActivity extends Activity {

    //정보들
    Integer new_progressValue = 50;
    boolean new_is_1_yes = false;
    boolean new_is_2_yes = false;
    //날짜
    Date today;
    SimpleDateFormat format1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        //팝업 만들어졌을 때 날짜 데이터 정해짐
        //날짜 포맷 정함
        format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        today = new Date();
//        format1.format(today).toString();

        //산책 메인 화면에서 넘어온 아이디 정보 받기
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");

        //파일 이름
        final String fileName = "/popupResult"+userID+".txt";

        //라디오 버튼
        final RadioGroup rd_1 = findViewById(R.id.rd_1);
        final RadioGroup rd_2 = findViewById(R.id.rd_2);
        final RadioButton rdbtn_1_yes = findViewById(R.id.rdbtn_1_yes);
        final RadioButton rdbtn_2_yes = findViewById(R.id.rdbtn_2_yes);


        //캔슬 버튼
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //(뒤로가기=) 액티비티(팝업) 닫기
                finish();
            }
        });

        //OK 버튼
        Button btn_ok = findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    //라디오 버튼 체크확인
                    if(rdbtn_1_yes.isChecked()){
                        new_is_1_yes = true;
                    }
                    else {
                        new_is_1_yes = false;
                    }

                    if(rdbtn_2_yes.isChecked()){
                        new_is_2_yes = true;
                    }
                    else{
                        new_is_2_yes = false;
                    }

                    //두질문 모두 체크되어야함
                    if ((rd_1.getCheckedRadioButtonId() == -1 ) || (rd_2.getCheckedRadioButtonId() == -1))
                    {
                        // no radio buttons are checked
                        Toast.makeText(PopupActivity.this, "모든 문항을 체크하세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);

                    }
                    //두질문 모두 체크 되어있을 때
                    else
                    {

                        //파일에 만족도와 yes문항 결과를 씀
                        //파일을 먼저 읽어서 읽어온 마지막 날짜 문자열 위에까지 (기존내용) 복사해서 / 파일에 써야함
                        //파일을 먼저 읽어서 읽어와서 기존내용 저장

                        //파일 읽기
                        ArrayList<PopupResult> popupResultsList = new ArrayList<PopupResult>();
                        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요
                        //파일생성
                        File saveFile = new File(getFilesDir() , "/userData"); // 저장 경로
                        //폴더생성
                        if(!saveFile.exists()){ // 폴더 없을 경우
                            saveFile.mkdir(); // 폴더 생성
                        }

                        Integer lineNum = 1;


                        try(
                                FileReader rw = new FileReader(saveFile+fileName);
                                BufferedReader br = new BufferedReader( rw );
                        ){


                            String readLine = null ;
                            Integer i = 1;
                            PopupResult buffer = new PopupResult();

                            while( ( readLine = br.readLine() ) != null ){
                                //readLine 저장해야함
                                // 1: (나머지 3) 날짜 2: (나머지 2) 1번 yes 여부 3: (나머지 1) 2번 yes여부 4: (나머지 0) 만족도 점수

                                buffer.today = readLine;
                                buffer.is_1_yes = br.readLine();
                                buffer.is_2_yes = br.readLine();
                                buffer.progressValue = Integer.parseInt(br.readLine());

                                popupResultsList.add(buffer);

                                Log.d("123", "\n"+popupResultsList.get(i-1).today +"\n"+ popupResultsList.get(i-1).is_1_yes +"\n"+ popupResultsList.get(i-1).is_2_yes +"\n"+ popupResultsList.get(i-1).progressValue +"\n"+"모든 요소"  );
                                i++;


                            }
                            //끝
                            Toast.makeText(PopupActivity.this, "만족도 결과를 저장했습니다 :)", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);

                        }catch ( IOException e ) {
                            System.out.println(e);
                        }

                        if( popupResultsList.size() == 0){ //먼저 읽어왔는데, 전에 쓴 내용이 없을 때

                            //파일 이어쓰기
                            try(
                                    // 파일 객체 생성
                                    FileWriter fw_append = new FileWriter(saveFile+fileName, true);
                            ){

                                StringBuffer str = new StringBuffer();
                                str.append(format1.format(today).toString()+"\n");
                                str.append(new_is_1_yes+"\n");
                                str.append(new_is_2_yes+"\n");
                                str.append(new_progressValue+"\n");

                                //파일 쓰기
                                fw_append.write(String.valueOf(str));

                                Toast.makeText(PopupActivity.this, "만족도 결과를 저장했습니다 :)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);

                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }

                        else{ //읽어봤더니 전에 쓴 내용이 있을 때

                            //(기존내용) 복사해서 / 파일에 쓸지 이어서 쓸지 결정
                            //마지막 popupResult 요소 = 날짜가 가장 늦음

                            //날짜가 같다면
                            if(format1.format(today).toString().equals(popupResultsList.get(popupResultsList.size() -1 ).today)){

                                try(
                                        FileWriter fw = new FileWriter(saveFile+fileName, false);
                                ){

                                    //만족도 점수 저장 (덮어쓰기)


                                    //리스트 속 모든 정보 (마지막꺼 뺀) + 지금 들어가는 정보 저장 (덮어쓰기)
                                    // 1 날짜 2 yes1 3 yes2 4만족도 점수
                                    StringBuffer str = new StringBuffer();
                                    for(int i=0 ; i < popupResultsList.size()-2 ; i++){
                                        str.append(popupResultsList.get(i).getToday()+"\n");
                                        str.append(popupResultsList.get(i).getIs_1_yes()+"\n");
                                        str.append(popupResultsList.get(i).getIs_2_yes()+"\n");
                                        str.append(popupResultsList.get(i).getProgressValue()+"\n");
                                    }
                                    //지금 덮어쓰려는 정보 덮어쓰기

                                    str.append(format1.format(today).toString()+"\n");
                                    str.append(new_is_1_yes+"\n");
                                    str.append(new_is_2_yes+"\n");
                                    str.append(new_progressValue+"\n");

                                    //파일 쓰기
                                    fw.write(String.valueOf(str));
                                    Toast.makeText(PopupActivity.this, "만족도 결과를 저장했습니다 :)", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);

                                } catch (IOException e1) {
                                    // TODO Auto-generated catch bloc
                                    e1.printStackTrace();
                                }



                            }

                            else{ //날짜가 다르다면

                                //파일 이어쓰기
                                try(
                                        FileWriter fw_append = new FileWriter(saveFile+fileName, true);
                                ){

                                    StringBuffer str = new StringBuffer();
                                    str.append(format1.format(today).toString()+"\n");
                                    str.append(new_is_1_yes+"\n");
                                    str.append(new_is_2_yes+"\n");
                                    str.append(new_progressValue+"\n");

                                    //파일 쓰기
                                    fw_append.write(String.valueOf(str));

                                    Toast.makeText(PopupActivity.this, "만족도 결과를 저장했습니다 :)", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PopupActivity.this, MainActivity.class);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);

                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                            }
                        }


                        //액티비티(팝업) 닫기
                        finish();



                    }
                        //파일에 기록하기 (끝)
                    }

                });
            //OK 버튼 끝

        //만족도 증감소 버튼
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btn_minus = findViewById(R.id.btn_minus);
        Button btn_plus = findViewById(R.id.btn_plus);

        progressBar.setProgress(new_progressValue);


        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new_progressValue < 0) {
                    new_progressValue = 0;
                }
                else if(new_progressValue > 100){
                    new_progressValue =100;
                }
                else{
                    if (new_progressValue >= 0 && new_progressValue <= 100 ) {
                        new_progressValue = new_progressValue - 10;
                    }
                }

                progressBar.setProgress(new_progressValue);
            }

            });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_progressValue < 0 ) {
                    new_progressValue = 0;
                }
                else if(new_progressValue > 100){
                    new_progressValue =100;
                }
                else{

                    if (new_progressValue >= 0 && new_progressValue <= 100 ) {
                        new_progressValue = new_progressValue + 10;
                    }
                }

                progressBar.setProgress(new_progressValue);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


}
