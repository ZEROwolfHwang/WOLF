package com.yisipu.chartmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.yisipu.chartmap.adapter.SlideMenuAdapter;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.JuliBean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CollectDialog;
import com.yisipu.chartmap.dialog.CustomDialog;
import com.yisipu.chartmap.dialog.SelfDialog;
import com.yisipu.chartmap.provider.BitmapProviderAssets3;
import com.yisipu.chartmap.servicer.MyDataServicer;
import com.yisipu.chartmap.utils.ConvertUtils;
import com.yisipu.chartmap.utils.DecimalCalculate;
import com.yisipu.chartmap.utils.GetLongLati;
import com.yisipu.chartmap.utils.LocationUtils;
import com.yisipu.chartmap.utils.ServiceIsUser;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//测量Activity
public class CjActivity extends SerialPortActivity {
    private Animation myAnimation_Scale;
    /*
    减级别按钮
     */
    public void jianLevel(View view) {
        TileView tileView = getTileView();
        float a = tileView.getScale();
        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();

        if (b >0.5) {
            tileView.getMarkerLayout().removeAllViews();
            tileView.getCompositePathView().clear();
            myAnimation_Scale =new ScaleAnimation(0.4f, 0.1f,0.4f, 0.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            tileView.setAnimation(myAnimation_Scale);
            tileView.setAnimationDuration(800);
            tileView.smoothScaleFromCenter(b / 2);
            double z = Math.log(Constant.TimesNeed*b / 2) / Math.log(2);
            double s = Constant.gongliMaxBl / Math.pow(2, z);

            double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
            double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
            double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1);
            double s2 = Math.abs(x2 - point3[0]);
            Logger.i("sdgskk" + s2);
            android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                    .getLayoutParams();
            lp.width = (int) (s2 / 2);
            cj_lla_blc.setLayoutParams(lp);
            DecimalFormat df3 = new DecimalFormat("##0.00");
            tv_gongli.setText("" + df3.format(s) + "海里");
        }
    }

    /*
加级别按钮
 */
    public void jiaLevel(View view) {

        TileView tileView = getTileView();
        float a = tileView.getScale();
        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
        if (zls != null && zls.size() > 0) {
            if (b <= 8.0000f * (Math.pow(2, (zls.get(zls.size() - 1)) - 9 - 1))) {
                tileView.getMarkerLayout().removeAllViews();
                tileView.getCompositePathView().clear();
                myAnimation_Scale =new ScaleAnimation(0.6f, 0.8f, 0.6f, 0.8f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tileView.setAnimation(myAnimation_Scale);
                tileView.setAnimationDuration(800);
                tileView.smoothScaleFromCenter(b * 2);
                double z = Math.log(Constant.TimesNeed*b * 2) / Math.log(2);
                double s = Constant.gongliMaxBl / Math.pow(2, z);

                double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1);
                double s2 = Math.abs(x2 - point3[0]);
                Logger.i("sdgskk" + s2);
                android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                        .getLayoutParams();
                lp.width = (int) (s2 / 2);
                cj_lla_blc.setLayoutParams(lp);
                DecimalFormat df3 = new DecimalFormat("##0.00");
                tv_gongli.setText("" + df3.format(s) + "海里");
            }
        } else {
            if (b <= 4) {
                tileView.getMarkerLayout().removeAllViews();
                tileView.getCompositePathView().clear();
                myAnimation_Scale =new ScaleAnimation(0.6f, 0.8f, 0.6f, 0.8f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tileView.setAnimation(myAnimation_Scale);
                tileView.setAnimationDuration(800);
                tileView.smoothScaleFromCenter(b * 2);
                double z = Math.log(Constant.TimesNeed*b * 2) / Math.log(2);
                double s = Constant.gongliMaxBl / Math.pow(2, z);

                double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1);
                double s2 = Math.abs(x2 - point3[0]);
                Logger.i("sdgskk" + s2);
                android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                        .getLayoutParams();
                lp.width = (int) (s2 / 2);
                cj_lla_blc.setLayoutParams(lp);
                DecimalFormat df3 = new DecimalFormat("##0.00");
                tv_gongli.setText("" + df3.format(s) + "海里");
            }
        }
    }

