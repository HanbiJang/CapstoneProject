package myinformation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myregisterlogin.BuildConfig;
import com.example.myregisterlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import function1.WalkTimeMinuteResult;

public class MyInformCheck extends AppCompatActivity {
    private TextView tv_mydogage, tv_mydogsize, tv_mydogspecies, tv_baddog , tv_userID , tv_userName;
    private Button btn_edit ;
    private ImageButton btn_back , btn_see ,imgbtn_pluspic , imgbtn_setting;
    private ImageView imv_userpic;
    private File tempFile; //사진관련
    public int REQUEST_PERMISSIONS_REQUEST_CODE =200;

    //사용자 아이디
    String userID;

    //사진 관련
    private final int PICK_GALLERY_IMAGE = 200;
    //파일 관련
    File saveFile;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinformcheck_3);

        tv_mydogage = findViewById(R.id.tv_mydogage);
        tv_mydogsize = findViewById(R.id.tv_mydogsize);
        tv_mydogspecies = findViewById(R.id.tv_mydogspecies);
        tv_userID = findViewById(R.id.tv_userID);
        tv_userName = findViewById(R.id.tv_userName);
        tv_baddog = (TextView) findViewById(R.id.tv_baddog);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_see = (ImageButton) findViewById(R.id.btn_see);

        //MainActivity에서 넘어온 아이디 정보를 인텐트로 받음
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        //파일 관련
        saveFile = new File(getFilesDir() , "/userData"); // 저장 경로

        //이미지 설정
        imv_userpic = (ImageView) findViewById(R.id.imv_userpic);
        imv_userpic.setImageResource(R.drawable.user_pic);
        //이미지 설정



        //유저 아이디 보이기
        tv_userID.setText(userID);


        //뒤로가기
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed(); //취소버튼 누른거랑 동일한 효과
            }
        });

        //버튼 클릭시 수정화면으로 화면 전환
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);//메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


        //그냥 화면에서도 보이기
        //불리 구문, 서버에 반려견 데이터 & 사용자 닉네임 값 요청 구문
        Response.Listener<String> responseListner = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                try
                {
                    JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                    boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                    if (success)
                    {
                        String userName = jsonObject.getString("userName");
                        String myDogAge = jsonObject.getString("myDogAge");
                        String myDogSize = jsonObject.getString("myDogSize");
                        String myDogSpecies = jsonObject.getString("myDogSpecies");
                        String badDog = jsonObject.getString("badDog"); //php문으로 값 가져와서 변수안에 넣음

                        //텍스트 뷰에 데이터베이스에 있는 유저의 정보 보여주기
                        tv_userName.setText(userName);
                        tv_mydogage.setText(myDogAge+" 개월");
                        tv_mydogsize.setText(myDogSize);
                        tv_mydogspecies.setText(myDogSpecies);
                        tv_baddog.setText(badDog);

                        //다음에 필요할 인텐트에 userID 넣어주기 MyInformCheck -> MyInformChange
                        Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);
                        intent.putExtra("userID", userID);
                    }
                    else
                    {    //내정보 조회 실패
                        return;
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };
        //서버에서 정보 받아오기
        String userName = "";
        Integer myDogAge = 0;
        String myDogSize = "";
        String myDogSpecies = "";
        String badDog = "";

        MyInformCheckRequest myInformCheckRequest = new MyInformCheckRequest(userID, userName, myDogAge, myDogSize, myDogSpecies, badDog, responseListner);
        RequestQueue queue = Volley.newRequestQueue(MyInformCheck.this);
        queue.add(myInformCheckRequest);

        //새로고침 버튼
        btn_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //불리 구문, 서버에 반려견 데이터 요청 구문
                Response.Listener<String> responseListner = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //제이슨 오브젝트로 서버 전송 으로 반려견 정보 등록함 (일반 String 사용할수 없기때문) = 운반체
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response); //알트+엔터로 오류 처리
                            boolean success = jsonObject.getBoolean("success"); //php문에서 success 값을 가져옴 성공여부 알수 있음
                            if (success)
                            {
                                String userName = jsonObject.getString("userName");
                                String myDogAge = jsonObject.getString("myDogAge");
                                String myDogSize = jsonObject.getString("myDogSize");
                                String myDogSpecies = jsonObject.getString("myDogSpecies");
                                String badDog = jsonObject.getString("badDog"); //php문으로 값 가져와서 변수안에 넣음

                                //텍스트 뷰에 데이터베이스에 있는 유저의 정보 보여주기
                                tv_userName.setText(userName);
                                tv_mydogage.setText(myDogAge+" 개월");
                                tv_mydogsize.setText(myDogSize);
                                tv_mydogspecies.setText(myDogSpecies);
                                tv_baddog.setText(badDog);

                                //다음에 필요할 인텐트에 넣어주기 MyInformCheck -> MyInformChange
                                Intent intent =  new Intent(MyInformCheck.this, MyInformChange.class);
                                intent.putExtra("userID", userID);
                            }
                            else
                            {    //내정보 조회 실패
                                return;
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };
                //서버에서 정보 받아오기
                String userName = "";
                Integer myDogAge = 0;
                String myDogSize = "";
                String myDogSpecies = "";
                String badDog = "";

                MyInformCheckRequest myInformCheckRequest = new MyInformCheckRequest(userID, userName, myDogAge, myDogSize, myDogSpecies, badDog, responseListner);
                RequestQueue queue = Volley.newRequestQueue(MyInformCheck.this);
                queue.add(myInformCheckRequest);
            }
        });

        //파일 설정 관련
        imgbtn_setting = (ImageButton) findViewById(R.id.imgbtn_setting);
        imgbtn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent =  new Intent(MyInformCheck.this, MySettings.class);//메인 액티비티로 서버에서 받아온 아이디와 비밀번호 인텐트에 실어서 주기
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });



        //권한 설정
        checkPermissions();
        if(checkPermissions() ==true){
            //이미지 관련
            //파일을 읽고 파일 안에 정보가 있으면 그걸로 사진 표시함
            String userPicUri_str;
            userPicUri_str = getUserPicUri(userID,saveFile);

            if(userPicUri_str != null){
                tempFile = new File(userPicUri_str);
                setImage();
            }
            else{
                imv_userpic.setImageResource(R.drawable.user_pic);
            }
            //이미지 추가 버튼
            imgbtn_pluspic = (ImageButton) findViewById(R.id.imgbtn_pluspic);
            //설정 버튼에서 사용자 갤러리에서 사진을 가져와 등록함

            imgbtn_pluspic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, PICK_GALLERY_IMAGE);

                    // 사진 갤러리 호출
                    Intent intent_1 = new Intent(Intent.ACTION_PICK);
                    intent_1.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent_1.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent_1, PICK_GALLERY_IMAGE);
                }
            });
        }

        else{ //권한 없을 때
            //접근 권한 허용창 실행
            startPermissionRequest();

        }





    }


    //권한 관련
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    //앱에 권한이 필요한 이유를 설명하고자 하는 메소드.
    private void requestPermissions() {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

        if( shouldProviceRationale ) {

            new AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 권한이 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPermissionRequest();
                        }
                    })
                    .create()
                    .show();
        } else {
            startPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSIONS_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getUserPicUri(String userID, File saveFile) {

        final String fileName = "/userPicUri"+userID+".txt";

        //userPicUri 파일을 읽고 정보가 있으면 그걸로 사진 표시함
        //파일 읽기
        String userPicUri = null;
        //권한 필요 menifests 파일에 읽기 쓰기 권한 추가 필요

        //폴더생성
        if (!saveFile.exists()) { // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }


        try (
                FileReader rw = new FileReader(saveFile + fileName);
                BufferedReader br = new BufferedReader(rw);
        ) {

            String readLine = null;

            // 창이 만들어질때, 파일을 읽고 사용자의 이전 산책 결과들을 뽑아내어 리스트에 저장함
            while ((readLine = br.readLine()) != null) { //파일 읽기
                //readLine 저장해야함
                // 1: 날짜 2: 1번 산책 횟수 3:  산책 시간

                userPicUri = readLine;

            }
            //끝
            Log.d("8888", "와일문 에서 파일 내용 읽었음" + userPicUri);
            return userPicUri;


        } catch (IOException e) {
            System.out.println(e);
            return userPicUri;
        }


    }
    //갤러리에서 파일을 선택한 후 그 이미지가 이미지뷰에 적용되게 하기
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri selectedImageUri = data.getData();
//            //파일에 uri 정보 저장해놓기 (무조건 덮어쓰기)
//            saveUserPicUri(userID, selectedImageUri, saveFile);
//
//            Log.d("8888", "사진 띄워지는 유알아이 온액티비티 리저트" +selectedImageUri );
//            imv_userpic.setImageURI(selectedImageUri); //사용자 이미지 설정함
//
//            Log.d("8888", "유알아이 겟패쓰" + selectedImageUri.getPath() );
//
//        }
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == PICK_GALLERY_IMAGE) && (resultCode== Activity.RESULT_OK)){
            try {
                //Uri에서 이미지 경로를 얻어온다.
                String path_Str = getImagePathToUri(data.getData());

                //파일에 절대경로 정보 저장해놓기 (무조건 덮어쓰기)
                saveUserPicUri(userID, path_Str, saveFile);

                //이미지 데이터를 비트맵으로 받아온다.
                Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                //배치해놓은 ImageView에 set
                imv_userpic.setImageBitmap(image_bitmap);

                Toast.makeText(getBaseContext(), "Path_str : "+path_Str , Toast.LENGTH_SHORT).show();

            }

            catch (FileNotFoundException e) { 		e.printStackTrace(); 			}

            catch (IOException e)                 {		e.printStackTrace(); 			}

            catch (Exception e)		         {             e.printStackTrace();			}


        }



    }

    //회원 별로 사진 uri 를 기기내 파일에 저장함
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveUserPicUri(String userID, String userPicUri, File saveFile){
        //파일생성

        final String fileName = "/userPicUri"+userID+".txt";

        try(
                FileWriter fw = new FileWriter(saveFile+fileName, false); //덮어쓰기
        ){

            //사용자 정보 저장

            StringBuffer str = new StringBuffer();
            str.append(userPicUri);

            //파일 쓰기
            fw.write(String.valueOf(str));

        }catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


// 선택된 이미지 경로 가져오기

    public String getImagePathToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgPath;
    }

    private void setImage() {

        ImageView imageView = findViewById(R.id.imv_userpic);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }

}