package map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import registerreview.RegisterActivityReview;
import registerreview.RegisterRequestReview;

/**지도 메인 화면**/
public class MainMap extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;
    public static final int MAINMAP_CODE = 102;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private BitmapFactory itmapFactory;
    Bitmap bitmap, bitmap2;

    static String startName = null;
    static String endName = null;

    static TMapPoint startPoint = null;
    static TMapPoint endPoint = null;

    static String find_Name = null;
    static TMapPoint find_Point = null;

    private AlertDialog dialog;

    EditText search_txt;

    double now_lat, now_long;
    double touch_lat, touch_long;
    boolean isButtonClick = false;
    boolean isTouchMyLoc = false;
    TMapMarkerItem myLoc = null;

    List<UserPlace_data> UPList = new ArrayList<UserPlace_data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;

        Intent intent = getIntent();
        final String userID= intent.getStringExtra("userID");

        search_txt = (EditText)findViewById(R.id.et_search);

        ImageButton search_POI_btn = (ImageButton)findViewById(R.id.bt_search); //장소 검색 버튼
        final Button showPark_btn = findViewById(R.id.showPark); //현재 위치 공원 보기 버튼
        ImageButton myLocation_btn = findViewById(R.id.bt_add_location); //사용자 추천 장소 등록 버튼
        ImageButton position_btn = (ImageButton)findViewById(R.id.bt_my_position); //현재 위치 버튼
        final Button roadguide_btn = (Button)findViewById(R.id.roadguide_btn); //길찾기 버튼
        final Button start_setting_btn = (Button)findViewById(R.id.start_seeting_btn); //출발지 지정 버튼
        final Button end_setting_btn = (Button)findViewById(R.id.end_setting_btn); //도착지 지정 버튼
        final Button set_location_btn = (Button)findViewById(R.id.set_location_btn); //사용자 추천 장소 등록 기능에서 위치 지정 버튼


        //처음 시작시 출발지/도착지 지정, 위치 지정(사용자 추천 장소 등록 기능) 버튼 숨기기
        start_setting_btn.setVisibility(View.GONE);
        end_setting_btn.setVisibility(View.GONE);
        set_location_btn.setVisibility(View.GONE);

        //지도 띄우기
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.map_view);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey("l7xx5bf4f19856ac4a55b9e9d42e0db9512e");
        linearLayoutTmap.addView(tMapView);

        //현재위치로 표시될 아이콘 표시
        tMapView.setIconVisibility(true);

        //zoom레벨
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);


        //gps로 위치 잡기 => 실제 핸드폰으로 실행 시킬 때 사용
        //setGPS(tMapView);

        //현재 위치 버튼 터치 시 현재 위치로 지도 이동 (실제 핸드폰으로 실행 시킬 때만 정상 실행됨)
        position_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGPS(tMapView);
            }
        });

        //현재 위치 중심 좌표
        final TMapPoint nowPoint = tMapView.getCenterPoint();
        Log.e("현재 위치 중심 좌표: ",""+nowPoint.getLatitude());

        //tMapView.setCenterPoint(126.985302, 37.570841, false);
        new BackgroundTask().execute();
        addMarker(tMapView);

        //사용자 추천 장소 등록 버튼
        myLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //사용자 추천 장소 등록 버튼이 이미 클릭된 상태에서 다시 클릭하면 기능 사용 할 수 없게
                if(isTouchMyLoc==true){
                    tMapView.removeAllMarkerItem();
                    set_location_btn.setVisibility(View.GONE);
                    tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                        @Override
                        public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrtayList1, TMapPoint tMapPoint, PointF pointF) {
                            return false;
                        }

                        @Override
                        public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                            return false;
                        }
                    });
                    isTouchMyLoc=false;
                }

                //해당 기능 사용 가능인 상태일 때
                else {
                    //지도 위 터치하면 마커 생성될 수 있게
                    Toast.makeText(getApplicationContext(), "추가하고 싶은 장소를 지도에서 터치해주세요", Toast.LENGTH_LONG).show();
                    tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                        @Override
                        public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                            TMapPoint mp = tMapPoint;
                            myLoc = new TMapMarkerItem();
                            myLoc.setTMapPoint(mp);
                            myLoc.setPosition(1 / 2, 1);
                            tMapView.addMarkerItem("tpoint", myLoc);
                            TMapPoint tpoint2 = myLoc.getTMapPoint();
                            touch_lat = tpoint2.getLatitude();
                            touch_long = tpoint2.getLongitude();
                            Log.e("터치 위치: ", "Latitude: " + touch_lat + " Longitude: " + touch_long);
                            set_location_btn.setVisibility(View.VISIBLE);

                            return false;
                        }

                        @Override
                        public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                            return false;
                        }
                    });
                    isTouchMyLoc=true;
                }
            }
        });

        //사용자 추천 장소 등록
        final EditText txtEdit = new EditText(this);
        txtEdit.setTypeface(Typeface.DEFAULT);
        set_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isButtonClick=true;
                set_location_btn.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMap.this);
                dialog = builder.setTitle("이곳을 추천 장소로 등록하기").setMessage("장소명을 입력하고 확인을 누르면 추천 장소로 등록됩니다.\n").setView(txtEdit)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = txtEdit.getText().toString();
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
                                                Toast.makeText(getApplicationContext(), "추천 장소 등록 성공", Toast.LENGTH_LONG).show();

                                            } else
                                            {
                                                Toast.makeText(getApplicationContext(), "추천 장소 등록 실패", Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                UserPlaceRequest UPR = new UserPlaceRequest(name, touch_lat, touch_long, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainMap.this);
                                queue.add(UPR);

                                Log.e("등록할 장소 이름: ",name);
                                Toast.makeText(getApplicationContext(),"등록되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }).create();

                dialog.show();
                tMapView.removeAllMarkerItem();
                tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                    @Override
                    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrtayList1, TMapPoint tMapPoint, PointF pointF) {
                        return false;
                    }

                    @Override
                    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                        return false;
                    }
                });
            }
        });

        //장소 검색
        search_POI_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TMapData tmapdata = new TMapData();
                String search_name = search_txt.getText().toString();

                tmapdata.findAllPOI(search_name, new TMapData.FindAllPOIListenerCallback(){
                    TMapPoint mp = null;
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = (TMapPOIItem)poiItem.get(i);
                            TMapMarkerItem mk = new TMapMarkerItem();
                            mp = item.getPOIPoint();
                            bitmap = itmapFactory.decodeResource(mContext.getResources(), R.drawable.poi);

                            mk.setName(item.getPOIName());
                            mk.setIcon(bitmap);
                            mk.setTMapPoint(mp);

                            mk.setCalloutTitle(item.getPOIName());
                            mk.setCanShowCallout(true);

                            tMapView.addMarkerItem("markerItem" + i, mk);


                            /*Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());*/

                            //마커 클릭시 마커 이름, 위치 얻기
                            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                                @Override
                                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                    if(!arrayList.isEmpty()){
                                        find_Name = arrayList.get(0).getName();
                                        find_Point = arrayList.get(0).getTMapPoint();
                                        roadguide_btn.setVisibility(View.GONE);
                                        start_setting_btn.setVisibility(View.VISIBLE);
                                        end_setting_btn.setVisibility(View.VISIBLE);
                                        Log.e("마커 이름: ",""+find_Name);
                                        Log.e("마커의 위도/경도: ",""+find_Point);
                                    }
                                    else{
                                        roadguide_btn.setVisibility(View.VISIBLE);
                                        start_setting_btn.setVisibility(View.GONE);
                                        end_setting_btn.setVisibility(View.GONE);
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                    return false;
                                }
                            });
                        }
                    }

                });

            }
        });


        //현재 위치 공원 버튼
        showPark_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                find_Name=null;
                find_Point=null;

                TMapPoint tpoint = tMapView.getCenterPoint();

                addMarker(tMapView);
                //if(nowPoint!=tpoint){
                //    addMarker(tMapView);
                //}

                TMapData tmapdata = new TMapData();
                tmapdata.findAroundNamePOI(tpoint, "공원", new TMapData.FindAroundNamePOIListenerCallback() {
                    TMapPoint mp=null;
                    double x, y;
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {
                        for(int i = 0; i < arrayList.size(); i++) {
                            TMapPOIItem item = (TMapPOIItem) arrayList.get(i);
                            TMapMarkerItem mk = new TMapMarkerItem();
                            x = item.getPOIPoint().getLatitude();
                            y = item.getPOIPoint().getLongitude();
                            mp = item.getPOIPoint();

                            bitmap = itmapFactory.decodeResource(mContext.getResources(), R.drawable.poi);

                            mk.setName(item.getPOIName());
                            mk.setIcon(bitmap);
                            mk.setTMapPoint(mp);

                            mk.setCalloutTitle(item.getPOIName());
                            mk.setCanShowCallout(true);

                            bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_stat_name);
                            mk.setCalloutRightButtonImage(bitmap2);

                            tMapView.addMarkerItem("markerItem" + i, mk);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());

                            //마커 클릭시 => 마커 이름과 위치 가져오기, 출발지 도착지 지정 버튼 나타내기
                            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                                @Override
                                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                    if(!arrayList.isEmpty()){
                                        find_Name = arrayList.get(0).getName();
                                        find_Point = arrayList.get(0).getTMapPoint();
                                        roadguide_btn.setVisibility(View.GONE);
                                        start_setting_btn.setVisibility(View.VISIBLE);
                                        end_setting_btn.setVisibility(View.VISIBLE);
                                        Log.e("마커 이름: ",""+find_Name);
                                        Log.e("마커의 위도/경도: ",""+arrayList.get(0).getTMapPoint());
                                    }
                                    else{
                                        roadguide_btn.setVisibility(View.VISIBLE);
                                        start_setting_btn.setVisibility(View.GONE);
                                        end_setting_btn.setVisibility(View.GONE);
                                    }
                                    return false;
                                }

                                @Override
                                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                    return false;
                                }
                            });

                            //리뷰 보기 버튼
                            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                                @Override
                                public void onCalloutRightButton(TMapMarkerItem markerItem) {
                                    Intent intent = new Intent(MainMap.this,Review_main.class);
                                    intent.putExtra("place_name",find_Name);
                                    intent.putExtra("code",MAINMAP_CODE);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);
                                    Log.d("풍선뷰 Click: ", "선택 됨");
                                }
                            });

                            tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
                                @Override
                                public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
                                    String strMessage = "ClickEvent " + " id " + id + " " + markerItem2.latitude + " " + markerItem2.longitude;
                                    Log.d("log", strMessage);
                                }
                            });

                        }
                    }
                });

            }
        });

        //길찾기

        roadguide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double now_lat = setGPS(tMapView).getLatitude();
                double now_long = setGPS(tMapView).getLongitude();


                //Roadguide로 정보 넘기기
                Intent intent =  new Intent(MainMap.this, Roadguide.class);
                intent.putExtra("userID", userID);

                intent.putExtra("now_lat", now_lat);
                intent.putExtra("now_long",now_long);

                startActivityForResult(intent,101);
            }
        });

        //출발지 지정 버튼 클릭
        start_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(find_Name!=null) {
                    startName = find_Name;
                    startPoint = find_Point;
                }
                //
                Intent intent =  new Intent(MainMap.this, Roadguide.class);
                if(endName==null) {
                    intent.putExtra("start_name", startName);
                    intent.putExtra("end_name", "");
                    intent.putExtra("end_lat", 0.0);
                    intent.putExtra("end_long", 0.0);
                    intent.putExtra("now_lat", startPoint.getLatitude());
                    intent.putExtra("now_long", startPoint.getLongitude());
                    startActivity(intent);
                }
                else{
                    intent.putExtra("start_name", startName);
                    intent.putExtra("end_name", endName);
                    intent.putExtra("end_lat", endPoint.getLatitude());
                    intent.putExtra("end_long", endPoint.getLongitude());
                    intent.putExtra("now_lat", startPoint.getLatitude());
                    intent.putExtra("now_long", startPoint.getLongitude());
                    startActivity(intent);
                }
            }
        });

        //도착지 지정 버튼 클릭
        end_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(find_Name!=null) {
                    endName = find_Name;
                    endPoint = find_Point;
                }

                Intent intent =  new Intent(MainMap.this, Roadguide.class);
                intent.putExtra("end_name",endName);
                intent.putExtra("end_lat", endPoint.getLatitude());
                intent.putExtra("end_long", endPoint.getLongitude());
                if(startPoint==null){
                    intent.putExtra("now_lat", now_lat);
                    intent.putExtra("now_long",now_long);
                }
                else{
                    intent.putExtra("start_name",startName);
                    intent.putExtra("now_lat",startPoint.getLatitude());
                    intent.putExtra("now_long", startPoint.getLongitude());
                }

                startActivity(intent);
            }
        });

        //경로안내 Polyline 그리기
        if (this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("code")){
            Log.e("여기까진..","성공..");
            Double start_lat = intent.getExtras().getDouble("start_lat");
            Double start_lon = intent.getExtras().getDouble("start_lon");
            Double end_lat = intent.getExtras().getDouble("end_lat");
            Double end_lon = intent.getExtras().getDouble("end_lon");

            Log.e("넘어온 start_lat",""+start_lat);
            Log.e("넘어온 end_lat",""+start_lat);

            TMapData tMapData = new TMapData();
            startPoint = new TMapPoint(start_lat, start_lon);
            endPoint = new TMapPoint(end_lat, end_lon);
            ArrayList passList = null;

            tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 10,
                    new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine polyLine) {
                            tMapView.addTMapPath(polyLine);
                        }
                    });


        }


    }

    //현재 위치 설정
    public TMapPoint setGPS(TMapView tMapView){
        tmapgps = new TMapGpsManager(this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        tmapgps.OpenGps();

        Log.e("현재위치 좌표: ",""+tmapgps.getLocation().getLatitude());
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        return tmapgps.getLocation();
    }


    //공원 마커 표시
    public void addMarker(TMapView tMapView){
        TMapPoint mp = null;
        double x, y;
        bitmap = itmapFactory.decodeResource(mContext.getResources(), R.drawable.poi);

        for (int i = 0; i < UPList.size(); i++) {

            TMapMarkerItem mk = new TMapMarkerItem();
            x = UPList.get(i).getLatitude();
            y = UPList.get(i).getLongitude();
            mp = new TMapPoint(x, y);

            Log.d("UserLocationList", "Latitude: " + UPList.get(i).getLatitude() + ", Longitude: " + UPList.get(i).getLongitude());
            mk.setName(UPList.get(i).getPlaceName());
            mk.setIcon(bitmap);
            mk.setTMapPoint(mp);

            mk.setCalloutTitle(UPList.get(i).getPlaceName());
            mk.setCanShowCallout(true);

            bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_stat_name);
            mk.setCalloutRightButtonImage(bitmap2);

            tMapView.addMarkerItem("mkItem"+i,mk);

            //현재 화면에 보이는 영역만 마커 표시
            /*TMapPoint leftTop = tMapView.getLeftTopPoint();
            TMapPoint rightBottom = tMapView.getRightBottomPoint();
            if(leftTop.getLatitude()>x && rightBottom.getLatitude()<x && leftTop.getLongitude()<y && rightBottom.getLongitude()>y) {
                tMapView.addMarkerItem("markerItem" + i, mk);
            }*/

        }
    }


    //TMapGPS 관련 함수
    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode){
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            tMapView.setCenterPoint(location.getLongitude(),location.getLatitude());
        }
        now_lat = location.getLatitude();
        now_long = location.getLongitude();
    }


    //사용자가 등록한 추천 장소 DB에서 가져오기
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target; //접속할 주소

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                target="http://bi1724.dothome.co.kr/Get_UserPlace.php";
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
                String PlaceName;
                Double Latitude, Longitude;


                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    PlaceName = object.getString("PlaceName");
                    Latitude = object.getDouble("Latitude");
                    Longitude = object.getDouble("Longitude");
                    UserPlace_data UP = new UserPlace_data(PlaceName, Latitude, Longitude);
                    UPList.add(UP); //리스트 추가
                    Log.e("PlaceName: ",PlaceName+" lat: "+ Latitude +" lon: "+ Longitude);

                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy(){
        bitmap.recycle();
        bitmap=null;

        super.onDestroy();
    }
}
