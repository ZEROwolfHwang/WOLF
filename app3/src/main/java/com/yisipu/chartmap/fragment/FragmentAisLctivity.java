package com.yisipu.chartmap.fragment;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.AisListDetailActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.SerialPortActivity;
import com.yisipu.chartmap.adapter.AisListAdapter;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class FragmentAisLctivity extends Fragment {
    List<ShipBean> ls = new ArrayList<>();
    ListView lv;
    private Runnable runnable;
    AisListAdapter aadapter;
    SharedPreferences sp;
    ShipBean my_ship = null;
    private Activity mActivity;
    /*
是否正在报警 1为报警 0为不报警
 */
    public static int isRunningBaojing=-1;
    /*
    报警的船
     */
    public static List<ShipBean> BaoJingLs=new ArrayList<>();
    DBManager db;
    private static volatile FragmentAisLctivity fragmentAisLctivity;

    public static FragmentAisLctivity getInstance() {
        if (fragmentAisLctivity == null) {
            synchronized (FragmentAisLctivity.class) {
                if (fragmentAisLctivity == null) {
                    fragmentAisLctivity = new FragmentAisLctivity();
                }
            }
        }
        return fragmentAisLctivity;
    }

    public static boolean isShow() {
        return isShow;
    }

    public static void setIsShow(boolean isShow) {
        FragmentAisLctivity.isShow = isShow;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }

    List<ShipBean> ls2 = new ArrayList<>();
    private boolean toCreate = false;
    private boolean first = true;

    public void initList() {
        if (db == null) {
            db = new DBManager(mActivity);
        }
        ls2 = db.getShipBeans();
        ls.clear();
        ls.addAll(ls2);

        my_ship = db.getMyShip();
        if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1 && my_ship.getLatitude()<=90&& my_ship.getLongitude()<= 180) {
            for (ShipBean ss : ls) {
                if (ss.getLongitude() != -1 && ss.getLatitude() != -1 && ss.getLatitude() <= 90 && ss.getLongitude() <= 180&&!(ss.getLatitude()==0&&ss.getLongitude()==0)) {
                    //距离
                    ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                    ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                } else {
                    //距离
                    ss.setDistance(-1);
                    ss.setFangwei(-1);
                }
            }
        } else if (my_ship == null || my_ship.getLatitude() == -1 || my_ship.getLongitude() == -1 || (my_ship.getLatitude() > 90) || (my_ship.getLongitude() > 180)) {
            for (ShipBean ss : ls) {

                //距离
                ss.setDistance(-1);
                ss.setFangwei(-1);

            }
        }
        for (ShipBean ss : ls) {
            if(ss.getLongitude()==-1||ss.getLatitude()==-1||ss.getLongitude()>180||ss.getLatitude()>90||(ss.getLatitude()==0&&ss.getLongitude()==0)||my_ship == null || my_ship.getLatitude() == -1 || my_ship.getLongitude() == -1 || (my_ship.getLatitude() > 90) || (my_ship.getLongitude() > 180)) {
                //距离
                ss.setDistance(-1);
                ss.setFangwei(-1);
            }
        }

        Collections.sort(ls);
        String gjts= sp.getString("gjts", "关");

        String lpaqfw = sp.getString("lpaqfw", "关");

        if(gjts.equals("开")&&lpaqfw.equals("开")&&isRunningBaojing==1&&BaoJingLs!=null&&BaoJingLs.size()>0){
            for(ShipBean lsb:BaoJingLs){
                for(ShipBean ls2:ls){
                    if(lsb.getMMSI()==ls2.getMMSI()&&lsb.getId()==ls2.getId()){
                        ls2.setIsCollision(1);
                        break;
                    }
                }
            }
        }
        //初始化海图
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ls != null) {
                    if (aadapter != null) {
//                        if(ls.size()>0) {
//                            setRightString("船只:" + (ls.size() - 1));
//                        }


                        aadapter.notifyDataSetChanged();

                    }else{

                    }
                }
            }
        });

    }

    static public boolean isShow = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isShow = true;
            hidShuRuFa(mActivity);
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
            isShow = false;
        }
    }
    View mView=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mView!=null){
            ViewGroup parent= (ViewGroup) mView.getParent();
            if(parent!=null){
                parent.removeView(mView);
            }
            return mView;
        }
//        View mView = null;
//        if (mView == null && inflater != null) {
            mView = inflater.inflate(R.layout.activity_ais_list, null);
