package registerlogin;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{

    private EditText et_id, et_password;
    private Button btn_login, btn_register;


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

                            } else  //로그인 실패
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
