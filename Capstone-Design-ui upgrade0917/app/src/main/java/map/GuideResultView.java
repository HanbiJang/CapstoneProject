package map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myregisterlogin.R;

/**길찾기 결과 Adapter 설정**/
public class GuideResultView extends LinearLayout {
    TextView optionText;
    TextView timeText;
    TextView distanceText;

    public GuideResultView(Context context){
        super(context);
        init(context);
    }
    public GuideResultView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.guide_list,this,true);

        optionText = (TextView) findViewById(R.id.road_option);
        timeText = (TextView) findViewById(R.id.road_time);
        distanceText = (TextView) findViewById(R.id.road_distance);

    }
    public void setTimeText(Integer time){
        timeText.setText("  예상 소요 시간 "+time+"분");
    }
    public void setOptionText(String option){
        optionText.setText(option);
    }
    public void setDistanceText(Integer distance){
        distanceText.setText("  "+distance+"M");
    }
}
