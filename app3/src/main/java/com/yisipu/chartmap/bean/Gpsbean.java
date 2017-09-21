package com.yisipu.chartmap.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Gpsbean implements Serializable{


    /*
    经度
    */
    private double GpsLatitude;
    /*
    纬度
     */
    private double GpsLongitude;

    public double getGpsLatitude() {
        return GpsLatitude;
    }

    public void setGpsLatitude(double gpsLatitude) {
        GpsLatitude = gpsLatitude;
    }

    public double getGpsLongitude() {
        return GpsLongitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        GpsLongitude = gpsLongitude;
    }
}
