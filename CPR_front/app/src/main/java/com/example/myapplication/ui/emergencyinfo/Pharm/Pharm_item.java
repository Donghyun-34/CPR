package com.example.myapplication.ui.emergencyinfo.Pharm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Pharm_item{

    String pharm_name;
    String location;
    Double distance;

    public Pharm_item(String location, String pharm_name, Double distance){
        this.pharm_name = pharm_name;
        this.location = location;
        this.distance = distance;

    }

    public String getName() {
        return pharm_name;
    }

    public void setName(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}