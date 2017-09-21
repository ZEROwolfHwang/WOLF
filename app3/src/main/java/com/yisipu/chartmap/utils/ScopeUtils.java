package com.yisipu.chartmap.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class ScopeUtils {
    /**
     *电子围栏
     * @param px 纬度
     * @param py 经度
     * @param xD 多边形所有纬度
     * @param yD 多边形所有经度
     * @return
     */
    public static boolean containsPoint(double px, double py, ArrayList<Double> xD, ArrayList<Double> yD) {
        int verticesCount = xD.size();
        int nCross = 0;
        if(xD.size()>0&&yD.size()>0) {
            for (int i = 0; i < verticesCount; ++i) {
                double ix = xD.get(i);
                double iy = yD.get(i);
                double i1x = xD.get((i + 1) % verticesCount);
                double i1y = yD.get((i + 1) % verticesCount);

                // 求解 y=p.y 与 p1 p2 的交点
                if (iy == i1y) {   // p1p2 与 y=p0.y平行
                    continue;
                }
                if (py < Math.min(iy, i1y)) { // 交点在p1p2延长线上
                    continue;
                }
                if (py >= Math.max(iy, i1y)) { // 交点在p1p2延长线上
                    continue;
                }
                // 求交点的 X 坐标
                float x = (float) ((py - iy) * (i1x - ix)
                        / (i1y - iy) + ix);
                if (x > px) { // 只统计单边交点
                    nCross++;
                }
            }
        }else{
            return  false;
        }
        // 单边交点为偶数，点在多边形之外
        return (nCross % 2 == 1);
    }
    /*
      读取经纬度
     */
     public static double getJW(String str){
         double a=-1;
         if(str==null|| TextUtils.isEmpty(str)){
             return a;
         }
         String z=str.trim();

             String z2=z.substring(0,z.length()-2);
            try {
             a=  Double.parseDouble(z2);
                if(z.contains("E")){
                    a=a;
                }else if(z.contains("W")){
                    a=-a;
                }else if(z.contains("N")){
                     a=a;
                }else if(z.contains("S")){
                     a=-a;
                }
                return a;
            }catch (Exception excep){
                Logger.i("dgsbbbb"+excep.toString());
                return a;
            }


     }

}
