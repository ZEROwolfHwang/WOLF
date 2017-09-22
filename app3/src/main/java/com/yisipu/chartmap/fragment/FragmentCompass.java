package com.yisipu.chartmap.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CustomDialog;
import com.yisipu.chartmap.surfaceView.CompassMarkObject;
import com.yisipu.chartmap.surfaceView.MyCompass;
import com.yisipu.chartmap.utils.ConvertUtils;
import com.yisipu.chartmap.utils.LocationUtils;
import com.yisipu.chartmap.utils.PinyinTool;
import com.yisipu.chartmap.utils.ShipTypeUtils;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class FragmentCompass extends Fragment implements SensorEventListener, View.OnClickListener {
    private ShipBean sb;
    private SharedPreferences sp2;
    private TextView tv_chinese_name;
    private ConvertUtils convertUtils = new ConvertUtils();
    Bitmap ship_a;
    List<ShipBean> ls = null;
    private Runnable runnable;
    private EditText et_fanwei;
    private EditText et_baojing;
    private EditText et_fpjl;
    private Button compass_sz;
    View mView;
    private TextView tv_mmsi, tv_name, tv_huhao, tv_imo, tv_weidu, tv_jingdu, tv_hang_xiang, tv_fang_wei, tv_hang_su, tv_distance,
            tv_chan_shou, tv_acc, tv_zhuanxiang, tv_status, tv_ship_length,
            tv_ship_width, tv_country, tv_type;
    private Activity mActivity;
    private static volatile FragmentCompass fragmentCompass;

    public static FragmentCompass getInstance() {
        if (fragmentCompass == null) {
            synchronized (FragmentCompass.class) {
                if (fragmentCompass == null) {
                    fragmentCompass = new FragmentCompass();
                }
            }
        }
        return fragmentCompass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = null;
        if (mView == null && inflater != null) {
            mView = inflater.inflate(R.layout.activity_compass, null);
        }
        sp2 = getActivity().getSharedPreferences("sp", Context.MODE_APPEND);

        // 传感器管理器
        SensorManager sm = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
        // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
        finView(mView);
        initData();
        if (handler == null) {
            handler = new Handler();
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                //初始化海图
                initData();
                // TODO Auto-generated method stub
                //要做的事情
//                String cmd="!AIVDM,1,1,,B,8>qc9wh0@E=D85A5Dm@,2*49\r\n";
////        Logger.i("zzz:"+cmd4);
//                UART1Tx(cmd);
                Logger.i("aaaaaa1kr21212苛刻5");
                handler.postDelayed(this, 5000);

            }
        };

        handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.
        compass_sz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = et_baojing.getText().toString().trim();
                String b = et_fanwei.getText().toString().trim();
                String c = et_fpjl.getText().toString().trim();
                if (TextUtils.isEmpty(b)) {
                    Toast.makeText(getActivity(), "显示范围不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(a)) {
                    Toast.makeText(getActivity(), "报警范围不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(c)) {
                    Toast.makeText(getActivity(), "防碰距离不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pandun(b)) {
                    Toast.makeText(getActivity(), "显示范围数值格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pandun(c)) {
                    Toast.makeText(getActivity(), "防碰距离数值格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pandun(a)) {

                    double d = Double.parseDouble(a);
//           if(d<=10&&d>=0.5)
                    if (d <= 4 && d >= 0.1) {
                        if (d <= Double.parseDouble(b)) {
                            SharedPreferences.Editor editor = sp2.edit();
                            editor.putFloat("baojing", (float) d);
                            editor.commit();
                            myC.setBaojingNm(d);

                        }
                    } else {
                        Toast.makeText(getActivity(), "报警范围只能设置在0.5到4海里之间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getActivity(), "报警范围数值格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pandun(c)) {

                    double d = Double.parseDouble(c);
                    if (d <= 300 && d >= 1) {
                        if (d / 1825 <= Double.parseDouble(a)) {
                            SharedPreferences.Editor editor = sp2.edit();
                            editor.putInt("fpjl", (int) d);//单位是米
                            editor.commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), "防碰距离只能设置在1到300米之间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getActivity(), "防碰距离数值格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pandun(b)) {
                    double d = Double.parseDouble(b);
                    if (d <= 10 && d >= 0.5 && d >= Double.parseDouble(a)) {
                        myC.setFanweiNm(d);
                        SharedPreferences.Editor editor = sp2.edit();
                        editor.putFloat("fanwei", (float) d);
                        editor.commit();
                        Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                 //       ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        Toast.makeText(getActivity(), "显示范围只能设置在0.5到10海里之间,同时必须比报警范围大", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "显示范围数值格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        return mView;
    }

    public void finView(View view) {
        myC = (MyCompass) view.findViewById(R.id.sceneMyC);
        myC.setMy(new MyCompass.MyshipClickListener() {
            @Override
            public void onMyshipClick() {
                MyCompass.canClick = false;//这里需要结束事件的监听
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        DBManager db = new DBManager(mActivity);
                        sb = db.getMyShip();
                        if (sb != null) {
                            final CustomDialog dialog = new CustomDialog(mActivity);


                            if (!String.valueOf(sb.getMMSI()).equals("999999999") || !sb.getMyShip()) {
                                dialog.getTv_mmsi().setText("MMSI:" + sb.getMMSI());

                            } else {
                                dialog.getTv_mmsi().setText("MMSI:" + "000000000");

                            }
//                    dialog.getTv_mmsi().setText("MMSI:" + sb.getMMSI());
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
                                String a = ShipTypeUtils.getShipTypeString(sb.getType());

                                dialog.getTv_type().setText("类型:" + a);
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

                            final PinyinTool p = new PinyinTool();
                            if (!sb.getOtherName().equals("")) {
                                dialog.getTv_chinese_name().setText(sb.getOtherName());
                            } else {
                                if (!sb.getChineseNameChange().equals("") && !sb.getChineseName().equals("")) {
                                    try {
                                        String a = p.toPinYin(sb.getChineseNameChange());
                                        String b = p.toPinYin(sb.getChineseName());
                                        if (a.equals(b)) {
                                            dialog.getTv_chinese_name().setText(sb.getChineseNameChange());
                                        } else {
                                            dialog.getTv_chinese_name().setText(sb.getChineseName());
                                        }
                                    } catch (Exception exp) {
                                        if (!sb.getChineseName().equals("")) {

                                            dialog.getTv_chinese_name().setText(sb.getChineseName());
                                        }

                                    }


                                } else {
                                    dialog.getTv_chinese_name().setText(sb.getChineseName());
                                }

                            }
                            dialog.getSave_chinese_2().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String other = dialog.getTv_chinese_name().getText().toString().trim();
                                    if (sb.getChineseName().equals("")) {
                                        Toast.makeText(mActivity, "暂无英文船名不可修改中文船名", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (TextUtils.isEmpty(other)) {
                                        Toast.makeText(mActivity, "中文船名不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try {
                                        if (!sb.getChineseName().equals("")) {
                                            String a = p.toPinYin(other);
                                            String b = p.toPinYin(sb.getChineseName());
                                            if (a.equals(b)) {
                                                DBManager db = new DBManager(mActivity);
                                                sb.setChineseNameChange(other);
                                                db.addShipBean(sb);
                                                Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mActivity, "修改的中文船名必须为同音字", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } else {
                                            return;
                                        }
                                    } catch (Exception exp) {
                                        Toast.makeText(mActivity, "保存失败", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                            if (!sb.getOtherName().equals("")) {
                                dialog.getEt_other_name().setText(sb.getOtherName());
                            }
                            dialog.getSave_other_name().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String other = dialog.getEt_other_name().getText().toString().trim();
                                    if (TextUtils.isEmpty(other)) {
                                        Toast.makeText(mActivity, "别名不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    DBManager db = new DBManager(mActivity);
                                    sb.setOtherName(other);

                                    db.addShipBean(sb);
                                    Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.setOnPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    MyCompass.canClick = true;
                                }
                            });
                            dialog.show();

                        }

                    }

                }, 500);
            }


        });


        et_fanwei = (EditText) view.findViewById(R.id.et_fanwei);
        et_baojing = (EditText) view.findViewById(R.id.et_baojing);
        compass_sz = (Button) view.findViewById(R.id.compass_sz);
        et_fpjl = (EditText) view.findViewById(R.id.et_fpjl);
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
        et_fpjl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_fpjl.setText(et_fpjl.getText().toString());//添加这句后实现效果
                Spannable content = et_fpjl.getText();
                Selection.selectAll(content);

            }
        });
        myC.setFanweiNm(sp2.getFloat("fanwei", 6));
        myC.setBaojingNm(sp2.getFloat("baojing", 4));
        et_fanwei.setText("" + sp2.getFloat("fanwei", 6));
        et_baojing.setText("" + sp2.getFloat("baojing", 4));
        et_fpjl.setText("" + sp2.getInt("fpjl", 100));
    }

    MyCompass myC;
    ShipBean my_ship;
    Bitmap ship_b;

    //相对于原点的坐标
    double x;
    double y;

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

    private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();

    public void initData() {
        DBManager db = new DBManager(mActivity);
        OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;

        OPTIONS.outWidth = 128;
        OPTIONS.outHeight = 128;
        ship_a = BitmapFactory.decodeResource(mActivity.getResources(),
                R.drawable.ship_a, OPTIONS);
        ship_b = BitmapFactory.decodeResource(mActivity.getResources(),
                R.drawable.ship_b, OPTIONS);

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
        List<CompassMarkObject> markObjectList = new ArrayList<>();
        markObjectList.clear();
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
                        MyCompass.canClick = false;//这里需要结束事件的监听
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                final CustomDialog dialog = new CustomDialog(getActivity());


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
                                        MyCompass.canClick = true;
                                    }
                                });
                                dialog.show();

                            }
                        }, 500);
                    }
                });
                markObjectList.add(markObject);

            } else {
                continue;
            }

        }
        myC.addMarkList(markObjectList);
    }

    int i = 1;
    long curuDate = -1;
    long tempDate = -1;

    /*
    传感器
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            final float degree = event.values[0];
            Date date = new Date();
            curuDate = date.getTime();
            if (tempDate == -1) {
                tempDate = date.getTime();
            } else if (tempDate != -1 && curuDate != -1 && (curuDate - tempDate) > 10) {


                myC.setDegree(degree);
                tempDate = date.getTime();
                Logger.i("asaggg" + degree);

            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

    public static Handler handler = new Handler();

    /*
       隐藏输入法
        */
    public void hidShuRuFa(Context context) {


        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen && mActivity.getCurrentFocus() != null)

        {
            if (mActivity != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            hidShuRuFa(mActivity);
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause

        }
    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {
            if (mActivity != null) {
                hidShuRuFa(mActivity);
            }

            MyCompass.canClick = true;
            MyCompass.drawOk = false;
            stopPush();
        } else {
            if (mActivity != null) {
                hidShuRuFa(mActivity);
            }
            MyCompass.drawOk = true;
            MyCompass.canClick = true;

            startPush();
        }
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

    @Override
    public void onClick(View v) {

    }
}
