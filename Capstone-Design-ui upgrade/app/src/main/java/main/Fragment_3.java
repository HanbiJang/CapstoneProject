package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.myregisterlogin.R;

import map.MainMap;
import registerlogin.LoginActivity;
import shareddata.PreferenceManager;

public class Fragment_3 extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private Button btn_start;
    private CheckBox cb_dontsee;

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

       //다시 보지않기 체크박스가 체크되면 sharedPreference에 값을 저장함
       cb_dontsee = (CheckBox) view.findViewById(R.id.cb_dontsee);
       if(cb_dontsee.isChecked()){ //다시보지않기가 체크되어있다면
           //sharedPreference 에 다시 보지 않는다고 저장함
           boolean dontsee;
           PreferenceManager.setBoolean(getActivity(),"dontsee",true); //fragment는 context 가 없어서 getActivity()

       }
       else{
           PreferenceManager.setBoolean(getActivity(),"dontsee",false); //fragment는 context 가 없어서 getActivity()
       }

        return view;

    }
}
