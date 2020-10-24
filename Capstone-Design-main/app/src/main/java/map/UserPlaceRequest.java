package map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**DB에 사용자 정보 등록하기 위한 request**/
public class UserPlaceRequest extends StringRequest {
    final static private String URL = "http://bi1724.dothome.co.kr/Register_UserPlace.php";
    private Map<String,String> map;

    public UserPlaceRequest(String PlaceName, Double Latitude, Double Longitude, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("PlaceName", PlaceName);
        map.put("Latitude", ""+Latitude);
        map.put("Longitude",""+Longitude);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
