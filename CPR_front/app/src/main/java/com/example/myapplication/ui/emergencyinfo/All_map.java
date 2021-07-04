package com.example.myapplication.ui.emergencyinfo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.myapplication.ui.emergencyinfo.AED.Aed_item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.R;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class All_map extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        int positio = 0;
        Intent intent = getIntent();
        ArrayList<Aed_item> list = (ArrayList<Aed_item>) intent.getSerializableExtra("aed_list");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().isRotateGesturesEnabled();
        mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        initializeLocation();

        assert list != null;
        for(int position=0; position<list.size();position++){
            LatLng SEOUL = new LatLng(parseDouble(list.get(position).Lat), parseDouble(list.get(position).Lon));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEOUL);
            markerOptions.title(list.get(position).location);
            if(list.get(position).distance>1000) {
                markerOptions.snippet(list.get(position).distance/1000 + "km");
            }
            else{
                markerOptions.snippet(list.get(position).distance + "m");
            }
            mMap.addMarker(markerOptions);
        }
        LatLng location = new LatLng(intent.getExtras().getDouble("my_Lat"), intent.getExtras().getDouble("my_Lon"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeLocation();
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    public void initializeLocation() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    public void setLocation(LatLng latLng, String locname) {
        Intent intent = getIntent();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMinZoomPreference(15);
    }
}