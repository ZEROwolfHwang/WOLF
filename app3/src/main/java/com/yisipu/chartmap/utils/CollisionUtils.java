package com.yisipu.chartmap.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

/**碰撞算法
 * Created by Administrator on 2016/10/29 0029.
 */
public class CollisionUtils {

    public static double getcollisiontime(float x1, float y1, float x2, float y2, double L1, double L2, int v1, int v2) {

        double t1 = (Math.abs((((Math.atan(L1) * x1) - (Math.atan(L2) * x2) + y2 - y1) / (Math.atan(L1) - Math.atan(L2))) - x1)
                / Math.sin(L1)) / v1;

        double t2 = (Math.abs(x2 - (((Math.atan(L1) * x1) - (Math.atan(L2) * x2)) + y2 - y1) / Math.atan(L1) - Math.atan(L2))
                / (-Math.sin(L2))) / v2;
        double time = Math.abs(t1 - t2);

        return time;
    }

    /*
    判断是否会相撞
    v1为本船的 速度，v2为其他船的速度，jiao1为本船的船艏，jiao2为其他船的船艏
    x1 y1为本船的经纬度，x2，y2为其他船的经纬度
     */
    public static boolean getIsCollision(Context context,double v1, double v2, double jiao1, double jiao2, double x1, double y1, double x2, double y2){


        /*
   得到坐标点
    */
    double xv1=v1*Math.sin(jiao1* Math.PI / 180.0);
    double yv1=v1*Math.cos(jiao1* Math.PI / 180.0);
        double xv2=v2*Math.sin(jiao2* Math.PI / 180.0);
        double yv2=v2*Math.cos(jiao2* Math.PI / 180.0);
  /*
        得到差值
         */
  double sYv2v1=yv1-yv2;
  double sXv2v1=xv1-xv2;
       double sy=y2-y1;
        double sx=x2-x1;

        Logger.i("dsgss" + yv1 + "dks" + yv2 + "sl" + xv1 + "dsg" + xv2 + "sdsgh" + sYv2v1 + "z" + sXv2v1 + "h" + sy + "d" + sx);

        if((Math.abs(sXv2v1)<0.01&&Math.abs(sx)<0.01) ){

                if ((sYv2v1 *sy >=0) &&(sXv2v1*sx>=0)) {


                    return true;

                }else {
                    return  false;
                }


        }

        if((Math.abs(sYv2v1)<0.01&&Math.abs(sy)<0.01) ) {

            if (((sYv2v1 *sy) >= 0) &&((sXv2v1*sx)>=0)) {

                return true;

                }else {
                    return  false;
                }

        }

        double kv=sYv2v1/sXv2v1;
        double ks=sy/sx;
        if(Math.abs(kv-ks)<0.01){

              if ((sYv2v1 *sy > 0) &&(sXv2v1*sx>0)) {
                  return  true;
              }else{
                  return  false;
          }
        }else {
            return  false;
        }


    }

}
