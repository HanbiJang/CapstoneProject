package function1;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**DB에 속도 업데이트하기 위한 request**/
public class SpeedRequest extends StringRequest {

    final static private String URL = "http://bi1724.dothome.co.kr/Register_Speed.php";
    private Map<String,String> map;

    public SpeedRequest(String userID, Double mySpeed, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("mySpeed", ""+mySpeed);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