    private TileView tileView;
    private static final String TAG = CjActivity.class.getSimpleName();
    private final Handler mHandler = new Handler();
    private boolean isUartPass = false;
    private TextView tv_jing_weidu;
    private LocationManager locationManager = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;
    private TextView gps;
    private ConvertUtils convertUtils = new ConvertUtils();
    private DrawerLayout menuLayout;
    double zjl = 0;
    double fangwj;
    double juli;
    private TextView tv1;
    private CharSequence menuTitle = "菜单";
    private CharSequence activityTitle = "主菜单";
    private SlideMenuAdapter slideMenuAdapter;
    private ArrayList<String> menuNameList;
    private LinearLayout linearLayout, ll2, ll3, ll4, ll5, qk, cx;
    //    private ImageView dzwl, ms;
    WindowManager wm;
    int width;
    int height;
    private ArrayList<double[]> cjpoints;
    private ArrayList<double[]> jwpoints;
    private ArrayList<JuliBean> jlpoints;
    int x = 1;
    int x1 = 1;
    SimpleDateFormat formatter;
    /**经纬度弹窗*/
    private View viewLL;

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String str = new String(buffer, 0, size);
                Logger.i("哈哈哈哈" + new String(buffer, 0, size));
                Logger.d("ddsgg", "dsggss_1:" + str);

//
            }
        });
    }

    public void initTitle() {

        hideTitleRight();
//        setRightString("V1.04");
        setTitleText("距离测量");
        showTitle();
//        hideTitle();
    }

    private Runnable runnable;
    SharedPreferences sp;
    Thread gpsThread;
    Bitmap ship_a=null;
    Bitmap ship_b=null;
    Bitmap yuan_3=null;
    //地图的经纬度差
    double mapXLength = 60.0;
    double mapYLength = 28.0;
    //最左边的经度和最上面的维度
    double mapXLeft = 90;
    double mapYTop = 44;
    CustomDialog dialog;
    int dexX = Constant.minX5;
    int dexY = Constant.minY5;
    List<ImageView> imgList = new ArrayList<>();
    List<ShipBean> temp_ls = new ArrayList<>();

    public void initMap() {
        DBManager db = new DBManager(this);
        Bitmap bit = null;

//        sceneMap.clearMark();
//        ls = db.getShipBeans();

        Matrix matrix = new Matrix();
        TileView tileView = getTileView();
        for (ImageView sp2 : imgList) {


            tileView.getMarkerLayout().removeMarker(sp2);
        }
        imgList.clear();
         ShipBean sb=db.getMyShip();
            if (sb!=null&&sb.getLatitude() != -1 && sb.getLongitude() != -1 && !(sb.getLongitude() > 180) && !(sb.getLatitude() > 90)) {
                double[] point = {(sb.getLongitude() - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.WpTimes * (GetLongLati.getY(sb.getLatitude(), 5) - Constant.minY5 * 256)};
                //任何视图将做…
                ImageView marker = new ImageView(this);
                //拯救协调中心和标注定位

                Bitmap location;
                if (sb.getClassType() == 1) {
                    location = ship_a;

                } else {
                    location = ship_b;

                }
                if (sb.getMyShip()) {
                    int newWidth = 40;
                    int newHeight = 40;
                    marker.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                    if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != 0) {
                        matrix.setRotate((float) sb.getReal_sudu(), (float) location.getWidth() / 2, (float) location.getHeight() / 2);
                    }

                    // 设置旋转角度

                    // 重新绘制Bitmap
                    bit = Bitmap.createBitmap(location, 0, 0, location.getWidth(), location.getHeight(), matrix, true);
                    marker.setTag(sb);
                    marker.setImageBitmap(bit);
                    tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);
                    imgList.add(marker);
                    tileView.addMarker(marker,Constant.TimesNeed* point[0],Constant.TimesNeed* point[1], null, -0.5f);
            }
        }
    }

    List<ShipBean> ls = null;

    Bitmap b;

    //singleTask加载模式
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //这一句必须的，否则Intent无法获得最新的数据
    }

    @Override
    protected void onPause() {
        super.onPause();
        tileView.pause();
    }


    public TileView getTileView() {
        return tileView;
    }

    /**
     * This is a convenience method to scrollToAndCenter after layout (which won't happen if called directly in onCreate
     * see https://github.com/moagrius/TileView/wiki/FAQ
     */
    public void frameTo(final double x, final double y) {
        getTileView().post(new Runnable() {
            @Override
            public void run() {
                getTileView().scrollToAndCenter(x, y);
            }
        });
    }

    public static final double NORTH_WEST_LATITUDE = GetLongLati.getlat(Constant.minY5, 0, 5);//西北经度
    public static final double NORTH_WEST_LONGITUDE = GetLongLati.getLong(Constant.minX5, 0, 5);//西北纬度
    public static final double SOUTH_EAST_LATITUDE = GetLongLati.getlat(16, 0, 5);//东南经度
    public static final double SOUTH_EAST_LONGITUDE = GetLongLati.getLong((Constant.maxX5+1), 0, 5);//东南纬度

    private float windowWidth, windowHeight;
    private double CirX = 0;
    private double CirY = 0;
    float xDown, yDown, xUp;
    boolean isLongClickModule = false;
    boolean isLongClicking = false;

    /* 判断是否有长按动作发生
* @param lastX 按下时X坐标
* @param lastY 按下时Y坐标
* @param thisX 移动时X坐标
* @param thisY 移动时Y坐标
* @param lastDownTime 按下时间
* @param thisEventTime 移动时间
* @param longPressTime 判断长按时间的阀值
*/
    private boolean isLongPressed(float lastX, float lastY,
                                  float thisX, float thisY,
                                  long lastDownTime, long thisEventTime,
                                  long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
    }

    private MarkerLayout.MarkerTapListener markerTapListener = new MarkerLayout.MarkerTapListener() {

        @Override
        public void onMarkerTap(View view, int x, int y) {
            Logger.d("xxxxxxxx" + x + "y" + y);
            // get reference to the TileView
            //得到的tileview参考

            TileView tileView = getTileView();

            float fraction = tileView.getDetailLevelManager().getScale();
            Logger.d("getScale222"+ fraction);
            Logger.d("getHeight"+ tileView.getHeight());
            Logger.d("getWidth"+ tileView.getWidth());
            Logger.d("getBaseHeight" + tileView.getBaseHeight());
            Logger.d("getBaseWidth" + tileView.getBaseWidth());
            Logger.d("weidu" + (NORTH_WEST_LATITUDE + (SOUTH_EAST_LATITUDE - NORTH_WEST_LATITUDE) * ((y / (fraction + 0.0)) / (tileView.getBaseHeight() + 0.0))));
            Logger.d("jingdu" + (NORTH_WEST_LONGITUDE + (SOUTH_EAST_LONGITUDE - NORTH_WEST_LONGITUDE) * ((x / (fraction + 0.0)) / (tileView.getBaseWidth() + 0.0))));

            //我们保存了标记的标签中的坐标
            if (view.getTag() != null && view.getTag() instanceof double[]) {
                double[] position = (double[]) view.getTag();
            }
        }
    };
    //显示标记和路径的点列表
    private ArrayList<double[]> points = new ArrayList<>();{
        points.add(new double[]{Constant.TimesNeed*(120.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed*(118.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(23, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed*(116.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.TimesNeed*Constant.WpTimes * (GetLongLati.getY(22, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed*(120.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(23.5, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed*(118.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(23.2, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed*(116.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(22.3, 5) - Constant.minY5 * 256)});
    }

    /*
    用于组缩小放大
     */
    float startScale = -1;

    float endScale = -1;
    /*
    初始化海图
     */
    List<View> ls_view_mark = new ArrayList<>();
    /*
    设置比例尺
     */
    double blc_gongli = Constant.gongliMaxBl;
    /*
    比例尺改变的临时值
     */
    private float scaleChange=-1;
    public void initTileView() {
        final TileView tileView = getTileView();

        //我们不会使用一个采样，所以它的颜色同样的瓷砖
        tileView.setBackgroundColor(0xFFe7e7e7);
        tileView.addDetailLevel(0.5f, "tiles/map2/5/%d_%d_5.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(1f, "tiles/map2/6/%d_%d_6.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(2f, "tiles/map2/7/%d_%d_7.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(4f, "tiles/map2/8/%d_%d_8.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(8.0000f, "tiles/map2/9/%d_%d_9.png", Constant.wapianWidth, Constant.wapianWidth);
        final Paint paint = tileView.getDefaultPathPaint();
        if (zls != null && zls.size() > 0) {
            for (int i = 0; i < zls.size(); i++) {
                Logger.i("djfjksk" + 8.0000f * (Math.pow(2, (zls.get(i) - 9))));
                tileView.addDetailLevel((float) (8.0000f * (Math.pow(2, (zls.get(i) - 9)))), "tiles/map2/9/%d_%d_9.png", Constant.wapianWidth, Constant.wapianWidth);
            }
        }
        tileView.setBitmapProvider(new BitmapProviderAssets3());
        tileView.setSize(Constant.TimesNeed*Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.wapianWidth *(Constant.maxY5-Constant.minY5+1));
        tileView.setGetScaleChangedListener(new TileView.getScaleChanged() {
            @Override
            public void getScaleChanged2(float scale) {
                if ((scale * 4) % 2 == 0 && scale != scaleChange&&scaleChange!=-1&&tileView.getDetailLevelManager().getScale()==scale) {
                    Logger.i("dsgsgfffff" + scale);
                    scaleChange=scale;
                    tileView.getMarkerLayout().removeAllViews();
                    tileView.getCompositePathView().clear();
                    zjl = 0;
                    if (cjpoints != null && cjpoints.size() > 0) {

                        for (int i = 0; i < cjpoints.size(); i++) {
                            if (i == 0) {
                                ImageView marker = new ImageView(CjActivity.this);
                                double[] point = new double[]{cjpoints.get(0)[0], cjpoints.get(0)[1]};
                                int pointIntX = (int) point[0];
                                int pointIntY = (int) point[1];
                                double sx = point[0] - pointIntX;
                                double sy = point[1] - pointIntY;
                                double k = (sx * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                                double k2 = (sy * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                                float kh = (float) (-0.5 + k);
                                float khy = (float) (-0.5 + k2);
                                marker.setTag(point);
                                marker.setImageBitmap(yuan_3);
                                tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);

                                tileView.addMarker(marker, point[0], point[1], kh, khy);
                            } else {

                                ImageView marker = new ImageView(CjActivity.this);
                                double[] point = new double[]{cjpoints.get(i)[0], cjpoints.get(i)[1]};
                                int pointIntX = (int) point[0];
                                int pointIntY = (int) point[1];
                                double sx = point[0] - pointIntX;
                                double sy = point[1] - pointIntY;
                                double k = (sx * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                                double k2 = (sy * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                                float kh = (float) (-0.5 + k);
                                float khy = (float) (-0.5 + k2);
                                marker.setTag(point);
                                marker.setImageBitmap(yuan_3);
                                tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);

                                tileView.addMarker(marker, point[0], point[1], kh, khy);

                                TextView textView = new TextView(CjActivity.this);
                                double[] doub1 = jwpoints.get(i);
                                double[] doub2 = jwpoints.get(i - 1);
                                double jwtext = LocationUtils.gps2m(doub1[1], doub1[0], doub2[1], doub2[0]);
                                double fwj = LocationUtils.gps2d(doub1[1], doub1[0], doub2[1], doub2[0]);
                                DecimalFormat df3 = new DecimalFormat("##0.00");
                                if (fwj < 0) {
                                    textView.setText("" + df3.format(jwtext / 1852) + "海里" + "\n" + df3.format((fwj - 180) * -1) + "°");
                                    fangwj = (fwj - 180) * -1;
                                    juli = jwtext / 1852;
                                } else if (fwj >= 0) {
                                    textView.setText("" + df3.format(jwtext / 1852) + "海里" + "\n" + df3.format(fwj) + "°");
                                    fangwj = fwj;
                                    juli = jwtext / 1852;
                                }
                                zjl = zjl + jwtext;
                                tv1.setText("" + df3.format(zjl / 1852) + "海里");

                                textView.setTextColor(Color.BLACK);

                                double k21 = (sx * tileView.getDetailLevelManager().getScale()) / (120 + 0f);
                                double k22 = (sy * tileView.getDetailLevelManager().getScale()) / (80 + 0f);
                                float khx = (float) (-0.5 + k21);
                                float khyy = (float) (-0.5 + k22);
                                tileView.addMarker(textView, point[0], point[1], khx, khyy);
                            }
                        }

                    }
                    if (cjpoints != null && cjpoints.size() > 0) {
                        tileView.drawPath2(cjpoints.subList(0, cjpoints.size()), paint);
                    }
                }
            }
        });
        //获取编程DP指标
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //让我们开始框架的所有点的中心
        double x = 0;
        double y = 0;
        for (double[] point : points) {
            x = x + point[0];
            y = y + point[1];
        }
        int size = points.size();
        x = x / size;
        y = y / size;

        paint.setShadowLayer(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics),
                0x66000000
        );
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics));
        paint.setPathEffect(
                new CornerPathEffect(
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics)
                )
        );
        windowWidth = getResources().getDisplayMetrics().widthPixels;
        windowHeight = getResources().getDisplayMetrics().heightPixels;
        CirX = windowWidth / 2;
        CirY = windowWidth / 2;
        Canvas canvas = new Canvas();
        paint.setColor(Color.GREEN);
        canvas.drawCircle((float) CirX, (float) CirY, (float) 20 * 15, paint);
        tileView.draw(canvas);
        if (zls != null && zls.size() > 0) {
            //试验高于1
            tileView.setScaleLimits(0, (float) (8.0000f * (Math.pow(2, (zls.get(zls.size() - 1) - 9)))));
        } else {
            //试验高于1
            tileView.setScaleLimits(0, 8f);

        }
        //启动小，允许变焦

        if(ScrollX!=-1&&ScrollY!=-1&&main_scale!=-1) {
            tileView.setScale(main_scale);
            tileView.slideToAndCenter(ScrollX, ScrollY);
        }else {

            frameTo(x, y);
            tileView.setScale(0.5f);
        }
        scaleChange=tileView.getDetailLevelManager().getScale();
        qk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tileView.getMarkerLayout().removeAllViews();
                tileView.getCompositePathView().clear();
                zjl = 0;
                cjpoints.clear();
                jwpoints.clear();
                jlpoints.clear();
                tv1.setText(zjl + "海里");
            }
        });
          /*
      比例尺
       */

        cx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                zjl = 0;
                if (cjpoints.size() != 0) {
                    tileView.getMarkerLayout().removeAllViews();
                    tileView.getCompositePathView().clear();
                    cjpoints.remove(cjpoints.size() - 1);
                    jwpoints.remove(jwpoints.size() - 1);
                    if (jlpoints.size() != 0) {
                        jlpoints.remove(jlpoints.size() - 1);
                    }
                    if (cjpoints.size() == 1) {
                        tv1.setText("0海里");
                    }
                    for (int i = 0; i < cjpoints.size(); i++) {
                        Logger.d("cjpoints", "   " + cjpoints.size());
                        if (i == 0) {
                            ImageView marker = new ImageView(CjActivity.this);
                            marker.setTag(cjpoints.get(i));
                            marker.setImageBitmap(yuan_3);

                            double[] point = {cjpoints.get(i)[0], cjpoints.get(i)[1]};
                            int pointIntX = (int) point[0];
                            int pointIntY = (int) point[1];
                            double sx = point[0] - pointIntX;
                            double sy = point[1] - pointIntY;
                            double k = (sx * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                            double k2 = (sy * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                            float kh = (float) (-0.5 + k);
                            float khy = (float) (-0.5 + k2);
                            tileView.addMarker(marker, cjpoints.get(i)[0], cjpoints.get(i)[1], kh, khy);
                        } else {
                            ImageView marker = new ImageView(CjActivity.this);
                            marker.setTag(cjpoints.get(i));
                            marker.setImageBitmap(yuan_3);
                            tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);
                            double[] point = {cjpoints.get(i)[0], cjpoints.get(i)[1]};
                            int pointIntX = (int) point[0];
                            int pointIntY = (int) point[1];
                            double sx = point[0] - pointIntX;
                            double sy = point[1] - pointIntY;
                            double k11 = (sx * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                            double k12 = (sy * tileView.getDetailLevelManager().getScale()) / (40 + 0f);
                            float khx = (float) (-0.5 + k11);
                            float khyy = (float) (-0.5 + k12);
                            tileView.addMarker(marker, cjpoints.get(i)[0], cjpoints.get(i)[1], khx, khyy);
                            TextView textView = new TextView(CjActivity.this);
                            if (i > 0) {
                                double[] doub1 = cjpoints.get(i - 1);
                                double[] doub2 = cjpoints.get(i);
                                double jwtext = LocationUtils.gps2m(doub1[1], doub1[0], doub2[1], doub2[0]);
                                double fwj = LocationUtils.gps2d(doub1[1], doub1[0], doub2[1], doub2[0]);
                                Logger.d("jwtext", i + "   " + jwtext / 1852);
                                Logger.d("doub1", i + "   " + cjpoints.get(i - 1) + "2" + cjpoints.get(i));
                                DecimalFormat df3 = new DecimalFormat("##0.00");
                                JuliBean juliBean2 = new JuliBean();
                                juliBean2 = jlpoints.get(i - 1);
                                if (fwj < 0) {
                                    textView.setText("" + df3.format(juliBean2.getJuli()) + "海里" + "\n" + df3.format(juliBean2.getFangwei()) + "°");
                                } else if (fwj >= 0) {
                                    textView.setText("" + df3.format(juliBean2.getJuli()) + "海里" + "\n" + df3.format(juliBean2.getFangwei()) + "°");
                                }
                                zjl = zjl + juliBean2.getJuli();
                                tv1.setText(df3.format(zjl) + "海里");
                                textView.setTextColor(Color.BLACK);
                                double k = (sx * tileView.getDetailLevelManager().getScale()) / (120 + 0f);
                                double k2 = (sy * tileView.getDetailLevelManager().getScale()) / (80 + 0f);
                                float kh = (float) (-0.5 + k);
                                float khy = (float) (-0.5 + k2);
                                tileView.addMarker(textView, cjpoints.get(i)[0], cjpoints.get(i)[1], kh, khy);
                            }
                        }
                    }
                    if (cjpoints.size() > 1) {
                        tileView.drawPath2(cjpoints.subList(0, cjpoints.size()), paint);

                    }
                }
            }
        });
        // with padding, we might be fast enough to create the illusion of a seamless image
        //用填充，我们可能是足够快，以创造一个无缝的图像的错觉
        tileView.setViewportPadding(Constant.wapianWidth);

        // we're running from assets, should be fairly fast decodes, go ahead and render asap
        //我们从资产，应该是相当快速的解码，去渲染ASAP
        tileView.setShouldRenderWhilePanning(true);
        cjpoints = new ArrayList<>();
        jwpoints = new ArrayList<>();
        jlpoints = new ArrayList<>();
        tileView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TileView tileView = getTileView();
                //点击连线
                float scale2 = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
                float fraction = tileView.getDetailLevelManager().getScale();
                int zoom = 5;
                if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 0.5) {
                    zoom = 5;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 1.0) {
                    zoom = 6;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 2.0) {
                    zoom = 7;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 4.0) {
                    zoom = 8;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 8.0) {
                    zoom = 9;
                }
                if (zls != null && zls.size() > 0) {
                    for (int i = 0; i < zls.size(); i++) {
                        if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == (float) (8.0000f * (Math.pow(2, (zls.get(i) - 9))))) {
                            zoom = zls.get(i);
                        }
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xDown = event.getX();
                    yDown = event.getY();
                    Log.v("OnTouchListener", "Down");

                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理
                    //获取松开时的x坐标
                    if (isLongClickModule) {

                        isLongClickModule = false;
                        isLongClicking = false;
                    }
                    xUp = event.getX();

                    Log.v("OnTouchListener", "Up");
                    //按下和松开绝对值差当大于20时滑动，否则不显示
                    if ((xUp - xDown) > 20) {
                        //添加要处理的内容
                    } else if ((xUp - xDown) < -20) {
                        //添加要处理的内容
                    } else if (10 > (xDown - xUp)) {
                        tileView.getCompositePathView().clear();
                        int viewWidth = v.getWidth();
                        //经度lon 纬度lat
                        double jd = (GetLongLati.pixelToLng((((v.getScrollX() + event.getX()) / Constant.WpTimes) / (fraction / scale2) + Constant.minX5 * (256) * Constant.TimesNeed*scale2), zoom));
                        double wd = (GetLongLati.pixelToLat((((v.getScrollY() + event.getY()) / Constant.WpTimes) / (fraction / scale2) + (Constant.minY5 * (256) * Constant.TimesNeed*scale2)), zoom));
                        JuliBean juliBean = new JuliBean();

//                        double[] point = {(jd - GetLongLati.getLong(25, 0, 5)) / ((GetLongLati.getLong(28, 0, 5) - GetLongLati.getLong(25, 0, 5))) * Constant.wapianWidth * 3,Constant.WpTimes*(GetLongLati.getY(wd, 5) - 11 * 256)};
                        double[] jwpoint = {jd, wd};
                        jwpoints.add(jwpoint);

                        ImageView marker = new ImageView(CjActivity.this);
                        double poinhx = DecimalCalculate.add(v.getScrollX(), event.getX());
                        double poinhy = DecimalCalculate.add(v.getScrollY(), event.getY());
                        double poinhxx = DecimalCalculate.div(poinhx, scale2);
                        double poinhyy = DecimalCalculate.div(poinhy, scale2);
                        double[] point = {poinhxx, poinhyy};
                        int pointIntX = (int) poinhxx;
                        int pointIntY = (int) poinhyy;
                        double sx = poinhxx - pointIntX;
                        double sy = poinhyy - pointIntY;
                        double k = (sx * scale2) / (40 + 0f);
                        double k2 = (sy * scale2) / (40 + 0f);
                        float kh = (float) (-0.5 + k);
                        float khy = (float) (-0.5 + k2);
//                        cjpoints.add(new double[]{((jd - GetLongLati.getLong(25, 0, 5)) / ((GetLongLati.getLong(28, 0, 5) - GetLongLati.getLong(25, 0, 5))) *Constant.wapianWidth * 3),(Constant.WpTimes*(GetLongLati.getY(wd, 5) - 11 * 256))});
                        cjpoints.add(new double[]{(poinhxx), (poinhyy)});

                        marker.setTag(point);
                        marker.setImageBitmap(yuan_3);
//                        marker.setImageResource(Math.random() < 0.75 ? R.drawable.yuand3 : R.drawable.yuand3);
                        tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);

                        tileView.addMarker(marker, point[0], point[1], kh, khy);

//                       Logger.i("dsg"+view.getLeft()+"df"+view.getTop()+"sdg"+view.getBottom()+"sgs"+view.getRight()+"DGS"+v.getScrollX() +"sd"+ event.getX());
//                        tileView.addMarker(marker, point[0], point[1], null, -0.5f);
                        if (cjpoints.size() > 1) {
                            juliBean.setJingdu(jd);
                            juliBean.setWeidu(wd);


//                            tileView.drawPath(cjpoints.subList(0, cjpoints.size()), paint);
                            tileView.drawPath2(cjpoints.subList(0, cjpoints.size()), paint);
                            TextView textView = new TextView(CjActivity.this);
                            double[] doub1 = jwpoints.get(cjpoints.size() - 2);
                            double[] doub2 = jwpoints.get(cjpoints.size() - 1);
                            double jwtext = LocationUtils.gps2m(doub1[1], doub1[0], doub2[1], doub2[0]);
//                            Toast.makeText(CjActivity.this, "纬度：" + doub1[1] + "经度：" + doub1[0], Toast.LENGTH_SHORT).show();
                            double fwj = LocationUtils.gps2d(doub1[1], doub1[0], doub2[1], doub2[0]);
                            DecimalFormat df3 = new DecimalFormat("##0.00");
                            if (fwj < 0) {
                                textView.setText("" + df3.format(jwtext / 1852) + "海里" + "\n" + df3.format((fwj - 180) * -1) + "°");
                                fangwj = (fwj - 180) * -1;
                                juli = jwtext / 1852;
                            } else if (fwj >= 0) {
                                textView.setText("" + df3.format(jwtext / 1852) + "海里" + "\n" + df3.format(fwj) + "°");
                                fangwj = fwj;
                                juli = jwtext / 1852;
                            }
                            zjl = zjl + jwtext;
                            tv1.setText("" + df3.format(zjl / 1852)+"海里");
                            juliBean.setJuli(juli);
                            juliBean.setFangwei(fangwj);
                            jlpoints.add(juliBean);
                            textView.setTextColor(Color.BLACK);

                            double k21 = (sx * scale2) / (120 + 0f);
                            double k22 = (sy * scale2) / (80 + 0f);
                            float khx = (float) (-0.5 + k21);
                            float khyy = (float) (-0.5 + k22);
//                            tileView.addMarker(textView, point[0], point[1], null, null);
                            tileView.addMarker(textView, point[0], point[1], khx, khyy);
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //当滑动时背景为选中状态 //检测是否长按,在非长按时检测
                    if (!isLongClickModule) {
                        isLongClickModule = isLongPressed(xDown, yDown, event.getX(),
                                event.getY(), event.getDownTime(), event.getEventTime(), 300);
                        if(viewLL!= null){
                            tileView.getMarkerLayout().removeMarker(viewLL);
                        }
                    }
                    if (isLongClickModule && !isLongClicking) {
                        isLongClicking = true;
                    }
                }
                return false;

            }


        });
    }
    /*
    流方式读取图片
     */
    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opts);
    }
    /*
    流方式读取圆图片
     */
    public Bitmap readYBitmap(Context context, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
       opts.outWidth=40;
        opts.outHeight=40;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opts);
    }
    LinearLayout cj_lla_blc;
    TextView tv_gongli;
    TextView tv_line_cl;
    List<Integer> zls = new ArrayList<>();
    int ScrollX=-1;
    int ScrollY=-1;
    float main_scale=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_cj);
