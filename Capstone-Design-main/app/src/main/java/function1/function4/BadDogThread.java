package function1.function4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import function1.GetMyBadDogRequest;
import function1.MyStartWalk;
import myinformation.MyInformCheckRequest;
import shareddata.PreferenceManager;

public class BadDogThread extends AppCompatActivity implements Runnable {

    Boolean isRunning_badDog = true; // 산책 창을 만들었을 때부터 시작함
    Context context;
    ArrayList<String> otherDogSizeList;
    //myStartWalk 에서 받아온 정보
    String userID;
    String badDog;


    public BadDogThread(Context context , String userID,String badDog){
        this.context = context; //화면 설정
        this.userID = userID;
        this.badDog = badDog;

    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            while (isRunning_badDog) {
                Message msg = new Message();
                msg.arg1 = i++;
                handler1.sendMessage(msg);

                try {
                    Thread.sleep(10000); //10초마다 한번씩 실행
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //10초마다 실행하는 것을 실패
                            //오류 메시지 표시
                        }
                    });
                    return; // 인터럽트 받을 경우 return
                }
            }

        }
    }


    void showAlert(Context context, Integer otherDogSizeNum){

        //진동
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(700); //진동

        new AlertDialog.Builder(context)
                .setTitle("경고")
                .setMessage("20m내에 "+otherDogSizeNum+"명의 "+ badDog +" 사용자가 있습니다.")
                .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) { // 기본적으로 창은 닫히고 추가 작업은 없다(닫히면서 행해지는 것)
                    }
                }).show();

    }

    void showError(Context context){ //화면에 에러 문구 표시

        new AlertDialog.Builder(context)
                .setTitle("알림 중지")
                .setMessage("알림이 중지됩니다")
                .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) { // 기본적으로 창은 닫히고 추가 작업은 없다(닫히면서 행해지는 것)
                    }
                }).show();

    }

    //핸들러 : 스레드 내 실행 내용

    @SuppressLint("HandlerLeak")
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //서버에서 사용자 위치 받아서 저장하기
            //서버에서 사용자 정보를 가져와 변수에 저장한 후 badDogThread에 넘겨준다 : 넘길 정보 - 사용자의 기피견종 GetMyBadDog
            //불리 구문, 서버에 반려견 데이터 요청 구문
            Response.Listener<String> responseListner_GetMyLocationRequest = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                        boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                        if (success)
                        {
                            String myLatitude = jsonObject.getString("myLatitude");  //php문으로 값 가져와서 변수안에 넣음
                            String myLongitude = jsonObject.getString("myLongitude");  //php문으로 값 가져와서 변수안에 넣음
                            //프리퍼런스에 저장...
                            PreferenceManager.setString(context, "myLatitude"+userID,myLatitude);
                            PreferenceManager.setString(context, "myLongitude"+userID,myLongitude);

                            Log.d("77777", "사용자 위치 프리퍼런스 :"+PreferenceManager.getString(context,"myLatitude"+userID));
                            Log.d("77777", "사용자 경도 프리퍼런스 :"+PreferenceManager.getString(context,"myLongitude"+userID));

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

            String myLatitude ="";
            String myLongitude ="";

            Log.d("11111","배드덕 java의 유저 아이디: "+userID );
            GetMyLocationRequest getMyLocationRequest = new GetMyLocationRequest(userID, myLatitude, myLongitude, responseListner_GetMyLocationRequest);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(getMyLocationRequest);


//            Double myLatitude_d = Double.parseDouble(myLatitude);
//            Double myLongitude_d = Double.parseDouble(myLongitude);

            //*****이 위치와 , 기피견종, 아이디로 기피견종 알림 울리기 *****
            //불리 구문, 서버에 근방 기피견종 사용자 데이터 요청 구문
            Response.Listener<String> responseListner_GetOtherUserNum = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                        boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                        if (success)
                        {
                            String otherUserNum_str = jsonObject.getString("otherUserNum");  //php문으로 값 가져와서 변수안에 넣음
                            Integer otherUserNum = Integer.parseInt(otherUserNum_str); //숫자로 변환

                            Log.d("11111", "알림 사용자 수 :"+otherUserNum_str);
                            if(otherUserNum > 0){
                                showAlert(context,otherUserNum); //알람 울리기
                            }


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

            //위치 가져오기

            String badDog_str = badDog;
            String myLatitude_str = PreferenceManager.getString(context,"myLatitude"+userID);
            String myLongitude_str = PreferenceManager.getString(context,"myLongitude"+userID);
            String otherUserNum = "";

            GetOtherUserNum getOtherUserNum = new GetOtherUserNum(userID, badDog, myLatitude_str, myLongitude_str, otherUserNum, responseListner_GetOtherUserNum);
            RequestQueue queue_GetOtherUserNum = Volley.newRequestQueue(context);
            queue.add(getOtherUserNum);

        }

    };

}





