package function1;

public class WalkTimeMinuteResult {
    String today; //작성 날짜
    Integer walkTime; //오늘 산책한 횟수
    Integer walkMinute; //오늘 산책한 시간 (시간, 분, 초)

    public String getToday() {
        return today;
    }

    public Integer getWalkMinute() {
        return walkMinute;
    }

    public Integer getWalkTime() {
        return walkTime;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public void setWalkMinute(Integer walkMinute) {
        this.walkMinute = walkMinute;
    }

    public void setWalkTime(Integer walkTime) {
        this.walkTime = walkTime;
    }
}
