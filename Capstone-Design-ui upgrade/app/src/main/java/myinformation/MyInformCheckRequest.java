package myinformation;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyInformCheckRequest extends StringRequest {

    public MyInformCheckRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }//생성자

    //서버 url 설정하기
    final static private String URL = "http://bi1724.dothome.co.kr/MyInformCheck.php"; //반려견 정보 php파일
    private Map<String,String> map;
    public MyInformCheckRequest(String userID, String userName, Integer myDogAge, String myDogSize,String myDogSpecies,String badDog,Response.Listener<String> listner){
        super(Method.POST,URL,listner,null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userName",userName);
        map.put("myDogAge",myDogAge+"");
        map.put("myDogSize",myDogSize);
        map.put("myDogSpecies",myDogSpecies);
        map.put("badDog",badDog);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
