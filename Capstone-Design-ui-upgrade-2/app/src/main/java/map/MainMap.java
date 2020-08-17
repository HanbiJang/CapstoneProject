package map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myregisterlogin.R;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainMap extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private BitmapFactory itmapFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;

        Intent intent = getIntent();
        final String userID= intent.getStringExtra("userID");

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.Tmap);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey("l7xx5bf4f19856ac4a55b9e9d42e0db9512e");
        linearLayoutTmap.addView(tMapView);

        //현재위치로 표시될 아이콘 표시
        tMapView.setIconVisibility(true);

        //zoom레벨
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);


        //gps로 위치 잡기
        tmapgps = new TMapGpsManager(this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        tmapgps.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        //마커 표시
        TMapPoint tpoint = new TMapPoint(37.570841, 126.985302);

        TMapMarkerItem tItem = new TMapMarkerItem();

        tItem.setTMapPoint(tpoint);
        tItem.setName("SKT 타워");
        tItem.setVisible(TMapMarkerItem.VISIBLE);

        Bitmap bitmap = itmapFactory.decodeResource(mContext.getResources(),R.drawable.point);
        tItem.setIcon(bitmap);

        tMapView.setCenterPoint(126.985302, 37.570841, false);
        tMapView.addMarkerItem("SKT 타워", tItem);

    }

    @Override
    public void onLocationChange(Location location) {
        if(m_bTrackingMode){
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }


    //csv 파일 읽기
    private List<file_info> testList=new ArrayList<>();

    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.gangbukseo);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8") )
        );

        String line="";

        try{
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line);
                String[] tokens = line.split(",");
                file_info t = new file_info();
                if(tokens[1].length()>0){
                    t.setParkName(tokens[1]);
                }
                if(tokens[2].length()>0){
                    t.setCategory(tokens[2]);
                }
                if (tokens[5].length() > 0) {
                    t.setLatitude(Double.parseDouble(tokens[5]));
                } else {
                    t.setLongitude(0);
                }
                if (tokens[6].length() > 0) {
                    t.setLongitude(Double.parseDouble(tokens[6]));
                } else {
                    t.setLongitude(0);
                }

                testList.add(t);
                Log.d("MyActivity", "Just created: " + t);

            }
        } catch (IOException e){
            Log.d("MyActivity","Error reading data file on line"+line, e);
            e.printStackTrace();
        }
    }

    //Context getter, setter 함수
    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
