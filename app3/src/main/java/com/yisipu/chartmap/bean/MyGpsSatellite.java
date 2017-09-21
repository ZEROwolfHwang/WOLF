package com.yisipu.chartmap.bean;

import android.location.GpsSatellite;
import android.location.LocationManager;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/15.
 */
public class MyGpsSatellite implements Serializable,Comparable<MyGpsSatellite>{
    /* These package private values are modified by the GpsStatus class */
    boolean mValid;
    boolean mHasEphemeris;
    boolean mHasAlmanac;
    boolean mUsedInFix;
    int mPrn;
    float mSnr;
    float mElevation;
    float mAzimuth;

    MyGpsSatellite(int prn) {
        mPrn = prn;
    }

    public MyGpsSatellite() {
    }

    /**
     * Used by {@link LocationManager#getGpsStatus} to copy LocationManager's
     * cached GpsStatus instance to the client's copy.
     */
    void setStatus(MyGpsSatellite satellite) {
        if (satellite == null) {
            mValid = false;
        } else {
            mValid = satellite.mValid;
            mHasEphemeris = satellite.mHasEphemeris;
            mHasAlmanac = satellite.mHasAlmanac;
            mUsedInFix = satellite.mUsedInFix;
            mSnr = satellite.mSnr;
            mElevation = satellite.mElevation;
            mAzimuth = satellite.mAzimuth;
        }
    }

    /**
     * Returns the PRN (pseudo-random number) for the satellite.
     *
     * @return PRN number
     */
    public int getPrn() {
        return mPrn;
    }

    /**
     * Returns the signal to noise ratio for the satellite.
     *
     * @return the signal to noise ratio
     */
    public float getSnr() {
        return mSnr;
    }

    /**
     * Returns the elevation of the satellite in degrees.
     * The elevation can vary between 0 and 90.
     *
     * @return the elevation in degrees
     */
    public float getElevation() {
        return mElevation;
    }

    /**
     * Returns the azimuth of the satellite in degrees.
     * The azimuth can vary between 0 and 360.
     *
     * @return the azimuth in degrees
     */
    public float getAzimuth() {
        return mAzimuth;
    }

    /**
     * Returns true if the GPS engine has ephemeris data for the satellite.
     *
     * @return true if the satellite has ephemeris data
     */
    public boolean hasEphemeris() {
        return mHasEphemeris;
    }

    /**
     * Returns true if the GPS engine has almanac data for the satellite.
     *
     * @return true if the satellite has almanac data
     */
    public boolean hasAlmanac() {
        return mHasAlmanac;
    }

    /**
     * Returns true if the satellite was used by the GPS engine when
     * calculating the most recent GPS fix.
     *
     * @return true if the satellite was used to compute the most recent fix.
     */
    public boolean usedInFix() {
        return mUsedInFix;
    }

    @Override
    public int compareTo(MyGpsSatellite another) {
        if(this.getSnr()>=30&&this.getSnr()<=50){
           if(another.getSnr()<30||another.getSnr()>50) {
               return  -1;
           }
            if(this.getSnr()<another.getSnr()){
                return -1;
            }else if(this.getSnr()==another.getSnr()){
                return 0;
            }else {
                return  1;
            }

            }else{
            return 1;
        }

    }

    public void setmValid(boolean mValid) {
        this.mValid = mValid;
    }

    public void setmHasEphemeris(boolean mHasEphemeris) {
        this.mHasEphemeris = mHasEphemeris;
    }

    public void setmHasAlmanac(boolean mHasAlmanac) {
        this.mHasAlmanac = mHasAlmanac;
    }

    public void setmUsedInFix(boolean mUsedInFix) {
        this.mUsedInFix = mUsedInFix;
    }

    public void setmPrn(int mPrn) {
        this.mPrn = mPrn;
    }

    public void setmSnr(float mSnr) {
        this.mSnr = mSnr;
    }

    public void setmElevation(float mElevation) {
        this.mElevation = mElevation;
    }

    public void setmAzimuth(float mAzimuth) {
        this.mAzimuth = mAzimuth;
    }
}
