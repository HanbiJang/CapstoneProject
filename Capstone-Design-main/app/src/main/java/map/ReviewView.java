package map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myregisterlogin.R;

/**리뷰 Adapter에 들어갈 내용**/
public class ReviewView extends LinearLayout {
    TextView userID_txt;
    TextView score_txt;
    TextView reviewTxt_txt;

    public ReviewView(Context context){
        super(context);
        init(context);
    }

    public ReviewView(Context context, AttributeSet attr){
        super(context, attr);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_list,this,true);

        userID_txt = (TextView) findViewById(R.id.user_id);
        score_txt = (TextView) findViewById(R.id.user_score);
        reviewTxt_txt = (TextView) findViewById(R.id.review_txt);


    }

    public void setUserID(String userID){userID_txt.setText(userID);}
    public void setScore(double score){score_txt.setText(""+score+"점");}
    public void setReviewTxt(String reviewTxt){reviewTxt_txt.setText(reviewTxt);}


}
