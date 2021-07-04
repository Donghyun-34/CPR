package com.example.myapplication.ui.emergencyinfo.Emergency_room;


public class Emergencyroom_item{

    String hospital_name;
    String location;
    Double distance;

    public Emergencyroom_item(String location, String hospital_name, Double distance){
        this.hospital_name = hospital_name;
        this.location = location;
        this.distance = distance;

    }

    public String getName() {
        return hospital_name;
    }

    public void setName(String hospital_name) {
        this.hospital_name = hospital_name;
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