package function1;

//팝업 설문 정보 사용자 정의 클래스
public class PopupResult {

    String today;

    String is_1_yes;

    String is_2_yes;

    String progressValue;

    public String getToday() {
        return today;
    }

    public String getIs_1_yes() {
        return is_1_yes;
    }

    public String getIs_2_yes() {
        return is_2_yes;
    }

    public String getProgressValue() {
        return progressValue;
    }

    public void setIs_1_yes(String is_1_yes) {
        this.is_1_yes = is_1_yes;
    }

    public void setIs_2_yes(String is_2_yes) {
        this.is_2_yes = is_2_yes;
    }

    public void setProgressValue(String progressValue) {
        this.progressValue = progressValue;
    }

    public void setToday(String today) {
        this.today = today;
    }
}