//        sceneMap = (MyMap) findViewById(R.id.sceneMap);
        tileView = (TileView) findViewById(R.id.cj_view);
        tileView.setSaveEnabled(true);
        qk = (LinearLayout) findViewById(R.id.cj_qk);
        cx = (LinearLayout) findViewById(R.id.cj_cx);
        /*
       比例尺
         */
        cj_lla_blc = (LinearLayout) findViewById(R.id.cj_lla_blc);
        tv_gongli = (TextView) findViewById(R.id.tv_gongli);
        tv_line_cl = (TextView) findViewById(R.id.tv_line_cl);
        cx = (LinearLayout) findViewById(R.id.cj_cx);
        Intent intentz = getIntent();
        if (intentz != null) {
            zls = (List<Integer>) intentz.getSerializableExtra("zList");

            ScrollX=intentz.getIntExtra("scrollX",-1);
            ScrollY=intentz.getIntExtra("scrollY",-1);
            main_scale=intentz.getFloatExtra("main_scale",-1);

        }
        ship_a = readBitmap(CjActivity.this, R.drawable.aleichuan);
        ship_b = readBitmap(CjActivity.this, R.drawable.bleichuan);
        yuan_3= readYBitmap(CjActivity.this, R.drawable.yuand3);
        initTileView();
        gps = (TextView) findViewById(R.id.tv_jing_weidu);
