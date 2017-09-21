package com.yisipu.chartmap.constant;

import android.content.Intent;

import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Constant {
    public static final double EARTH_RADIUS = 6378137.0;

    public static List<ShipBean> l2 = new ArrayList<>();

    /*
    广播按键 action
     */
    public static final String Aisaction = "com.yisipu.aisget";
    public static final String SOS_ACTION = "com.zowee.SOS_SHORT_PRESS";
    public static final String SOS_LONG_ACTION = "com.zowee.SOS_LONG_PRESS";
    public static final String AIS_SHORT_PRESS = "com.zowee.AIS_SHORT_PRESS";//AIS键
    public static final String AIS_LONG_PRESS = "com.zowee.AIS_LONG_PRESS";//AIS键长按
    public static final String GPS_SHORT_PRESS = "com.zowee.GPS_SHORT_PRESS";//GPS键
    public static final String GPS_LONG_PRESS = "com.zowee.GPS_LONG_PRESS";//GPS键长按
    public static final String SHORTCUT_1_SHORT_PRESS = "com.zowee.SHORTCUT_1_SHORT_PRESS";//快捷1键
    public static final String SHORTCUT_1_LONG_PRESS = "com.zowee.SHORTCUT_1_LONG_PRESS";//快捷1长按
    public static final String SHORTCUT_2_SHORT_PRESS = "com.zowee.SHORTCUT_2_SHORT_PRESS";//快捷2键
    public static final String SHORTCUT_2_LONG_PRESS = "com.zowee.SHORTCUT_2_LONG_PRESS";//快捷2长按
    public static final String KAIJIQIDONG="com.yisipu.kaijiqidong";//开机启动广播
    public static final String CLOSE_SERVICE="com.yisipu.close_service";//关闭服务广播
   /*
   每张瓦片的px
    */
    public static final int  wapianWidth=512;
       /*
   每张瓦片的 x y倍数
    */
    public static final double  WpTimes=wapianWidth/256;

    /*
    比例尺最大尺寸 80海里
     */
    public static final double  gongliMaxBl=160;
    /*
航点船 图片资源
*/
    public static final int[]  hdImage={
            R.drawable.image_0,
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4,
            R.drawable.image_5

    };


    /*
    5级别地图最小x
     */
    public static final int minX5=23;
    /*
5级别地图最大x
 */
    public static final int maxX5=30;
    /*
5级别地图最大y
*/
    public static final int maxY5=18;

    /*
5级别地图最小y
*/
    public static final int minY5=11;
    /*
 开始级别倍数
  */
    public static final float startDetail=0.5f;
    /*
需增加开始级别的倍数 1f/startDetail=2
*/
    public static final int TimesNeed=2;

}
