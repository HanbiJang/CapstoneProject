package function1.walktimecalculater;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import function1.PopupResult;
import function1.alldogsforwalktime.AllDogs;
import function1.alldogsforwalktime.LargeDogs;
import function1.alldogsforwalktime.MediumDogs;
import function1.alldogsforwalktime.SmallDogs;
import myinformation.DbUserInform;


public class WalkTimeCalculater {

    //만족도 결과 파일 이름 가져오기
    public String returnFileName(String userID ,File saveFile){
        String fileName = "/popupResult"+userID+".txt";
        return fileName;
    }

    //사용자 정보 파일에서 아이디에 해당하는 사용자의 개종류와 개나이를 UserInform 구조체로 가져옴
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public DbUserInform returnDbUserInform(String userID, File saveFile){
        DbUserInform dbUserInform = new DbUserInform();
        //사용자 정보 파일(dbUserInform.txt) 에서 userInform 변수에 받아옴

        ///
        try(
                FileReader rw = new FileReader(saveFile+ "/dbUserInform"+userID+".txt");
                BufferedReader br = new BufferedReader( rw );
        ){

                Integer lineNum = 1;
                String readLine = null ;


            while( ( readLine =  br.readLine()) != null ){
                    //readLine 저장해야함
                    // 1:userID 2:myDogAge_int 3:myDogSize 4:myDogSpecies 5:badDog

                    if ( lineNum%5 == 1){
                        dbUserInform.setUserID(readLine);
                    }
                    else if (lineNum%5 == 2){
                        dbUserInform.setMyDogAge_int(readLine);
                    }
                    else if ( lineNum%5 == 3){
                        dbUserInform.setMyDogSize(readLine);
                    }

                    else if( lineNum%5 == 4) { //lineNum%4 == 0
                        dbUserInform.setMyDogSpecies(readLine);
                    }
                    else{
                        dbUserInform.setBadDog(readLine);
                    }

                    lineNum++;

            }



        }catch ( IOException e ) {
            System.out.println(e);
        }


        ///
        return dbUserInform;
    }

    //산책횟수 계산기
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer firstWalkTimeCalculater(String userID, File saveFile){
        Integer firstWalkTime =0 ;
        DbUserInform userInform1 = new DbUserInform();

        //파일에서 아이디에 해당하는 사용자의 개종류와 개나이를 DbUserInform 구조체로 가져옴
        userInform1 = returnDbUserInform(userID,saveFile);

        SmallDogs smallDogs = new SmallDogs();
        MediumDogs mediumDogs = new MediumDogs();
        LargeDogs largeDogs = new LargeDogs();

        if(userInform1.getMyDogSize() != null){

            if(userInform1.getMyDogSize().equals(smallDogs.getDogSize()) ){ //소형견이면
                firstWalkTime = smallDogs.getWalkCount();
                return firstWalkTime;
            }
            else if (userInform1.getMyDogSize().equals(mediumDogs.getDogSize()) ){ //중형견이면
                firstWalkTime = mediumDogs.getWalkCount();
                return firstWalkTime;
            }
            else {
                firstWalkTime = largeDogs.getWalkCount(); //대형견이면
                return firstWalkTime;
            }
        }
        else{
            return firstWalkTime;
        }


    }

    //추천 산책시간 개종류, 나이별 계산기
    public Integer recoWalkMinuteByAgeCalcul(DbUserInform userInform, AllDogs allDogs){  //db 에서 가져온 유저개정보 userInform, 계산용 개 클래스
        Integer firstWalkMinute;
        AllDogs allDogs1;
        allDogs1 = allDogs;

        //반려견 나이 입력
        Integer myDogAge_int;
        try {
            myDogAge_int = Integer.parseInt(userInform.getMyDogAge_int());
        }catch(NumberFormatException e){
            myDogAge_int=0;
        }

        if (myDogAge_int < allDogs1.getAdultMonth()) { //성견 나이보다 사용자 개나이가 작으면
            firstWalkMinute = allDogs1.getWalkTimeBeforeAdult(); //어린개 추천시간 가져옴
            return firstWalkMinute;
        } else if (myDogAge_int < allDogs1.getOldDogMonth()) { //사용자 개나이가 성견이상 노견 미만이면
            firstWalkMinute = allDogs1.getWalkTimeAdult(); //성견 추천시간 가져옴
            return firstWalkMinute;
        } else {
            firstWalkMinute = allDogs1.getWalkTimeAfterAdult(); //사용자 개나이가 노견이면
            return firstWalkMinute; //노견 추천시간 가져옴
        }
    }

    // 최초 산책시간 계산기 (만족도 반영 x)
    // db에서 아이디에 해당하는 사용자 개정보를 얻어내서 만족도 반영안된 산책시간을 계산함
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer firstWalkMinuteCalculater(String userID, File saveFile) {
        Integer firstWalkMinute =0; //만족도 반영 안한 산책 시간
        SmallDogs smallDogs = new SmallDogs();
        MediumDogs mediumDogs = new MediumDogs();
        LargeDogs largeDogs = new LargeDogs();

        DbUserInform userInform = new DbUserInform();

        //db에서 개나이, 개무게 종류 받아오기
        userInform = returnDbUserInform(userID,saveFile);

        if(userInform.getMyDogSize() != null){

            if (userInform.getMyDogSize().equals(smallDogs.getDogSize())) { //사용자 개무게종류가 소형견이면
                firstWalkMinute= recoWalkMinuteByAgeCalcul(userInform, smallDogs); // 추천시간은 (만족도 반영 x ) 소형견의 나이별 산책시간
            }
            else if (userInform.getMyDogSize().equals(mediumDogs.getDogSize())) { //사용자 개무게종류가 중형견이면
                firstWalkMinute= recoWalkMinuteByAgeCalcul(userInform, mediumDogs);
            }
            else {
                firstWalkMinute = recoWalkMinuteByAgeCalcul(userInform, largeDogs); //사용자 개무게종류가 대형견이면
            }
        }

        return firstWalkMinute; //산책시간 반환하기
    }