//        ms = (ImageView) findViewById(R.id.main_ms);
//        dzwl = (ImageView) findViewById(R.id.main_dzwl);


//        b = BitmapFactory
//                .decodeResource(getResources(), R.drawable.test);
        BitmapFactory.Options options = new BitmapFactory.Options();
//
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        options.inSampleSize = 1;
//        b = BitmapFactory
//                .decodeResource(getResources(), R.drawable.test, options);
//        sceneMap.setBitmap(b);

//        ship_a = BitmapFactory.decodeResource(getResources(),
//                R.drawable.aleichuan, options);
//        ship_b = BitmapFactory.decodeResource(getResources(),
//                R.drawable.bleichuan, options);
        initTitle();
//        new TimeThread().start();
        boolean isUse = ServiceIsUser.isServiceWork(this, "com.yisipu.chartmap.servicer.MyDataServicer");
        if (!isUse) {
            Intent service = new Intent(CjActivity.this, MyDataServicer.class);
            this.startService(service);
        }
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("fuwu", 1);//keyname是储存数据的键值名，同一个对象可以保存多个键值
        editor.commit();//提交保存修改
        tv_jing_weidu = (TextView) findViewById(R.id.tv_jing_weidu);
//        SharedPreferences.Editor editor = sp.edit();//获取编辑对象

        Intent intent = new Intent();
        intent.setAction(Constant.KAIJIQIDONG);
        CjActivity.this.sendBroadcast(intent);
