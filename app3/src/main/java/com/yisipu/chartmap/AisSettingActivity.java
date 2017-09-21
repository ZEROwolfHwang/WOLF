package com.yisipu.chartmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.TsDialog;
import com.yisipu.chartmap.servicer.MyDataServicer;
import com.yisipu.chartmap.utils.LangUtils;

import java.util.List;

/**
 * Ais设置Activity
 * Created by Administrator on 2016/7/29.
 */
public class AisSettingActivity extends SerialPortActivity {
    private MyDataServicer msgService;
    private SharedPreferences sp;
    private EditText et_mmsi, et_ship_name, et_huhao, et_type, et_A, et_B, et_C, et_D;
    private ImageView ts;
    TsDialog dialog;
    private  static  boolean toGetMMSI=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_ais_setting);
        initView();
//        DBManager dbManager=new DBManager(this);
//        ShipBean shipBean22=new ShipBean();
//        shipBean22.setMyShip(false);
//        shipBean22.setMMSI(311115377);
//        shipBean22.setLatitude(24.48122);
//        shipBean22.setLongitude(118.1903);
//        shipBean22.setReal_sudu(60);
//        shipBean22.setSog(170);
//        dbManager.addShipBean(shipBean22);
        ts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TsDialog(AisSettingActivity.this);
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        initTitle();
//        gpsInit();


       new Thread(new Runnable() {
           @Override
           public void run() {

                    /*
                正在发ais设置指令
                 */
                   int a = sp.getInt("isAisSetting", 1);
                   if (a == 1) {
                       try {
                           Thread.sleep(2000);
                           //读取端口
                           //查询mmsi
                           String cmd4 = "$DUAIQ,010*55\r\n";
                           UART1Tx(cmd4);
                           Thread.sleep(2000);
                           if(TextUtils.isEmpty(et_mmsi.getText().toString().trim())) {
//                               String cmd4 = "$DUAIQ,010*55\r\n";
                               UART1Tx(cmd4);
                           }
                       } catch (Exception ex) {

                       }


                   } else {
                       //读取端口
                       //查询mmsi
                       String cmd4 = "$DUAIQ,010*55\r\n";
                       UART1Tx(cmd4);
                       try {
                           Thread.sleep(2000);
                       }catch (Exception ec){

                       }
                       if(TextUtils.isEmpty(et_mmsi.getText().toString().trim())) {
//                           String cmd4 = "$DUAIQ,010*55\r\n";
                           UART1Tx(cmd4);
                       }
                   }



           }
       }).start();

//        Logger.i("zzz:"+cmd4);
//        sp.getInt("ais_mmsi",0);
//        et_mmsi.setText(""+sp.getInt("ais_mmsi",0));

    }


    ShipBean sb3 = new ShipBean();

    @Override
    void receiver(String str) {
        Logger.i("fffffaaa" + str);

        if (str.contains("$PAIS,010")) {

            String number = str.trim().substring(12, 21);
            Logger.i("hase1" + number);
            final int a = Integer.valueOf(number);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (et_mmsi != null) {
//                        et_mmsi.setText("" + a);
                        if(!String.valueOf(a).equals("999999999")) {

                            et_mmsi.setText(""+a);
                        }else {

                            et_mmsi.setText("000000000");
                        }
                        sb3.setMMSI(a);
                    } else {
                        et_mmsi = (EditText) findViewById(R.id.et_mmsi);
//                        et_mmsi.setText("" + a);
                        if(!String.valueOf(a).equals("999999999")) {

                            et_mmsi.setText(""+a);
                        }else {

                            et_mmsi.setText("000000000");
                        }
                        sb3.setMMSI(a);
                    }
                }
            });

            String cmd2 = "$DUAIQ,SSD*20\r\n";
