package com.example.myapplication.ui.emergencyinfo.AED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication.R;

public class AeditemView extends LinearLayout {
    TextView Aed_location;
    TextView Aed_place;
    TextView Aed_distance;


    public AeditemView(Context context) {
        super(context);
        inflation_init(context);
    }

    public AeditemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflation_init(context);
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.aed_item, this, true);

        Aed_location = (TextView) findViewById(R.id.location);
        Aed_place = (TextView) findViewById(R.id.place);
        Aed_distance = (TextView) findViewById(R.id.distance);
    }

    public void setLocation(String location){
        Aed_location.setText(location);
    }
    public void setPlace(String place){

        Aed_place.setText(place);
    }
    @SuppressLint("SetTextI18n")
    public void setDistance(Double distance){
        if(distance>1000){Aed_distance.setText(Double.toString(distance/1000)+"km");}
        else{Aed_distance.setText(Double.toString(distance)+"m");}
    }
}