//        tv_jing_weidu.setText("纬度:"+sp.getFloat("my_latitude",0)+"   "+"经度:"+sp.getFloat("my_longitude",0));
//        tv_jing_weidu.setTextColor(0xffeb4f38);


//        DBManager db=new DBManager(this);
//        ShipBean sb3=new ShipBean();
//        sb3.setMMSI(412555551);
//        sb3.setLongitude(117);
//        sb3.setReal_sudu(170);
//        sb3.setLatitude(23.7);
//        sb3.setClassType(1);
//        db.addShipBean(sb3);
//        ShipBean sb4=new ShipBean();
//        sb4.setMMSI(412555557);
//        sb4.setLongitude(117);
//        sb4.setClassType(1);
//        sb4.setReal_sudu(230);
//        sb4.setLatitude(23.8);
//        db.addShipBean(sb4);
//        ShipBean sb5=new ShipBean();
//        sb5.setMMSI(412555558);
//        sb5.setLongitude(117);
//        sb5.setClassType(1);
//        sb5.setLatitude(23.9);
//        sb5.setReal_sudu(170);
//        sb5.setMyShip(true);
//        db.addShipBean(sb5);
//        ls=db.getShipBeans();
//        ShipBean sb2=db.getEditShipBean(412555555);
//        if(sb2!=null){
//            Logger.i("zouo5556" + sb2.getMMSI());
//        }
//
//        if(null!=ls) {
//
//            Logger.i("咋咋5556" + ls.size());
//            for(ShipBean s1:ls){
//                Logger.i("zouo5556999" + s1.getMMSI());
//            }
//        }
//        db.closeDb();
//        db.closeDb();
//        if (handler == null) {
//            handler = new Handler();
//        }
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                //初始化海图
//                initMap();
//                // TODO Auto-generated method stub
//                //要做的事情
////                String cmd="!AIVDM,1,1,,B,8>qc9wh0@E=D85A5Dm@,2*49\r\n";
//////        Logger.i("zzz:"+cmd4);
////                UART1Tx(cmd);
//                Logger.i("aaaaaa1kr21212苛刻5");
//                handler.postDelayed(this, 3000);
//
//            }
//        };
//
//        handler.postDelayed(runnable, 3000);//每两秒执行一次runnable.

//        String name = "app_name";
//        Resources rc=getResources();
//        int id = rc.getIdentifier(name, "string", "com.yisipu.chartmap");
//        if (id == 0) {
//            Logger.e(TAG, "Lookup id for resource '"+name+"' failed");
//            // graceful error handling code here
//        }else{
//            Logger.i("hahah"+getString(id));
//        }
//        Vdm vdm = new Vdm();
//
//        try {
//            vdm.parse("!AIVDM,1,1,,A,9@00;fC81m1Hrm`Wv25CK`@00000,0*1D\n");
//            vdm.getMsgId();
//            AisMessage message = AisMessage.getApplication(vdm);
////            Toast.makeText(this,message.getMsgId()+"",Toast.LENGTH_SHORT).show();
//           Logger.i(message.getMsgId()+"sgdsfg");
//
//
//        }catch (SentenceException e){
//e.printStackTrace();
//        }catch (SixbitException e){
//            e.printStackTrace();
//        }catch(AisMessageException e)
//        {
//            e.printStackTrace();
//        }
//        mUartTest   = new UatrTest();
//        mUartTest.init_UART0();
//        mUartTest.init_UART1();


//        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
////        String provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
//        locationListener=new LocationListener(){
//
//            @Override
//            public void onLocationChanged(final Location loc) {
//
//                // TODO Auto-generated method stub
//                //定位資料更新時會回呼
//                Logger.d("GPS-NMEA", loc.getLatitude() + "," +  loc.getLongitude());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv_jing_weidu.setText("纬度:"+loc.getLatitude()+"  经度:"+loc.getLongitude());
//                        tv_jing_weidu.setTextColor(0xff56abe4);
//                    }
//                });
//
//                SharedPreferences.Editor etor=sp.edit();
//                etor.putFloat("my_latitude",(float)(loc.getLatitude()));
//                etor.putFloat("my_longitude",(float)(loc.getLongitude()));
//                etor.commit();
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                // TODO Auto-generated method stub
//                //定位提供者如果關閉時會回呼，並將關閉的提供者傳至provider字串中
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                // TODO Auto-generated method stub
//                //定位提供者如果開啟時會回呼，並將開啟的提供者傳至provider字串中
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                // TODO Auto-generated method stub
//                Logger.d("GPS-NMEA", provider + "");
//                //GPS狀態提供，這只有提供者為gps時才會動作
//                switch (status) {
//                    case LocationProvider.OUT_OF_SERVICE:
//                        Logger.d("GPS-NMEA","OUT_OF_SERVICE");
//                        break;
//                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                        Logger.d("GPS-NMEA"," TEMPORARILY_UNAVAILABLE");
//                        break;
//                    case LocationProvider.AVAILABLE:
//                        Logger.d("GPS-NMEA","" + provider + "");
//
//                        break;
//                }
//
//            }
//        };
//        nmeaListener = new GpsStatus.NmeaListener() {
//            public void onNmeaReceived(long timestamp, String nmea) {
//                Logger.d("GPS-NMEA", nmea);
//
//                UART0Tx(nmea+'\n');
//            }
//        };
//        gpsThread= new Thread(new Runnable() {
//            @Override
//            public void run () {
//
//                try {
//
//                    Looper.prepare();
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener,Looper.myLooper());
//                    locationManager.addNmeaListener(nmeaListener);
//                    Looper.loop();
//                } catch (SecurityException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        });
//        gpsThread.start();
//        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
//            locationManager.addNmeaListener(nmeaListener);
//        } catch (SecurityException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        uartTextView   = (TextView) findViewById(R.id.textView);
//        DisplayTestResult(uartTextView, 0);
//        setClickListener();

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        width = dm.widthPixels;
        height = dm.heightPixels;
        menuLayout = (DrawerLayout) findViewById(R.id.cj_layout);
//        menuElementsList = (ListView) findViewById(R.id.menu_elements);
        linearLayout = (LinearLayout) findViewById(R.id.cj_ll);

        ll2 = (LinearLayout) findViewById(R.id.cj_lla2);
        ll3 = (LinearLayout) findViewById(R.id.cj_lla3);
        ll4 = (LinearLayout) findViewById(R.id.cj_lla4);
        ll5 = (LinearLayout) findViewById(R.id.cj_lla5);
        // 设置阴影
