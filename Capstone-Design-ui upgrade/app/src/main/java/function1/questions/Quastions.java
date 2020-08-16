package function1.questions;

import java.util.HashMap;

public class Quastions {
    HashMap<String,String> quastions;

    public Quastions(){
        quastions = new HashMap();
        setQuastions(quastions);
    }

    public HashMap<String, String> getQuastions() {
        return quastions;
    }

    public void setQuastions(HashMap quastions){
        String quastion1 = "집에 돌아와서도 반려견이 활발하게\n 움직이거나 뛰어다니나요?";
        String quastion2 = "산책 직후 집에 돌아와서 잠을 오랫동안\n자나요?";

        quastions.put("quastion1","+"); // ( 질문내용, yes 했을 시의 결과 )
        quastions.put("quastion2","-"); // + 시간 늘림 , - 시간 줄임
    }
}