//			findView(mView);

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
//        gpsInit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager db = new DBManager(mActivity);
                Date dt = new Date();
                Long time = dt.getTime();
                db.deleteTimeShipBean(time);
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DBManager db = new DBManager(AisListActivity.this);
//                ls = db.getShipBeans();
//                my_ship = db.getMyShip();
//
//                if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1) {
//                    for (ShipBean ss : ls) {
//
//                        //距离
//                        ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//                        ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//
//                    }
//                }
//
//                Collections.sort(ls);
//                //初始化海图
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(ls!=null){
//                            if(aadapter!=null){
//                                if(ls.size()>0) {
//                                    setRightString("船只:" + (ls.size() - 1));
//
//                                }
//
//                                aadapter=new AisListAdapter(AisListActivity.this,ls);
//                                lv.setAdapter(aadapter);
//aadapter.notifyDataSetChanged();
//
//                            }else{
//aadapter=new AisListAdapter(AisListActivity.this,ls);
//                                lv.setAdapter(aadapter);
//                                aadapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//            }
//        }).start();


//        DBManager db = new DBManager(AisListActivity.this);
//                ls = db.getShipBeans();
//                my_ship = db.getMyShip();
//
//                if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1) {
//                    for (ShipBean ss : ls) {
//
//                        //距离
//                        ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//                        ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//
//                    }
//                }
////
//                Collections.sort(ls);

        lv = (ListView) mView.findViewById(R.id.lv_ship);
        if (ls.size() > 0) {
            aadapter = new AisListAdapter(mActivity, ls);

            lv.setAdapter(aadapter);
        }
//        if(ls.size()>0) {
//            mActivitysetRightString("船只:" + (ls.size() - 1));
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager db = new DBManager(mActivity);

                ls2 = db.getShipBeans();
                ls.clear();
                ls.addAll(ls2);

                my_ship = db.getMyShip();

                if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1 &&my_ship.getLatitude()<=90&&(my_ship.getLongitude() <=180)) {
                    for (ShipBean ss : ls) {
                        if (ss.getLongitude() != -1 && ss.getLatitude() != -1 && ss.getLatitude() <=90 && ss.getLongitude() <=180&&!(ss.getLatitude()==0&&ss.getLongitude()==0)) {
                            //距离
                            ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                            ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                        } else {
                            //距离
                            ss.setDistance(-1);
                            ss.setFangwei(-1);
                        }
                    }
                } else if (my_ship == null || my_ship.getLatitude() == -1 || my_ship.getLongitude() == -1 || (my_ship.getLatitude() > 90) || (my_ship.getLongitude() > 180)) {
                    for (ShipBean ss : ls) {

                        //距离
                        ss.setDistance(-1);
                        ss.setFangwei(-1);

                    }
                }
                for (ShipBean ss : ls) {
                    if(ss.getLongitude()==-1||ss.getLatitude()==-1||ss.getLongitude()>180||ss.getLatitude()>90||(ss.getLatitude()==0&&ss.getLongitude()==0)||my_ship == null || my_ship.getLatitude() == -1 || my_ship.getLongitude() == -1 || (my_ship.getLatitude() > 90) || (my_ship.getLongitude() > 180)) {
                       //距离
                        ss.setDistance(-1);
                    ss.setFangwei(-1);
                }

                }
                Collections.sort(ls);
                if(isRunningBaojing==1&&BaoJingLs!=null&&BaoJingLs.size()>0){
                    for(ShipBean lsb:BaoJingLs){
                        for(ShipBean ls2:ls){
                            if(lsb.getMMSI()==ls2.getMMSI()){
                                ls2.setIsCollision(1);
                                break;
                            }
                        }
                    }
                }
                //初始化海图
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ls != null) {
                            if (aadapter != null) {
//                                if(ls.size()>0) {
//                                    setRightString("船只:" + (ls.size() - 1));
//
//                                }

//                                aadapter = new AisListAdapter(mActivity, ls);
//                                lv.setAdapter(aadapter);
                                aadapter.notifyDataSetChanged();

                            } else {
                                aadapter = new AisListAdapter(mActivity, ls);
                                lv.setAdapter(aadapter);
                                aadapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }).start();
        startPush();
//        }
        //发送查询mmsi命令
        String cmd4 = "$DUAIQ,010*55\r\n";
//        Logger.i("zzz:"+cmd4);
        ((SerialPortActivity) mActivity).UART1Tx(cmd4);
//        if(handler==null){
//            handler=new Handler();
//        }
//        runnable=new Runnable() {
//            @Override
//            public void run() {
//
//                initList();
//                // TODO Auto-generated method stub
//                //要做的事情
////                String cmd="!AIVDM,1,1,,B,8>qc9wh0@E=D85A5Dm@,2*49\r\n";
//////        Logger.i("zzz:"+cmd4);
////                UART1Tx(cmd);
//                Logger.i("aaaaaa1kr21212苛刻5");
//                handler.postDelayed(this, 5000);
//
//            }
//        };
//
//        handler.postDelayed(runnable, 5000);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                initList();
                Collections.sort(ls);
                Intent intent = new Intent(getActivity(), AisListDetailActivity.class);
