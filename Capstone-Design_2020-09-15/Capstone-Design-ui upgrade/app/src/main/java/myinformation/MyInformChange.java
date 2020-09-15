package myinformation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import function1.MyStartWalkMain;
import function1.PopupResult;
import function1.alldogsforwalktime.AllDogs;
import function1.alldogsforwalktime.LargeDogs;
import function1.alldogsforwalktime.MediumDogs;
import function1.alldogsforwalktime.SmallDogs;
import main.MainActivity;
import registerlogin.RegisterMyInform;

public class MyInformChange extends AppCompatActivity {

    private EditText et_mydogage;
    private Button btn_apply;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inform_change);

        //MyInformCheck에서 넘어온 아이디 정보를 인텐트로 받음
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");

        //뒤로가기
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //서버에서 받은 내용을 파일에 씀
        //파일 읽기
        final String fileName = "/dbUserInform"+userID+".txt";
        final ArrayList<PopupResult> popupResultsList = new ArrayList<PopupResult>();
        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요
        //파일생성
        final File saveFile = new File(getFilesDir() , "/userData"); // 저장 경로
        //폴더생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }

        btn_apply = findViewById(R.id.btn_apply);
        et_mydogage= findViewById(R.id.et_mydogage);
        final Spinner sp_baddog = (Spinner) findViewById(R.id.sp_baddog);
        final Spinner sp_mydogspecies = (Spinner) findViewById(R.id.sp_mydogspecies);

        //사용자 정보

        //뒤로가기
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //적용하기 버튼 클릭
        btn_apply.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String myDogAge="";
                String myDogSize = "";
                String myDogSpecies = "";
                String badDog = "";

                //MyInformCheck에서 넘어온 아이디 정보를 인텐트로 받음
                Intent intent = getIntent();
                final String userID = intent.getStringExtra("userID");

                //불리 구문
                Response.Listener<String> responseListner = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        //제이슨 오브젝트 로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success)  //Myinformcheck 액티비티로 화면전환, 내정보 확인함
                            {
                                Toast.makeText(getApplicationContext(), "내 반려견 정보 등록 성공", Toast.LENGTH_LONG).show();
//                                //전 화면으로 돌아가기
                                onBackPressed(); //취소버튼 누른 효과

                            } else
                            {    //등록 실패
                                Toast.makeText(getApplicationContext(), "내 반려견 정보 등록 실패 ", Toast.LENGTH_LONG).show();
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };

                //반려견 나이 입력
                Integer myDogAge_int =null;
                String myDogAge_str = null;
                myDogAge_str = et_mydogage.getText().toString();

                //스피너에서 값 잡아서 넣기
                myDogSpecies = String.valueOf(sp_mydogspecies.getSelectedItem());
                badDog = String.valueOf(sp_baddog.getSelectedItem());

                if((myDogSpecies.length() != 0) && (badDog.length() != 0) && (myDogAge_str.length() != 0))
                {
                    //숫자로 변환하기
                    myDogAge_int = Integer.parseInt(myDogAge_str);

                    //개 이름으로 무게종류 찾기

                    AllDogs allDogs = new AllDogs();
                    myDogSize = allDogs.findDogSize(myDogSpecies);


                    try(
                            FileWriter fw = new FileWriter(saveFile+fileName, false); //덮어쓰기
                    ){

                        //사용자 정보 저장

                        StringBuffer str = new StringBuffer();
                        //리스트 속 모든 정보 (마지막꺼 뺀) + 지금 들어가는 정보 저장 (덮어쓰기)
                        //지금 덮어쓰려는 정보 덮어쓰기

                        str.append(userID +"\n");
                        str.append(myDogAge_int +"\n");
                        str.append(myDogSize +"\n");
                        str.append(myDogSpecies +"\n");
                        str.append(badDog +"\n");

                        //파일 쓰기
                        fw.write(String.valueOf(str));

                        Log.d("77777777777777777777", String.valueOf(str)+"*************************");

                    }catch(IOException e1) {
                        // TODO Auto-generated catch block
                        Log.d("77777777777777777777", " 파일 덮어쓰기 안됨 사용자 정보 "+"**********************");
                        e1.printStackTrace();
                    }

                    //서버로 볼리를 이용해서 (레지스터 리퀘스트) 요청을 함
                    MyInformChangeRequest myInformChangeRequest = new MyInformChangeRequest(userID ,myDogAge_int,myDogSize, myDogSpecies, badDog,responseListner);
                    RequestQueue queue = Volley.newRequestQueue(MyInformChange.this);
                    queue.add(myInformChangeRequest);

                }
                else{
                    Toast.makeText(MyInformChange.this, "입력 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
