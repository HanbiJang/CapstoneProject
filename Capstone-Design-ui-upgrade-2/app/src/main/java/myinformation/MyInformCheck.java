package myinformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MyInformCheck extends AppCompatActivity {
    private TextView tv_mydogage, tv_mydogsize, tv_mydogspecies, tv_baddog;
    private Button btn_edit , btn_see;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinformcheck_2);

        tv_mydogage = findViewById(R.id.tv_mydogage);
        tv_mydogsize = findViewById(R.id.tv_mydogsize);
        tv_mydogspecies = findViewById(R.id.tv_mydogspecies);
        tv_baddog = (TextView) findViewById(R.id.tv_baddog);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_see = (Button) findViewById(R.id.btn_see);


        //MainActivity에서 넘어온 아이디 정보를 인텐트로 받음
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

        //버튼 클릭시 수정화면으로 화면 전환
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);//메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


        //그냥 화면에서도 보이기
        //불리 구문, 서버에 반려견 데이터 요청 구문
        Response.Listener<String> responseListner = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                try
                {
                    JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                    boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                    if (success)
                    {
                        String myDogAge = jsonObject.getString("myDogAge");
                        String myDogSize = jsonObject.getString("myDogSize");
                        String myDogSpecies = jsonObject.getString("myDogSpecies");
                        String badDog = jsonObject.getString("badDog"); //php문으로 값 가져와서 변수안에 넣음

                        //텍스트 뷰에 데이터베이스에 있는 유저의 정보 보여주기
                        tv_mydogage.setText(myDogAge+" 개월");
                        tv_mydogsize.setText(myDogSize);
                        tv_mydogspecies.setText(myDogSpecies);
                        tv_baddog.setText(badDog);

                        //다음에 필요할 인텐트에 넣어주기 MyInformCheck -> MyInformChange
                        Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);
                        intent.putExtra("userID", userID);
                    }
                    else
                    {    //내정보 조회 실패
                        return;
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
        //서버에서 정보 받아오기
        Integer myDogAge = 0;
        String myDogSize = "";
        String myDogSpecies = "";
        String badDog = "";

        MyInformCheckRequest myInformCheckRequest = new MyInformCheckRequest(userID, myDogAge, myDogSize, myDogSpecies, badDog, responseListner);
        RequestQueue queue = Volley.newRequestQueue(MyInformCheck.this);
        queue.add(myInformCheckRequest);

        btn_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //불리 구문, 서버에 반려견 데이터 요청 구문
                Response.Listener<String> responseListner = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success)
                            {
                                String myDogAge = jsonObject.getString("myDogAge");
                                String myDogSize = jsonObject.getString("myDogSize");
                                String myDogSpecies = jsonObject.getString("myDogSpecies");
                                String badDog = jsonObject.getString("badDog"); //php문으로 값 가져와서 변수안에 넣음

                                //텍스트 뷰에 데이터베이스에 있는 유저의 정보 보여주기
                                tv_mydogage.setText(myDogAge+" 개월");
                                tv_mydogsize.setText(myDogSize);
                                tv_mydogspecies.setText(myDogSpecies);
                                tv_baddog.setText(badDog);

                                //다음에 필요할 인텐트에 넣어주기 MyInformCheck -> MyInformChange
                                Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);
                                intent.putExtra("userID", userID);
                            }
                            else
                            {    //내정보 조회 실패
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };
                //서버에서 정보 받아오기
                Integer myDogAge = 0;
                String myDogSize = "";
                String myDogSpecies = "";
                String badDog = "";

                MyInformCheckRequest myInformCheckRequest = new MyInformCheckRequest(userID, myDogAge, myDogSize, myDogSpecies, badDog, responseListner);
                RequestQueue queue = Volley.newRequestQueue(MyInformCheck.this);
                queue.add(myInformCheckRequest);
            }
        });




    }
}