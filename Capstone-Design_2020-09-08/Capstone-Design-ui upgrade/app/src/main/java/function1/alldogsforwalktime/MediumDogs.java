package function1.alldogsforwalktime;

import java.util.ArrayList;
import java.util.HashMap;

public class MediumDogs extends AllDogs {

//    String dogSize;
//    HashMap<String,String> dogSpecies;
//    Integer adultMonth;
//    Integer walkCount;
//    Integer walkTimeBeforeAdult;
//    Integer walkTimeAdult;
//    Integer walkTimeAfterAdult;

    public MediumDogs(){
        dogSize = "중형견";
        dogSpecies = new HashMap();
        setDogSpecies(dogSpecies);
        adultMonth = 12;
        oldDogMonth = 10*12;
        walkCount = 2;
        walkTimeBeforeAdult =0;
        walkTimeAdult = 90;
        walkTimeAfterAdult = 45;
    }

    @Override
    public void setDogSpecies(HashMap<String,String> dogSpecies){
        dogSpecies.put("스피츠",dogSize);
        dogSpecies.put("시바견",dogSize);
        dogSpecies.put("웰시코기",dogSize);
        dogSpecies.put("비글",dogSize);
        dogSpecies.put("프렌치불독",dogSize);

    }
}
