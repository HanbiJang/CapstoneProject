//20200915

package function1.function4;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyNullLocationRequest extends StringRequest {
    public MyNullLocationRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    } //생성자

    //서버 url 설정하기
    final static private String URL = "http://bi1724.dothome.co.kr/MyNullLocationRequest.php"; //반려견 정보 php파일
    private Map<String,String> map;
    //userID,myLatitude,myLongitude,responseListner
    public MyNullLocationRequest(String userID, Double myLatitude, Double myLongitude, Response.Listener<String> listner){
        super(Method.POST,URL,listner,null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("myLatitude",myLatitude+"");
        map.put("myLongitude",myLongitude+"");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}