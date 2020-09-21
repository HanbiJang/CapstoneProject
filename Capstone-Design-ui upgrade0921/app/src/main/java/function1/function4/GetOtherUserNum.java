package function1.function4;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class GetOtherUserNum extends StringRequest {

    public GetOtherUserNum(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }//생성자

    //서버 url 설정하기
    final static private String URL = "http://bi1724.dothome.co.kr/GetOtherUserNum.php"; //반려견 정보 php파일
    private Map<String,String> map;
    public GetOtherUserNum(String userID, String badDog, String myLatitude, String myLongitude,String otherUserNum,Response.Listener<String> listner){
        super(Request.Method.POST,URL,listner,null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("badDog",badDog);
        //숫자여야하는 수들
        map.put("myLatitude",myLatitude);
        map.put("myLongitude",myLongitude);
        map.put("otherUserNum",otherUserNum);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
