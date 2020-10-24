package map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewDeleteRequest extends StringRequest {
    final static private String URL = "http://bi1724.dothome.co.kr/Delete_Review.php";
    private Map<String,String> map;

    public ReviewDeleteRequest(String placeName, String userID, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("placeName", placeName);
        map.put("userID", userID);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
