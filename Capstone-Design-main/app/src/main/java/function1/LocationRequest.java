package function1;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**현재 위치 갱신을 위한 request**/
public class LocationRequest extends StringRequest {

    final static private String URL = "http://bi1724.dothome.co.kr/Register_Location.php";
    private Map<String,String> map;

    public LocationRequest(Double myLatitude, Double myLongitude, String userID, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("myLatitude", ""+myLatitude);
        map.put("myLongitude",""+myLongitude);
        map.put("userID",userID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
