package map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewListRequest extends StringRequest {

    final static private String URL = "http://bi1724.dothome.co.kr/Review_List_Post.php";
    private Map<String,String> map;

    public ReviewListRequest(String placeName, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);
        map = new HashMap<>();
        map.put("placeName", placeName);
    }




    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
