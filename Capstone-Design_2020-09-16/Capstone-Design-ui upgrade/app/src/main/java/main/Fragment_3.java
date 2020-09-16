package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myregisterlogin.R;

import map.MainMap;
import myinformation.MySettings;
import registerlogin.LoginActivity;
import shareddata.PreferenceManager;

public class Fragment_3 extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private Button btn_start;
    private Switch sw_dontsee;

    // newInstance constructor for creating fragment with arguments
    public static Fragment_3 newInstance(int page, String title) {
        Fragment_3 fragment = new Fragment_3();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");



    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        //버튼에 대한 함수 정의

        //시작하기 버튼
        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        //20200915
        sw_dontsee = view.findViewById(R.id.sw_dontsee);
        if((PreferenceManager.getString(getContext(),"dontsee")).equals("true")){ //설정되어 있다면
            sw_dontsee.setChecked(true);
        }
        else{
            sw_dontsee.setChecked(false);
        }
        sw_dontsee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    //활성화
                    PreferenceManager.setString(getContext(), "dontsee","true");
                }else{
                    //비활성화
                    PreferenceManager.setString(getContext(), "dontsee","false");
                }
            }
        });

        return view;

    }
}
