package registerreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivityReview extends AppCompatActivity {

    private TextView place_txt;
    private Button cancel;
    private String userID;
    private String placeName;
    private Double reviewScore;
    private String reviewTxt;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write);

        place_txt = (TextView)findViewById(R.id.place_name);
        cancel = (Button)findViewById(R.id.cancel_write);
        final Button enroll = (Button)findViewById(R.id.review_enroll_btn2);
        final EditText et_userScore = (EditText)findViewById(R.id.register_score);
        final EditText et_reviewTxt = (EditText)findViewById(R.id.review_txt_register);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        Log.d("userID: ",userID);
        placeName = intent.getExtras().getString("place_name");
        place_txt.setText(placeName);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //DB에 리뷰 등록하기
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score = et_userScore.getText().toString();
                String reviewtxt = et_reviewTxt.getText().toString();

                //내용을 입력하지 않았을 때 경고
                if(reviewtxt.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityReview.this);
                    dialog = builder.setMessage("내용을 입력해주세요").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }
                //별점을 입력하지 않았을 때 경고
                if(score.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityReview.this);
                    dialog = builder.setMessage("별점을 입력해주세요").setPositiveButton("확인",null).create();
                    dialog.show();
                    return;
                }

                reviewScore = Double.valueOf(score);
                reviewTxt = et_reviewTxt.getText().toString();
                Log.d("review Score: ",""+reviewScore);
                Log.d("reviewTxt: ",reviewTxt);
                Log.d("userID: ",userID);
                Log.d("placeName: ",placeName);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success)
                            {
                                Toast.makeText(getApplicationContext(), "리뷰 등록 성공", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivityReview.this, map.Review_main.class); //메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                                intent.putExtra("userID", userID);
                                intent.putExtra("placeName", placeName);
                                intent.putExtra("reviewTxt", reviewTxt);
                                intent.putExtra("reviewScore", reviewScore);
                                finish();

                            } else
                            {
                                Toast.makeText(getApplicationContext(), "리뷰 등록 실패", Toast.LENGTH_LONG).show();
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };

                RegisterRequestReview RAR = new RegisterRequestReview(userID, placeName, reviewTxt, reviewScore, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivityReview.this);
                queue.add(RAR);
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}
