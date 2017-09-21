package com.yisipu.chartmap.utils;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/31.
 */
public class LocationUtils {
    private static final String TAG = "LocationUtils";

    /*
    计算距离
     */
    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * Constant.EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    // 计算方位角pab。
    public static double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;

        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;

//     d = Math.round(d*10000);
        return d;
    }
    //判断海里内是否有60马力以上的船舶

    /**
     * @param context
     * @param distance    设置的海里范围
     * @param db
     * @param myLatitude  本船的经度
     * @param myLongitude 本船的纬度
     * @return
     */
    public static boolean aisstate(Context context, int distance, DBManager db, double myLatitude, double myLongitude) {
        List<ShipBean> list = new ArrayList<>();
        int x = 0;
        if (db == null) {
            db = new DBManager(context);
        }
        list = db.getSog(60);
        for (ShipBean shipBean : list) {
            double latitude = shipBean.getLatitude();//其他船的经度
            double longitude = shipBean.getLongitude();//其他船的纬度
            double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
            if (distance2 > (double) distance) {
                x = 1;
                break;
            }
        }
        if (x == 1) {
            return true;
        } else {
            return false;
        }

    }
    //判断海里内是否有船舶

    /**
     * @param context
     * @param distance    设置报警的海里范围
     * @param db
     * @param myLatitude  本船的纬度
     * @param myLongitude 本船的经度
     * @return
     */
//    public static boolean lpbj(Context context, Float distance, DBManager db, double myLatitude, double myLongitude) {
//        List<ShipBean> list = new ArrayList<>();
//        Logger.d("距离2", "进来了lpbj");
//        int x = 0;
//        if (db == null) {
//            db = new DBManager(context);
//        }
//        list = db.getShipBeans();
//        for (ShipBean shipBean : list) {
//            boolean bc=shipBean.getMyShip();
//            if(bc==false){
//            double latitude = shipBean.getLatitude();//其他船的经度
//            double longitude = shipBean.getLongitude();//其他船的纬度
//            double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
//            double aqfw=(double)distance;
//            Logger.d(TAG,"距离："+distance2);
//            Logger.i("距离距离"+aqfw);
//            Logger.i("其他船的经度"+latitude);
//            Logger.i("其他船的纬度"+longitude);
//            if (distance2 < aqfw) {
//                x = 1;
//                break;
//            }
//        }
//        }
//        if (x == 1) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
    public static boolean lpbj(Context context, Float distance, DBManager db, double myLatitude, double myLongitude) {
        List<ShipBean> list = new ArrayList<>();
        int x = 0;
        if (db == null) {
            db = new DBManager(context);
        }
        list = db.getShipBeans();
//        List<Integer> ls
        for (ShipBean shipBean : list) {
            boolean bc = shipBean.getMyShip();
            if (bc == false) {
                double longitude = shipBean.getLongitude();//其他船的经度
                double latitude = shipBean.getLatitude();//其他船的纬度
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("lpbj", "没进入");
//                if (latitude == 181 && longitude == 91 && myLatitude == 181 && myLongitude ==91) {
                if (longitude != -1 && latitude != -1 && latitude != 91 && longitude != 181 && myLatitude != 91 && myLongitude != 181) {
                    double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
                    double aqfw = (double) distance;
                    Logger.d(TAG, "距离：" + distance2);
                    Logger.d("lpbj", "进入了");
                    Logger.i("距离距离" + aqfw);
                    Logger.d("距离aa", "距离：" + distance2);
                    Logger.i("其他船的纬度" + latitude);
                    Logger.i("其他船的经度" + longitude);
                    Logger.d("其他船的纬度2", "" + latitude);
                    Logger.d("其他船的经度2", "" + longitude);
                    if (distance2 < aqfw && distance2 != 0) {
                        x = 1;
                        break;
                    }
                }
            }
        }
        if (x == 1) {
            return true;
        } else {
            return false;
        }

    }
    public static List<ShipBean> lpbjList(Context context, Float distance, DBManager db, double myLatitude, double myLongitude) {
        List<ShipBean> list = new ArrayList<>();
        List<ShipBean> list2 = new ArrayList<>();
        int x = 0;
        if (db == null) {
            db = new DBManager(context);
        }
        list = db.getShipBeans();
//        List<Integer> ls
        for (ShipBean shipBean : list) {
            boolean bc = shipBean.getMyShip();
            if (bc == false) {
                double longitude = shipBean.getLongitude();//其他船的经度
                double latitude = shipBean.getLatitude();//其他船的纬度
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("lpbj", "没进入");
//                if (latitude == 181 && longitude == 91 && myLatitude == 181 && myLongitude ==91) {
                if (longitude != -1 && latitude != -1 && latitude != 91 && longitude != 181 && myLatitude != 91 && myLongitude != 181) {
                    double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
                    double aqfw = (double) distance;
                    Logger.d(TAG, "距离：" + distance2);
                    Logger.d("lpbj", "进入了");
                    Logger.i("距离距离" + aqfw);
                    Logger.d("距离aa", "距离：" + distance2);
                    Logger.i("其他船的纬度" + latitude);
                    Logger.i("其他船的经度" + longitude);
                    Logger.d("其他船的纬度2", "" + latitude);
                    Logger.d("其他船的经度2", "" + longitude);
                    if (distance2 < aqfw && distance2 != -1) {
                        list2.add(shipBean);
                        x = 1;
//                        break;
                    }
                }
            }
        }
        return list2;
    }
