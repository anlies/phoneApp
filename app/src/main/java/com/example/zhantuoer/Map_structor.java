package com.example.zhantuoer;

public class Map_structor {
    //用户手机号
    private String phoneNumber;
    //经度
    private double longitude;
    //纬度
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Map_structor(String phoneNumber, double longitude, double latitude) {
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