//        Logger.i("aaa:"+cmd2);
            UART1Tx(cmd2);

        } else if (str.contains("$AISSD")) {
            String s = str.toString().trim();
            Logger.i("hasezzz1" + s);
            String name = s.substring(8, 28);

            String[] arr = s.split(",|\\*");


            et_ship_name.setText(arr[2]);
            et_A.setText(arr[3]);
            et_B.setText(arr[4]);
            et_C.setText(arr[5]);
            et_D.setText(arr[6]);
            sb3.setEnglishName(arr[2]);
            sb3.setDimBow(Integer.valueOf(arr[3]));
            sb3.setDimPort(Integer.valueOf(arr[5]));
            sb3.setDimStarboard(Integer.valueOf(arr[6]));
            sb3.setDimStern(Integer.valueOf(arr[4]));
            sb3.setChineseName(LangUtils.getInstance().parseEnglist(arr[2]));
            tv_chinese.setText(sb3.getChineseName());
//            String huhao=s.substring(7,14);
////                    et_huhao.setText(huhao);
//            String name=s.substring(15,35);
////                    et_ship_name.setText(name);
//            String A=s.substring(36,39);
////                    et_A.setText(A);
//            String B=s.substring(40,43);
////                    et_B.setText(B);
//            String C=s.substring(44,46);
////                    et_C.setText(C);
//            String D=s.substring(47,49);
//                    et_D.setText(D);
            String cmd3 = "$DUAIQ,VSD*25\r\n";
            Logger.i("bbb:" + cmd3);
            UART1Tx(cmd3);

        } else if (str.contains("$AIVSD")) {
            String s = str.toString().trim();
            Logger.i("hase1" + s);
            String type = s.substring(7, 10);
            et_type.setText(type);
            sb3.setType(Integer.valueOf(type));
            sb3.setMyShip(true);

            DBManager db = new DBManager(this);
            db.addShipBean(sb3);
            toGetMMSI=false;
            Toast.makeText(AisSettingActivity.this, "读取数据成功", Toast.LENGTH_SHORT).show();
        } else if (str.contains("$DUAIR,1,013*")) {
            String a2 = "PAIS,010,,," + mmsi;
            String c = getXORCheck(a2.toCharArray());
            Logger.i("dsgdsg:" + c);
            for (int i = 0; i < c.length(); i++) {
                if (Character.isLowerCase(c.charAt(i))) {
                    c = c.replace(c.charAt(i), Character.toUpperCase(c.charAt(i)));
                }
            }

            if (c.length() == 1) {
                c = "0" + c;
            }

            String mmsi_cmd = "$PAIS,010,,," + mmsi + "*" + c + "\r\n";
            Logger.i("dsgdsg:" + mmsi_cmd);
            UART1Tx(mmsi_cmd);
        } else if (str.contains("$DUAIR,1,010*")) {
/*
修改船舶属性
 */
//        String a3="AISSD,A--A,B--B,C,D,E,F,G,H*hh"+mmsi;
            if (huhao.length() < 7) {
                for (int i = 0; i < 7 - huhao.length(); i++) {
                    huhao += " ";
                }
            }
            if (name.length() < 20) {
                for (int i = 0; i < 20 - name.length(); i++) {
                    name += " ";
                }
            }

            if (A.length() < 3) {
                for (int i = 0; i < 3 - A.length(); i++) {
                    A = "0" + A;
                }
            }
            if (B.length() < 3) {
                for (int i = 0; i < 3 - B.length(); i++) {
                    B = "0" + B;
                }
            }
            if (C.length() < 2) {
                for (int i = 0; i < 2 - C.length(); i++) {
                    C = "0" + C;
                }
            }
            if (D.length() < 2) {
                for (int i = 0; i < 2 - D.length(); i++) {
                    D = "0" + D;
                }
            }
//        String a3="AISSD,,"+name+","+A+","+B+","+C+","+D+",1,AI";

            String a3 = "AISSD,," + name + "," + A + "," + B + "," + C + "," + D + ",1,AI";
////        String a3="AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI";
            String c2 = getXORCheck(a3.toCharArray());
            Logger.i("dsgdsg:" + c2);
            for (int i = 0; i < c2.length(); i++) {
                if (Character.isLowerCase(c2.charAt(i))) {
                    c2 = c2.replace(c2.charAt(i), Character.toUpperCase(c2.charAt(i)));
                }
            }

            if (c2.length() == 1) {
                c2 = "0" + c2;
            }
//        String ship_cmd="$AISSD,5677777,56667777888777777777,122,154,23,24,1,AI*4B\r\n";
//        String ship_cmd="$AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI"+"*"+c2+"\r\n";
//        String ship_cmd="$AISSD,,name,122,110,11,30,1,AI*"+c2+"\r\n";
//
            String ship_cmd = "$AISSD,," + name + "," + A + "," + B + "," + C + "," + D + ",1,AI" + "*" + c2 + "\r\n";
            Logger.i("dsgdsg:" + ship_cmd);
            UART1Tx(ship_cmd);

        } else if (str.contains("$DUAIR,1,SSD*")) {
 /*
        配置航行静态数据
        */
            if (type.length() < 3) {
                for (int i = 0; i < 3 - type.length(); i++) {
                    type = "0" + type;
                }
            }
            String a4 = "AIVSD," + type + ",,,,,,,,";

            String c3 = getXORCheck(a4.toCharArray());
            Logger.i("dsgdsg:" + c3);
            for (int i = 0; i < c3.length(); i++) {
                if (Character.isLowerCase(c3.charAt(i))) {
                    c3 = c3.replace(c3.charAt(i), Character.toUpperCase(c3.charAt(i)));
                }
            }

            if (c3.length() == 1) {
                c3 = "0" + c3;
            }

            String type_cmd = "$AIVSD," + type + ",,,,,,,," + "*" + c3 + "\r\n";
            Logger.i("dsgdsg:" + type_cmd);
            UART1Tx(type_cmd);

        } else if (str.contains("$DUAIR,1,VSD*")) {
            Toast.makeText(AisSettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
        } else if (str.contains("$DUAIR,0,013*")) {
            Toast.makeText(AisSettingActivity.this, "设置MMSI失败", Toast.LENGTH_SHORT).show();
        } else if (str.contains("$DUAIR,0,SSD*")) {
            Toast.makeText(AisSettingActivity.this, "设置SSD失败", Toast.LENGTH_SHORT).show();
        } else if (str.contains("$DUAIR,0,VSD*")) {
            Toast.makeText(AisSettingActivity.this, "设置VSD失败", Toast.LENGTH_SHORT).show();
        }
//                    Toast.makeText(AisSettingActivity.this,"dsggg"+str,Toast.LENGTH_SHORT).show();


    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {

    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }

//


    @Override
    protected void onResume() {
//        stopPush();
        super.onResume();
        toGetMMSI=true;
    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }

    private TextView tv_chinese;

    public void initView() {
        ts = (ImageView) findViewById(R.id.ais_se_ts);
        et_mmsi = (EditText) findViewById(R.id.et_mmsi);
        et_mmsi.setKeyListener(null);
        et_ship_name = (EditText) findViewById(R.id.et_ship_name);
        et_ship_name.setKeyListener(null);
        et_huhao = (EditText) findViewById(R.id.et_huhao);
        et_huhao.setKeyListener(null);
        et_type = (EditText) findViewById(R.id.et_type);
        et_type.setKeyListener(null);
        et_A = (EditText) findViewById(R.id.et_A);
        et_A.setKeyListener(null);
        et_B = (EditText) findViewById(R.id.et_B);
        et_B.setKeyListener(null);
        et_C = (EditText) findViewById(R.id.et_C);
        et_C.setKeyListener(null);
        et_D = (EditText) findViewById(R.id.et_D);
        et_D.setKeyListener(null);
        tv_chinese = (TextView) findViewById(R.id.tv_ship_name_chinese);
        tv_chinese.setKeyListener(null);

    }

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;

    public void gpsInit() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location loc) {
                // TODO Auto-generated method stub
                //定位資料更新時會回呼
                Logger.d("GPS-NMEA", loc.getLatitude() + "," + loc.getLongitude());
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

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            locationManager.addNmeaListener(nmeaListener);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    //认证版
    public void initTitle() {
        hideTitleRight();

        setTitleText("AIS参数");
        showTitle();
    }

    //    public void initTitle() {
//        hideTitleRight();
//
//        setTitleText("AIS设置");
//        showTitle();
//    }
    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String str = new String(buffer, 0, size);
                Logger.i("哈哈哈哈" + new String(buffer, 0, size));
                Logger.d("ddsgg", "dsggss_1:" + str);

                if (str.contains("$DUAIR")) {
                    Logger.d("dkkg33", "sdfwdegwwg_1:" + str);

                    if (str.contains("$DUAIR,1,013*")) {
                        String a2 = "PAIS,010,,," + mmsi;
                        String c = getXORCheck(a2.toCharArray());
                        Logger.i("dsgdsg:" + c);
                        for (int i = 0; i < c.length(); i++) {
                            if (Character.isLowerCase(c.charAt(i))) {
                                c = c.replace(c.charAt(i), Character.toUpperCase(c.charAt(i)));
                            }
                        }

                        if (c.length() == 1) {
                            c = "0" + c;
                        }

                        String mmsi_cmd = "$PAIS,010,,," + mmsi + "*" + c + "\r\n";
                        Logger.i("dsgdsg:" + mmsi_cmd);
                        UART1Tx(mmsi_cmd);
                    } else if (str.contains("$DUAIR,1,010*")) {
/*
修改船舶属性
 */
//        String a3="AISSD,A--A,B--B,C,D,E,F,G,H*hh"+mmsi;
                        if (huhao.length() < 7) {
                            for (int i = 0; i < 7 - huhao.length(); i++) {
                                huhao += " ";
                            }
                        }
                        if (name.length() < 20) {
                            for (int i = 0; i < 20 - name.length(); i++) {
                                name += " ";
                            }
                        }

                        if (A.length() < 3) {
                            for (int i = 0; i < 3 - A.length(); i++) {
                                A = "0" + A;
                            }
                        }
                        if (B.length() < 3) {
                            for (int i = 0; i < 3 - B.length(); i++) {
                                B = "0" + B;
                            }
                        }
                        if (C.length() < 2) {
                            for (int i = 0; i < 2 - C.length(); i++) {
                                C = "0" + C;
                            }
                        }
                        if (D.length() < 2) {
                            for (int i = 0; i < 2 - D.length(); i++) {
                                D = "0" + D;
                            }
                        }
//        String a3="AISSD,,"+name+","+A+","+B+","+C+","+D+",1,AI";

                        String a3 = "AISSD,," + name + "," + A + "," + B + "," + C + "," + D + ",1,AI";
////        String a3="AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI";
                        String c2 = getXORCheck(a3.toCharArray());
                        Logger.i("dsgdsg:" + c2);
                        for (int i = 0; i < c2.length(); i++) {
                            if (Character.isLowerCase(c2.charAt(i))) {
                                c2 = c2.replace(c2.charAt(i), Character.toUpperCase(c2.charAt(i)));
                            }
                        }

                        if (c2.length() == 1) {
                            c2 = "0" + c2;
                        }
//        String ship_cmd="$AISSD,5677777,56667777888777777777,122,154,23,24,1,AI*4B\r\n";
//        String ship_cmd="$AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI"+"*"+c2+"\r\n";
//        String ship_cmd="$AISSD,,name,122,110,11,30,1,AI*"+c2+"\r\n";
//
                        String ship_cmd = "$AISSD,," + name + "," + A + "," + B + "," + C + "," + D + ",1,AI" + "*" + c2 + "\r\n";
                        Logger.i("dsgdsg:" + ship_cmd);
                        UART1Tx(ship_cmd);

                    } else if (str.contains("$DUAIR,1,SSD*")) {
 /*
        配置航行静态数据
        */
                        if (type.length() < 3) {
                            for (int i = 0; i < 3 - type.length(); i++) {
                                type = "0" + type;
                            }
                        }
                        String a4 = "AIVSD," + type + ",,,,,,,,";

                        String c3 = getXORCheck(a4.toCharArray());
                        Logger.i("dsgdsg:" + c3);
                        for (int i = 0; i < c3.length(); i++) {
                            if (Character.isLowerCase(c3.charAt(i))) {
                                c3 = c3.replace(c3.charAt(i), Character.toUpperCase(c3.charAt(i)));
                            }
                        }

                        if (c3.length() == 1) {
                            c3 = "0" + c3;
                        }

                        String type_cmd = "$AIVSD," + type + ",,,,,,,," + "*" + c3 + "\r\n";
                        Logger.i("dsgdsg:" + type_cmd);
                        UART1Tx(type_cmd);

                    } else if (str.contains("$DUAIR,1,VSD*")) {
                        Toast.makeText(AisSettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    }
//                    else  if(str.contains("$DUAIR,0,013*")){
//                        Toast.makeText(AisSettingActivity.this,"设置MMSI失败",Toast.LENGTH_SHORT).show();
//                    }else if(str.contains("$DUAIR,0,SSD*")){
//                        Toast.makeText(AisSettingActivity.this,"设置SSD失败",Toast.LENGTH_SHORT).show();
//                    }else if(str.contains("$DUAIR,0,VSD*")){
//                        Toast.makeText(AisSettingActivity.this,"设置VSD失败",Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(AisSettingActivity.this,"dsggg"+str,Toast.LENGTH_SHORT).show();
                } else if (str.contains("$PAIS,010")) {

                    String number = str.trim().substring(12, 21);
                    Logger.i("hase1" + number);
                    int a = Integer.valueOf(number);
//
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("ais_mmsi", a);
                    editor.commit();
                    if (et_mmsi != null) {
                        if(!String.valueOf(a).equals("999999999")) {

                            et_mmsi.setText(""+a);
                        }else {

                            et_mmsi.setText("000000000");
                        }
//                        et_mmsi.setText("" + a);
                    } else {
                        et_mmsi = (EditText) findViewById(R.id.et_mmsi);
//                        et_mmsi.setText("" + a);
                        if(!String.valueOf(a).equals("999999999")) {

                            et_mmsi.setText(""+a);
                        }else {

                            et_mmsi.setText("000000000");
                        }
                    }
                    String cmd2 = "$DUAIQ,SSD*20\r\n";
//        Logger.i("aaa:"+cmd2);
                    UART1Tx(cmd2);


                } else if (str.contains("$AISSD")) {
                    String s = str.toString().trim();
                    Logger.i("hase1" + s);
//                   String huhao=s.substring(7,14);
////                    et_huhao.setText(huhao);
                    String[] arr = s.split(",|\\*");

                    et_ship_name.setText(arr[2]);
                    et_A.setText(arr[3]);
                    et_B.setText(arr[4]);
                    et_C.setText(arr[5]);
                    et_D.setText(arr[6]);
//                    String A=s.substring(36,39);
////
//                    String B=s.substring(40,43);
////                    et_B.setText(B);
//                    String C=s.substring(44,46);
////                    et_C.setText(C);
//                    String D=s.substring(47,49);
//                    et_D.setText(D);
                    String cmd3 = "$DUAIQ,VSD*25\r\n";
                    Logger.i("bbb:" + cmd3);
                    UART1Tx(cmd3);

                } else if (str.contains("$AIVSD")) {
                    String s = str.toString().trim();
                    Logger.i("hase1" + s);
                    String type = s.substring(7, 10);
                    et_type.setText(type);
                    Toast.makeText(AisSettingActivity.this, "读取数据成功", Toast.LENGTH_SHORT).show();
                }
            }

//      r          if (str.contains("$GP")) {
//                    Logger.d("dkkg", "_1:" + str);
//                }
//            }
        });
    }

    String name;
    String mmsi;
    String huhao;
    String type;
    String A;
    String B;
    String C;
    String D;
