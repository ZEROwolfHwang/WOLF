package com.yisipu.chartmap;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CustomDialog;
import com.yisipu.chartmap.surfaceView.CompassMarkObject;
import com.yisipu.chartmap.surfaceView.MyCompass;
import com.yisipu.chartmap.utils.ConvertUtils;
import com.yisipu.chartmap.utils.LocationUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Ais设置Activity
 * Created by Administrator on 2016/7/29.
 */
public class CompassActivity extends SerialPortActivity implements SensorEventListener {
    private ShipBean sb;
    private SharedPreferences sp2;
    private TextView tv_chinese_name;
    private ConvertUtils convertUtils = new ConvertUtils();
    Bitmap ship_a;
    List<ShipBean> ls = null;
    private Runnable runnable;
    private EditText et_fanwei;
    private EditText et_baojing;
    private TextView tv_mmsi, tv_name, tv_huhao, tv_imo, tv_weidu, tv_jingdu, tv_hang_xiang, tv_fang_wei, tv_hang_su, tv_distance,
            tv_chan_shou, tv_acc, tv_zhuanxiang, tv_status, tv_ship_length,
            tv_ship_width, tv_country, tv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_compass);
        sp2 = getSharedPreferences("sp", MODE_APPEND);

        initTitle();
        // 传感器管理器
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
        // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
        sm.registerListener(CompassActivity.this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
        finView();
        initData();
        if (handler == null) {
            handler = new Handler();
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                //初始化海图
                initData();
                Logger.i("aaaaaa1kr21212苛刻5");
                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.

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

    CustomDialog dialog;

    public void finView() {
        myC = (MyCompass) findViewById(R.id.sceneMyC);
        myC.setMy(new MyCompass.MyshipClickListener() {
            @Override
            public void onMyshipClick() {
                DBManager db = new DBManager(CompassActivity.this);
                sb = db.getMyShip();
                if (sb != null) {
//                Toast.makeText(CompassActivity.this,"点击事件",Toast.LENGTH_SHORT).show();
                    dialog = new CustomDialog(CompassActivity.this);


                    dialog.getTv_mmsi().setText("MMSI:" + sb.getMMSI());
                    if (null != sb.getHuhao()) {
                        dialog.getTv_huhao().setText("呼号:" + sb.getHuhao());
                    }
                    if (null != sb.getIMO()) {
                        dialog.getTv_imo().setText("IMO:" + sb.getIMO());
                    }
                    if (sb.getLatitude() <= 90) {
                        String weidu = convertUtils.Latitude(sb.getLatitude());
                        dialog.getTv_weidu().setText("纬度:" + weidu);
                    }
                    if (sb.getLongitude() <= 180) {
                        String jingdu = convertUtils.Longitude(sb.getLongitude());
                        dialog.getTv_jingdu().setText("经度:" + jingdu);
                    }
                    if (!(sb.getCog() >= 3600) && sb.getCog() != -1) {
                        dialog.getTv_hang_xiang().setText("航向:" + sb.getCog() / (10.0));
                    }
                    DecimalFormat df = new DecimalFormat("##0.0");
                    if (sb.getSog() != 1023 && sb.getSog() != -1) {
                        dialog.getTv_hang_su().setText("航速:" + df.format(sb.getSog() / (10.0)) + " Kn");
                    }
                    if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != -1) {

                        dialog.getTv_chan_shou().setText("船艏:" + sb.getReal_sudu());
                    }
                    if (sb.getPrecision() != -1) {
                        dialog.getTv_acc().setText("精度:" + sb.getPrecision());
                    }
                    if (sb.getRot() != -1) {
                        dialog.getTv_zhuanxiang().setText("转向:" + sb.getRot());
                    }
                    if (sb.getStatus() != -1) {
                        dialog.getTv_status().setText("状态:" + sb.getStatus());
                    }
                    if (-1 != sb.getDimBow() && -1 != sb.getDimStern()) {
                        dialog.getTv_ship_length().setText("船长:" + (sb.getDimBow() + sb.getDimStern()));
                    }
                    if (-1 != sb.getDimStarboard() && -1 != sb.getDimPort()) {

                        dialog.getTv_ship_width().setText("船宽:" + (sb.getDimStarboard() + sb.getDimPort()));
                    }
                    if (sb.getType() != -1) {
                        dialog.getTv_type().setText("类型:" + sb.getType());
                    }
                    dialog.getTv_name().setText("英文船名:" + sb.getEnglishName());
                    if (sb.getDistance() != -1 && sb.getMyShip() != true && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
                        DecimalFormat df3 = new DecimalFormat("##0.00");
                        dialog.getTv_distance().setText("距离:" + df3.format(sb.getDistance() / (1852)) + " nm");

                    }
                    if (sb.getFangwei() != -1 && sb.getMyShip() != true && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
                        if (sb.getFangwei() < 0) {

                            sb.setFangwei(sb.getFangwei() + 360);
                        }
                        DecimalFormat df2 = new DecimalFormat("##0.0");

                        dialog.getTv_fang_wei().setText("方位:" + df2.format(sb.getFangwei()) + " °");
                    }
                    if (sb.getCountry() != -1) {
                        String name = "country_type_" + sb.getCountry();
                        Resources rc = getResources();
                        int id = rc.getIdentifier(name, "string", "com.yisipu.chartmap");
                        if (id == 0) {
                        } else {
                            dialog.getTv_country().setText("国家:" + getString(id));
                        }
                    }
                    if (!sb.getChineseName().equals("")) {
                        dialog.getTv_chinese_name().setText("中文船名:" + sb.getChineseName());
                    }
                    dialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        et_fanwei = (EditText) findViewById(R.id.et_fanwei);
        et_baojing = (EditText) findViewById(R.id.et_baojing);
        et_fanwei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_fanwei.setText(et_fanwei.getText().toString());//添加这句后实现效果
                Spannable content = et_fanwei.getText();
                Selection.selectAll(content);

            }
        });
        et_baojing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_baojing.setText(et_baojing.getText().toString());//添加这句后实现效果
                Spannable content = et_baojing.getText();
                Selection.selectAll(content);

            }
        });
        myC.setFanweiNm(sp2.getFloat("fanwei", 20));
        myC.setBaojingNm(sp2.getFloat("baojing", 8));
        et_fanwei.setText("" + sp2.getFloat("fanwei", 20));
        et_baojing.setText("" + sp2.getFloat("baojing", 8));

    }

    MyCompass myC;
    ShipBean my_ship;
    Bitmap ship_b;

    //相对于原点的坐标
    double x;
    double y;

    public void initData() {
        DBManager db = new DBManager(this);
        // /这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
        ship_a = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship_a);
        ship_b = BitmapFactory.decodeResource(getResources(),
                R.drawable.ship_b);

        myC.clearMark();
        ls = db.getShipBeans();

        my_ship = db.getMyShip();
        if (my_ship != null) {
            Logger.i("ddf" + my_ship.getClassType());
            if (my_ship.getClassType() == 1) {
                myC.setBitmap(ship_a);
            } else {
                myC.setBitmap(ship_b);
            }
            if (my_ship.getReal_sudu() != -1 && my_ship.getReal_sudu() != 511) {
                myC.setChuanshou(my_ship.getReal_sudu());
            }
        } else {
            myC.setBitmap(ship_b);
        }
        if (my_ship != null && my_ship.getLatitude() != -1 && my_ship.getLongitude() != -1 && my_ship.getLatitude() != 91 && my_ship.getLongitude() != 181) {
            for (ShipBean ss : ls) {

                //距离
                ss.setDistance(LocationUtils.gps2m(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                ss.setFangwei(LocationUtils.gps2d(my_ship.getLatitude(), my_ship.getLongitude(), ss.getLatitude(), ss.getLongitude()));

            }
        }
        for (final ShipBean sb : ls) {
            if (((sb.getDistance() / (1852)) <= myC.getFanweiNm()) && sb.getDistance() != -1 && sb.getFangwei() != -1 && sb.getMyShip() != true) {
                CompassMarkObject markObject = new CompassMarkObject();
                DecimalFormat df2 = new DecimalFormat("##0.0");

                Logger.i("方位：" + sb.getFangwei());

                x = Math.cos(2 * Math.PI / 360 * Double.valueOf(df2.format(sb.getFangwei()))) * (sb.getDistance() / (1852));
                y = Math.sin(2 * Math.PI / 360 * Double.valueOf(df2.format(sb.getFangwei()))) * (sb.getDistance() / (1852));
                Logger.i("方位：" + Double.valueOf(df2.format(sb.getFangwei())) + "x:" + Math.cos(Double.valueOf(df2.format(sb.getFangwei()))));
                markObject.setMapX((float) x);
                markObject.setMapY((float) y);
                Logger.i("dskdkgk" + sb.getMMSI() + "y=" + y);
                if (sb.getClassType() == 1) {
                    markObject.setmBitmap(ship_a);
                } else {
                    markObject.setmBitmap(ship_b);
                }
                if (sb.getReal_sudu() != -1 && sb.getReal_sudu() != 511) {
                    markObject.setChuanshou(sb.getReal_sudu());
                }

                markObject.setMarkListener(new CompassMarkObject.MarkClickListener() {

                    @Override
                    public void onMarkClick(int x, int y) {
                        dialog = new CustomDialog(CompassActivity.this);
                        dialog.getTv_mmsi().setText("MMSI:" + sb.getMMSI());
                        if (null != sb.getHuhao()) {
                            dialog.getTv_huhao().setText("呼号:" + sb.getHuhao());
                        }
                        if (null != sb.getIMO()) {
                            dialog.getTv_imo().setText("IMO:" + sb.getIMO());
                        }
                        if (sb.getLatitude() <= 90) {
                            String weidu = convertUtils.Latitude(sb.getLatitude());
                            dialog.getTv_weidu().setText("纬度:" + weidu);
                        }
                        if (sb.getLongitude() <= 180) {
                            String jingdu = convertUtils.Longitude(sb.getLongitude());
                            dialog.getTv_jingdu().setText("经度:" + jingdu);
                        }
                        if (!(sb.getCog() >= 3600) && sb.getCog() != -1) {
                            dialog.getTv_hang_xiang().setText("航向:" + sb.getCog() / (10.0));
                        }
                        DecimalFormat df = new DecimalFormat("##0.0");
                        if (sb.getSog() != 1023 && sb.getSog() != -1) {
                            dialog.getTv_hang_su().setText("航速:" + df.format(sb.getSog() / (10.0)) + " Kn");
                        }
                        if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != -1) {

                            dialog.getTv_chan_shou().setText("船艏:" + sb.getReal_sudu());
                        }
                        if (sb.getPrecision() != -1) {
                            dialog.getTv_acc().setText("精度:" + sb.getPrecision());
                        }
                        if (sb.getRot() != -1) {
                            dialog.getTv_zhuanxiang().setText("转向:" + sb.getRot());
                        }
                        if (sb.getStatus() != -1) {
                            dialog.getTv_status().setText("状态:" + sb.getStatus());
                        }
                        if (-1 != sb.getDimBow() && -1 != sb.getDimStern()) {
                            dialog.getTv_ship_length().setText("船长:" + (sb.getDimBow() + sb.getDimStern()));
                        }
                        if (-1 != sb.getDimStarboard() && -1 != sb.getDimPort()) {

                            dialog.getTv_ship_width().setText("船宽:" + (sb.getDimStarboard() + sb.getDimPort()));
                        }
                        if (sb.getType() != -1) {
                            dialog.getTv_type().setText("类型:" + sb.getType());
                        }
                        dialog.getTv_name().setText("英文船名:" + sb.getEnglishName());
                        if (sb.getDistance() != -1 && sb.getMyShip() != true && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
                            DecimalFormat df3 = new DecimalFormat("##0.00");
                            dialog.getTv_distance().setText("距离:" + df3.format(sb.getDistance() / (1852)) + " nm");

                        }
                        if (sb.getFangwei() != -1 && sb.getMyShip() != true && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
                            if (sb.getFangwei() < 0) {

                                sb.setFangwei(sb.getFangwei() + 360);
                            }
                            DecimalFormat df2 = new DecimalFormat("##0.0");

                            dialog.getTv_fang_wei().setText("方位:" + df2.format(sb.getFangwei()) + " °");
                        }
                        if (sb.getCountry() != -1) {
                            String name = "country_type_" + sb.getCountry();
                            Resources rc = getResources();
                            int id = rc.getIdentifier(name, "string", "com.yisipu.chartmap");
                            if (id == 0) {
                            } else {
                                dialog.getTv_country().setText("国家:" + getString(id));
                            }
                        }
                        if (!sb.getChineseName().equals("")) {
                            dialog.getTv_chinese_name().setText("中文船名:" + sb.getChineseName());
                        }
                        dialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                myC.addMark(markObject);
            } else {
                continue;
            }

        }
    }


    public void initTitle() {
        hideTitleRight();

        setTitleText("罗盘");
        showTitle();
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

    int i = 1;

    /*
    传感器
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            final float degree = event.values[0];

            i++;
            if (i == 10) {
                myC.setDegree(degree);
                i = 1;
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setting(View view) {
        String a = et_baojing.getText().toString().trim();
        String b = et_fanwei.getText().toString().trim();
        if (TextUtils.isEmpty(b)) {
            Toast.makeText(CompassActivity.this, "显示范围不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(a)) {
            Toast.makeText(CompassActivity.this, "报警范围不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pandun(b)) {
            Toast.makeText(CompassActivity.this, "显示范围数值格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pandun(a)) {

            double d = Double.parseDouble(a);
            if (d <= 10 && d >= 0.5) {
                if (d <= Double.parseDouble(b)) {
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putFloat("baojing", (float) d);
                    editor.commit();
                    myC.setBaojingNm(d);

                }
            } else {
                Toast.makeText(CompassActivity.this, "报警范围只能设置在0.5到10海里之间", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(CompassActivity.this, "报警范围数值格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pandun(b)) {
            double d = Double.parseDouble(b);
            if (d <= 20 && d >= 0.5 && d >= Double.parseDouble(a)) {
                myC.setFanweiNm(d);
                SharedPreferences.Editor editor = sp2.edit();
                editor.putFloat("fanwei", (float) d);
                editor.commit();
                Toast.makeText(CompassActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CompassActivity.this, "显示范围只能设置在0.5到20海里之间,同时必须比报警范围大", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(CompassActivity.this, "显示范围数值格式不对", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private boolean pandun(String str) {
        boolean ret = true;
        try {
            double d = Double.parseDouble(str);
            ret = true;
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    @Override
    protected void onStop() {
        stopPush();
        super.onStop();
    }

    @Override
    protected void onResume() {

//        initMap();
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
                    initData();

                    Logger.i("aaaaaa1kr21212苛刻4");
                    handler.postDelayed(this, 5000);

                }
            };
            handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.
        }
    }
}
