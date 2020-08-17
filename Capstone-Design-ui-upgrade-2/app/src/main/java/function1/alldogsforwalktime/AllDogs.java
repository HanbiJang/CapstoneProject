package function1.alldogsforwalktime;

import java.util.HashMap;

//개들의 부모 클래스
public class AllDogs {

    String dogSize;
    HashMap<String,String> dogSpecies;
    Integer adultMonth;
    Integer oldDogMonth;
    Integer walkCount;
    Integer walkTimeBeforeAdult;
    Integer walkTimeAdult;
    Integer walkTimeAfterAdult;


    public String getDogSize(){ return dogSize; }
    public HashMap<String,String> getDogSpecies(){
        return dogSpecies;
    }

    public Integer getWalkCount() {
        return walkCount;
    }

    public Integer getAdultMonth() {
        return adultMonth;
    }

    public Integer getOldDogMonth(){ return oldDogMonth;}

    public Integer getWalkTimeAdult() {
        return walkTimeAdult;
    }

    public Integer getWalkTimeAfterAdult() {
        return walkTimeAfterAdult;
    }

    public Integer getWalkTimeBeforeAdult() {
        return walkTimeBeforeAdult;
    }

    public void setDogSpecies(HashMap<String,String> dogSpecies) { };

    //개 종이름으로 무게(대중소형견) 찾기
    public String findDogSize(String myDogSpecies){
        String dogSize;
        HashMap<String,String> ham;
        HashMap<String,String> largeDogsList;
        HashMap<String,String> mediumDogsList;
        HashMap<String,String> smallDogsList;

        largeDogsList = new HashMap();
        mediumDogsList = new HashMap();
        smallDogsList = new HashMap();
        ham= new HashMap();

        LargeDogs largeDog = new LargeDogs();
        MediumDogs mediumDogs= new MediumDogs();
        SmallDogs smallDogs= new SmallDogs();

        largeDogsList.putAll(largeDog.dogSpecies);
        mediumDogsList.putAll(mediumDogs.dogSpecies);
        smallDogsList.putAll(smallDogs.dogSpecies);


        if(largeDog != null && mediumDogsList != null && smallDogs != null ){

            ham.putAll(largeDogsList);
            ham.putAll(mediumDogsList);
            ham.putAll(smallDogsList);
            dogSize = (String) ham.get(myDogSpecies);

            return dogSize;
        }
        else {
            return null;
        }

    }

}
