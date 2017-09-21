package com.yisipu.chartmap.utils;

import java.util.Dictionary;

/**
 * Created by Administrator on 2016/10/19.
 */
public class GetLongLati {
    /*
    获得经度
     */
    public  static double getLong(int X,int inx_x,int Z){
//        double lon_deg = xtile  * 360.0 - 180.0;
//        double lat_rad = atan (sinh(pi * (1 - 2 * (ytile))));
//        double lat_deg = lat_rad* 180.0 / pi;
//
//        xtile ytile是对应的索引
//
//
//        lon_deg这个是算出来 经度（以度为单位）
//        lat_deg这个是算出来 纬度（以度为单位）

//        图片26_15_5.png，X=26，y=15，Z=5，
//        现在是在5级缩放，全世界像素点总和:Pointsize=(2的（5+8）次方)，
//        计算左上角（inx_x = 0,inx_y=0,对应图片的坐标）
        double Pointsize=Math.pow(2,Z+8);
        double xtile=(X*256+inx_x)/Pointsize;

        double lon_deg = xtile  * 360.0 - 180.0;
        return  lon_deg;

    }
    /*
    通过经度得到 x像素
     */
    public static double getX(double lon_deg,int z){
        double Pointsize=Math.pow(2,z+8);


       double a1= DecimalCalculate.add(lon_deg,180);
        double a2=DecimalCalculate.div(a1,360.0);
        return  DecimalCalculate.mul(a2,Pointsize);

//        double Pointsize=Math.pow(2,z+8);
//        double x=(lon_deg+180)/(360.0)*Pointsize;
//
//        return x;
//        return (lon_deg + 180) * (256L << z) / 360.0;
    }
    /*
  获得纬度
   */
    public static double getlat(int Y,int inx_y,int Z){
//        double lon_deg = xtile  * 360.0 - 180.0;
//        double lat_rad = atan (sinh(pi * (1 - 2 * (ytile))));
//        double lat_deg = lat_rad* 180.0 / pi;
//
//        xtile ytile是对应的索引
//
//
//        lon_deg这个是算出来 经度（以度为单位）
//        lat_deg这个是算出来 纬度（以度为单位）

//        图片26_15_5.png，X=26，y=15，Z=5，
//        现在是在5级缩放，全世界像素点总和:Pointsize=(2的（5+8）次方)，
//        计算左上角（inx_x = 0,inx_y=0,对应图片的坐标）
        double Pointsize=Math.pow(2,Z+8);

       double ytile=(Y*256+inx_y)/Pointsize;



        double lat_rad = Math.atan (Math.sinh(Math.PI * (1 - 2 * (ytile))));
//        (这个是中间转换量，为了算出lat_deg)
        double lat_deg = lat_rad* 180.0 /Math.PI;
        return  lat_deg;
    }
    public static String getTileNumber(final double lat, final double lon, final int zoom) {
        int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
        int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
        if (xtile < 0)
            xtile=0;
        if (xtile >= (1<<zoom))
            xtile=((1<<zoom)-1);
        if (ytile < 0)
            ytile=0;
        if (ytile >= (1<<zoom))
            ytile=((1<<zoom)-1);
        return("" + zoom + "/" + xtile + "/" + ytile);
    }
    public static  double getY(double lat, int zoom) {
        double a1=DecimalCalculate.mul(lat,Math.PI);
        double a2=DecimalCalculate.div(a1,180);
        double siny = Math.sin(a2);
        double a3=DecimalCalculate.div(1+siny,1-siny);

        double y=Math.log(a3);
        double a4=DecimalCalculate.div(y,2 * Math.PI);
        double a5=DecimalCalculate.sub(1,a4);
       return DecimalCalculate.mul(128 << zoom,a5);

//        double siny = Math.sin(lat * Math.PI / 180);
//        double y = Math.log((1 + siny) / (1 - siny));
//        return (128 << zoom) * (1 - y / (2 * Math.PI));
    }
    /*
 通过纬度得到 y序列
  */
    public static double getYiile(double lat,int zoom){
        double ytile= Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
        if (ytile < 0)
            ytile=0;
        if (ytile >= (1<<zoom))
            ytile=((1<<zoom)-1);
        return ytile;
    }

   static double a = 6378245.0;
     static double ee = 0.00669342162296594323;
      static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
     static  double  transformLat(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x *Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static  double transformLon(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 地球坐标转换为火星坐标
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param wgLat  地球坐标
     * @param wgLon
     *
     * mglat,mglon 火星坐标
     */
    public static double  transform2MarsLat(double wgLat, double wgLon)
    {
        double mgLat;
//        if (outOfChina(wgLat, wgLon))
//        {
//            mgLat  = wgLat;
//
//            return mgLat;
//        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        mgLat = wgLat + dLat;
       return mgLat;

    }
    /**
     * 地球坐标转换为火星坐标
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param wgLat  地球坐标
     * @param wgLon
     *
     * mglat,mglon 火星坐标
     */
    public static double  transform2MarsLong(double wgLat, double wgLon)
    {
        double mgLon;
//        if (outOfChina(wgLat, wgLon))
//        {
//
//            mgLon = wgLon;
//            return mgLon;
//        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

        mgLon = wgLon + dLon;
       return  mgLon;
    }
    public  static  boolean outOfChina(double lat, double lon)
    {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    public static double pixelToLat(double pixelY, int zoom) {

        double y = 2 * Math.PI * (1 - pixelY / (128 << zoom));

        double z = Math.pow(Math.E, y);

        double siny= DecimalCalculate.div(z - 1,z+1);
        double a=DecimalCalculate.mul(Math.asin(siny) , 180 );
       return DecimalCalculate.div(a,Math.PI);

//        double siny = (z - 1) / (z + 1);
//
//        return Math.asin(siny) * 180 / Math.PI;

    }


    public static double latToPixel(double lat, int zoom) {

        double siny = Math.sin(lat * Math.PI / 180);

        double y = Math.log((1 + siny) / (1 - siny));

        return (128 * 2^ zoom) * (1 - y / (2 * Math.PI));

    }
    //像素X到经度
    static public double  pixelToLng(double pixelX, int zoom) {
        double a=DecimalCalculate.mul( pixelX,360);
       double a2= DecimalCalculate.div(a,(256 << zoom));
      return   DecimalCalculate.sub(a2,180);
//        return pixelX * 360 / (256 << zoom) - 180;
    }




}
