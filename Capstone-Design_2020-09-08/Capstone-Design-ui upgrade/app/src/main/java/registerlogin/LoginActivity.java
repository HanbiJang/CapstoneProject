package registerlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import main.MainActivity;
import shareddata.PreferenceManager;

import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{

    private EditText et_id, et_password;
    private Button btn_login, btn_register;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_mydogage);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_apply);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener()  //회원가입 버튼을 클릭시 수행
        {
            @Override
            public void onClick(View view)
            {
                Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mContext = this;

        ////////////자동로그인 기능//////////////
        //로그인 버튼 클릭 전에 로그인을 바로 실행해야함
        //sharedPreference 에 저장된 (로그인이 성공된) 아이디, 비밀번호 값으로 로그인을 시도함

        String loginID = PreferenceManager.getString(mContext,"loginID");
        String loginPassword = PreferenceManager.getString(mContext,"loginPassword");

        if(loginID.equals("") || loginPassword.equals("")) { //저장된 데이터가 없을 때
            //아무 동작 하지 않음
        }

        else{ //sharedPreference 안에 정보가 있을 때
            //그 저장된 정보로 로그인을 시도함
            Response.Listener<String> responseListener;

            ///
            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                        boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                        if (success)//서버로 로그인 성공 //서버로부터 유저 아이디와 패스워드가 맞느냐 틀리냐 구분, 로그인 기능 구현
                        {
                            String loginID = jsonObject.getString("userID");
                            String loginPassword = jsonObject.getString("userPassword"); //서버에서 변수로 값 받아오기

                            //로그인 성공 메시지 띄우기
                            Toast.makeText(mContext, "아이디: " + loginID + "님 환영해요!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class); //메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                            intent.putExtra("userID", loginID);
                            intent.putExtra("userPassword", loginPassword);
                            startActivity(intent);


                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            };
            //서버로 볼리를 이용해서 (로그인 리퀘스트)요청을 함
            LoginRequest loginRequest = new LoginRequest(loginID, loginPassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);


        }


        ////////////자동로그인 기능 끝//////////////


        btn_login.setOnClickListener(new View.OnClickListener()  //로그인 버튼을 클릭시 수행
        {
            @Override
            public void onClick(View view)
            {
                //et 에 입력되어있는 값을 get해 가져와서 변수에 넣는다
                String userID = et_id.getText().toString();
                String userPassword =et_password.getText().toString();

                Response.Listener<String> responseListener;
                responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                            {
                                JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success)//서버로 로그인 성공 //서버로부터 유저 아이디와 패스워드가 맞느냐 틀리냐 구분, 로그인 기능 구현
                            {
                                String userID = jsonObject.getString("userID");
                                String userPassword = jsonObject.getString("userPassword"); //서버에서 변수로 값 받아오기

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class); //메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                startActivity(intent);

                                //로그인 성공 메시지 띄우기
                                Toast.makeText(mContext, "ID: " + userID + "님 환영해요!", Toast.LENGTH_SHORT).show();

                                //로그인을 성공했을 시, sharedPreference에 로그인이 성공된 사용자 아이디, 비번 값을 저장함
                                //지속 로그인 기능 -sharedPreference 기능 활용
                                PreferenceManager.setString(mContext, "loginID",userID);
                                PreferenceManager.setString(mContext, "loginPassword",userPassword);


                            } else  // 일반 로그인 실패
                            {
                                Toast.makeText(getApplicationContext(), "  아이디와 비밀번호를 확인해주세요 ", Toast.LENGTH_LONG).show();
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 볼리를 이용해서 (로그인 리퀘스트)요청을 함
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
