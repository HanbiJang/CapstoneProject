package registerlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_password, et_name;
    private Button btn_register;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2); //컨트롤+클릭 => xml 파일로 이동됨

        et_id = findViewById(R.id.et_mydogage);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.btn_back);

        //뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //회원가입
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            //회원가입 버튼 내부 기능
            @Override
            public void onClick(View view){
                //et 에 입력되어있는 값을 get해 가져와서 변수에 넣는다
                String userID =  et_id.getText().toString();
                String userPassword =  et_password.getText().toString();
                String  userName =  et_name.getText().toString();

                final String userID2 = userID;
                final String userPassword2 = userPassword;
                final String userName2 = userName;

                ////

                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //회원가입
                        //제이슨 오브젝트 로 서버 전송 으로 회원가입함 (일반 String 사용할수 없기때문) = 운반체
                        try {
                            JSONObject jsonObject1 = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject1.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success) {

                                //회원가입 성공 시
                                Intent intent = new Intent(RegisterActivity.this, RegisterMyInform.class); //시작 액티비티 , 이동할 액티비티
                                intent.putExtra("userID",userID2);
                                intent.putExtra("userPassword",userPassword2);
                                intent.putExtra("userName",userName2);

                                //반려견 화면으로 이동
                                startActivity(intent);

                            } else {
                                //회원등록 실패
                                Toast.makeText(getApplicationContext(), "회원 등록 실패 ", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };

                        ////

                        if( userID2.length() != 0 && userPassword2.length() != 0 && userName2.length() != 0 ){

                            //서버로 볼리를 이용해서 (레지스터 리퀘스트) 요청을 함 (회원 투플 추가)
                            RegisterRequest registerRequest = new RegisterRequest(userID,userPassword,userName, responseListner);
                            RequestQueue queue1 = Volley.newRequestQueue(RegisterActivity.this);
                            queue1.add(registerRequest);

                        }

                        else{
                            Toast.makeText(RegisterActivity.this, "입력 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }


                    }




        });
    }
}

//            //회원가입 버튼 내부 기능
//            @Override
//            public void onClick(View view)
//            {
//                //et 에 입력되어있는 값을 get해 가져와서 변수에 넣는다
//                String userID =  et_id.getText().toString();
//                String userPassword =  et_password.getText().toString();
//                String  userName =  et_name.getText().toString();
//
//                final String userID2 = userID;
//                final String userPassword2 = userPassword;
//                final String userName2 = userName;
//
//                Response.Listener<String> responseListner = new Response.Listener<String>(){
//                    @Override
//                    public void onResponse(String response) {
//
//                        //회원가입
//                        //제이슨 오브젝트 로 서버 전송 으로 회원가입함 (일반 String 사용할수 없기때문) = 운반체
//                        try
//                        {
//                            JSONObject jsonObject1 = new JSONObject(response); //알트+엔터로 오류 처리
//                            boolean success1 = jsonObject1.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
//                            if (success1)
//                            {
//
//                            } else
//                            {
//                                //회원등록 실패
//                                Toast.makeText(getApplicationContext(), "회원 등록 실패 ", Toast.LENGTH_LONG).show();
//                                return;
//                            }
//
//                        } catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//
//
//
//                if( userID2.length() != 0 && userPassword2.length() != 0 && userName2.length() != 0 ){
//                    Intent intent = new Intent(RegisterActivity.this, RegisterMyInform.class); //시작 액티비티 , 이동할 액티비티
//
//                    //회원가입 성공 시
//                    intent.putExtra("userID",userID2);
//                    intent.putExtra("userPassword",userPassword2);
//                    intent.putExtra("userName",userName2);
//
//                    //반려견 화면으로 이동
//                    startActivity(intent);
//                }
//
//                else{
//                        Toast.makeText(RegisterActivity.this, "입력 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
//                }
//
//                    }
//
//                }
//            }
//        });


//            }


//        }

