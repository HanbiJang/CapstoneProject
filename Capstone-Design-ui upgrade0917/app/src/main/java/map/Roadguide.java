package map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myregisterlogin.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**길찾기 화면**/
public class Roadguide extends AppCompatActivity {
    EditText start_txt;
    EditText end_txt;
    ListView listView;
    RoadAdapter adapter;
    Button road_search_btn;
    static double start_lat, start_long, end_lat, end_long;
    static Integer time, time2, distance, distance2 =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadguide);

        listView = (ListView)findViewById(R.id.road_list);
        adapter = new RoadAdapter();

        road_search_btn = (Button)findViewById(R.id.road_search_btn);
        ImageButton start_search_btn = (ImageButton)findViewById(R.id.start_search_btn);
        ImageButton end_search_btn = (ImageButton)findViewById(R.id.end_search_btn);
        start_txt = (EditText)findViewById(R.id.start);
        end_txt = (EditText)findViewById(R.id.end);

        //산책시작메인 화면에서 넘어온 아이디 정보 & 지도 화면에서 넘어온 데이터 받기
        Intent intent = getIntent();
        String end_name = intent.getExtras().getString("end_name");
        String start_name = intent.getExtras().getString("start_name");
        end_lat = intent.getExtras().getDouble("end_lat");
        end_long = intent.getExtras().getDouble("end_long");
        start_lat = intent.getExtras().getDouble("now_lat");
        start_long = intent.getExtras().getDouble("now_long");

        final String userID= intent.getStringExtra("userID");

        //넘어온 정보로 텍스트뷰 설정
        if(start_txt!=null)
            start_txt.setText(start_name);
        if(end_txt!=null)
            end_txt.setText(end_name);

        Log.e("start_lat: ",""+start_lat);
        Log.e("end_lat: ",""+end_lat);

        //출발지, 도착지 tmapPoint 설정
        TMapData tMapData = new TMapData();
        TMapPoint point1 = new TMapPoint(start_lat, start_long);
        TMapPoint point2 = new TMapPoint(end_lat, end_long);

        //출발지와 도착지 모두 설정 되었을 때 길찾기 하기
        if(start_lat!=0.0 && end_lat!=0.0) {

            //대로우선 길찾기(xml 파싱)
            new tmapAsyncTask().execute();

            //추천경로 길찾기
            tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataAllListenerCallback() {

                @Override
                public void onFindPathDataAll(Document document) {
                    Element root = document.getDocumentElement();
                    NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");

                    time = Integer.parseInt(root.getElementsByTagName("tmap:totalTime").item(0).getTextContent().trim()) / 60;
                    distance = Integer.parseInt(root.getElementsByTagName("tmap:totalDistance").item(0).getTextContent().trim());
                    Log.e("total Distance: ", "" + root.getElementsByTagName("tmap:totalDistance").item(0).getTextContent().trim());
                    for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                        NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                        for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                            if (nodeListPlacemarkItem.item(j).getNodeName().equals("description")) {
                                Log.d("debug", nodeListPlacemarkItem.item(j).getTextContent().trim());
                            }
                        }
                    }
                }

            });
        }

        //'길찾기' 버튼 클릭시
        road_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("시간 설정...",""+time2+"분");
                Log.e("거리 설정...",""+distance2+"M");
                adapter.addItem(new guide_list("추천경로",time,distance));
                adapter.addItem(new guide_list("대로우선",time2,distance2));
                listView.setAdapter(adapter);
            }
        });

        //출발지 검색 버튼 클릭시
        start_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Roadguide.this, MainMap.class);
                startActivity(intent);
            }
        });


    }

    //tmap 경로안내 API 이용해서 길찾기 결과 정보 파싱
    private static void getXMLData(Double start_lat,Double start_long,Double end_lat, Double end_long){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            String queryUrl2 = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=xml&callback=result&appKey=l7xx95f2e1d70e6a430484c5f00181f5ea93&startX=" + start_long + "&startY=" + start_lat + "&endX=" + end_long + "&endY=" + end_lat + "&startName=%EC%B6%9C%EB%B0%9C%EC%A7%80&endName=%EB%8F%84%EC%B0%A9%EC%A7%80&searchOption=4";

            URL url = new URL(queryUrl2);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결
            Document doc = builder.parse(is);

            Element root = doc.getDocumentElement();
            NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");

            time2 = Integer.parseInt(root.getElementsByTagName("tmap:totalTime").item(0).getTextContent().trim()) / 60;
            distance2 = Integer.valueOf(root.getElementsByTagName("tmap:totalDistance").item(0).getTextContent().trim());
            Log.e("Time 대로", "" + time2 + "분");
            Log.e("total Distance 대로", "" + root.getElementsByTagName("tmap:totalDistance").item(0).getTextContent().trim());
            int t = 0;
            for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                    if (nodeListPlacemarkItem.item(j).getNodeName().equals("description")) {
                        Log.d("debug", nodeListPlacemarkItem.item(j).getTextContent().trim());
                    }

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static class tmapAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            getXMLData(start_lat, start_long, end_lat, end_long);
            return null;
        }
    }

    //길찾기 결과 목록 어댑터 설정
    class RoadAdapter extends BaseAdapter {
        ArrayList<guide_list> items = new ArrayList<guide_list>();

        @Override
        public int getCount(){
            return items.size();
        }
        public void addItem(guide_list item){
            items.add(item);
        }
        @Override
        public Object getItem(int position){
            return items.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup){
            GuideResultView view = new GuideResultView(getApplicationContext());
            guide_list item = items.get(position);
            view.setTimeText(item.getTime());
            view.setOptionText(item.getOption());
            view.setDistanceText(item.getDistance());

            LinearLayout clickArea = (LinearLayout)view.findViewById(R.id.clickArea);
            clickArea.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    //결과 목록 클릭하면 지도화면으로 데이터 넘기기 & 화면 이동
                    Intent intent = new Intent(Roadguide.this, MainMap.class);
                    intent.putExtra("start_lat",start_lat);
                    intent.putExtra("start_lon",start_long);
                    intent.putExtra("end_lat",end_lat);
                    intent.putExtra("end_lon",end_long);
                    intent.putExtra("code",101);

                    startActivity(intent);

                }
            });

            return view;
        }

    }
}
