package com.yisipu.chartmap.utils;

/**
 * Created by Administrator on 2016/8/30 0030.
 */

import java.text.DecimalFormat;


public class ConvertUtils {
    /*/
    经度格式转换
     */
    public static String Longitude(double lng) {
        String whole = String.valueOf(lng);//转换成String
        String[] wholes = whole.split("\\.");//分隔小数点
        String degree = wholes[0];//取到度数
        int dr2 = Integer.valueOf(degree).intValue();
        int dr=Math.abs(dr2);//取度绝对值
        DecimalFormat df2 = new DecimalFormat("000");//设置整数位两位
        String degree2 = df2.format(dr);
        double num = Double.valueOf(degree.toString());//把整数部分转成double
        double dvalue = lng - num;//小数部分0.****
        double ho2 = dvalue * 60;
        double h=Math.abs(ho2);//取分绝对值
//        String h2 = String.valueOf(h);
//        String[] h2s = h2.split("\\.");
        DecimalFormat df = new DecimalFormat("00.000");//设置为整数两位，小数3位
        String hour = df.format(h);
        String dfm="";
        if (lng > 0) {
            dfm = degree2 + "°" + hour + "′" + " " + "E";
        } else {
            dfm = degree2 + "°" + hour + "′" + " " + "W";
        }
        return dfm;
    }

    /*
    纬度格式转换
    */

    public static String Latitude(double lat) {
        String whole = String.valueOf(lat);//转换成String
        String[] wholes = whole.split("\\.");//分隔小数点
        String degree = wholes[0];//取到度数
        int dr2 = Integer.valueOf(degree).intValue();
        int dr=Math.abs(dr2);//取度绝对 值
        DecimalFormat df2 = new DecimalFormat("00");//设置整数位两位
        String degree2 = df2.format(dr);
        double num = Double.valueOf(degree.toString());//把整数部分转成double
        double dvalue = lat - num;//小数部分0.****
        double ho2 = dvalue * 60;
        double h=Math.abs(ho2);//取分绝对值
//        String h2 = String.valueOf(h);
//        String[] h2s = h2.split("\\.");
        DecimalFormat df = new DecimalFormat("00.000");//设置为整数两位，小数3位
        String hour = df.format(h);
        String dfm="";
        if (lat > 0) {
            dfm = degree2 + "°" + hour + "′" + " " + "N";
        } else {

            dfm = degree2 + "°" + hour + "′" + " " + "S";
        }
        return dfm;
    }
    public static double  DuToFen(double d,double f,double m){
        double r=d+f/60+m/3600;
        return r;
    }
}
