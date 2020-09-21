package map;

/**리뷰에 보여줄 내용**/
public class review_data {
    String userID = "";
    double reviewScore=0.0;
    String reviewText ="";

    public review_data(String userID, String reviewTxt, double score){
        this.userID=userID;
        this.reviewScore=score;
        this.reviewText=reviewTxt;
    }

    public void setUserID(String userID){this.userID=userID;}
    public String getUserID() {return userID; }
    public void setScore(double score){this.reviewScore=score;}
    public double getScore() {return reviewScore;}
    public void setReviewTxt(String reviewTxt){this.reviewText=reviewTxt;}
    public String getReviewTxt() {return reviewText;}
}