//        if (x == 1) {
//            return true;
//        } else {
//            return false;
//        }

//    }

    /**
     * 距离和已知纬度转换成经度
     *
     * @param distance
     * @return
     */
    public static double doLngDegress(long distance, double Lat) {
        double lngDegree = 2 * Math.asin(Math.sin((double) distance / 12742) / Math.cos(Lat));
        // 转换弧度
        lngDegree = lngDegree * (180 / Math.PI);
        return lngDegree;
    }

    /**
     * 距离转换成纬度
     *
     * @param distance
     * @return
     */
    public static double doLatDegress(long distance) {
        double latDegrees = (double) distance / 6371;
        // 转换弧度
        latDegrees = latDegrees * (180 / Math.PI);
        return latDegrees;
    }

//判断海里内是否有速度过滤以上的船舶

    /**
     *
     * @param context
     * @param distance 设置报警的海里范围
     * @param db
     * @param myLatitude 本船的纬度
     * @param myLongitude 本船的经度
     * @param mysog 本船的速度
     * @param sdgl 过滤速度
     * @return
     */

    public static boolean lpbj2(Context context, Float distance, DBManager db, double myLatitude, double myLongitude, int mysog, float sdgl) {
        List<ShipBean> list = new ArrayList<>();
        int x = 0;
        if (db == null) {
            db = new DBManager(context);
        }
        list = db.getShipBeans();
        for (ShipBean shipBean : list) {
            boolean bc = shipBean.getMyShip();
            if (bc == false) {
                double longitude = shipBean.getLongitude();//其他船的经度
                double latitude = shipBean.getLatitude();//其他船的纬度
                double sog = shipBean.getSog()/10.0;//其他船的速度
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("lpbj", "没进入");
//                if (latitude == 181 && longitude == 91 && myLatitude == 181 && myLongitude ==91) {
                if (longitude != -1 && latitude != -1 && latitude != 91 && longitude != 181 && myLatitude != 91 && myLongitude != 181) {
                    double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
                    double aqfw = (double) distance;
                    Logger.d(TAG, "距离：" + distance2);
                    Logger.d("lpbj", "进入了");
                    Logger.i("距离距离" + aqfw);
                    Logger.d("距离aa", "距离：" + distance2);
                    Logger.i("其他船的纬度" + latitude);
                    Logger.i("其他船的经度" + longitude);
                    Logger.d("其他船的纬度2", "" + latitude);
                    Logger.d("其他船的经度2", "" + longitude);
                    if (distance2 < aqfw && distance2 != 0&&sog>sdgl&&mysog/(10.0)>sdgl) {
                        x = 1;
                        break;
                    }
                }
            }
        }
        if (x == 1) {
            return true;
        } else {
            return false;
        }

    }


    //判断海里内是否有速度过滤以上的船舶

    /**
     *
     * @param context
     * @param distance 设置报警的海里范围
     * @param db
     * @param myLatitude 本船的纬度
     * @param myLongitude 本船的经度
     * @param mysog 本船的速度
     * @param sdgl 过滤速度
     * @return
     */
     /*
     返回速度过滤后的船的经纬度列表
      */
    public static List<ShipBean> lpbj3(Context context, Float distance, DBManager db, double myLatitude, double myLongitude, int mysog, float sdgl) {
        List<ShipBean> list= new ArrayList<>();
        List<ShipBean> list2= new ArrayList<>();
        int x = 0;
        if (db == null) {
            db = new DBManager(context);
        }
        list = db.getShipBeans();
        for (ShipBean shipBean : list) {
            boolean bc = shipBean.getMyShip();
            if (bc == false) {
                double longitude = shipBean.getLongitude();//其他船的经度
                double latitude = shipBean.getLatitude();//其他船的纬度
                double sog = shipBean.getSog()/10.0;//其他船的速度
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("其他船的经度1", "" + latitude);
                Logger.d("lpbj", "没进入");
//                if (latitude == 181 && longitude == 91 && myLatitude == 181 && myLongitude ==91) {
                if (longitude != -1 && latitude != -1 && latitude != 91 && longitude != 181 && myLatitude != 91 && myLongitude != 181) {
                    double distance2 = gps2m(myLatitude, myLongitude, latitude, longitude) / 1852;
                    double aqfw = (double) distance;
                    Logger.d(TAG, "距离：" + distance2);
                    Logger.d("lpbj", "进入了");
                    Logger.i("距离距离" + aqfw);
                    Logger.d("距离aa", "距离：" + distance2);
                    Logger.i("其他船的纬度" + latitude);
                    Logger.i("其他船的经度" + longitude);
                    Logger.d("其他船的纬度2", "" + latitude);
                    Logger.d("其他船的经度2", "" + longitude);
                    if (distance2 < aqfw && distance2 != 0&&sog>sdgl&&mysog/(10.0)>sdgl) {
                      list2.add(shipBean);
                       continue;
                    }
                }
            }
        }
       return  list2;
    }
}
