package com.yisipu.chartmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.adapter.GpsListAdapate;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.surfaceView.SatellitesView;

import java.util.ArrayList;
import java.util.List;

public class GpsActivity extends SerialPortActivity {
    private int minTime = 1000;
    private int minDistance = 0;
    private static final String TAG = "GpsView";
    private LocationManager locationManager;
    private SatellitesView satellitesView;
    private Bitmap bg, spots;//背景和圆点的bitmap图
    private List<GpsSatellite> satelliteList = new ArrayList<>();
    private static List<GpsSatellite> gpsList2 = new ArrayList<>();
    private List<GpsSatellite> gpsList = new ArrayList<>();
    private ListView gpslv;
    private GpsListAdapate gpsListAdapate;
    private Runnable runnable;
    private TextView gpsstate, pdoptv, hdoptv;
    private Location myLocation = null;
    //    private static float pdop = 0;//位置精度
//    private static float hdop = 0;//水平精度
    private String pdop = "";//位置精度
    private String hdop = "";//水平精度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_gps);
        initTitle();
        hdoptv = (TextView) findViewById(R.id.gps_hdop);
        pdoptv = (TextView) findViewById(R.id.gps_pdop);
        gpsstate = (TextView) findViewById(R.id.gps_state);
        bg = BitmapFactory.decodeResource(getResources(),
                R.drawable.compass1);
        spots = BitmapFactory.decodeResource(getResources(),
                R.drawable.satellite_mark);
//        gpslv = (ListView) findViewById(R.id.gps_lv);
        satellitesView = (SatellitesView) findViewById(R.id.satellitesView);
        satellitesView.setBitmap(bg, spots);
        registerListener();
        satellitesView.setSatellites(satelliteList);
        if (gpsList.size() == 0) {
            Toast.makeText(GpsActivity.this, "正在加载", Toast.LENGTH_SHORT).show();
        }
        gpsList.addAll(gpsList2);
//        gpsListAdapate = new GpsListAdapate(gpsList, this);
        if (gpsList != null) {
            gpslv.setAdapter(gpsListAdapate);
            startPush();
        }
//        gpsList.clear();
//        gpsList.addAll(gpsList2);


    }


    @Override
    void receiver(String str) {

    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {

    }

    @Override
    void hdopreceiver(Jdbean jdbean) {
        if (jdbean != null) {
            pdop = jdbean.getPdop();
            hdop = jdbean.getHdop();
            if (!jdbean.equals("1")) {
                if (!hdop.equals("")&&!hdop.equals("500.0")&&!hdop.equals("500")) {
                    hdoptv.setText( hdop);
                }
                if (!pdop.equals("")&&!pdop.equals("500.0")&&!pdop.equals("500")) {
                    pdoptv.setText(pdop);
                }
            } else if (jdbean.equals("1")) {
                pdoptv.setText("0.0");
                hdoptv.setText("0.0");
            }
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

    @Override
    public void onBackPress(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initTitle() {
        hideTitleRight();
        setTitleText("定位");
        showTitle();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        //侦听位置信息(经纬度变化)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime, minDistance, locationListener);
        // 侦听GPS状态，主要是捕获到的各个卫星的状态
        locationManager.addGpsStatusListener(gpsStatusListener);
        //TODO:考虑增加监听传感器中的方位数据，以使罗盘的北能自动指向真实的北向

    }

    /**
     * 移除监听
     */
    private void unregisterListener() {
        if (locationManager != null) {
            locationManager.removeGpsStatusListener(gpsStatusListener);
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * 坐标位置监听
     */
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            int x = 1;
            StringBuffer sb = new StringBuffer();
            if (location != null) {
                x = 2;
            }
            int fmt = Location.FORMAT_DEGREES;
            sb.append(Location.convert(location.getLongitude(), fmt));
            sb.append(" ");
            sb.append(Location.convert(location.getLatitude(), fmt));
//            pdop = location.getAccuracy();
            Logger.d("GPS-NMEA", location.getLatitude() + "," + location.getLongitude() + "GPS界面");
            juDgeState(x);
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

    /**
     * Gps状态监听
     */
    private GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX: {

                    // 第一次定位时间UTC gps可用
                    // Log.v(TAG,"GPS is usable");
                    int i = gpsStatus.getTimeToFirstFix();
                    break;
                }

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {// 周期的报告卫星状态
                    // 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
                    Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
//                    List<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();
                    satelliteList.clear();
                    gpsList2.clear();
                    if (satellites != null)
                        for (GpsSatellite satellite : satellites) {
                            // 包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
                    /*
                     * satellite.getElevation(); //卫星仰角
					 * satellite.getAzimuth();   //卫星方位角
					 * satellite.getSnr();       //信噪比
					 * satellite.getPrn();       //伪随机数，可以认为他就是卫星的编号
					 * satellite.hasAlmanac();   //卫星历书
					 * satellite.hasEphemeris();
					 * satellite.usedInFix();
					 */
                            satelliteList.add(satellite);
                            gpsList2.add(satellite);

                        }

                    break;
                }

                case GpsStatus.GPS_EVENT_STARTED: {

                    break;
                }

                case GpsStatus.GPS_EVENT_STOPPED: {

                    break;
                }

                default:

                    break;
            }
        }
    };

    public void initList() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (gpsList != null) {
                gpsList.clear();
                gpsListAdapate.notifyDataSetInvalidated();
                if (gpsList2.size() > 0) {
                    gpsList.addAll(gpsList2);
                    gpsListAdapate.notifyDataSetChanged();
                }
//                }
            }

        });


    }

    public void startPush() {

        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    satellitesView.setSatellites(satelliteList);
                    initList();
                    handler.postDelayed(this, 2000);

                }
            };
            handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
        satellitesView.setBitmap(bg, spots);
    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }

    @Override
    protected void onDestroy() {
        unregisterListener();
        super.onDestroy();
    }

    private void juDgeState(final int flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case 1:
                        gpsstate.setText("未定位");
                        gpsstate.setTextColor(Color.RED);
                        break;
                    case 2:
                        gpsstate.setText("定位");
//                        pdoptv.setText("位置精度：" + pdop);
                        gpsstate.setTextColor(Color.GREEN);

                        break;
                }
            }
        });
    }
}
