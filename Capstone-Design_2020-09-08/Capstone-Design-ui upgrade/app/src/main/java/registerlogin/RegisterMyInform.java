package registerlogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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

import function1.PopupResult;
import function1.alldogsforwalktime.AllDogs;
import myinformation.MyInformChangeRequest;

public class RegisterMyInform extends AppCompatActivity {

    private EditText et_mydogage;
    private Button btn_apply ;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_my_inform);

        //뒤로가기
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //회원가입 화면에서 넘어온 회원 가입 정보를 인텐트로 받음
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String userPassword = intent.getStringExtra("userPassword");
        final String userName = intent.getStringExtra("userName");

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

        //적용하기 버튼 클릭 ++ 회원가입 기능
        btn_apply.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String myDogAge=null;
                String myDogSize =null;
                String myDogSpecies =null;
                String badDog = null;


                //회원가입
                //회원가입 버튼 내부 기능

                    //et 에 입력되어있는 값을 get해 가져와서 변수에 넣는다
                    String userID2 = userID;
                    String userPassword2 =  userPassword;
                    String  userName2 =  userName;
                    //int 정보 : Integer.parseInt( );


                //불리 구문
                Response.Listener<String> responseListner1 = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        //개정보 입력
                        //제이슨 오브젝트 로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                        try
                        {
                            JSONObject jsonObject2 = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject2.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success)  //Myinformcheck 액티비티로 화면전환, 내정보 확인함
                            {
                                //회원가입 성공 완료
                                //로그인 화면으로 돌아가기
                                Intent intent = new Intent(RegisterMyInform.this, LoginActivity.class); //시작 액티비티 , 이동할 액티비티
                                Toast.makeText(getApplicationContext(), "회원가입 되었습니다", Toast.LENGTH_LONG).show();
                                startActivity(intent);

                            } else
                            {    //등록 실패
                                Toast.makeText(getApplicationContext(), "회원가입 실패. 관리자에게 문의하세요.", Toast.LENGTH_LONG).show();
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }



                    }
                };

                //반려견 나이 입력
                Integer myDogAge_int = null;
                String myDogAge_str = null;
                myDogAge_str = et_mydogage.getText().toString();

                //스피너에서 값 잡아서 넣기
                myDogSpecies = String.valueOf(sp_mydogspecies.getSelectedItem());
                badDog = String.valueOf(sp_baddog.getSelectedItem());


                //스피너에서 넣은 값이 있을 때
                if((myDogSpecies.length() != 0) && (badDog.length() != 0) && (myDogAge_str.length() != 0))
                {
                    //(개나이) 숫자로 변환하기
                    myDogAge_int = Integer.parseInt(myDogAge_str);

                    //개 이름으로 무게종류 찾기
                    AllDogs allDogs = new AllDogs();
                    myDogSize = allDogs.findDogSize(myDogSpecies);


                    //서버로 볼리를 이용해서 (레지스터 리퀘스트) 요청을 함 (만들어진 투플 안에 개정보 입력)
                    MyInformChangeRequest myInformChangeRequest = new MyInformChangeRequest(userID ,myDogAge_int,myDogSize, myDogSpecies, badDog,responseListner1);
                    RequestQueue queue = Volley.newRequestQueue(/*registerlogin.*/RegisterMyInform.this);
                    queue.add(myInformChangeRequest);


                    //사용자 정보 파일저장
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

                    }catch(IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }


                }
                else{
                    Toast.makeText(RegisterMyInform.this, "입력 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}