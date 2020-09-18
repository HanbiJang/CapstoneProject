package map;

/**길찾기**/
public class guide_list {
    String option;
    Integer time;
    Integer distance;

    public guide_list(String option, Integer time, Integer distance){
        this.option = option;
        this.time = time;
        this.distance = distance;
    }

    public void setOption(String option){
        this.option = option;
    }
    public String getOption(){
        return option;
    }
    public void setTime(Integer time){
        this.time = time;
    }
    public Integer getTime(){
        return time;
    }
    public void setDistance(Integer distance){
        this.distance = distance;
    }
    public Integer getDistance(){
        return distance;
    }
}
