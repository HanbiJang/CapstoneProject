package function1;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ForGraph {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<WalkTimeMinuteResult> readWalkTimeMinuteResultFileAndGet(File saveFile, String fileName) {

        //파일 읽기
        ArrayList<WalkTimeMinuteResult> walkTimeMinuteResultsList = new ArrayList<>();
        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요


        try (            FileReader rw = new FileReader(saveFile + fileName);
                         BufferedReader br = new BufferedReader(rw);
                         )
        {
            String readLine;
            Integer i = 1;

            //MyStartWalkMain 창이 만들어질때, 파일을 읽고 사용자의 이전 산책 결과들을 뽑아내어 리스트에 저장함

            while ((readLine = br.readLine()) != null) { //파일 읽기
                //readLine 저장해야함
                // 1: 날짜 2: 1번 산책 횟수 3:  산책 시간

                WalkTimeMinuteResult buffer = new WalkTimeMinuteResult();

                buffer.setToday(readLine);
                buffer.setWalkTime(Integer.parseInt(br.readLine()));
                buffer.setWalkMinute(Integer.parseInt(br.readLine()));

                walkTimeMinuteResultsList.add(buffer);

            }

            //반환
            return walkTimeMinuteResultsList;

            //끝
        } catch (IOException e) {
            e.printStackTrace();
            walkTimeMinuteResultsList = null;
            return walkTimeMinuteResultsList;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<PopupResult> readPopupResultFileAndGet(File saveFile, String fileName) {

        //파일 읽기
        ArrayList<PopupResult> popupResultsList = new ArrayList<>();
        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요

        try(
                FileReader rw = new FileReader(saveFile+fileName);
                BufferedReader br = new BufferedReader( rw );
        )
        {
            String readLine;
            Integer i = 1;
            PopupResult buffer = new PopupResult();

            while( ( readLine = br.readLine() ) != null ){
                //readLine 저장해야함
                // 1: (나머지 3) 날짜 2: (나머지 2) 1번 yes 여부 3: (나머지 1) 2번 yes여부 4: (나머지 0) 만족도 점수

                buffer.today = readLine;
                buffer.is_1_yes = br.readLine();
                buffer.is_2_yes = br.readLine();
                buffer.progressValue = Integer.parseInt(br.readLine());

                popupResultsList.add(buffer);
                i++;
            }
            //끝
            return popupResultsList;

        }catch ( IOException e )
        {
            System.out.println(e);
            popupResultsList = null;
            return popupResultsList;
        }

    }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void makeGraph(LineChart lineChart, File saveFile, String fileName1, String fileName2, String userID)
        {
            //chartData하나에 linedataset이 set1, set2로 두개의 라인을 가진 그래프 : 기본설정
            ArrayList<Entry> entry1 = new ArrayList<>();
            ArrayList<Entry> entry2 = new ArrayList<>();

            //파일 읽어야함
            ArrayList<PopupResult> popupResultArrayList;
            ArrayList<WalkTimeMinuteResult> walkTimeMinuteResultArrayList;

            //그래프 생성
            LineData chartData = new LineData();

            //파일 읽고 값가져오기
            popupResultArrayList = readPopupResultFileAndGet(saveFile,fileName1);
            walkTimeMinuteResultArrayList = readWalkTimeMinuteResultFileAndGet(saveFile,fileName2);


        if(popupResultArrayList != null && walkTimeMinuteResultArrayList != null ){
//        리스트의 맨뒤부터 최대 10개 뽑아낸다
            Integer maxListSize;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                maxListSize = Integer.min(popupResultArrayList.size(),walkTimeMinuteResultArrayList.size()); //더 작은 수가 그래프 표시 갯수

                if(maxListSize >= 10){

                    for(int i = 0 ; i < 10 ; i++){ //10개만 그래프로 뽑음

                        Integer progressVale;
                        Integer walkMinute;
                        progressVale = popupResultArrayList.get(popupResultArrayList.size() - 1 - i).getProgressValue(); //만족도
                        walkMinute= walkTimeMinuteResultArrayList.get(popupResultArrayList.size() - 1 - i).getWalkMinute(); //산책한 시간

                        entry1.add(new Entry(i, progressVale)); //만족도
                        entry2.add(new Entry(i, walkMinute)); //산책한 시간


                    }
                }

                else{ //10보다 사이즈가 작을 때 1개~9개 일때

                    //있는 수 만큼 그래프로 뽑음
                    for(int i = 0 ; i < maxListSize ; i++){

                        Integer progressVale;
                        Integer walkMinute;
                        progressVale = popupResultArrayList.get(popupResultArrayList.size() - 1 - i).getProgressValue(); //만족도
                        walkMinute= walkTimeMinuteResultArrayList.get(popupResultArrayList.size() - 1 - i).getWalkMinute(); //산책한 시간

                        entry1.add(new Entry(i, progressVale)); //만족도
                        entry2.add(new Entry(i, walkMinute)); //산책한 시간


                    }
                    //남은 수는 i, 0 으로 채움
                    for(int i = maxListSize ; i < 10 ; i++){
                        entry1.add(new Entry(i, 0)); //만족도
                        entry2.add(new Entry(i, 0)); //산책한 시간
                    }
                }
            }




        }
        else
        {
            //그래프에 들어갈 만족도, 산책시간 정보 모두 없을 때
            //그래프에 들어갈 좌표값 입력
            for(int i = 0 ; i <10 ; i ++ ){
                entry1.add(new Entry(i,0));
                entry2.add(new Entry(i,0));
            }
        }

        //그래프 설정
            XAxis xAxis = lineChart.getXAxis(); // x 축 설정
            xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
            xAxis.setLabelCount(10, true);

            YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
            yAxisRight.setDrawLabels(false);
            yAxisRight.setDrawAxisLine(false);
            yAxisRight.setDrawGridLines(false);
            //y축의 활성화를 제거함

        lineChart.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정
        LineDataSet set1 = new LineDataSet(entry1, "만족도");
        set1.setColor(Color.BLUE); // 차트의 선 색 설정
        set1.setCircleColor(Color.BLUE); // 차트의 points 점 색 설정
        chartData.addDataSet(set1);
        set1.setDrawFilled(true); // 차트 아래 fill(채우기) 설정
        set1.setFillColor(Color.BLUE); // 차트 아래 채우기 색 설

        LineDataSet set2 = new LineDataSet(entry2, "산책 시간");
        set2.setColor(Color.GREEN); // 차트의 선 색 설정
        set2.setCircleColor(Color.GREEN); // 차트의 points 점 색 설정
        chartData.addDataSet(set2);
        set2.setDrawFilled(true); // 차트 아래 fill(채우기) 설정
        set2.setFillColor(Color.GREEN); // 차트 아래 채우기 색 설정


        //그래프 만들기
        lineChart.setData(chartData);
        lineChart.invalidate(); //Invalidate()로 차트 갱신

    }


}