//测试版本的保存配置监听
//    public void save_setting(View view) {
//
//        mmsi = et_mmsi.getText().toString().trim();
//        if (TextUtils.isEmpty(mmsi)) {
//            Toast.makeText(this, "MMSI不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (mmsi.length() != 9) {
//            Toast.makeText(this, "MMSI输入格式，请输入9位数字", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        name = et_ship_name.getText().toString().trim();
//        if (TextUtils.isEmpty(name)) {
//            Toast.makeText(this, "船名不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!Validator.isInAscII(name)) {
//            Toast.makeText(this, "字符输入格式不对", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        huhao = et_huhao.getText().toString().trim();
////        if(TextUtils.isEmpty(huhao)){
////            Toast.makeText(this,"呼号不能为空",Toast.LENGTH_SHORT).show();
////            return;
////        }
////        if(!Validator.isInAscII(huhao)){
////            Toast.makeText(this,"字符输入格式不对",Toast.LENGTH_SHORT).show();
////            return;
////        }
//        type = et_type.getText().toString().trim();
//        if (TextUtils.isEmpty(type)) {
//            Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int a = Integer.valueOf(type);
//        if (a > 255) {
//            Toast.makeText(this, "类型范围为0-255", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        A = et_A.getText().toString().trim();
//        if (TextUtils.isEmpty(A)) {
//            Toast.makeText(this, "A不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int b1 = Integer.valueOf(A);
//        if (b1 > 511) {
//            Toast.makeText(this, "A范围为0-511", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        B = et_B.getText().toString().trim();
//        if (TextUtils.isEmpty(B)) {
//            Toast.makeText(this, "B不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int b2 = Integer.valueOf(B);
//        if (b2 > 511) {
//            Toast.makeText(this, "B范围为0-511", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        C = et_C.getText().toString().trim();
//        if (TextUtils.isEmpty(C)) {
//            Toast.makeText(this, "C不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int b3 = Integer.valueOf(C);
//        if (b3 > 63) {
//            Toast.makeText(this, "C范围为0-63", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        D = et_D.getText().toString().trim();
//        if (TextUtils.isEmpty(D)) {
//            Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int b4 = Integer.valueOf(D);
//        if (b4 > 63) {
//            Toast.makeText(this, "类型范围为0-63", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        /*
//        先发送复位的消息
//         */
//        String mmsi_cmd1 = "$PAIS,013,,,[[KanDLe]]*10\r\n";
//        Logger.i("dsgdsg:" + mmsi_cmd1);
//        UART1Tx(mmsi_cmd1);
////        /*
////        修改mmsi
////        计算校验和
////         */
////        String a2="PAIS,010,,,"+mmsi;
////        String c=getXORCheck(a2.toCharArray());
////        Logger.i("dsgdsg:"+c);
////        for(int i=0;i<c.length();i++){
////            if(Character.isLowerCase(c.charAt(i))){
////                c=c.replace(c.charAt(i),Character.toUpperCase(c.charAt(i)));
////            }
////        }
////
////        if(c.length()==1){
////            c="0"+c;
////        }
////
////        String mmsi_cmd="$PAIS,010,,,"+mmsi+"*"+c+"\r\n";
////Logger.i("dsgdsg:"+mmsi_cmd);
////        UART1Tx(mmsi_cmd);
///*
//修改船舶属性
// */
////        String a3="AISSD,A--A,B--B,C,D,E,F,G,H*hh"+mmsi;
////        if(huhao.length()<7){
////            for(int i=0;i<7-huhao.length();i++){
////                huhao+=" ";
////            }
////        }
////        if(name.length()<20){
////            for(int i=0;i<20-name.length();i++){
////                name+=" ";
////            }
////        }
////
////        if(A.length()<3){
////            for(int i=0;i<3-A.length();i++){
////                A="0"+A;
////            }
////        }
////            if(B.length()<3){
////            for(int i=0;i<3-B.length();i++){
////                B="0"+B;
////            }
////        }
////        if(C.length()<2){
////            for(int i=0;i<2-C.length();i++){
////                C="0"+C;
////            }
////        }
////        if(D.length()<2){
////            for(int i=0;i<2-D.length();i++){
////                D="0"+D;
////            }
////        }
////        String a3="AISSD,,"+name+","+A+","+B+","+C+","+D+",1,AI";
//
////        String a3="AISSD,,"+name+","+A+","+B+","+C+","+D+",1,AI";
////////        String a3="AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI";
////        String c2=getXORCheck(a3.toCharArray());
////        Logger.i("dsgdsg:"+c2);
////        for(int i=0;i<c2.length();i++){
////            if(Character.isLowerCase(c2.charAt(i))){
////                c2=c2.replace(c2.charAt(i),Character.toUpperCase(c2.charAt(i)));
////            }
////        }
////
////        if(c2.length()==1){
////            c2="0"+c2;
////        }
//////        String ship_cmd="$AISSD,5677777,56667777888777777777,122,154,23,24,1,AI*4B\r\n";
//////        String ship_cmd="$AISSD,"+huhao+","+name+","+A+","+B+","+C+","+D+",1,AI"+"*"+c2+"\r\n";
//////        String ship_cmd="$AISSD,,name,122,110,11,30,1,AI*"+c2+"\r\n";
//////
////        String ship_cmd="$AISSD,,"+name+","+A+","+B+","+C+","+D+",1,AI"+"*"+c2+"\r\n";
////        Logger.i("dsgdsg:"+ship_cmd);
////        UART1Tx(ship_cmd);
////        $AISSD,A--A,B--B,C,D,E,F,G,H*hh
////
////        /*
////        配置航行静态数据
////        */
////        if(type.length()<3){
////            for(int i=0;i<3-type.length();i++){
////                type="0"+type;
////            }
////        }
////        String a4="AIVSD,"+type+",,,,,,,,";
////
////        String c3=getXORCheck(a4.toCharArray());
////        Logger.i("dsgdsg:"+c3);
////        for(int i=0;i<c3.length();i++){
////            if(Character.isLowerCase(c3.charAt(i))){
////                c3=c3.replace(c3.charAt(i),Character.toUpperCase(c3.charAt(i)));
////            }
////        }
////
////        if(c3.length()==1){
////            c3="0"+c3;
////        }
////
////        String type_cmd="$AIVSD,"+type+",,,,,,,,"+"*"+c3+"\r\n";
////        Logger.i("dsgdsg:"+type_cmd);
////        UART1Tx(type_cmd);
////        String cmd3="$DUAIQ,VSD*25\r\n";
////        Logger.i("bbb:"+cmd3);
////        UART1Tx(cmd3);
////        String cmd2="$DUAIQ,SSD*20\r\n";
////        Logger.i("aaa:"+cmd2);
////        UART1Tx(cmd2);
////        String cmd4="$DUAIQ,010*55\r\n";
////        Logger.i("zzz:"+cmd4);
////        UART1Tx(cmd4);
//
//
//    }

    /*
    校验和
     */
    private String getXORCheck(char[] b) {

        char x = 0;
        for (int i = 0; i < b.length; i++)
            x ^= b[i];

        return String.format("%x", (int) x);
    }

    @Override
    protected void onStop() {
        super.onStop();
        toGetMMSI=false;
    }

    @Override
    protected void onDestroy() {

        toGetMMSI=false;
        super.onDestroy();

    }


}
