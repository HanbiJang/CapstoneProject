package function1.alldogsforwalktime;
import java.util.HashMap;

public class LargeDogs extends AllDogs {

//    String dogSize;
//    HashMap<String,String> dogSpecies;
//    Integer walkCount;
//    Integer adultMonth;
//    Integer walkTimeBeforeAdult;
//    Integer walkTimeAdult;
//    Integer walkTimeAfterAdult;

    public LargeDogs(){
        dogSize = "대형견";
        dogSpecies = new HashMap();
        setDogSpecies(dogSpecies);
        walkCount = 2;
        adultMonth = 15;
        oldDogMonth = 10*12;
        walkTimeBeforeAdult =0;
        walkTimeAdult = 120;
        walkTimeAfterAdult = 45;
    }


    @Override
    public void setDogSpecies(HashMap<String,String> dogSpecies){
        dogSpecies.put("골든리트리버",dogSize);
        dogSpecies.put("시베리안 허스키",dogSize);
        dogSpecies.put("레브라도 리트리버",dogSize);
        dogSpecies.put("보더콜리",dogSize);
        dogSpecies.put("알레스카 말라뮤트",dogSize);
        dogSpecies.put("사모예드",dogSize);
        dogSpecies.put("잉글리쉬 불독",dogSize);
        dogSpecies.put("대형 믹스",dogSize);
    }
}
