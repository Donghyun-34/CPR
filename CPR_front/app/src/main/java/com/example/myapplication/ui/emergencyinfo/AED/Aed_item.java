package com.example.myapplication.ui.emergencyinfo.AED;

import java.io.Serializable;

public class Aed_item implements Serializable {
    public String location;
    public String place;
    public Double distance;
    public String time;
    public String Lat;
    public String Lon;

    public Aed_item(String location, String place, Double distance){
        this.location = location;
        this.place = place;
        this.distance = distance;

    }

    public Aed_item(String location, String place, Double distance,String time){
        this.location = location;
        this.place = place;
        this.distance = distance;
        this.time = time;
    }

    public Aed_item(String location, String place, Double distance, String Lat, String Lon){
        this.location = location;
        this.place = place;
        this.distance = distance;
        this.time = time;
        this.Lat=Lat;
        this.Lon=Lon;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location =location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getDistance() {
        return distance;
    }

    public String setDistance(Double distance) {
        this.distance = distance;
        return Double.toString(this.distance);
    }

    public String getoptime() {
        return time;
    }

    public String settime(String time) {
        this.time = time;
        return this.time;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String Lon) {
        this.Lat = Lon;
    }

}