//        menuLayout.setDrawerShadow(R.drawable.drawer_shadow,
//                GravityCompat.START);
        tv1 = (TextView) findViewById(R.id.cj_jl);
//        tv2 = (TextView) findViewById(R.id.main_wd);
//        tv3 = (TextView) findViewById(R.id.tv3);
//        tv4 = (TextView) findViewById(R.id.tv4);
//        sj = (TextView) findViewById(R.id.main_sj);
//        nl = (TextView) findViewById(R.id.main_nl);
//        dw = (TextView) findViewById(R.id.main_dw);

//        int sfdw = sp.getInt("sfdw", 1);
//        if (sfdw == 1) {
//            dw.setText("未定位");
//            dw.setTextColor(Color.RED);
//        } else if (sfdw == 2) {
//            dw.setText("已定位");
//            dw.setTextColor(Color.GREEN);
//        }
//        menuNameList = new ArrayList<String>();
//        menuNameList.add("AIS列表");
//        menuNameList.add("定位");
//        menuNameList.add("罗盘");
//        menuNameList.add("设置");
//        slideMenuAdapter = new SlideMenuAdapter(this, menuNameList);
//        menuElementsList.setAdapter(slideMenuAdapter);
//        menuElementsList.setOnItemClickListener(new DrawerItemClickListener());
        // 使APPIcon也能响应点击事件
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//        selectItem(0);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(420, 280);//导入上面所述的包
//        layoutParams.gravity = Gravity.RIGHT;
//        linearLayout.setLayoutParams(layoutParams);
//        linearLayout.setBackgroundColor(Color.parseColor("#3fcebcbc"));
        linearLayout.setBackgroundResource(R.drawable.corners_bg);
//        tv1.setTextSize(18);
//        tv2.setTextSize(18);
//        tv3.setTextSize(18);
//        tv4.setTextSize(18);
//        menuToggle = new ActionBarDrawerToggle(this, menuLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close
//        ) {
//            public void onDrawerClosed(View view) {
////                getActionBar().setTitle(activityTitle);
//                invalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
//            }
//
//            public void onDrawerOpened(View drawerView) {
////                getActionBar().setTitle(menuTitle);
//                invalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
//            }
//        };
//        menuLayout.setDrawerListener(menuToggle);
//        linearLayout.setOnTouchListener(new onDoubleClick());

    }



    @Override
    void receiver(String str) {

    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {
//        if (gpsbean != null) {
//            String jingdu = convertUtils.Longitude(gpsbean.getGpsLongitude());
//            String weidu = convertUtils.Latitude(gpsbean.getGpsLatitude());
//            DBManager dbManager = new DBManager(this);
//            if (dbManager != null) {
//                int hs = dbManager.getMyShip().getSog();
//                int hx = dbManager.getMyShip().getCog();
//                if (!(hx >= 3600) && hx != -1) {
//                    tv4.setText("航向: " + hx / (10.0) + "°");
//                }
//                DecimalFormat df = new DecimalFormat("##0.0");
//                if (hs != 1023 && hs != -1) {
//                    tv3.setText("航速: " + df.format(hs / (10.0)) + " Kn");
//                }
//            }
//            tv1.setText("经度：" + jingdu);
//            tv2.setText("纬度：" + weidu);
//        }
////        switch (x1) {
////            case 1:
////                dw.setText("未定位");
////                dw.setTextColor(Color.RED);
////                break;
////            case 2:
////                dw.setText("定位");
//////                        pdoptv.setText("位置精度：" + pdop);
////                dw.setTextColor(Color.GREEN);
////
////                break;
////        }
//        sp = getSharedPreferences("sp", MODE_PRIVATE);
//        int sfdw = sp.getInt("sfdw", 1);
//        if (sfdw == 1) {
//            dw.setText("未定位");
//            dw.setTextColor(Color.RED);
//        } else if (sfdw == 2) {
//            dw.setText("已定位");
//            dw.setTextColor(Color.GREEN);
//        }
    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }


    /*
       停止三秒传一次
        */
    public void stopPush() {
        if (runnable != null) {

            handler.removeCallbacks(runnable);
            runnable = null;
        }

    }

    /*
    开始
     */
    public void startPush() {
        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    initMap();
                    // TODO Auto-generated method stub
                    //要做的事情
//                    String cmd = "!AIVDM,1,1,,B,8>qc9wh0@E=D85A5Dm@,2*49\r\n";
////        Logger.i("zzz:"+cmd4);
//                    UART1Tx(cmd);
                    Logger.i("aaaaaa1kr21212苛刻4");
                    handler.postDelayed(this, 5000);

                }
            };
            handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.
        }
    }

    @Override
    protected void onStop() {
        stopPush();
        super.onStop();
    }

    @Override
    protected void onResume() {
        BitmapFactory.Options options = new BitmapFactory.Options();
//
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        options.inSampleSize = 2;
//        b = BitmapFactory
//                .decodeResource(getResources(), R.drawable.test, options);
//        sceneMap.setBitmap(b);
        if(ship_a==null) {
            ship_a = BitmapFactory.decodeResource(getResources(),
                    R.drawable.aleichuan, options);
        }
        if(ship_b==null) {
            ship_b = BitmapFactory.decodeResource(getResources(),
                    R.drawable.bleichuan, options);
        }
//        initMap();


        super.onResume();
        tileView.resume();
        startPush();
    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }

    @Override
    public void finish() {
//        mUartTest.close_UART0();
//        mUartTest.close_UART1();
//
//        mHandler.removeCallbacksAndMessages(null);
        super.finish();
    }

    private void setClickListener() {
//        uartTextView.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                mUartTest.UART0Tx("$PAIS,GPSOP,1,1,1,1,1,1,1*61\r\n"+
//                              "$PAIS,GPSDATA,,,1*6E\r\n"+
//                        "$DUAIQ,GPSOP*3F\r\n"
//                        );
//                mUartTest.UART1Tx(
//                        "$PAIS,GPSDATA,,,1*6E\r\n"+
//                        "$DUAIQ,GPSOP*3F\r\n"
//                );
//                mHandler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                      /*  byte[] mRx1Bffer = mUartTest.getUart1RxBuffer();
//
//                        isUartPass = (mRx1Bffer[0] == '5') && (mRx1Bffer[1] == '5');
//                        if(isUartPass)
//                            DisplayTestResult(uartTextView, 2);
//                        else
//                            DisplayTestResult(uartTextView, 1);*/
//                    }
//                }, 5);
//            }
//        });

    }

    //    public static void DisplayTestResult(TextView textview, int resultTest) {
//        if (0 == resultTest)
//            textview.setBackgroundResource(R.color.gray);
//        else
//            textview.setBackgroundResource(2 == resultTest
//                    ? R.color.dark_green : R.color.red);
//    }
   /*
   跳转到Ais列表
    */
//    public void goToAisList(View view) {
//        Intent intent = new Intent(MainActivity.this, AisListActivity.class);
//        startActivity(intent);
//    }

    /*
  跳转到Ais设置
   */
