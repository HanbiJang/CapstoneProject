package registerlogin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    public LoginRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    } //생성자

    //서버 URL 설정하기
    final static private String URL = "http://bi1724.dothome.co.kr/Login.php"; //로그인 php파일
    private Map<String,String> map;
    //$userID, $userPassword, $userName
    public LoginRequest(String userID, String userPassword, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        //int 정보는 + "" 로 스트링으로 바꾸기
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
