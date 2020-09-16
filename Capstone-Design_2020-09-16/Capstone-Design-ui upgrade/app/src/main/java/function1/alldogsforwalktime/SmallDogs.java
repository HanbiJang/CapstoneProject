package function1.alldogsforwalktime;

import java.util.HashMap;

public class SmallDogs extends AllDogs {

//    String dogSize;
//    HashMap<String,String> dogSpecies;
//    Integer adultMonth;
//    Integer walkCount;
//    Integer walkTimeBeforeAdult;
//    Integer walkTimeAdult;
//    Integer walkTimeAfterAdult;

    public SmallDogs(){
        dogSize = "소형견";
        dogSpecies = new HashMap();
        setDogSpecies(dogSpecies);
        adultMonth = 10;
        oldDogMonth = 10*12;
        walkCount = 2;
        walkTimeBeforeAdult =0;
        walkTimeAdult = 45;
        walkTimeAfterAdult = 45;
    }

    @Override
    public void setDogSpecies(HashMap<String,String> dogSpecies){
        dogSpecies.put("포메라니안",dogSize);
        dogSpecies.put("비숑프리제 ",dogSize);
        dogSpecies.put("푸들",dogSize);
        dogSpecies.put("닥스훈트",dogSize);
        dogSpecies.put("말티즈",dogSize);
        dogSpecies.put("시츄",dogSize);
        dogSpecies.put("요크셔테리어",dogSize);
        dogSpecies.put("치와와",dogSize);
    }
}