//    public void goToAisSetting(View view){
//        Intent intent=new Intent(MainActivity.this,AisSettingActivity.class);
//        startActivity(intent);
//    }
    /*
     跳转到设置
      */
    public void goToSetting(View view) {
        Intent intent = new Intent(CjActivity.this, ViewPagerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
        tileView.destroy();
        tileView = null;
    }

    // 点击返回 回到home界面
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//
//            home.addCategory(Intent.CATEGORY_HOME);
//
//            startActivity(home);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPress(View view) {
////        onBackPressed();
//
//        Intent home = new Intent(Intent.ACTION_MAIN);
////
//        home.addCategory(Intent.CATEGORY_HOME);
//
//        startActivity(home);
//
//
//    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Logger.i("按下了back键   onBackPressed()");
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // 如果菜单已经打开，则关闭
//        boolean drawerOpen = menuLayout.isDrawerOpen(menuElementsList);
////		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
//
//        return super.onPrepareOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // 打开或关闭drawer
//        if (menuToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
////		switch (item.getItemId()) {
//////		case R.id.action_search:
//////			Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();
//////			return true;
////		default:
//        return super.onOptionsItemSelected(item);
////		}
//    }

//    private class DrawerItemClickListener implements
//            ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//                                long id) {
//            selectItem(position);
//        }
//    }

//    private void selectItem(int position) {
//
////        Fragment fragment = new MenuFragment();
////        Bundle args = new Bundle();
////        args.putInt("position", position);
////        fragment.setArguments(args);
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
////        menuElementsList.setItemChecked(position, true);
////        slideMenuAdapter.setSelectedPosition(position);
////        slideMenuAdapter.notifyDataSetChanged();
////        setTitle(menuNameList.get(position));
//
////        setTitle("菜单");
//        menuLayout.closeDrawer(menuElementsList);
//        if (position == 0) {
//            Intent intent = new Intent(CjActivity.this, ViewPagerActivity.class);
//            intent.putExtra("extra", "1");
//            startActivity(intent);
//        }
//        if (position == 1) {
//            Intent intent = new Intent(CjActivity.this, ViewPagerActivity.class);
//            intent.putExtra("extra", "2");
//            startActivity(intent);
//        }
//        if (position == 2) {
//            Intent intent = new Intent(CjActivity.this, ViewPagerActivity.class);
//            intent.putExtra("extra", "3");
//            startActivity(intent);
//        }
//        if (position == 3) {
//            Intent intent = new Intent(CjActivity.this, ViewPagerActivity.class);
//            intent.putExtra("extra", "4");
//            startActivity(intent);
//        }
//    }

//    @Override
//    public void setTitle(CharSequence title) {
//        activityTitle = title;
//        getActionBar().setTitle(activityTitle);
//    }

    /**
     * 点击菜单按钮，打开或关闭menu
     */

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        menuToggle.syncState();
//
//    }

    //双击事件
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        menuToggle.onConfigurationChanged(newConfig);
//    }


//    class onDoubleClick implements View.OnTouchListener {
//        // 计算点击的次数
//        private int count = 0;
//        // 第一次点击的时间 long型
//        private long firstClick = 0;
//        // 最后一次点击的时间
//        private long lastClick = 0;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
//                if (firstClick != 0 && System.currentTimeMillis() - firstClick > 500) {
//                    count = 0;
//                }
//                count++;
//                if (count == 1) {
//                    firstClick = System.currentTimeMillis();
//                } else if (count == 2) {
//                    lastClick = System.currentTimeMillis();
//                    // 两次点击小于500ms 也就是连续点击
//                    if (lastClick - firstClick < 500) {
//                        if (x % 2 == 1) {
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);//导入上面所述的包
//                            linearLayout.setLayoutParams(layoutParams);
//                            layoutParams.gravity = Gravity.CENTER;
//                            linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                            x++;
//                            tv1.setTextSize(37);
//                            tv2.setTextSize(37);
//                            tv3.setTextSize(37);
//                            tv4.setTextSize(37);
//                            dw.setTextSize(37);
//                            nl.setTextSize(37);
//                            sj.setTextSize(37);
//                            dw.setVisibility(View.VISIBLE);
//                            nl.setVisibility(View.VISIBLE);
//                            sj.setVisibility(View.VISIBLE);
//                            ll2.setVisibility(View.GONE);
//                            ll3.setVisibility(View.GONE);
//                            ll4.setVisibility(View.GONE);
//                            ll5.setVisibility(View.GONE);
//                        } else {
//                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(420, 280);//导入上面所述的包
//                            layoutParams.gravity = Gravity.RIGHT;
//                            linearLayout.setLayoutParams(layoutParams);
//                            linearLayout.setBackgroundResource(R.drawable.corners_bg);
////                            linearLayout.setBackgroundColor(Color.parseColor("#3fcebcbc"));
//                            tv1.setTextSize(18);
//                            tv2.setTextSize(18);
//                            tv3.setTextSize(18);
//                            tv4.setTextSize(18);
//                            dw.setVisibility(View.GONE);
//                            nl.setVisibility(View.GONE);
//                            sj.setVisibility(View.GONE);
//                            ll2.setVisibility(View.VISIBLE);
//                            ll3.setVisibility(View.VISIBLE);
//                            ll4.setVisibility(View.VISIBLE);
//                            ll5.setVisibility(View.VISIBLE);
//                            x++;
//                        }
////                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(100,100));
//                        Toast.makeText(CjActivity.this, "双击了" + width + height, Toast.LENGTH_SHORT).show();
//                    }
//                    clear();
//                }
//            }
//
//            return false;
//        }

    // 清空状态

//        private void clear() {
//            count = 0;
//            firstClick = 0;
//            lastClick = 0;
//        }
//    }

    /**
     * 坐标位置监听
     */
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            x1 = 1;
            StringBuffer sb = new StringBuffer();
            if (location != null) {
                x1 = 2;
            }
            int fmt = Location.FORMAT_DEGREES;
            sb.append(Location.convert(location.getLongitude(), fmt));
            sb.append(" ");
            sb.append(Location.convert(location.getLatitude(), fmt));
//            pdop = location.getAccuracy();
            Logger.d("GPS-NMEA", location.getLatitude() + "," + location.getLongitude() + "GPS界面");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {

                case LocationProvider.OUT_OF_SERVICE:
                    Logger.d("GPS-NMEA", "OUT_OF_SERVICE");
//                    juDgeState(0);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Logger.d("GPS-NMEA", " TEMPORARILY_UNAVAILABLE");
//                    juDgeState(1);
                    break;
                case LocationProvider.AVAILABLE:
                    Logger.d("GPS-NMEA", "" + provider + "");
//                    juDgeState(2);

                    break;
            }


        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {


        }

    };

//    class TimeThread extends Thread {
//        @Override
//        public void run() {
//            do {
//                try {
//                    Thread.sleep(1000);
//                    Message msg = new Message();
//                    msg.what = 1;  //消息(一个整型值)
//                    mHandler2.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } while (true);
//        }
//    }

    //在主线程里面处理消息并更新UI界面
//    private Handler mHandler2 = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    long sysTime = System.currentTimeMillis();
//                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
//                    sj.setText("时间：" + sysTimeStr); //更新时间
//                    Calendar calendar = Calendar.getApplication();
//                    Lunar lunar = new Lunar(calendar);
//                    nl.setText("农历：" + lunar.toString());
//                    sp = getSharedPreferences("sp", MODE_PRIVATE);
//                    String dl = sp.getString("kg", "关");
//                    if (dl.equals("开")) {
//                        dzwl.setImageResource(R.drawable.dzwl2);
//                    } else {
//                        dzwl.setImageResource(R.drawable.dzwl1);
//                    }
//                    String mos = sp.getString("mode", "标准模式");
//                    if (mos.equals("标准模式")) {
//                        ms.setImageResource(R.drawable.bzms);
//                    } else {
//                        ms.setImageResource(R.drawable.znms);
//                    }
//                    break;
//                default:
//                    break;
//
//            }
//        }
//    };

    //    private void juDgeState(final int flag) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (flag) {
