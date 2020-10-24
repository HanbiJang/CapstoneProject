package map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import registerreview.RegisterActivityReview;

/**리뷰 등록 메인 화면**/
public class Review_main extends AppCompatActivity {

    public static final int REVIEW_MAIN_CODE = 103;

    ListView listView = null;
    Adapter_review adapter = null;
    List<review_data> reviewList;
    View view;
    TextView place_txt, review_score;
    Button enroll_btn, refresh_btn;
    static String place_name=null;
    static String userID; //20.09.20

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_main);

        place_txt = (TextView) findViewById(R.id.review_place);
        enroll_btn = (Button) findViewById(R.id.review_enroll_btn);
        review_score = (TextView) findViewById(R.id.review_score);

        listView = (ListView) findViewById(R.id.review_listview);
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        reviewList = new ArrayList<review_data>();

        refresh_btn = (Button) findViewById(R.id.bt_refresh_review);

        // 20.09.20 위치 변경
        Intent intent = getIntent();
        place_name = intent.getExtras().getString("place_name");
        place_txt.setText(place_name);
        userID = intent.getStringExtra("userID");

        //final ArrayList<review_data> item = new ArrayList<review_data>();

        new BackgroundTask().execute();
        new BackgroundTask2().execute();

        //리스트뷰 어댑터 설정
        adapter = new Adapter_review(getApplicationContext(),reviewList);
        listView.setAdapter(adapter);
        //reviewList.clear();


        //리뷰 등록 하기 버튼
        enroll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Review_main.this,registerreview.RegisterActivityReview.class);
                intent.putExtra("place_name",place_name);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        //새로고침 버튼 20.09.20
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
                new BackgroundTask2().execute();
            }
        });


    }

    //한 장소에 대한 리뷰 별점 평균 가져와서 보여주기
    class BackgroundTask2 extends AsyncTask<Void, Void, String> {
        String target; //접속할 주소

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voids) {

            try{

                target="http://bi1724.dothome.co.kr/Review_Score_AVG.php?placeName="+ place_name;

                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream(); //넘어오는 결과값 저장
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //inputstream 내용 버퍼에 담아서 읽을 수 있게
                String temp;
                String err = "등록된 리뷰가 없습니다.";
                StringBuilder stringBuilder = new StringBuilder();

                temp = bufferedReader.readLine();

                if(temp==null){
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return err;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.e("별점 평균 temp ",temp);
                return new String("별점 "+temp+"점"); //trim();
            } catch (Exception e){
                return new String("Exception: "+e.getMessage());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("별점 평균 postExecute: ",result);
            review_score.setText(result);
        }
    }


    //DB에 등록된 리뷰 가져와서 보여주기
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target; //접속할 주소

        @Override
        protected void onPreExecute() {

            try {
                target="http://bi1724.dothome.co.kr/Review_List.php?placeName="+ place_name;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream(); //넘어오는 결과값 저장
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //inputstream 내용 버퍼에 담아서 읽을 수 있게
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp=bufferedReader.readLine())!=null){ //buffer에서 받아온 값을 한줄씩 읽으면서 temp에 넣기(null이 아닐때까지)
                    stringBuilder.append(temp+"\n");
                    Log.e("temp: ",temp);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String userID, reviewText;
                Double reviewScore;

                reviewList.clear(); //20.09.20
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    userID = object.getString("userID");
                    reviewText = object.getString("reviewText");
                    reviewScore = object.getDouble("reviewScore");
                    review_data review = new review_data(userID, reviewText, reviewScore);
                    reviewList.add(review);
                    Log.e("userID: ",userID+" score: "+reviewScore+" text: "+reviewText);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    class Adapter_review extends BaseAdapter {
        private Context context;
        List<review_data> reviewList = new ArrayList<>();

        public Adapter_review(Context context, List<review_data> reviewList) {
            this.context = context;
            this.reviewList = reviewList;
        }


        public void addItem(review_data item) {
            reviewList.add(item);
        }

        @Override
        public int getCount() {
            return reviewList.size();
        }

        @Override
        public Object getItem(int position) {
            return reviewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ReviewView view = new ReviewView(getApplicationContext());
            final review_data item = reviewList.get(position);

            view.setUserID(item.getUserID());
            view.setScore(item.getScore());
            view.setReviewTxt(item.getReviewTxt());

            //리뷰 삭제 버튼 20.09.20
            Button delete_btn = (Button) view.findViewById(R.id.bt_delete_review);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("getUserID",item.getUserID());
                    if(item.getUserID().equals(userID)) {
                        // 아이템 삭제
                        reviewList.remove(position);
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                    } else if(!item.getUserID().equals(userID)){
                        Toast.makeText(getApplicationContext(), "삭제할 수 없습니다.", Toast.LENGTH_LONG).show();
                    }

                    // listview 갱신.
                    adapter.notifyDataSetChanged();
                    new BackgroundTask().execute();
                    new BackgroundTask2().execute();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success)
                                {
                                    //Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();

                                } else
                                {
                                    Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_LONG).show();
                                    return;
                                }

                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    };

                    ReviewDeleteRequest RDR = new ReviewDeleteRequest(place_name, userID, responseListener);
                    Log.e("Delete request: ",place_name+"  "+userID);
                    RequestQueue queue = Volley.newRequestQueue(Review_main.this);
                    queue.add(RDR);

                }

            });

            return view;
        }
    }

}
