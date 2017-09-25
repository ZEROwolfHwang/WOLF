package com.yisipu.chartmap.gps;

/**
 * Created by Administrator on 2016/9/22.
 */
public class MyLocation {
    public double latitude;
    public double longitude;
    public long updateTime;//最后更新时间，用于做精确度择优
    public float accuracy;
    public float speed;
    private static MyLocation myLocation;
    MyLocation(){}

    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static MyLocation getInstance(){
        if(myLocation == null)myLocation = new MyLocation();
        return myLocation;
    }

    @Override
    public String toString() {
        return "MyLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", updateTime=" + updateTime +
                ", accuracy=" + accuracy +
                ", speed=" + speed +
                '}';
    }
}
