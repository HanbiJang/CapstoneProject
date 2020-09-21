package registerreview;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequestReview extends StringRequest {

    //서버 URL 설정하기
    final static private String URL = "http://bi1724.dothome.co.kr/Register_Review.php"; //로그인 php파일
    private Map<String,String> map;

    public RegisterRequestReview(String userID, String placeName, String reviewText, Double reviewScore, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("placeName", placeName);
        map.put("reviewText", reviewText);
        map.put("reviewScore", ""+reviewScore);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
