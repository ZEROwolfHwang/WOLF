package com.yisipu.chartmap.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class SatelliteBean implements Serializable{
    int Elevation; //卫星仰角
    int  Azimuth;   //卫星方位角
    int Snr;       //信噪比
   int Prn;       //伪随机数，可以认为他就是卫星的编号

    public SatelliteBean() {
    }

    public int getElevation() {
        return Elevation;
    }

    public void setElevation(int elevation) {
        Elevation = elevation;
    }

    public int getAzimuth() {
        return Azimuth;
    }

    public void setAzimuth(int azimuth) {
        Azimuth = azimuth;
    }

    public int getSnr() {
        return Snr;
    }

    public void setSnr(int snr) {
        Snr = snr;
    }

    public int getPrn() {
        return Prn;
    }

    public void setPrn(int prn) {
        Prn = prn;
    }

    @Override
    public String toString() {
        return "SatelliteBean{" +
                "Elevation=" + Elevation +
                ", Azimuth=" + Azimuth +
                ", Snr=" + Snr +
                ", Prn=" + Prn +
                '}';
    }
}
