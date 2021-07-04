package com.example.myapplication.ui.emergencyinfo.Emergency_room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication.R;
import androidx.annotation.Nullable;

public class EmergencyroomView  extends LinearLayout {
    TextView hospital_name;
    TextView hospital_location;
    TextView hospital_distance;


    public EmergencyroomView(Context context) {
        super(context);
        inflation_init(context);
    }

    public EmergencyroomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflation_init(context);
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.emergencyroom_item, this, true);

        hospital_name = (TextView) findViewById(R.id.name);
        hospital_location = (TextView) findViewById(R.id.location);
        hospital_distance = (TextView) findViewById(R.id.distance);
    }

    public void setLocation(String location){
        hospital_location.setText(location);
    }
    public void setName(String name){
        hospital_name.setText(name);
    }
    @SuppressLint("SetTextI18n")
    public void setDistance(Double distance){
        hospital_distance.setText(Double.toString(distance));
    }
}