    //산책 만족도 결과 반영된 산책 '횟수' 계산
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer WalkTimeCalculater(String userID , File saveFile){ //사용자의 개무게
        Integer calculedWalkTime =0;
        Integer noCalculedWalkTime =0;

        noCalculedWalkTime = firstWalkTimeCalculater(userID,saveFile); //db에서 사용자 개정보 얻어내서 만족도 반영 x 산책 횟수 반환함

        //만족도가 있거나 없을 경우
        //만족도 결과 반영 계산
        calculedWalkTime = setiWalkTimeCalcul(noCalculedWalkTime);

        return calculedWalkTime;
    }

    //산책횟수에 만족도 결과 반영
    private Integer setiWalkTimeCalcul(Integer noCalculedWalkTime) {
        //만족도가 있을 경우
        //계산 과정 (미정)

        //만족도가 없을 경우
        return noCalculedWalkTime;
    }

    //**************************산책 만족도 결과 반영 산책 '시간' 계산***********************
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer WalkMinuteCalculater(String userID , File saveFile){ //내부저장소 파일 경로 saveFile
        Integer calculedWalkMinute;
        Integer noCalculedWalkMinute;
        calculedWalkMinute = firstWalkMinuteCalculater(userID,saveFile);
        noCalculedWalkMinute = firstWalkMinuteCalculater(userID,saveFile); //만족도 반영 안된 추천 산책시간

        // 질문 1의 true 갯수에 의해 10분씩 산책시간 더하기
        // 질문 2의 true 갯수에 의해 10분씩 산책시간 빼기
        // 음수면 10분으로 고정하기


        Integer is_1_yes_trueNum = 0;          //true인 is_1_yes 와 is_2_yes 갯수
        Integer is_2_yes_trueNum = 0;
        ArrayList<PopupResult> popupResultsList;

        //계산과정
        //파일에서 만족도 조사 결과를 가져옴 ArrayList<PopupResult> 에서 true인 is_1_yes 와 is_2_yes 개수를 각각 세서 결과 합산
        popupResultsList = new ArrayList<PopupResult>();
        popupResultsList= returnFilePopupResult(userID , saveFile); //사용자 아이디별로 파일에서 모든 결과를 가져옴 // PopupResult 형 ArrayList 반환



        if ( popupResultsList != null){ // 만족도 파일이 존재하고 내용도 있을 때

            //true인 is_1_yes 와 is_2_yes 갯수 세기

            for (int i = 0 ; i<popupResultsList.size() ; i ++){
                if ( popupResultsList.get(i).getIs_1_yes() != null){
                    if( popupResultsList.get(i).getIs_1_yes().equals("true")){
                        is_1_yes_trueNum ++;
                    }
                } // 만족도 파일이 존재하고 내용도 있을 때

                if ( popupResultsList.get(i).getIs_2_yes() != null){
                    if( popupResultsList.get(i).getIs_2_yes().equals("true")){
                        is_2_yes_trueNum ++;
                    }
                }

            }

            Log.d("777777777777777" , is_1_yes_trueNum +"      " +is_2_yes_trueNum + "**********yes갯수");

            //만족도에 의해 10분씩 결과 합산
            // is_1_yes_trueNum *  10분씩 산책시간 더하기
            // is_1_yes_trueNum * 10분씩 산책시간 빼기

            calculedWalkMinute = noCalculedWalkMinute + (10*is_1_yes_trueNum) - (10*is_2_yes_trueNum);
        }

        if (calculedWalkMinute < 0){ //만족도 결과에 의해 음수면 10분으로 맞추기
            calculedWalkMinute = 10;
        }

        return calculedWalkMinute;
    }

    //사용자 아이디별로 파일에서 모든 결과를 가져옴 // PopupResult 형 ArrayList 반환
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<PopupResult> returnFilePopupResult(String userID , File saveFile) {
        ArrayList<PopupResult> popupResultsList = new ArrayList<PopupResult>();

        /////
        String fileName = returnFileName(userID , saveFile);

        try(
                FileReader rw = new FileReader(saveFile+fileName);
                BufferedReader br = new BufferedReader( rw );
        ){

            Integer lineNum = 1;
            String readLine = null ;


            while( ( readLine =  br.readLine()) != null ){
                //readLine 저장해야함
                // 1: (나머지 3) 날짜 2: (나머지 2) 1번 yes 여부 3: (나머지 1) 2번 yes여부 4: (나머지 0) 만족도 점수
                PopupResult buffer = new PopupResult();

                if ( lineNum%4 == 3){
                    buffer.setToday(readLine);
                }
                else if (lineNum%4 == 2){
                    buffer.setIs_1_yes(readLine);
                }
                else if ( lineNum%4 == 1){
                    buffer.setIs_2_yes(readLine);
                }

                else { //lineNum%4 == 0
                    buffer.setProgressValue(Integer.parseInt(readLine));
                }
                lineNum++;
                popupResultsList.add(buffer);

            }

        }catch ( IOException e ) { //만족도 결과 파일이 없는 경우
            System.out.println(e);
            popupResultsList = null;
        }

            /////

        return popupResultsList;
    }

}
