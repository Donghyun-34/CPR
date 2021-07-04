package com.example.myapplication.ui.emergencyinfo.Pharm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication.R;
import androidx.annotation.Nullable;

public class PharmitemView extends LinearLayout {
    TextView pharm_name;
    TextView pharm_location;
    TextView pharm_distance;


    public PharmitemView(Context context) {
        super(context);
        inflation_init(context);
    }

    public PharmitemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflation_init(context);
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pharm_item, this, true);

        pharm_name = (TextView) findViewById(R.id.name);
        pharm_location = (TextView) findViewById(R.id.location);
        pharm_distance = (TextView) findViewById(R.id.distance);
    }

    public void setLocation(String location){
        pharm_location.setText(location);
    }
    public void setName(String name){
        pharm_name.setText(name);
    }
    @SuppressLint("SetTextI18n")
    public void setDistance(Double distance){
        pharm_distance.setText(Double.toString(distance));
    }
}
