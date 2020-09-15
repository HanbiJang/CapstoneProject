package function1.function4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import myinformation.MyInformCheckRequest;

public class BadDogThread extends AppCompatActivity implements Runnable {

    Boolean isRunning_badDog = true; // 산책 창을 만들었을 때부터 시작함
    Context context;
    ArrayList<String> otherDogSizeList;
    //서버에서 정보 받아오기
    String userID = "";
    String userName = "";
    Integer myDogAge = 0;
    String myDogSize = "";
    String myDogSpecies = "";
    String badDog = ""; //이 정보만 쓸 것임


    public BadDogThread(Context context , String userID){
        setContext(context);
        setUserID(userID);
        setBadDog(userID);
        otherDogSizeList = new ArrayList<>(); //다른 개 사이즈 리스트 만들기

        // *********************************************
        // 임의값 설정 (근처에 6명의 다른 사용자가 있음! 다른 사용자들의 무게견종 넣기)
        // 대형견 2마리 소형견 3마리 중형견 1마리
        otherDogSizeList.add("대형견");
        otherDogSizeList.add("대형견");
        otherDogSizeList.add("소형견");
        otherDogSizeList.add("소형견");
        otherDogSizeList.add("소형견");
        otherDogSizeList.add("중형견");
        // *********************************************

    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setContext(Context context) {
        this.context = context; //화면 설정

    }

    //사용자의 정보 가져오기
    public void setBadDog(String userID){
        //사용자의 기피 견종 정보 서버에서 가져오기
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
                        badDog = jsonObject.getString("badDog"); //php문으로 값 가져와서 변수안에 넣음

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

        MyInformCheckRequest myInformCheckRequest = new MyInformCheckRequest(userID, userName, myDogAge, myDogSize, myDogSpecies, badDog, responseListner);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(myInformCheckRequest);

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
                    Thread.sleep(5000); //5초마다 한번씩 위치 갱신, 반려견 알림 실행
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //10초마다 실행하는 것을 실패
                            //오류 메시지 표시
                            showError(context);


                        }
                    });
                    return; // 인터럽트 받을 경우 return
                }
            }

        }
    }


    void showAlert(Context context, Integer otherDogSizeNum){
        new AlertDialog.Builder(context)
                .setTitle("경고")
                .setMessage("근처에 "+otherDogSizeNum+"명의 "+badDog +"사용자가 있습니다.")
                .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) { // 기본적으로 창은 닫히고 추가 작업은 없다(닫히면서 행해지는 것)
                    }
                }).show();
    }

    void showError(Context context){ //화면에 에러 문구 표시

        new AlertDialog.Builder(context)
                .setTitle("알림")
                .setMessage("알림이 종료되었습니다")
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

//            @SuppressLint("DefaultLocale") String result = String.format("%02d 시간 %02d 분 %02d 초", hour, min, sec);

            //반경 10m 내의 사용자들의 반려견 사이즈 == 내 기피견종 일때 경고 표시

            //서버에서 반경 10m 내의 사용자들의 반려견 사이즈 가져오기

            //20200915
            //반경 10m이내의 개가 대형견 2마리 소형견 3마리 중형견 1마리라고 가정

            Integer otherDogSizeNum = 0;
            for(int i= 0 ; i<otherDogSizeList.size(); i++)
            {

                if(badDog.equals(otherDogSizeList.get(i))){ //기피견종이 근처 다른사용자의 개 사이즈종과 같으면
                    otherDogSizeNum++; //기피견종 개 마릿수 증가

                }
            }

            if(otherDogSizeNum > 0) {
                showAlert(context, otherDogSizeNum); //근처에 00명의 baddog 사용자가 있습니다. 표시
            }

        }

    };

    }





