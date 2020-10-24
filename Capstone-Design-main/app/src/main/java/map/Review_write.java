package map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myregisterlogin.R;

public class Review_write extends AppCompatActivity {

    public static final int REVIEW_WRITE_CODE = 104;

    TextView place_txt;
    Button cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write);
        //View v = inflater.inflate(R.layout.review_write, container, false);

        place_txt = (TextView)findViewById(R.id.place_name);
        cancel = (Button)findViewById(R.id.cancel_write);

        Intent intent = getIntent();
        final String place_name = intent.getExtras().getString("place_name");
        place_txt.setText(place_name);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //return v;
    }
}