//                    case 1:
//                        dw.setText("未定位");
//                        dw.setTextColor(Color.RED);
//                        break;
//                    case 2:
//                        dw.setText("定位");
////                        pdoptv.setText("位置精度：" + pdop);
//                        dw.setTextColor(Color.GREEN);
//                        break;
//                }
//            }
//        });
//    }
     /*
    归到本船的点击
     */
    public void cjtoMyShip(View view) {

        DBManager db = new DBManager(this);
        ShipBean sb = db.getMyShip();
        if (null != sb && sb.getLatitude() != -1 && sb.getLongitude() != -1 && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
            double jd = sb.getLongitude();
            double wd = sb.getLatitude();

            frameToWithScale(Constant.TimesNeed*(jd - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1),Constant.TimesNeed* Constant.WpTimes * (GetLongLati.getY(wd, 5) - Constant.minY5 * 256), tileView.getDetailLevelManager().getScale());
        } else {
            Toast.makeText(this, "本船暂未定位", Toast.LENGTH_SHORT).show();
        }
    }

    public void frameToWithScale(final double x, final double y, final float scale) {
        getTileView().post(new Runnable() {
            @Override
            public void run() {
                getTileView().slideToAndCenterWithScale(x, y, scale);
            }
        });

    }


    /*
搜索点击事件
*/
//    SelfDialog dialog2;
    private EditText et_w_d;
    private EditText et_w_f;
    private EditText et_w_m;
    private EditText et_j_d;
    private EditText et_j_f;
    private EditText et_j_m;
    CollectDialog cdDialog;
    double wd_d;
    double jd_d;

    public void cjsearcher(View view) {
      final  SelfDialog dialog2 = new SelfDialog(this);
//        et_w_d=(EditText) dialog2.findViewById(R.id.et_w_d);
//        et_w_f=(EditText) dialog2.findViewById(R.id.et_w_f);
//        et_w_m=(EditText) dialog2.findViewById(R.id.et_w_m);
//        et_j_d=(EditText)dialog2. findViewById(R.id.et_j_d);
//        et_j_f=(EditText) dialog2.findViewById(R.id.et_j_f);
//        et_j_m=(EditText) dialog2.findViewById(R.id.et_j_m);
        dialog2.setNoOnclickListener(null, new SelfDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog2.dismiss();
            }
        });
        dialog2.setYesOnclickListener(null, new SelfDialog.onYesOnclickListener() {

            @Override
            public void onYesClick() {
                String wd = dialog2.getEt_w_d().getText().toString();
                String wf = dialog2.getEt_w_f().getText().toString();
                String wm = dialog2.getEt_w_m().getText().toString();
                String jd = dialog2.getEt_j_d().getText().toString();
                String jf = dialog2.getEt_j_f().getText().toString();
                String jm = dialog2.getEt_j_m().getText().toString();
                if (TextUtils.isEmpty(wd)) {
                    Toast.makeText(CjActivity.this, "纬度不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(jd)) {
                    Toast.makeText(CjActivity.this, "经度不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(wd)) > 90) {
                    Toast.makeText(CjActivity.this, "纬度范围在90到-90,请重输", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(jd)) > 180) {
                    Toast.makeText(CjActivity.this, "经度范围在180到-180,请重输", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(jd)) == 180) {
                    if (!TextUtils.isEmpty(jm) || !TextUtils.isEmpty(jf)) {
                        Toast.makeText(CjActivity.this, "经度范围在180到-180,请重输", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (Math.abs(Integer.valueOf(wd)) == 90) {
                    if (!TextUtils.isEmpty(wm) || !TextUtils.isEmpty(wf)) {
                        Toast.makeText(CjActivity.this, "纬度范围在90到-90,请重输", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                double dwd;
                double djd;
                if (TextUtils.isEmpty(wf)) {
                    dwd = Double.parseDouble(wd);

                } else if (TextUtils.isEmpty(wm)) {
                    dwd = Double.parseDouble(wd) + Double.parseDouble(wf) / (60 + 0.0);
                } else {
                    dwd = Double.parseDouble(wd) + Double.parseDouble(wf) / (60 + 0.0) + Double.parseDouble(wm) / (3600 + 0.0);
                }
                if (TextUtils.isEmpty(jf)) {
                    djd = Double.parseDouble(jd);
                } else if (TextUtils.isEmpty(jm)) {
                    djd = Double.parseDouble(jd) + Double.parseDouble(jf) / (60 + 0.0);
                } else {
                    djd = Double.parseDouble(jd) + Double.parseDouble(jf) / (60 + 0.0) + Double.parseDouble(jm) / (3600 + 0.0);
                }

                frameToWithScale(Constant.TimesNeed*(djd - GetLongLati.getLong( Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong( Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.TimesNeed*Constant.WpTimes * (GetLongLati.getY(dwd, 5) - Constant.minY5 * 256), 0.5f);
                viewLL = getLayoutInflater().inflate(R.layout.home_map_fav_float, null);
//                       float fraction=tileView.getDetailLevelManager().getScale();
                ImageView iv = (ImageView) viewLL.findViewById(R.id.iv_close);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TileView tileView = getTileView();
                        if (viewLL != null) {
                            tileView.getMarkerLayout().removeMarker(viewLL);
                            viewLL = null;
                        }
                    }
                });
                viewLL.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cdDialog = new CollectDialog(CjActivity.this);
                        cdDialog.setNoOnclickListener(null, new CollectDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                cdDialog.dismiss();

                            }
                        });
                        cdDialog.setYesOnclickListener(null, new CollectDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                String name = cdDialog.getEt_name().getText().toString();
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(CjActivity.this, "航点名不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                DBManager db = new DBManager(CjActivity.this);
                                CollectPointBean cp = db.getCollect(name);
                                if (cp != null) {
                                    Toast.makeText(CjActivity.this, "航点名已存在，请重新命名", Toast.LENGTH_SHORT).show();

                                    return;
                                }
                                CollectPointBean a = new CollectPointBean();
                                a.setLongitude(jd_d);
                                a.setLatitude(wd_d);
                                a.setName(name);
                                a.setType(0);
                                db.addCollectPoint(a);
                                cdDialog.dismiss();
                                Toast.makeText(CjActivity.this, "航点收藏成功", Toast.LENGTH_SHORT).show();
                            }
                        });
//                               Toast.makeText(MainActivity.this,"加入收藏",Toast.LENGTH_SHORT).show();

                        cdDialog.show();


                    }
                });
                float scale2 = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
                int zoom = 5;
                if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 0.5) {
                    zoom = 5;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 1) {
                    zoom = 6;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 2.0) {
                    zoom = 7;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 4.0) {
                    zoom = 8;
                } else if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == 8.0) {
                    zoom = 9;
                }
                if (zls != null && zls.size() > 0) {
                    for (int i = 0; i < zls.size(); i++) {
                        if (tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() == (float) (8.0000f * (Math.pow(2, (zls.get(i) - 9))))) {
                            zoom = zls.get(i);
                        }
                    }
                }
                TextView wd2 = (TextView) viewLL.findViewById(R.id.tv_wd);
                TextView jd2 = (TextView) viewLL.findViewById(R.id.tv_jd);
//                       Logger.d("getJingduhah","xxxxxxxxxxx"+(GetLongLati.pixelToLng((x/( fraction/scale2)+25*(256)*scale2),zoom))+"y"+(GetLongLati.pixelToLat((y/( fraction/scale2)+(11*(256)*scale2)),zoom)));
//                double wd_d=(GetLongLati.pixelToLat(((v.getScrollY()+event.getY())/( fraction/scale2)+(11*(256)*scale2)),zoom));
//                double jd_d=(GetLongLati.pixelToLng(((v.getScrollX()+event.getX())/( fraction/scale2)+25*(256)*scale2),zoom));
                wd2.setText("纬度:" + ConvertUtils.Latitude(dwd));
                jd2.setText("经度:" + ConvertUtils.Longitude(djd));

                TileView tile = getTileView();
                MarkerLayout.LayoutParams layout = new MarkerLayout.LayoutParams(40, 100);
                viewLL.setLayoutParams(layout);
                double[] point = {(djd - GetLongLati.getLong( Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5+1), 0, 5) - GetLongLati.getLong( Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5-Constant.minX5+1), Constant.WpTimes * (GetLongLati.getY(dwd, 5) - Constant.minY5 * 256)};
                for (View view3 : ls_view_mark) {
                    tileView.getMarkerLayout().removeMarker(view3);
                }
                ls_view_mark.clear();
                ls_view_mark.add(viewLL);
                tile.addMarker(viewLL,Constant.TimesNeed* point[0], Constant.TimesNeed*point[1], -0.5f, -1f);

//

                dialog2.dismiss();


            }
        });

        dialog2.show();
    }

}