//                Bundle bun=new Bundle();
//                bun.putSerializable("ShipBean",ls.get(position));
                intent.putExtra("ShipBean", ls.get(position));
                startActivity(intent);
            }
        });

        return mView;
    }

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;
    int index = 1;
    Thread gpsThread;

    public void gpsInit() {
//        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
////        String provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
//        locationListener=new LocationListener(){
//
//            @Override
//            public void onLocationChanged(final Location loc) {
//                index++;
//                // TODO Auto-generated method stub
//                //定位資料更新時會回呼
//                Logger.d("GPS-NMEA", loc.getLatitude() + "," + loc.getLongitude());
//                Logger.d("GPS-NMEA", loc.getLatitude() + "," + loc.getLongitude());
//
//                         Boolean istrue = false;
//                         for (ShipBean sb : ls) {
//                             if (sb.getMyShip() == true) {
//                                 sb.setLongitude(loc.getLongitude());
//                                 sb.setLatitude(loc.getLatitude());
//                                 istrue = true;
//                             }
//                         }
////                   if(istrue){
//                         for (ShipBean ss : ls) {
//
//                             //距离
//                             ss.setDistance(LocationUtils.gps2m(loc.getLatitude(), loc.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//                             ss.setFangwei(LocationUtils.gps2d(loc.getLatitude(), loc.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//
//                         }
//                         if(index>=1) {
//                             if (ls.size() > 0) {
//                                 if(aadapter!=null) {
//                                     runOnUiThread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             aadapter.notifyDataSetChanged();
//                                         }
//                                     });
//
//                                 }
//                             }
//                             index=1;
//                         }
////                   }
//
//
//                         SharedPreferences.Editor etor = sp.edit();
//                         etor.putFloat("my_latitude", (float) (loc.getLatitude()));
//                         etor.putFloat("my_longitude", (float) (loc.getLongitude()));
//                         etor.commit();
//                     }
//
//
//
//
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
//                        break;
//                }
//
//            }
//        };
//        nmeaListener = new GpsStatus.NmeaListener() {
//            public void onNmeaReceived(long timestamp, String nmea) {
//                Logger.d("GPS-NMEA", nmea);
//                //发送gps消息到gps端口
//                UART0Tx(nmea+'\n');
//            }
//        };

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location loc) {
                index++;
                // TODO Auto-generated method stub
                //定位資料更新時會回呼
                Logger.d("GPS-NMEA1111", loc.getLatitude() + "," + loc.getLongitude());
                Logger.d("GPS-NMEA111", loc.getLatitude() + "," + loc.getLongitude());
                DBManager d = new DBManager(mActivity);
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
//        Logger.i("zzz:"+cmd4);
                    ((SerialPortActivity) mActivity).UART1Tx(z);
                }
//                   if(istrue){
//                    for (ShipBean ss : ls) {
//
//                        //距离
//                        ss.setDistance(LocationUtils.gps2m(loc.getLatitude(), loc.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//                        ss.setFangwei(LocationUtils.gps2d(loc.getLatitude(), loc.getLongitude(), ss.getLatitude(), ss.getLongitude()));
//
//                    }
//                    if(index>=5) {
//                        if (ls.size() > 0) {
//                            if(aadapter!=null) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        aadapter.notifyDataSetChanged();
//                                    }
//                                });
//
//                            }
//                        }
//                        index=1;
//                    }
//                   }


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
                ((SerialPortActivity) mActivity).UART0Tx(nmea + '\n');
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

//    @Override
//    public void onBackPress(View view) {
//        Intent intent = new Intent(getActivity(), MainActivity.class);
////        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    String tem;
    Thread solveThread;


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
                    // TODO Auto-generated method stub
                    //要做的事情
                    Logger.i("aaaaaa1kr21212苛刻4");
                    handler.postDelayed(this, 5000);

                }
            };
            handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.
        }
    }


    /*
    隐藏输入法
     */
    public void hidShuRuFa(Context context) {


    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    boolean isOpen = imm.isActive();
        if(isOpen&& mActivity.getCurrentFocus()!=null)

        {
        if(mActivity!=null) {
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {

//            startPush();
            if(mActivity!=null) {
                hidShuRuFa(mActivity);
            }
            stopPush();
        } else {
//            stopPush();
            if(mActivity!=null) {
                hidShuRuFa(mActivity);
            }
            startPush();
        }
    }

    public  Handler handler = new Handler();

}
