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

        aed_adapter = new aedAdapter();//?????????
        pharm_adapter = new pharmAdapter();
        hospital_adapter = new hospitalAdapter();

        pharm_listView = findViewById(R.id.pharm_listview);
        aed_listView = findViewById(R.id.aed_listview);
        hospital_listView = findViewById(R.id.hospital_listview);

        pharm_listView.setAdapter(pharm_adapter);
        aed_listView.setAdapter(aed_adapter);//??????????????? ?????? ?????????????????? ?????????
        hospital_listView.setAdapter(hospital_adapter);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        //Dashboard ???????????? ?????? ?????? ??? ?????? ????????? ?????? ??????????????? ?????? ??????
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
        public int getCount() {//???????????? ??????
            return items.size();
        }

        public void addItem(Aed_item item) {
            items.add(item);
        }

        public void clears(){ }

        @Override
        public Object getItem(int position) {//???????????? ???????????? ?????? ???????????? ????????? ???????????? ??????
            return items.get(position);
        }

        @Override
        //???????????? ??????????????? ???????????? ????????????, ????????? ????????? ???????????? position??? ?????? ???????????? ??????????????? position??? ?????????
        public long getItemId(int position) {
            return position;
        }

        @Override
        //????????? ???????????? ?????? ?????? ????????? ?????? ??????
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
        public int getCount() {//???????????? ??????
            return items.size();
        }

        public void addItem(Pharm_item item) {
            items.add(item);
        }
        public void clears(){}
        @Override
        public Object getItem(int position) {//???????????? ???????????? ?????? ???????????? ????????? ???????????? ??????
            return items.get(position);
        }
        @Override
        //???????????? ??????????????? ???????????? ????????????, ????????? ????????? ???????????? position??? ?????? ???????????? ??????????????? position??? ?????????
        public long getItemId(int position) {
            return position;
        }
        @Override
        //????????? ???????????? ?????? ?????? ????????? ?????? ??????
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
        public int getCount() {//???????????? ??????
            return items.size();
        }

        public void addItem(Emergencyroom_item item) {
            items.add(item);
        }
        public void clears(){

        }
        @Override
        public Object getItem(int position) {//???????????? ???????????? ?????? ???????????? ????????? ???????????? ??????
            return items.get(position);
        }
        @Override
        //???????????? ??????????????? ???????????? ????????????, ????????? ????????? ???????????? position??? ?????? ???????????? ??????????????? position??? ?????????
        public long getItemId(int position) {
            return position;
        }
        @Override
        //????????? ???????????? ?????? ?????? ????????? ?????? ??????
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

    // ????????? ???????????? ??? ???????????? ???????????? ?????? ????????? ???????????? ??????????????? ????????? ??????
    public void buttonClicked(View v){
        if (v.getId() == R.id.aed_button) {// ???????????? ???????????? ????????? ??????
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

    // ????????? ???????????? ??? ???????????? ???????????? ?????? ????????? ???????????? ??????????????? ????????? ??????
    public void pharm_buttonClicked(View v){
        if (v.getId() == R.id.pharm_button) {// ???????????? ???????????? ????????? ??????
            getTask task = new getTask();
            gpsTracker = new GpsTracker(Emergency_info.this);
            if(aed_adapter.items!=null) aed_adapter.items.clear();
            if(pharm_adapter.items!=null) pharm_adapter.items.clear();
            if(hospital_adapter.items!=null) hospital_adapter.items.clear();
            task.execute();
        }
    }

    public void hospital_buttonClicked(View v){
        if (v.getId() == R.id.hospital_button) {// ???????????? ???????????? ????????? ??????
            getTask2 task2 = new getTask2();
            gpsTracker = new GpsTracker(Emergency_info.this);
            if(aed_adapter.items!=null) aed_adapter.items.clear();
            if(pharm_adapter.items!=null) pharm_adapter.items.clear();
            if(hospital_adapter.items!=null) hospital_adapter.items.clear();
            task2.execute();
        }
    }

    //????????? ??????
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
            URL url= new URL(queryUrl); // ???????????? ??? ?????? url??? URL ????????? ??????.
            InputStream is= url.openStream(); // url ????????? ??????????????? ??????

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            // inputstream ???????????? xml ????????????
            xpp.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // ?????? ?????? ????????????

                        if(tag.equals("item"));
                        else if(tag.equals("buildAddress")){//??????

                            xpp.next();
                            // addr ????????? TEXT ???????????? ?????????????????? ??????
                            loc=xpp.getText();
                        }

                        else if(tag.equals("buildPlace")){//??????
                            xpp.next();
                            pla=xpp.getText();
                        }

                        else if(tag.equals("manager")){
                            xpp.next();
                        }

                        else if(tag.equals("wgs84Lat")){//??????
                            xpp.next();
                            lat = xpp.getText();
                        }

                        else if(tag.equals("wgs84Lon")){//?????? ??? ?????????
                            xpp.next();
                            lon = xpp.getText();
                            dis=getDistance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),lat,lon);
                            aed_adapter.addItem(new Aed_item(loc,pla,dis,lat,lon));
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // ?????? ?????? ????????????
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // ?????? ??? ?????? ??? StringBuffer ????????? ?????? ??????
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
            URL url= new URL(queryUrl); // ???????????? ??? ?????? url??? URL ????????? ??????.
            InputStream is= url.openStream(); // url ????????? ??????????????? ??????

            XmlPullParserFactory factory2= XmlPullParserFactory.newInstance();
            XmlPullParser xpp2= factory2.newPullParser();
            // inputstream ???????????? xml ????????????
            xpp2.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp2.next();
            int eventType= xpp2.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp2.getName(); // ?????? ?????? ????????????

                        if(tag.equals("item"));
                        else if(tag.equals("dutyName")){//??????
                            xpp2.next();
                            // addr ????????? TEXT ???????????? ?????????????????? ??????
                            loc=xpp2.getText();
                        }

                        else if(tag.equals("dutyAddr")){//??????
                            xpp2.next();
                            pla=xpp2.getText();
                        }

                        else if(tag.equals("startTime")){//??????
                            xpp2.next();
                            tim=xpp2.getText();
                        }

                        else if(tag.equals("latitude")){//??????
                            xpp2.next();
                            lat = xpp2.getText();
                        }

                        else if(tag.equals("longitude")){//?????? ??? ?????????
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
                        tag= xpp2.getName(); // ?????? ?????? ????????????
                        break;
                }
                eventType= xpp2.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // ?????? ??? ?????? ??? StringBuffer ????????? ?????? ??????
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
            URL url= new URL(queryUrl); // ???????????? ??? ?????? url??? URL ????????? ??????.
            InputStream is= url.openStream(); // url ????????? ??????????????? ??????

            XmlPullParserFactory factory3= XmlPullParserFactory.newInstance();
            XmlPullParser xpp3= factory3.newPullParser();
            // inputstream ???????????? xml ????????????
            xpp3.setInput( new InputStreamReader(is, StandardCharsets.UTF_8) );
            String tag;
            xpp3.next();
            int eventType= xpp3.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp3.getName(); // ?????? ?????? ????????????

                        if(tag.equals("item"));
                        else if(tag.equals("dutyName")){//??????
                            xpp3.next();
                            // addr ????????? TEXT ???????????? ?????????????????? ??????
                            loc=xpp3.getText();
                        }

                        else if(tag.equals("dutyAddr")){//??????
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
                        tag= xpp3.getName(); // ?????? ?????? ????????????
                        break;
                }
                eventType= xpp3.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return buffer.toString(); // ?????? ??? ?????? ??? StringBuffer ????????? ?????? ??????
        return null;
    }


    /*
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
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
                    Toast.makeText(Emergency_info.this, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Emergency_info.this, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Emergency_info.this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(Emergency_info.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(Emergency_info.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    public String getCurrentAddress( double latitude, double longitude) {
        //????????????... GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }


    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Emergency_info.this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
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

    public double getDistance(double lat1 , double lon1 , String lat2 , String lon2 ){//?????? ?????? ???????????? ????????????
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
