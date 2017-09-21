package com.yisipu.chartmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.adapter.AisListAdapter;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dk.dma.ais.sentence.Vdm;

/**
 * Ais列表Activity
 * Created by Administrator on 2016/7/29.
 */
public class AisListActivity extends SerialPortActivity {
    List<ShipBean> ls = new ArrayList<>();


    ListView lv;
    private Runnable runnable;
    AisListAdapter aadapter;
    SharedPreferences sp;
    ShipBean my_ship = null;

    DBManager db;
    List<ShipBean> ls2 = new ArrayList<>();
    private boolean toCreate = false;
    private boolean first = true;

    public void initList() {
        if (db == null) {
            db = new DBManager(this);
        }
        ls2 = db.getShipBeans();
        ls.clear();
        ls.addAll(ls2);

        my_ship = db.getMyShip();
        if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1) {
            for (ShipBean ss : ls) {

                //距离
                ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));

            }
        }

        Collections.sort(ls);
        //初始化海图
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ls != null) {
                    if (aadapter != null) {
                        if (ls.size() > 0) {
                            setRightString("船只:" + (ls.size() - 1));
                        }
                        aadapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_ais_list);
        initTitle();
        sp = getSharedPreferences("sp", MODE_PRIVATE);

        DBManager db = new DBManager(this);
        Date dt = new Date();
        Long time = dt.getTime();
        db.deleteTimeShipBean(time);

        lv = (ListView) findViewById(R.id.lv_ship);
        if (ls.size() > 0) {
            aadapter = new AisListAdapter(this, ls);

            lv.setAdapter(aadapter);
        }
        if (ls.size() > 0) {
            setRightString("船只:" + (ls.size() - 1));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager db = new DBManager(AisListActivity.this);

                ls2 = db.getShipBeans();
                ls.clear();
                ls.addAll(ls2);

                my_ship = db.getMyShip();

                if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1) {
                    for (ShipBean ss : ls) {

                        //距离
                        ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                        ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));

                    }
                }

                Collections.sort(ls);
                //初始化海图
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ls != null) {
                            if (aadapter != null) {
                                if (ls.size() > 0) {
                                    setRightString("船只:" + (ls.size() - 1));

                                }

                                aadapter = new AisListAdapter(AisListActivity.this, ls);
                                lv.setAdapter(aadapter);
                                aadapter.notifyDataSetChanged();

                            } else {
                                aadapter = new AisListAdapter(AisListActivity.this, ls);
                                lv.setAdapter(aadapter);
                                aadapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }).start();
        startPush();

        String cmd4 = "$DUAIQ,010*55\r\n";

        UART1Tx(cmd4);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Collections.sort(ls);
                Intent intent = new Intent(AisListActivity.this, AisListDetailActivity.class);
                intent.putExtra("ShipBean", ls.get(position));
                startActivity(intent);
            }
        });

    }


    @Override
    void receiver(String str) {

    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {

    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }


    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;
    int index = 1;
    Thread gpsThread;

    public void gpsInit() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location loc) {
                index++;
                // TODO Auto-generated method stub
                //定位資料更新時會回呼
                Logger.d("GPS-NMEA1111", loc.getLatitude() + "," + loc.getLongitude());
                Logger.d("GPS-NMEA111", loc.getLatitude() + "," + loc.getLongitude());
                DBManager d = new DBManager(AisListActivity.this);
                Boolean istrue = false;
                ShipBean sb2;
                for (ShipBean sb : ls) {
                    if (sb.getMyShip() == true) {
                        sb2 = sb;
                        sb2.setLongitude(loc.getLongitude());
                        sb2.setLatitude(loc.getLatitude());
                        istrue = true;
                        d.addShipBean(sb2);
                    }
                }
                if (!istrue) {
                    String z = "$DUAIQ,010*55\r\n";
                    UART1Tx(z);
                }


                SharedPreferences.Editor etor = sp.edit();
                etor.putFloat("my_latitude", (float) (loc.getLatitude()));
                etor.putFloat("my_longitude", (float) (loc.getLongitude()));
                etor.commit();
            }


            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                //定位提供者如果關閉時會回呼，並將關閉的提供者傳至provider字串中
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                //定位提供者如果開啟時會回呼，並將開啟的提供者傳至provider字串中
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Logger.d("GPS-NMEA", provider + "");
                //GPS狀態提供，這只有提供者為gps時才會動作
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        Logger.d("GPS-NMEA", "OUT_OF_SERVICE");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Logger.d("GPS-NMEA", " TEMPORARILY_UNAVAILABLE");
                        break;
                    case LocationProvider.AVAILABLE:
                        Logger.d("GPS-NMEA", "" + provider + "");
                        break;
                }

            }
        };
        nmeaListener = new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String nmea) {
                Logger.d("GPS-NMEA", nmea);
                //发送gps消息到gps端口
                UART0Tx(nmea + '\n');
            }
        };
        gpsThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Looper.prepare();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener, Looper.myLooper());
                    locationManager.addNmeaListener(nmeaListener);
                    Looper.loop();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


        });
        gpsThread.start();


    }

    @Override
    public void onBackPress(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initTitle() {
//        hideTitleRight();
        setTitleText("AIS列表");
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

    String tem;
    Thread solveThread;

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {

        solveThread = new Thread(new Runnable() {
            @Override
            public void run() {


//                Logger.i("asfsff" + new String(buffer, 0, size));
                Logger.i("asfsff" + new String(buffer, 0, size));
//                    uartTextView.append(new String(buffer, 0, size));
                String tem5;
                tem5 = new String(buffer, 0, size);
                tem = tem5.trim();
                Vdm vdm = new Vdm();

                if (tem.contains("$PAIS,010,,,")) {
                    //查询本船mmsi

                    Logger.i("aislist" + tem);

                    String number = tem.trim().substring(12, 21);
                    Logger.i("aislist" + number);

                    int a = Integer.valueOf(number);

                    if (a != 0) {
                        ShipBean sb = new ShipBean();
                        sb.setMMSI(a);
                        double a1 = sp.getFloat("my_latitude", -1);
                        double b1 = sp.getFloat("my_longitude", -1);
                        if (a1 == -1 || b1 == -1 || a1 == 91 || b1 == 181) {

                            sb.setMyShip(true);
                        } else {
                            sb.setLatitude(a1);
                            sb.setLongitude(b1);
                            sb.setMyShip(true);
                        }
                        DBManager d = new DBManager(AisListActivity.this);
                        d.addShipBean(sb);
                    }
                }
            }
        });
        solveThread.start();

    }

    @Override
    protected void onResume() {
        startPush();
        super.onResume();
    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

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
                    initList();
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
    protected void onDestroy() {
        super.onDestroy();
    }
}

