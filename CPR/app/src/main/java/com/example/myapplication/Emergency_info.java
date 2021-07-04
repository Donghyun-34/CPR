package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.ui.emergencyinfo.AED.Aed_item;
import com.example.myapplication.ui.emergencyinfo.AED.AeditemView;
import com.example.myapplication.ui.emergencyinfo.All_map;
import com.example.myapplication.ui.emergencyinfo.Emergency_room.EmergencyroomView;
import com.example.myapplication.ui.emergencyinfo.Emergency_room.Emergencyroom_item;
import com.example.myapplication.ui.emergencyinfo.GpsTracker;
import com.example.myapplication.ui.emergencyinfo.Pharm.Pharm_item;
import com.example.myapplication.ui.emergencyinfo.Pharm.PharmitemView;
import com.example.myapplication.ui.emergencyinfo.itemMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Emergency_info extends AppCompatActivity {
    ListView aed_listView;
    ListView pharm_listView;
    ListView hospital_listView;

    pharmAdapter pharm_adapter;
    aedAdapter aed_adapter;
    hospitalAdapter hospital_adapter;


    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);

        aed_adapter = new aedAdapter();//객체화
        pharm_adapter = new pharmAdapter();
        hospital_adapter = new hospitalAdapter();

        pharm_listView = findViewById(R.id.pharm_listview);
        aed_listView = findViewById(R.id.aed_listview);
        hospital_listView = findViewById(R.id.hospital_listview);

        pharm_listView.setAdapter(pharm_adapter);
        aed_listView.setAdapter(aed_adapter);//리스트뷰가 어떤 어댑터이용할 것인지
        hospital_listView.setAdapter(hospital_adapter);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        //Dashboard 화면에서 버튼 클릭 시 해당 정보가 바로 출력되도록 하는 부분
        Intent intent = getIntent();
        int count = intent.getIntExtra("msg", 0);

        switch (count){
            case 1:
                getTask2 task2 = new getTask2();
                gpsTracker = new GpsTracker(Emergency_info.this);
                task2.execute();
                break;
            case 2:
                getTask task = new getTask();
                gpsTracker = new GpsTracker(Emergency_info.this);
                task.execute();
                break;
            case 3:
                gpsTracker = new GpsTracker(Emergency_info.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                aed_adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
                break;
            default:
                break;
        }

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Button all_map = (Button)findViewById(R.id.all_map_btn);
        all_map.setOnClickListener(v -> {
            Intent intent_map = new Intent(Emergency_info.this, All_map.class);
            if(aed_adapter.items.size()!=0){
                gpsTracker = new GpsTracker(Emergency_info.this);
                intent_map.putExtra("aed_list",aed_adapter.items);
                intent_map.putExtra("my_Lat",gpsTracker.getLatitude());
                intent_map.putExtra("my_Lon",gpsTracker.getLongitude());
                startActivity(intent_map);
            }
        });
    }

    private class getTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            getPharmData();
            return null;
        }
        protected void onPostExecute(String doc){
            aed_adapter.notifyDataSetChanged();
        }
    }

    private class getTask2 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            getHospitalData();
            return null;
        }
        protected void onPostExecute(String doc){
            aed_adapter.notifyDataSetChanged();
        }
    }

    class aedAdapter extends BaseAdapter {
        ArrayList<Aed_item> items = new ArrayList<>();
        @Override
        public int getCount() {//데이터의 개수
            return items.size();
        }

        public void addItem(Aed_item item) {
            items.add(item);
        }

        public void clears(){ }

        @Override
        public Object getItem(int position) {//아이템의 인덱스를 통해 아이템을 하나씩 받아오는 역활
            return items.get(position);
        }

        @Override
        //아이템의 아디디값을 인덱스로 받아오고, 앞에서 각각의 아이템에 position을 통해 인덱스을 반들었으니 position을 받아옴
        public long getItemId(int position) {
            return position;
        }

        @Override
        //각각의 아이템을 위한 뷰를 지정해 주는 역할
        public View getView(int position, View convertView, ViewGroup parent) {
            AeditemView view = null;
            if(convertView == null){
                view = new AeditemView(Emergency_info.this);
            }
            else {
                view = (AeditemView) convertView;
            }
            Aed_item item = items.get(position);
            view.setLocation(item.getLocation());
            view.setPlace(item.getPlace());
            view.setDistance(item.getDistance());
            //view.setoptime(item.getoptime());

            aed_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Emergency_info.this, itemMap.class);
                    intent.putExtra("pla",items.get(position).place);
                    intent.putExtra("loc",items.get(position).location);
                    intent.putExtra("Lat",items.get(position).Lat);
                    intent.putExtra("Lon",items.get(position).Lon);
                    intent.putExtra("dis",items.get(position).distance);
                    Toast.makeText(Emergency_info.this, items.get(position).getPlace(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    //finish();
                }
            });
            return view;
        }
    }

    class pharmAdapter extends BaseAdapter {
        ArrayList<Pharm_item> items = new ArrayList<>();
        @Override
        public int getCount() {//데이터의 개수
            return items.size();
        }

        public void addItem(Pharm_item item) {
            items.add(item);
        }
        public void clears(){}
        @Override
        public Object getItem(int position) {//아이템의 인덱스를 통해 아이템을 하나씩 받아오는 역활
            return items.get(position);
        }
        @Override
        //아이템의 아디디값을 인덱스로 받아오고, 앞에서 각각의 아이템에 position을 통해 인덱스을 반들었으니 position을 받아옴
        public long getItemId(int position) {
            return position;
        }
        @Override
        //각각의 아이템을 위한 뷰를 지정해 주는 역할
        public View getView(int position, View convertView, ViewGroup parent) {

            PharmitemView view =null;
            if(convertView == null){
                view = new PharmitemView(Emergency_info.this);
            }
            else {
                view = (PharmitemView) convertView;
            }
            Pharm_item item = items.get(position);

            view.setName(item.getName());
            view.setLocation(item.getLocation());
            view.setDistance(item.getDistance());
            return view;
        }
    }

    class hospitalAdapter extends BaseAdapter {
        ArrayList<Emergencyroom_item> items = new ArrayList<>();
        @Override
        public int getCount() {//데이터의 개수
            return items.size();
        }

        public void addItem(Emergencyroom_item item) {
            items.add(item);
        }
        public void clears(){

        }
        @Override
        public Object getItem(int position) {//아이템의 인덱스를 통해 아이템을 하나씩 받아오는 역활
            return items.get(position);
        }
        @Override
        //아이템의 아디디값을 인덱스로 받아오고, 앞에서 각각의 아이템에 position을 통해 인덱스을 반들었으니 position을 받아옴
        public long getItemId(int position) {
            return position;
        }
        @Override
        //각각의 아이템을 위한 뷰를 지정해 주는 역할
        public View getView(int position, View convertView, ViewGroup parent) {

            EmergencyroomView view =null;
            if(convertView == null){
                view = new EmergencyroomView(Emergency_info.this);
            }
            else {
                view = (EmergencyroomView) convertView;
            }
            Emergencyroom_item item = items.get(position);

            view.setName(item.getName());
            view.setLocation(item.getLocation());
            view.setDistance(item.getDistance());
            return view;
        }
    }

    // 버튼을 클릭했을 때 쓰레드를 생성하여 해당 함수를 실행하여 텍스트뷰에 데이터 출력
    public void buttonClicked(View v){
        if (v.getId() == R.id.aed_button) {// 쓰레드를 생성하여 돌리는 구간
            gpsTracker = new GpsTracker(Emergency_info.this);
            if(aed_adapter.items!=null) aed_adapter.items.clear();
            if(pharm_adapter.items!=null) pharm_adapter.items.clear();
            if(hospital_adapter.items!=null) hospital_adapter.items.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText(data);
                            aed_adapter.notifyDataSetChanged();

                        }
                    });
                }
            }).start();
        }
    }

    // 버튼을 클릭했을 때 쓰레드를 생성하여 해당 함수를 실행하여 텍스트뷰에 데이터 출력
    public void pharm_buttonClicked(View v){
        if (v.getId() == R.id.pharm_button) {// 쓰레드를 생성하여 돌리는 구간
            getTask task = new getTask();
            gpsTracker = new GpsTracker(Emergency_info.this);
            if(aed_adapter.items!=null) aed_adapter.items.clear();
            if(pharm_adapter.items!=null) pharm_adapter.items.clear();
            if(hospital_adapter.items!=null) hospital_adapter.items.clear();
            task.execute();
        }
    }

    public void hospital_buttonClicked(View v){
        if (v.getId() == R.id.hospital_button) {// 쓰레드를 생성하여 돌리는 구간
            getTask2 task2 = new getTask2();
            gpsTracker = new GpsTracker(Emergency_info.this);
            if(aed_adapter.items!=null) aed_adapter.items.clear();
            if(pharm_adapter.items!=null) pharm_adapter.items.clear();
            if(hospital_adapter.items!=null) hospital_adapter.items.clear();
            task2.execute();
        }
    }

    //데이터 파싱
    void getData(){

        String lat="0";
        String lon="0";

        String loc="0";
        String pla="0";
        Double dis=1.0;

        String queryUrl="http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedLcinfoInqire?serviceKey=ZJ9cBq1X%2FnAmp2BUYq7bAurX%2BwirEd8VArnemG%2FyTofcQkmzRggGV1M5zKlPNom2S9v9VDAdqApNO2iLiwOmqQ%3D%3D&WGS84_LAT="
                +gpsTracker.getLatitude()
                +"&WGS84_LON="+gpsTracker.getLongitude()
                +"&pageNo=1&numOfRows=10";


        try {
            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            // inputstream 으로부터 xml 입력받기
            xpp.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기

                        if(tag.equals("item"));
                        else if(tag.equals("buildAddress")){//주소

                            xpp.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            loc=xpp.getText();
                        }

                        else if(tag.equals("buildPlace")){//장소
                            xpp.next();
                            pla=xpp.getText();
                        }

                        else if(tag.equals("manager")){
                            xpp.next();
                        }

                        else if(tag.equals("wgs84Lat")){//위도
                            xpp.next();
                            lat = xpp.getText();
                        }

                        else if(tag.equals("wgs84Lon")){//경도 및 거리차
                            xpp.next();
                            lon = xpp.getText();
                            dis=getDistance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),lat,lon);
                            aed_adapter.addItem(new Aed_item(loc,pla,dis,lat,lon));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환
    }


    void getPharmData(){

        String lat="0";
        String lon="0";

        String loc="0";
        String pla="0";
        Double dis=1.0;
        String tim="0";

        String queryUrl="http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyLcinfoInqire?serviceKey=ZJ9cBq1X%2FnAmp2BUYq7bAurX%2BwirEd8VArnemG%2FyTofcQkmzRggGV1M5zKlPNom2S9v9VDAdqApNO2iLiwOmqQ%3D%3D&WGS84_LON="
                +gpsTracker.getLongitude()
                +"&WGS84_LAT="+gpsTracker.getLatitude()
                +"&pageNo=1&numOfRows=10";


        try {
            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결

            XmlPullParserFactory factory2= XmlPullParserFactory.newInstance();
            XmlPullParser xpp2= factory2.newPullParser();
            // inputstream 으로부터 xml 입력받기
            xpp2.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp2.next();
            int eventType= xpp2.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp2.getName(); // 태그 이름 얻어오기

                        if(tag.equals("item"));
                        else if(tag.equals("dutyName")){//이름
                            xpp2.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            loc=xpp2.getText();
                        }

                        else if(tag.equals("dutyAddr")){//위도
                            xpp2.next();
                            pla=xpp2.getText();
                        }

                        else if(tag.equals("startTime")){//전화
                            xpp2.next();
                            tim=xpp2.getText();
                        }

                        else if(tag.equals("latitude")){//위도
                            xpp2.next();
                            lat = xpp2.getText();
                        }

                        else if(tag.equals("longitude")){//경도 및 거리차
                            xpp2.next();
                            lon = xpp2.getText();
                            dis=getDistance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),lat,lon);
                            //aed_adapter.addItem(new aed_item(loc,pla,dis,tim));
                            aed_adapter.addItem(new Aed_item(loc,pla,dis,lat,lon));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp2.getName(); // 태그 이름 얻어오기
                        break;
                }
                eventType= xpp2.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환
    }

    String getHospitalData(){

        String lat="0";
        String lon="0";

        String loc="0";
        String pla="0";
        Double dis=1.0;
        String tim="0";

        String queryUrl="http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytLcinfoInqire?serviceKey=ZJ9cBq1X%2FnAmp2BUYq7bAurX%2BwirEd8VArnemG%2FyTofcQkmzRggGV1M5zKlPNom2S9v9VDAdqApNO2iLiwOmqQ%3D%3D&WGS84_LON="
                +gpsTracker.getLongitude()
                +"&WGS84_LAT="+gpsTracker.getLatitude()
                +"&pageNo=1&numOfRows=10";


        try {
            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결

            XmlPullParserFactory factory3= XmlPullParserFactory.newInstance();
            XmlPullParser xpp3= factory3.newPullParser();
            // inputstream 으로부터 xml 입력받기
            xpp3.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp3.next();
            int eventType= xpp3.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp3.getName(); // 태그 이름 얻어오기

                        if(tag.equals("item"));
                        else if(tag.equals("dutyName")){//이름
                            xpp3.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            loc=xpp3.getText();
                        }

                        else if(tag.equals("dutyAddr")){//주소
                            xpp3.next();
                            pla=xpp3.getText();
                        }

                        else if(tag.equals("startTime")){
                            xpp3.next();
                            tim=xpp3.getText();
                        }

                        else if(tag.equals("latitude")){
                            xpp3.next();
                            lat = xpp3.getText();
                        }

                        else if(tag.equals("longitude")){
                            xpp3.next();
                            lon = xpp3.getText();
                            dis=getDistance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),lat,lon);
                            //aed_adapter.addItem(new aed_item(loc,pla,dis,tim));
                            aed_adapter.addItem(new Aed_item(loc,pla,dis,lat,lon));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp3.getName(); // 태그 이름 얻어오기
                        break;
                }
                eventType= xpp3.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환
        return null;
    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) { }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(Emergency_info.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Emergency_info.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Emergency_info.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Emergency_info.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) { }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Emergency_info.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(Emergency_info.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(Emergency_info.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(Emergency_info.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Emergency_info.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public double getDistance(double lat1 , double lon1 , String lat2 , String lon2 ){//위도 경도 이용하여 거리계산
        double distance;

        double lat3 = Double.parseDouble(lat2);
        double lon3 = Double.parseDouble(lon2);

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat3);
        locationB.setLongitude(lon3);

        distance = (int) locationA.distanceTo(locationB);

        return distance;
    }
}
