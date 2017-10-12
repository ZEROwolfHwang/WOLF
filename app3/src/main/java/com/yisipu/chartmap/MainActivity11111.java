package com.yisipu.chartmap;

import android.app.Activity;
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
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.paths.CompositePathView;
import com.qozix.tileview.widgets.ZoomPanLayout;
import com.yisipu.chartmap.adapter.SlideMenuAdapter;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CollectDialog;
import com.yisipu.chartmap.dialog.CustomDialog;
import com.yisipu.chartmap.dialog.CustomDialog3;
import com.yisipu.chartmap.dialog.CustomDialog4;
import com.yisipu.chartmap.dialog.SelfDialog;
import com.yisipu.chartmap.gps.AlxLocationManager;
import com.yisipu.chartmap.provider.BitmapProviderAssets3;
import com.yisipu.chartmap.tools.SPUtils;
import com.yisipu.chartmap.utils.ConvertUtils;
import com.yisipu.chartmap.utils.DecimalCalculate;
import com.yisipu.chartmap.utils.ExtenSdCard;
import com.yisipu.chartmap.utils.GetLongLati;
import com.yisipu.chartmap.utils.LocationUtils;
import com.yisipu.chartmap.utils.PersonService;
import com.yisipu.chartmap.utils.PinyinTool;
import com.yisipu.chartmap.utils.ServiceIsUser;
import com.yisipu.chartmap.utils.SharePrefenerArrary;
import com.yisipu.chartmap.utils.ShipTypeUtils;
import com.yisipu.chartmap.utils.TxtFileUtile;
import com.yisipu.chartmap.utils.Xml_Operate;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity11111 extends Activity {
    private final String AIS_SW_DEVICE_ELEC5V = "/sys/customer/uart_elec5v";//AIS控制发射
    private final String AIS_SW_DEVICE_NODE = "/sys/customer/ais_en";
    private TileView tileView;

    private ConvertUtils convertUtils = new ConvertUtils();
    private DrawerLayout menuLayout;
    private ListView menuElementsList;
    private ActionBarDrawerToggle menuToggle;
    private TextView tv1, tv2, tv3, tv4, dw, sj, nl;
    private CharSequence activityTitle = "主菜单";
    private SlideMenuAdapter slideMenuAdapter;
    private ArrayList<String> menuNameList;
    private LinearLayout linearLayout, ll2, ll3, ll4, ll5;
    private ImageView dzwl;
    int width;
    int height;

    int x = 1;
    int x1 = 1;
    private Animation myAnimation_Scale;
    /**
     * 经纬度弹窗
     */
    private View viewLL;
    private MainActivity11111 mActivity;

    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opts);
    }

   /* @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String str = new String(buffer, 0, size);
                Logger.i("哈哈哈哈" + new String(buffer, 0, size));
                Logger.d("ddsgg", "dsggss_1:" + str);
                if (null != my_ship) {
                    if (str.contains("$DUAIR,1,013*")) {
                        String a2 = "PAIS,010,,," + my_ship.getMMSI();
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

                        String mmsi_cmd = "$PAIS,010,,," + my_ship.getMMSI() + "*" + c + "\r\n";
                        Logger.i("dsgdsg:" + mmsi_cmd);
                        UART1Tx(mmsi_cmd);
                    } else if (str.contains("$DUAIR,1,010*")) {
*//*
修改船舶属性
 *//*
//        String a3="AISSD,A--A,B--B,C,D,E,F,G,H*hh"+mmsi;
                        String huhao = my_ship.getHuhao();
                        if (my_ship.getHuhao().length() < 7) {
                            for (int i = 0; i < 7 - my_ship.getHuhao().length(); i++) {
                                huhao += " ";
                            }
                        }
                        String name = my_ship.getEnglishName();
                        if (name.length() < 20) {
                            for (int i = 0; i < 20 - name.length(); i++) {
                                name += " ";
                            }
                        }
                        String A = "" + my_ship.getDimBow();
                        if (A.length() < 3) {
                            for (int i = 0; i < 3 - A.length(); i++) {
                                A = "0" + A;
                            }
                        }
                        String B = "" + my_ship.getDimStern();
                        if (B.length() < 3) {
                            for (int i = 0; i < 3 - B.length(); i++) {
                                B = "0" + B;
                            }
                        }
                        String C = "" + my_ship.getDimPort();
                        if (C.length() < 2) {
                            for (int i = 0; i < 2 - C.length(); i++) {
                                C = "0" + C;
                            }
                        }
                        String D = "" + my_ship.getDimStarboard();
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
 *//*
        配置航行静态数据
        *//*
                        String type = "" + my_ship.getType();
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
                        if (my_ship != null) {
                            String phone = (String) SPUtils.get(MainActivity.this, "sosphone", "0591968195");
                           // String phone = sp.getString("sosphone", "0591968195");

                            String a = xml_Operate.WriteXml(my_ship, "0", phone);
                            my_ship.setMyShip(true);
                            DBManager db = new DBManager(MainActivity.this);
                            db.addShipBean(my_ship);
                            xml_Operate.Write(a, pathSettingXml);
                        }
//            Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    } else if (str.contains("$DUAIR,0,013*")) {
//            Toast.makeText(MainActivity.this, "设置MMSI失败", Toast.LENGTH_SHORT).show();
                    } else if (str.contains("$DUAIR,0,SSD*")) {
//            Toast.makeText(MainActivity.this, "设置SSD失败", Toast.LENGTH_SHORT).show();
                    } else if (str.contains("$DUAIR,0,VSD*")) {
//            Toast.makeText(MainActivity.this, "设置VSD失败", Toast.LENGTH_SHORT).show();
                    }
//
                }
            }
        });
    }
*/

    public void initTitle() {

//        hideTitleLeft();
//        setRightString("V1.04");
//        setTitleText("首页");
//        showTitle();
//        hideTitle();
    }

    private Runnable runnable;
    SharedPreferences sp;
    Bitmap ship_a = null;
    Bitmap ship_b = null;
    CustomDialog dialog;
    List<ImageView> imgList = new ArrayList<>();
    List<CompositePathView.DrawablePath> yuan = new ArrayList<>();
    Bitmap yuan_fen, yuan_lan, yuan_hong;
    /*
    船首线
     */
    List<ImageView> LineimgList = new ArrayList<>();
    String md;//模式
    int hl;//蓝色范围
    String gj;//范围报警
    int fpjl;//防碰距离
    float baojing;//报警距离
    List<ShipBean> ls = null;
    ShipBean my_ship2 = null;
    static String dianliang = "";

    //    CustomDialog4 cusm4=null;
    /*
    获取电量
     */
    public void getDianLiang() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
        String level = sp.getString("levelBarry", "-1");
        if (!level.equals("-1") && !dianliang.equals(level)) {
            dianliang = level;
            final CustomDialog4 cusm4 = new CustomDialog4(this);


            //判断设置的模式

            cusm4.setOnPositiveListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    cusm4.dismiss();
                }
            });

            cusm4.show();
        }

    }
     /*
           船首线
             */

    public void drawLine(double x, double y, int chuanshou, double sog) {
        final Paint paint = tileView.getDefaultPathPaint();
        List<double[]> cjpoints = new ArrayList<>();
        cjpoints.add(new double[]{x, y});
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);

        cjpoints.add(new double[]{x, y - (200 / (2.0 * tileView.getDetailLevelManager().getScale()))});
        CompositePathView.DrawablePath path = tileView.drawPath2(cjpoints.subList(0, cjpoints.size()), paint);
        yuan.add(path);


    }

    public void initMap() {

        getDianLiang();

        DBManager db = new DBManager(this);
        Bitmap bit = null;
        Bitmap bit2 = null;

        ls = db.getShipBeans();

        Matrix matrix = new Matrix();
        TileView tileView = getTileView();
        for (ImageView sp2 : LineimgList) {


            tileView.getMarkerLayout().removeMarker(sp2);
        }
        for (ImageView sp2 : imgList) {


            tileView.getMarkerLayout().removeMarker(sp2);
        }
        for (CompositePathView.DrawablePath sp2 : yuan) {


            tileView.getCompositePathView().removePath(sp2);
        }
        imgList.clear();
        my_ship2 = db.getMyShip();
        if (my_ship2 != null && my_ship2.getLatitude() != -1 && my_ship2.getLongitude() != -1 && my_ship2.getLatitude() != 91 && my_ship2.getLongitude() != 181) {
            for (ShipBean ss : ls) {
                if (ss.getLongitude() != -1 && ss.getLatitude() != -1 && !(ss.getLatitude() > 90) && !(ss.getLongitude() > 180) && !(ss.getLatitude() == 0 && ss.getLongitude() == 0)) {
                    //距离
                    ss.setDistance(LocationUtils.gps2m(my_ship2.getLatitude(), my_ship2.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                    ss.setFangwei(LocationUtils.gps2d(my_ship2.getLatitude(), my_ship2.getLongitude(), ss.getLatitude(), ss.getLongitude()));
                } else {
                    //距离
                    ss.setDistance(-1);
                    ss.setFangwei(-1);
                }
            }
        }
        for (ShipBean ss : ls) {
            if (ss.getLongitude() == -1 || ss.getLatitude() == -1 || ss.getLongitude() > 180 || ss.getLatitude() > 90 || (ss.getLatitude() == 0 && ss.getLongitude() == 0))
                //距离
                ss.setDistance(-1);
            ss.setFangwei(-1);

        }
//        tileView.getCompositePathView().clear();

        for (final ShipBean sb : ls) {
            if (sb.getLatitude() != -1 && sb.getLongitude() != -1 && !(sb.getLongitude() > 180) && !(sb.getLatitude() > 90) && !(sb.getLongitude() == 0 && sb.getLatitude() == 0)) {
                double[] point = {(sb.getLongitude() - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(sb.getLatitude(), 5) - Constant.minY5 * 256)};

//                points.add(new double[]{(120.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(24,5)-11*256});
//                points.add(new double[]{(118.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(23,5)-11*256});
//                points.add(new double[]{(116.0-GetLongLati.getLong(25,0,5))/((GetLongLati.getLong(28,0,5)-GetLongLati.getLong(25,0,5)))*256*3, GetLongLati.getY(22,5)-11*256});
                // any view will do...
                //任何视图将做…
                ImageView marker = new ImageView(this);
                // save the coordinate for centering and callout positioning
                //拯救协调中心和标注定位

                Bitmap location;
                if (sb.getClassType() == 1) {
                    location = ship_a;

                } else {
                    location = ship_b;

                }
                if (sb.getMyShip()) {
                    md = (String) SPUtils.get(MainActivity11111.this,"mode", "标准模式");
                    if (md.equals("智能模式")) {
                        hl = (int) SPUtils.get(MainActivity11111.this, "hl", 1);

                        double jd2 = LocationUtils.doLngDegress((1852 * hl) / 1000, sb.getLatitude());
                        double x2 = ((jd2 + sb.getLongitude()) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                        double s2 = Math.abs(x2 - point[0]);
                        Paint paint = new Paint();
                        paint.setColor(0xff4876ff);
                        paint.setAlpha(100);
                        paint.setAntiAlias(true);
                        CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();

                        Path path = new Path();
//        path.addCircle();

                        path.addCircle((float) (Constant.TimesNeed * point[0]), (float) (Constant.TimesNeed * point[1]), (float) s2, Path.Direction.CW);
                        drawablePath.path = path;
                        drawablePath.paint = paint;
                        yuan.add(drawablePath);
                        tileView.drawPath(drawablePath);
                    }
                    gj = (String) SPUtils.get(mActivity,"lpaqfw", "关");
                    if (gj.equals("开")) {
                        fpjl = (int) SPUtils.get(MainActivity11111.this,"fpjl", 300);//单位是米
                        baojing = (float) SPUtils.get(mActivity,"baojing", 0.5f);//单位海里

//
                        double jd2 = LocationUtils.doLngDegress(fpjl / 1000, sb.getLatitude());
                        double x2 = ((jd2 + sb.getLongitude()) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                        double s2 = Math.abs(x2 - point[0]);
                        Paint paint = new Paint();
                        paint.setColor(0xffFF0000);
                        paint.setAlpha(100);
                        paint.setAntiAlias(true);
                        CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
                        Path path = new Path();

                        path.addCircle((float) (Constant.TimesNeed * point[0]), (float) (Constant.TimesNeed * point[1]), (float) s2, Path.Direction.CW);

                        drawablePath.path = path;
                        drawablePath.paint = paint;
                        yuan.add(drawablePath);
                        tileView.drawPath(drawablePath);

                        if (baojing <= 3.5 || tileView.getDetailLevelManager().getScale() != 512) {
                            double jd3 = LocationUtils.doLngDegress((long) ((1852 * baojing) / 1000), sb.getLatitude());
                            double x3 = ((jd3 + sb.getLongitude()) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                            double s3 = Math.abs(x3 - point[0]);
                            Paint paint1 = new Paint();
                            paint1.setColor(0xffFFBBFF);
                            paint1.setAlpha(100);
                            paint1.setAntiAlias(true);
                            CompositePathView.DrawablePath drawablePath1 = new CompositePathView.DrawablePath();
                            Path path1 = new Path();

                            path1.addCircle((float) (Constant.TimesNeed * point[0]), (float) (Constant.TimesNeed * point[1]), (float) s3, Path.Direction.CW);
                            drawablePath1.path = path1;
                            drawablePath1.paint = paint1;

                            yuan.add(drawablePath1);
                            tileView.drawPath(drawablePath1);
                        }
                        int newWidth = 40;
                        int newHeight = 40;
                        marker.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                    } else {
                        int newWidth = 40;
                        int newHeight = 40;
                        marker.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                        matrix.setScale(0.5f, 0.5f);
                    }
                    Matrix line_matrix = new Matrix();
                    if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != 0) {
                        matrix.setRotate((float) sb.getReal_sudu(), (float) location.getWidth() / 2, (float) location.getHeight() / 2);
                    }

                    // 设置旋转角度
                    Bitmap.Config config = Bitmap.Config.RGB_565;
                    // 重新绘制Bitmap

                    bit = Bitmap.createBitmap(location, 0, 0, location.getWidth(), location.getHeight(), matrix, true);
                    Logger.i(bit.getConfig() + "");
                    marker.setTag(sb);
                    marker.setImageBitmap(bit);
                    tileView.getMarkerLayout().setMarkerTapListener(markerTapListener);
                    ImageView line = new ImageView(this);
                    Bitmap line2 = line_chuanshou;
                    Bitmap line22 = line_chuanshou2;
                    Bitmap line222 = line_chuanshou3;
                    if (sb.getSog() != -1 && sb.getSog() != 1023) {
                        int newWidth = 4;
                        int newHeight = 40;
                        if ((sb.getSog() / 10.0) < 10 && (sb.getSog() / 10.0) > 0) {
                            newWidth = 4;
                            newHeight = 80;
                            line.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                            if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != 0) {

                                line_matrix.setRotate((float) sb.getReal_sudu(), (float) line2.getWidth() / 2, (float) line2.getHeight() / 2);
                            }
                            bit2 = Bitmap.createBitmap(line2, 0, 0, line2.getWidth(), line2.getHeight(), line_matrix, true);

                            line.setImageBitmap(bit2);
                            LineimgList.add(line);
                            tileView.addMarker(line, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], null, -0.5f);
                        } else if ((sb.getSog() / 10.0) < 15 && (sb.getSog() / 10.0) > 10) {
                            newWidth = 4;
                            newHeight = 80;
                            line.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                            line_matrix.setScale(0.5f, 0.5f);
                            line.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                            if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != 0) {

                                line_matrix.setRotate((float) sb.getReal_sudu(), (float) line22.getWidth() / 2, (float) line22.getHeight() / 2);
                            }
                            bit2 = Bitmap.createBitmap(line22, 0, 0, line22.getWidth(), line22.getHeight(), line_matrix, true);

                            line.setImageBitmap(bit2);
                            LineimgList.add(line);
                            tileView.addMarker(line, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], null, -0.5f);
                        } else if ((sb.getSog() / 10.0) >= 15) {
                            newWidth = 4;
                            newHeight = 80;
                            line.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                            if (sb.getReal_sudu() != 511 && sb.getReal_sudu() != 0) {

                                line_matrix.setRotate((float) sb.getReal_sudu(), (float) line222.getWidth() / 2, (float) line222.getHeight() / 2);
                            }
                            bit2 = Bitmap.createBitmap(line222, 0, 0, line222.getWidth(), line222.getHeight(), line_matrix, true);

                            line.setImageBitmap(bit2);
                            LineimgList.add(line);
                            tileView.addMarker(line, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], null, -0.5f);
                        }

                    }

                    imgList.add(marker);
                    if (sb.getMyShip()) {
                        tileView.addMarker2(marker, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], null, -0.5f, 80, 80);
                    } else {
                        tileView.addMarker2(marker, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], null, -0.5f, 60, 60);
                    }


                } else {
                    continue;
                }
            }
        }
    }


    Bitmap b;
    Bitmap line_chuanshou, line_chuanshou2, line_chuanshou3;
    String collect_name = null;

    //singleTask加载模式
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); //这一句必须的，否则Intent无法获得最新的数据
        Intent a = getIntent();
        if (a != null) {
            collect_name = a.getStringExtra("collect_name");

        }
        if (null != collect_name) {
            DBManager dbManager = new DBManager(this);
            CollectPointBean cb = dbManager.getCollect(collect_name);
            if (cb != null) {
                float scale2 = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
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
                wd_d = cb.getLatitude();
                jd_d = cb.getLongitude();
                TileView tile = getTileView();
                ImageView view = new ImageView(this);
                if (cb.getImage() != -1) {
                    view.setImageResource(Constant.hdImage[cb.getImage()]);
                } else {
                    view.setImageResource(Constant.hdImage[0]);
                }
                TextView view1 = new TextView(this);
                view1.setText(cb.getName());
                double[] point = {(jd_d - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(wd_d, 5) - Constant.minY5 * 256)};
                for (View view3 : ls_view_mark) {
                    tileView.getMarkerLayout().removeMarker(view3);
                }
                ls_view_mark.clear();
                ls_view_mark.add(view);
                ls_view_mark.add(view1);
                frameTo(Constant.TimesNeed * point[0], Constant.TimesNeed * point[1]);
                tile.addMarker(view1, (Constant.TimesNeed * point[0]), (Constant.TimesNeed * point[1]), 0.5f, -1f);
                tile.addMarker(view, (Constant.TimesNeed * point[0]), (Constant.TimesNeed * point[1]), 0f, 0f);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        tileView.pause();
    }


    public TileView getTileView() {
        return tileView;
    }

    public void frameTo(final double x, final double y) {
        getTileView().post(new Runnable() {
            @Override
            public void run() {
                getTileView().scrollToAndCenter(x, y);
            }
        });
    }

    public void frameToWithScale(final double x, final double y, final float scale) {
        getTileView().post(new Runnable() {
            @Override
            public void run() {
                getTileView().slideToAndCenterWithScale(x, y, scale);
            }
        });

    }

    public static final double NORTH_WEST_LATITUDE = GetLongLati.getlat(Constant.minY5, 0, 5);//西北经度
    public static final double NORTH_WEST_LONGITUDE = GetLongLati.getLong(Constant.minX5, 0, 5);//西北纬度
    public static final double SOUTH_EAST_LATITUDE = GetLongLati.getlat(16, 0, 5);//东南经度
    public static final double SOUTH_EAST_LONGITUDE = GetLongLati.getLong((Constant.maxX5 + 1), 0, 5);//东南纬度

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
            Logger.d("xxxxxxx", "x" + x + "y" + y);
            // get reference to the TileView
            //得到的tileview参考

            TileView tileView = getTileView();

            float fraction = tileView.getDetailLevelManager().getScale();
            float scale2 = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();

            Logger.d("getScale222" + fraction + "ddf" + tileView.getDetailLevelManager().getCurrentDetailLevel().getScale() + "sg" + tileView.getDetailLevelManager().getDetailLevelForScale().getScale());
            Logger.d("getHeight" + tileView.getHeight());
            Logger.d("getWidth" + tileView.getWidth());
            Logger.d("getBaseHeight" + tileView.getBaseHeight());
            Logger.d("getBaseWidth" + tileView.getBaseWidth());
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
            Logger.d("weidu" + (NORTH_WEST_LATITUDE + (SOUTH_EAST_LATITUDE - NORTH_WEST_LATITUDE) * ((y / (fraction + 0.0)) / (tileView.getBaseHeight() + 0.0))));
            Logger.d("jingdu" + (NORTH_WEST_LONGITUDE + (SOUTH_EAST_LONGITUDE - NORTH_WEST_LONGITUDE) * ((x / (fraction + 0.0)) / (tileView.getBaseWidth() + 0.0))));
            Logger.d("getJingduhah" + (GetLongLati.pixelToLng((x / (fraction / scale2) + Constant.minX5 * (256) * scale2), zoom)) + "y" + (GetLongLati.pixelToLat((y / (fraction / scale2) + (Constant.minY5 * (256) * scale2)), zoom)));
            //我们保存了标记的标签中的坐标
            if (null != view.getTag() && view.getTag() instanceof ShipBean) {
                final ShipBean sb = (ShipBean) view.getTag();
                // lets center the screen to that coordinate
                double[] position = {(sb.getLongitude() - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(sb.getLatitude(), 5) - Constant.minY5 * 256)};
                //让屏幕到该坐标
                tileView.slideToAndCenter((Constant.TimesNeed * position[0]), (Constant.TimesNeed * position[1]));
                // create a simple callout
                dialog = new CustomDialog(MainActivity11111.this);
                if (!String.valueOf(sb.getMMSI()).equals("999999999") || !sb.getMyShip()) {
                    dialog.getTv_mmsi().setText("MMSI:" + sb.getMMSI());

                } else {
                    dialog.getTv_mmsi().setText("MMSI:" + "000000000");

                }

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
                dialog.getSave_chinese_2().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String other = dialog.getTv_chinese_name().getText().toString().trim();
                        if (sb.getChineseName().equals("")) {
                            Toast.makeText(MainActivity11111.this, "暂无英文船名不可修改中文船名", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(other)) {
                            Toast.makeText(MainActivity11111.this, "中文船名不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            if (!sb.getChineseName().equals("")) {
                                String a = p.toPinYin(other);
                                String b = p.toPinYin(sb.getChineseName());
                                if (a.equals(b)) {
                                    DBManager db = new DBManager(MainActivity11111.this);
                                    sb.setChineseNameChange(other);
                                    db.addShipBean(sb);
                                    Toast.makeText(MainActivity11111.this, "保存成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity11111.this, "修改的中文船名必须为同音字", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Exception exp) {
                            Toast.makeText(MainActivity11111.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                if (!sb.getOtherName().equals("")) {
                    dialog.getEt_other_name().setText(sb.getOtherName());
                }
                dialog.getSave_other_name().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String other = dialog.getEt_other_name().getText().toString().trim();
                        if (TextUtils.isEmpty(other)) {
                            Toast.makeText(MainActivity11111.this, "别名不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DBManager db = new DBManager(MainActivity11111.this);
                        sb.setOtherName(other);
                        db.addShipBean(sb);
                        Toast.makeText(MainActivity11111.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setOnPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    };

    //显示标记和路径的点列表
    private ArrayList<double[]> points = new ArrayList<>();

    {
        points.add(new double[]{Constant.TimesNeed * (120.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed * (118.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(23, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed * (116.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(22, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed * (120.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(23.5, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed * (118.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(23.2, 5) - Constant.minY5 * 256)});
        points.add(new double[]{Constant.TimesNeed * (116.0 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(22.3, 5) - Constant.minY5 * 256)});

    }

    List<View> ls_view_mark = new ArrayList<>();
    CollectDialog cdDialog;
    double wd_d;
    double jd_d;
    /*
    用于组缩小放大
     */
    float startScale = -1;

    float endScale = -1;

    /*
    初始化海图
     */
    public void initTileView() {
        final TileView tileView = getTileView();

        tileView.setBackgroundColor(0xFFe7e7e7);
        tileView.addDetailLevel(0.5f, "tiles/map2/5/%d_%d_5.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(1f, "tiles/map2/6/%d_%d_6.png", Constant.wapianWidth, Constant.wapianWidth);


        tileView.addDetailLevel(2f, "tiles/map2/7/%d_%d_7.png", Constant.wapianWidth, Constant.wapianWidth);
        tileView.addDetailLevel(4f, "tiles/map2/8/%d_%d_8.png", Constant.wapianWidth, Constant.wapianWidth);

        tileView.addDetailLevel(8.0000f, "tiles/map2/9/%d_%d_9.png", Constant.wapianWidth, Constant.wapianWidth);
        if (zls != null && zls.size() > 0) {
            for (int i = 0; i < zls.size(); i++) {
                Logger.i("djfjksk" + 8.0000f * (Math.pow(2, (zls.get(i) - 9))));
                tileView.addDetailLevel((float) (8.0000f * (Math.pow(2, (zls.get(i) - 9)))), "tiles/map2/9/%d_%d_9.png", Constant.wapianWidth, Constant.wapianWidth);
            }
        }

        tileView.setBitmapProvider(new BitmapProviderAssets3());
        tileView.setSize(Constant.TimesNeed * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.wapianWidth * (Constant.maxY5 - Constant.minY5 + 1));

        tileView.setMarkerAnchorPoints(-0.5f, -1.0f);

        //获取编程DP指标
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Paint paint = tileView.getDefaultPathPaint();

        double x = 0;
        double y = 0;
        for (double[] point : points) {
            x = x + point[0];
            y = y + point[1];
        }
        int size = points.size();
        x = x / size;
        y = y / size;
        frameTo(x, y);
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
            tileView.setScaleLimits(0, (float) ((8f * (Math.pow(2, (zls.get(zls.size() - 1) - 9))))));
        } else {
            //试验高于1
            tileView.setScaleLimits(0, 8f);


        }

        //启动小，允许变焦
        tileView.setScale(0.5f);

        tileView.addZoomPanListener(new ZoomPanLayout.ZoomPanListener() {
            @Override
            public void onPanBegin(int x, int y, Origination origin) {
                Logger.i("shgsdgB:" + x + "Y:" + y);
            }

            @Override
            public void onPanUpdate(int x, int y, Origination origin) {
                Logger.i("shgsdgU:" + x + "Y:" + y);
            }

            @Override
            public void onPanEnd(int x, int y, Origination origin) {
                Logger.i("shgsdgE:" + x + "Y:" + y);
            }

            @Override
            public void onZoomBegin(float scale, Origination origin) {
                Logger.i("sgsdg" + scale);
                startScale = getTileView().getDetailLevelManager().getScale();
            }

            @Override
            public void onZoomUpdate(float scale, Origination origin) {
                Logger.i("sgsdg2" + scale);
            }

            @Override
            public void onZoomEnd(float scale, Origination origin) {
                float maxScale = 16f;
                if (zls != null && zls.size() > 0) {
                    //试验高于1
                    maxScale = (float) (8f * (Math.pow(2, (zls.get(zls.size() - 1) - 9))));

                } else {
                    //试验高于1
                    maxScale = 8f;

                }
                Logger.i("sgsdg3" + scale);
                endScale = scale;
                if (startScale != -1 && (Constant.TimesNeed * startScale * 2) % 2 == 0) {
                    if ((endScale - startScale) >= 0.5 * startScale && startScale <= maxScale) {
                        float b = 2 * startScale;
                        tileView.setScaleFromCenter(b);
                        double z = Math.log(Constant.TimesNeed * b) / Math.log(2);
                        double s = Constant.gongliMaxBl / Math.pow(2, z);
//                    Logger.i("z" + z + "sgdg" + s + "dgs" + a + "sgss" + b);
//                          blc_gongli=blc_gongli/Math.pow(2,z);
                        double[] point = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                        double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                        double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                        double s2 = Math.abs(x2 - point[0]);
                        Logger.i("sdgskk" + s2);
                        android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                                .getLayoutParams();
                        lp.width = (int) (s2 / 2);
                        cj_lla_blc.setLayoutParams(lp);
                        DecimalFormat df3 = new DecimalFormat("##0.00");
                        tv_gongli.setText("" + df3.format(s) + "海里");
                    } else if ((startScale - endScale) >= (startScale / 2) && startScale >= 1) {
                        float b = startScale / 2;
                        tileView.setScaleFromCenter(b);
                        double z = Math.log(Constant.TimesNeed * b) / Math.log(2);
                        double s = Constant.gongliMaxBl / Math.pow(2, z);
//                    Logger.i("z" + z + "sgdg" + s + "dgs" + a + "sgss" + b);
//                          blc_gongli=blc_gongli/Math.pow(2,z);
                        double[] point = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                        double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                        double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                        double s2 = Math.abs(x2 - point[0]);
                        Logger.i("sdgskk" + s2);
                        android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                                .getLayoutParams();
                        lp.width = (int) (s2 / 2);
                        cj_lla_blc.setLayoutParams(lp);
                        DecimalFormat df3 = new DecimalFormat("##0.00");
                        tv_gongli.setText("" + df3.format(s) + "海里");
                    } else {
                        float b = startScale;
                        tileView.setScaleFromCenter(b);
                        double z = Math.log(Constant.TimesNeed * b) / Math.log(2);
                        double s = Constant.gongliMaxBl / Math.pow(2, z);
//                    Logger.i("z" + z + "sgdg" + s + "dgs" + a + "sgss" + b);
//                          blc_gongli=blc_gongli/Math.pow(2,z);
                        double[] point = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                        double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                        double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                        double s2 = Math.abs(x2 - point[0]);
                        Logger.i("sdgskk" + s2);
                        android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                                .getLayoutParams();
                        lp.width = (int) (s2 / 2);
                        cj_lla_blc.setLayoutParams(lp);
                        DecimalFormat df3 = new DecimalFormat("##0.00");
                        tv_gongli.setText("" + df3.format(s) + "海里");
                    }
                    Logger.i("sgkkkk3" + scale + "sdgsg" + startScale);
                    startScale = -1;
                }

            }
        });
      /*
      比例尺
       */

        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
        tileView.setScaleFromCenter(b);
        double z = Math.log(Constant.TimesNeed * b) / Math.log(2);
        double s = Constant.gongliMaxBl / Math.pow(2, z);

        double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
        double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
        double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
        double s2 = Math.abs(x2 - point3[0]);
        Logger.i("sdgskk" + s2);
        android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                .getLayoutParams();
        lp.width = (int) (s2 / 2);

        cj_lla_blc.setLayoutParams(lp);
        DecimalFormat df3 = new DecimalFormat("##0.00");
        tv_gongli.setText("" + df3.format(s) + "海里");

        //用填充，我们可能是足够快，以创造一个无缝的图像的错觉
        tileView.setViewportPadding(Constant.wapianWidth);

        //我们从资产，应该是相当快速的解码，去渲染ASAP
        tileView.setShouldRenderWhilePanning(true);
        tileView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TileView tileView = getTileView();
                float fraction = tileView.getDetailLevelManager().getScale();
                Logger.d("getScalegjgj" + fraction);
                Logger.d("getPivotX" + tileView.getPivotX() + "   y" + tileView.getPivotY());
                Logger.d("getX" + tileView.getX() + "   y" + tileView.getY());
                Logger.d("getScaleX" + tileView.getOffsetX() + "   y" + tileView.getOffsetY());
                Logger.d("getLeft" + tileView.getRight() + "   y" + tileView.getBottom());
                Logger.d("getScaledHeight  x" + tileView.getScaledHeight() + "   y" + tileView.getScaledWidth());
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
                        int viewWidth = v.getWidth();
                        if (xDown < viewWidth / 3) {
                            //靠左点击
//                            Toast.makeText(RealMapTileViewActivity.this, "靠左点击", Toast.LENGTH_LONG).show();
                        } else if (xDown > viewWidth / 3 && xDown < viewWidth * 2 / 3) {
                            //中间点击
//                            Toast.makeText(RealMapTileViewActivity.this, "中间点击", Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(RealMapTileViewActivity.this, "靠右点击", Toast.LENGTH_LONG).show();
                            //靠右点击
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //当滑动时背景为选中状态 //检测是否长按,在非长按时检测
                    if (!isLongClickModule) {
                        isLongClickModule = isLongPressed(xDown, yDown, event.getX(),
                                event.getY(), event.getDownTime(), event.getEventTime(), 300);
                        if (viewLL != null) {
                            tileView.getMarkerLayout().removeMarker(viewLL);
                            viewLL = null;
                        }
                    }
                    if (isLongClickModule && !isLongClicking) {
                        //处理长按事件
//                       Logger.d("weidu",""+(NORTH_WEST_LATITUDE+(SOUTH_EAST_LATITUDE-NORTH_WEST_LATITUDE)*(((v.getScrollY()+event.getY())/(fraction+0.0))/(tileView.getBaseHeight()+0.0))));
//                       Logger.d("jingdu",""+(NORTH_WEST_LONGITUDE+(SOUTH_EAST_LONGITUDE-NORTH_WEST_LONGITUDE)*(((v.getScrollX()+event.getX())/(fraction+0.0))/(tileView.getBaseWidth()+0.0))));
//                       Toast.makeText(MainActivity.this, "xDown:"+xDown+"yDown:"+yDown+"长按"+"getY:"+event.getY()+"getX:"+event.getX()+"getXReal"+(v.getScrollX()+event.getX())+"getYReal"+(v.getScrollY()+event.getY()), Toast.LENGTH_LONG).show();
                        isLongClicking = true;
//                        final View view = getLayoutInflater().inflate(R.layout.home_map_fav_float, null);
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
                                cdDialog = new CollectDialog(MainActivity11111.this);
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
                                            Toast.makeText(MainActivity11111.this, "航点名不能为空", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        DBManager db = new DBManager(MainActivity11111.this);
                                        CollectPointBean cp = db.getCollect(name);
                                        if (cp != null) {
                                            Toast.makeText(MainActivity11111.this, "航点名已存在，请重新命名", Toast.LENGTH_SHORT).show();

                                            return;
                                        }
                                        int position = cdDialog.getPosition();
                                        CollectPointBean a = new CollectPointBean();
                                        a.setLongitude(jd_d);
                                        a.setLatitude(wd_d);
                                        a.setName(name);
                                        a.setType(0);
                                        a.setImage(position);
                                        Logger.i("sasfa" + position);
                                        db.addCollectPoint(a);
                                        cdDialog.dismiss();
                                        Toast.makeText(MainActivity11111.this, "航点收藏成功", Toast.LENGTH_SHORT).show();
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
                        TextView wd = (TextView) viewLL.findViewById(R.id.tv_wd);
                        TextView jd = (TextView) viewLL.findViewById(R.id.tv_jd);
                        wd_d = (GetLongLati.pixelToLat(((((v.getScrollY() + event.getY()) / (Constant.WpTimes + 0.0)) / (fraction / scale2) + (Constant.minY5 * (256) * Constant.TimesNeed * scale2))), zoom));
                        jd_d = (GetLongLati.pixelToLng(((((v.getScrollX() + event.getX()) / (Constant.WpTimes + 0.0)) / (fraction / scale2) + Constant.minX5 * (256) * Constant.TimesNeed * scale2)), zoom));
                        Logger.i("dskdsgkg" + (v.getScrollY() + event.getY()) + "dsh" + (v.getScrollX() + event.getX()) + "sg" + scale2 + "bb" + zoom + "ffg" + wd_d + "dsg" + jd_d);

                        wd.setText("纬度:" + ConvertUtils.Latitude(wd_d));
                        jd.setText("经度:" + ConvertUtils.Longitude(jd_d));

                        TileView tile = getTileView();
                        MarkerLayout.LayoutParams layout = new MarkerLayout.LayoutParams(40, 100);
                        viewLL.setLayoutParams(layout);
                        Logger.i("sgssgs" + Constant.WpTimes * (GetLongLati.getY(wd_d, 5)) + "sgsg" + (Constant.WpTimes * (GetLongLati.getY(wd_d, 5) - Constant.minY5 * 256)) + "afa" + GetLongLati.getY(wd_d, 5) + "dfhh" + GetLongLati.getY(wd_d, 6) + "dsg" + GetLongLati.getX(jd_d, 5) + "dfsg" + GetLongLati.getY(wd_d, 6) + "asdgs" + GetLongLati.getX(jd_d, 6));
                        int DX = 25;
                        int DY = 11;


                        double poinhx = DecimalCalculate.add(v.getScrollX(), event.getX());
                        double poinhy = DecimalCalculate.add(v.getScrollY(), event.getY());
                        double poinhxx = DecimalCalculate.div(poinhx, scale2);
                        double poinhyy = DecimalCalculate.div(poinhy, scale2);

                        double[] point = {poinhxx, poinhyy};
                        int pointIntX = (int) poinhxx;
                        int pointIntY = (int) poinhyy;
                        double sx = poinhxx - pointIntX;
                        double sy = poinhyy - pointIntY;
                        for (View view3 : ls_view_mark) {
                            tileView.getMarkerLayout().removeMarker(view3);
                        }
                        ls_view_mark.clear();

                        ls_view_mark.add(viewLL);
                        double k = (sx * scale2) / (550 + 0f);
                        double k2 = (sy * scale2) / (180 + 0f);
                        float kh = (float) (-0.5 + k);
                        float khy = (float) (-1 + k2);
                        tile.addMarker(viewLL, point[0], point[1], kh, khy);
                        if (scale2 <= 64) {
                            frameToWithScale(point[0], point[1], scale2);
                        }
                    }
                } else {
                    //其他模式
                }
                return false;

            }


        });
        tileView.setGetScaleChangedListener(new TileView.getScaleChanged() {
            @Override
            public void getScaleChanged2(float scale) {
                for (CompositePathView.DrawablePath sp2 : yuan) {
                    tileView.getCompositePathView().removePath(sp2);
                }
            }
        });
    }

    /*
搜索点击事件
*/

    private EditText et_w_d;
    private EditText et_w_f;
    private EditText et_w_m;
    private EditText et_j_d;
    private EditText et_j_f;
    private EditText et_j_m;

    public void searcher(View view) {

        final SelfDialog dialog2 = new SelfDialog(this);
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
                    Toast.makeText(MainActivity11111.this, "纬度不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(jd)) {
                    Toast.makeText(MainActivity11111.this, "经度不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(wd)) > 90) {
                    Toast.makeText(MainActivity11111.this, "纬度范围在90到-90,请重输", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(jd)) > 180) {
                    Toast.makeText(MainActivity11111.this, "经度范围在180到-180,请重输", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Math.abs(Integer.valueOf(jd)) == 180) {
                    if (!TextUtils.isEmpty(jm) || !TextUtils.isEmpty(jf)) {
                        Toast.makeText(MainActivity11111.this, "经度范围在180到-180,请重输", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (Math.abs(Integer.valueOf(wd)) == 90) {
                    if (!TextUtils.isEmpty(wm) || !TextUtils.isEmpty(wf)) {
                        Toast.makeText(MainActivity11111.this, "纬度范围在90到-90,请重输", Toast.LENGTH_SHORT).show();
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

                frameToWithScale(Constant.TimesNeed * (djd - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(dwd, 5) - Constant.minY5 * 256), 0.5f);
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
                        cdDialog = new CollectDialog(MainActivity11111.this);
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
                                    Toast.makeText(MainActivity11111.this, "航点名不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                DBManager db = new DBManager(MainActivity11111.this);
                                CollectPointBean cp = db.getCollect(name);
                                if (cp != null) {
                                    Toast.makeText(MainActivity11111.this, "航点名已存在，请重新命名", Toast.LENGTH_SHORT).show();

                                    return;
                                }
                                CollectPointBean a = new CollectPointBean();
                                a.setLongitude(jd_d);
                                a.setLatitude(wd_d);
                                a.setName(name);
                                a.setType(0);
                                db.addCollectPoint(a);
                                cdDialog.dismiss();
                                Toast.makeText(MainActivity11111.this, "航点收藏成功", Toast.LENGTH_SHORT).show();
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
                wd2.setText("纬度:" + ConvertUtils.Latitude(dwd));
                jd2.setText("经度:" + ConvertUtils.Longitude(djd));

                TileView tile = getTileView();
                MarkerLayout.LayoutParams layout = new MarkerLayout.LayoutParams(40, 100);
                viewLL.setLayoutParams(layout);
                double[] point = {(djd - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(dwd, 5) - Constant.minY5 * 256)};
                for (View view3 : ls_view_mark) {
                    tileView.getMarkerLayout().removeMarker(view3);
                }
                ls_view_mark.clear();
                ls_view_mark.add(viewLL);
                tile.addMarker(viewLL, Constant.TimesNeed * point[0], Constant.TimesNeed * point[1], -0.5f, -1f);

                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    /*
  测量点击事件
   */
    public void measure(View view) {
        Intent intent = new Intent(MainActivity11111.this, CjActivity.class);
        if (zls != null && zls.size() > 0) {
            intent.putExtra("zList", (Serializable) zls);
        }
        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
        int scrollX = tileView.getScrollX();
        int scrollY = tileView.getScrollY();
//        tileView.slideToAndCenterWithScale(scrollX,scrollY,b);
        intent.putExtra("main_scale", b);
        intent.putExtra("scrollX", scrollX);
        intent.putExtra("scrollY", scrollY);
        startActivity(intent);
    }

    /*
    导航点击事件
     */
    public void satnav(View view) {
        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();
        Intent intent = new Intent(MainActivity11111.this, DhActivity.class);
        if (zls != null && zls.size() > 0) {
            intent.putExtra("zList", (Serializable) zls);
        }
        intent.putExtra("main_scale", b);
        startActivity(intent);
    }

    /*
      收藏点击事件
       */
    public void collect(View view) {
        Intent intent = new Intent(this, CollectActivity.class);
        startActivity(intent);
    }

    /*
    归到本船的点击
     */
    public void toMyShip(View view) {

        DBManager db = new DBManager(this);
        ShipBean sb = db.getMyShip();
        if (null != sb && sb.getLatitude() != -1 && sb.getLongitude() != -1 && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
            double jd = sb.getLongitude();
            double wd = sb.getLatitude();

            frameToWithScale(Constant.TimesNeed * (jd - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.TimesNeed * Constant.WpTimes * (GetLongLati.getY(wd, 5) - Constant.minY5 * 256), tileView.getDetailLevelManager().getScale());
        } else {
            Toast.makeText(this, "本船暂未定位", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    减级别按钮
     */
    public void jianLevel(View view) {
        TileView tileView = getTileView();
        float a = tileView.getScale();
        float b = tileView.getDetailLevelManager().getCurrentDetailLevel().getScale();

        int scrollX = tileView.getScrollX() + tileView.getHalfWidth();
        int scrollY = tileView.getScrollY() + tileView.getHalfHeight();

        Logger.i("saaa" + tileView.getHalfWidth() + "zzz" + tileView.getHalfHeight() + "DSS" + scrollX + "sg" + scrollY + b);

        if (b > 0.5) {
            myAnimation_Scale = new ScaleAnimation(1f, 0.1f, 1f, 0.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            tileView.setAnimation(myAnimation_Scale);
            tileView.setAnimationDuration(800);
            tileView.smoothScaleFromCenter(b / 2);

            double z = Math.log(Constant.TimesNeed * b / 2) / Math.log(2);
            double s = Constant.gongliMaxBl / Math.pow(2, z);

            double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
            double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
            double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
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
            if (b <= 8f * (Math.pow(2, (zls.get(zls.size() - 1)) - 9 - 1))) {
                myAnimation_Scale = new ScaleAnimation(0.5f, 0.8f, 0.5f, 0.8f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tileView.setAnimation(myAnimation_Scale);
                tileView.setAnimationDuration(800);
                tileView.smoothScaleFromCenter(b * 2);
                double z = Math.log(Constant.TimesNeed * b * 2) / Math.log(2);
                double s = Constant.gongliMaxBl / Math.pow(2, z);

                double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
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
                myAnimation_Scale = new ScaleAnimation(0.5f, 0.8f, 0.5f, 0.8f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tileView.setAnimation(myAnimation_Scale);
                tileView.setAnimationDuration(800);
                tileView.smoothScaleFromCenter(b * 2);
                double z = Math.log(Constant.TimesNeed * b * 2) / Math.log(2);
                double s = Constant.gongliMaxBl / Math.pow(2, z);

                double[] point3 = {(120 - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1), Constant.WpTimes * (GetLongLati.getY(24, 5) - Constant.minY5 * 256)};
                double jd2 = LocationUtils.doLngDegress((long) (1852 * Constant.gongliMaxBl / 1000), 24);
                double x2 = ((jd2 + 120) - GetLongLati.getLong(Constant.minX5, 0, 5)) / ((GetLongLati.getLong((Constant.maxX5 + 1), 0, 5) - GetLongLati.getLong(Constant.minX5, 0, 5))) * Constant.wapianWidth * (Constant.maxX5 - Constant.minX5 + 1);
                double s2 = Math.abs(x2 - point3[0]);
                Logger.i("sdgskk" + s2);
                android.view.ViewGroup.LayoutParams lp = cj_lla_blc
                        .getLayoutParams();
                lp.width = (int) (s2 / 2);
                cj_lla_blc.setLayoutParams(lp);
//                tv_gongli.setText("" + s + "海里");
                DecimalFormat df3 = new DecimalFormat("##0.00");
                tv_gongli.setText("" + df3.format(s) + "海里");
//        TileView tileView = getTileView();
//        float a = tileView.getScale();
//        if (a <=8) {
//
//                tileView.setScaleFromCenter(a*2);
//            DBManager db = new DBManager(this);
//            ShipBean sb = db.getMyShip();
//            if (null != sb && sb.getLatitude() != -1 && sb.getLongitude() != -1 && sb.getLatitude() <= 90 && sb.getLongitude() <= 180) {
//                double jd = sb.getLongitude();
//                double wd = sb.getLatitude();
//
//                frameToWithScale((jd - GetLongLati.getLong(25, 0, 5)) / ((GetLongLati.getLong(28, 0, 5) - GetLongLati.getLong(25, 0, 5))) * 256 * 3, GetLongLati.getY(wd, 5) - 11 * 256, a*2);
//
//
//            }else{
//                frameToWithScale((120 - GetLongLati.getLong(25, 0, 5)) / ((GetLongLati.getLong(28, 0, 5) - GetLongLati.getLong(25, 0, 5))) * 256 * 3, GetLongLati.getY(24, 5) - 11 * 256, a*2);
//            }


            }
        }
    }

    LinearLayout cj_lla_blc;
    TextView tv_gongli;
    TextView tv_line_cl;
    private Xml_Operate xml_Operate;
    ShipBean my_ship = null;
    String pathSettingXml;
    String pathDZWL;
    List<Integer> zls = new ArrayList<>();

    /*
  配置获取路径
   */
    public static String getPath() {

        if (ExtenSdCard.getSecondExterPath() != null) {
            if (ExtenSdCard.isSecondSDcardMounted()) {
                return ExtenSdCard.getSecondExterPath();
            }
        }

        return Environment.getExternalStorageDirectory().getPath();
    }

    private static final String DATABASE_PATH = getPath() + "/chartmap/map_database";


    CustomDialog3 warm = null;

    public void initDialogWarming() {
        warm = new CustomDialog3(this);


        //判断设置的模式

        warm.setOnPositiveListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                warm.dismiss();
            }
        });

        warm.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initDialogWarming();
        mActivity = this;
     /*   OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());


        sp = getSharedPreferences("sp", MODE_PRIVATE);
        File dir3 = new File(Environment.getExternalStorageDirectory().getPath() + "/chartmap");
        if (!dir3.exists())
            dir3.mkdir();
        File dir2 = new File(DATABASE_PATH);
        if (!dir2.exists())
            dir2.mkdir();*/

//        File dir = new File(DATABASE_PATH);
//
//        if (!dir.exists())
//            dir.mkdir();
        /*MapDataBaseXmlBean mbx = null;
        String pathDataBaseXml = DATABASE_PATH + "/" + "map_setting.xml";
        if (mbx == null) {
            mbx = MapDataBaseXmlUtil.readMapDataBase(pathDataBaseXml);
            if (mbx != null) {
                BitmapProviderAssets3.mdbx = mbx;
                Logger.i("sdgs" + mbx.toString());
            }
        }*/


//        sceneMap = (MyMap) findViewById(R.id.sceneMap);
        tileView = (TileView) findViewById(R.id.tile_view);
        tileView.setSaveEnabled(true);
        cj_lla_blc = (LinearLayout) findViewById(R.id.cj_lla_blc);
        tv_gongli = (TextView) findViewById(R.id.tv_gongli);
        tv_line_cl = (TextView) findViewById(R.id.tv_line_cl);
        dzwl = (ImageView) findViewById(R.id.main_dzwl);

         /*
       比例尺
         *//*
        if (mbx != null && mbx.isSign()) {
//            zls = HaiTuSqliteDb.getZoomList();
            for (int i = 0; i < (mbx.getLevelMax() - mbx.getLevelMin() + 1); i++) {
                zls.add(mbx.getLevelMin() + i);
            }
        }
        if (zls != null && zls.size() > 0) {
            Logger.i("hskdsize" + zls.size());
            for (int i = 0; i < zls.size(); i++) {
                Logger.i("djsjkjkg" + i + "dsgs" + zls.get(i));
            }
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        options.inSampleSize = 1;
//        b = BitmapFactory
//                .decodeResource(getResources(), R.drawable.test);
        line_chuanshou = readBitmap(MainActivity.this, R.drawable.line_chuanshou);
        line_chuanshou2 = readBitmap(MainActivity.this, R.drawable.line_chuanshou2);
        line_chuanshou3 = readBitmap(MainActivity.this, R.drawable.line_chuanshou3);
//        line_chuanshou = BitmapFactory
//                    .decodeResource(getResources(), R.drawable.line_chuanshou, options);
//            line_chuanshou2 = BitmapFactory
//                    .decodeResource(getResources(), R.drawable.line_chuanshou2, options);
//        line_chuanshou3 = BitmapFactory
//                .decodeResource(getResources(), R.drawable.line_chuanshou3,options);

//
        ship_a = readBitmap(MainActivity.this, R.drawable.aleichuan);
        ship_b = readBitmap(MainActivity.this, R.drawable.bleichuan);
        yuan_fen = readBitmap(MainActivity.this, R.drawable.yuan_fenhong);
        yuan_lan = readBitmap(MainActivity.this, R.drawable.yuan_lan);
        yuan_hong = readBitmap(MainActivity.this, R.drawable.yuan_hongse);
*/
        initTileView();
        initTitle();
        ///////////////////////////////////////////////////////////
//        new TimeThread().start();
        //////////////////////////////////////////////////////////
        boolean isUse = ServiceIsUser.isServiceWork(this, "com.yisipu.chartmap.servicer.MyDataServicer");
     /*   if (!isUse) {
            Intent service = new Intent(MainActivity.this, MyDataServicer.class);
            this.startService(service);
        }*/

//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("sfdw", 1);
        SPUtils.putAndApply(this,"sfdw",1);
//        editor.putBoolean("close_service", false);
        SPUtils.putAndApply(this,"close_service", false);
        SPUtils.putAndApply(this,"fuwu", 1);
        SPUtils.putAndApply(this,"sg", "开");
        SPUtils.putAndApply(this,"fpyp", "开");

//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("fuwu", 1);//keyname是储存数据的键值名，同一个对象可以保存多个键值
//        editor.putString("sg", "开");
//        editor.putString("fpyp", "开");
//        editor.commit();//提交保存修改

//        SharedPreferences.Editor editor = sp.edit();//获取编辑对象

        Intent intent = new Intent();
        intent.setAction(Constant.KAIJIQIDONG);
        MainActivity11111.this.sendBroadcast(intent);

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        width = dm.widthPixels;
        height = dm.heightPixels;
    /*    menuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
        menuElementsList = (ListView) findViewById(R.id.menu_elements);

        ll2 = (LinearLayout) findViewById(R.id.lla2);
        ll3 = (LinearLayout) findViewById(R.id.lla3);
        ll4 = (LinearLayout) findViewById(R.id.lla4);
        ll5 = (LinearLayout) findViewById(R.id.lla5);
        // 设置阴影
        menuLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);


//        int sfdw = sp.getInt("sfdw", 1);

        menuNameList = new ArrayList<String>();
        menuNameList.add("菜单");
        menuNameList.add("AIS列表");
        menuNameList.add("定位");
        menuNameList.add("罗盘");
        menuNameList.add("设置");
        menuNameList.add("退出");
        slideMenuAdapter = new SlideMenuAdapter(this, menuNameList);
        menuElementsList.setAdapter(slideMenuAdapter);
        /////////////////////////////////////////////////////////////
   //     menuElementsList.setOnItemClickListener(new DrawerItemClickListener());

     *//*    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(420, 280);//导入上面所述的包
       layoutParams.gravity = Gravity.RIGHT;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundResource(R.drawable.corners_bg);*//*
      *//*  tv1.setTextSize(18);
        tv2.setTextSize(18);
        tv3.setTextSize(18);
        tv4.setTextSize(18);*//*
        menuToggle = new ActionBarDrawerToggle(this, menuLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // 调用 onPrepareOptionsMenu()
            }
        };*/
        ///////////////////////////////////////////
//        menuLayout.setDrawerListener(menuToggle);

        //////////////////////////////////////////////////
     //   linearLayout.setOnTouchListener(new onDoubleClick());

       /* StringBuilder log = new StringBuilder();
        String inPath = getInnerSDCardPath();
        *//*
        配置文件路径
         *//*
        pathSettingXml = inPath + "/chartmap/" + "info.xml";
        log.append("内置SD卡路径：" + inPath + "\r\n");
        File a = new File(pathSettingXml);
        if (a.exists()) {
            xml_Operate = new Xml_Operate(MainActivity.this, pathSettingXml);
            xml_Operate.ReadXml();
            my_ship = xml_Operate.getMy_ship();
            String sosPhone = xml_Operate.getSOSMobile();
//            Logger.i("sosPhone"+sosPhone+"dsdf"+my_ship.toString());
            if (sosPhone != null && !TextUtils.isEmpty(sosPhone)) {
                SharedPreferences sp2 = getSharedPreferences("Phone", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp2.edit();
                editor1.putString("sosphone", sosPhone);
                editor1.commit();
            }
            String sign = xml_Operate.getSign();
//            if (my_ship != null) {
//                Toast.makeText(this, "sosPhone"+sosPhone+"dsd"+sign + "dd" + my_ship.toString(), Toast.LENGTH_SHORT).show();
//            }
            if (null != my_ship && sign != null && sign.equals("1")) {
                *//*SharedPreferences.Editor editor1 = sp.edit();

                editor1.putInt("isAisSetting", 1);
                editor1.commit();*//*
//                正在发ais设置指令
                SPUtils.putAndApply(mActivity,"isAisSetting", 1);
                 *//*
//        先发送复位的消息
//         *//*
                String mmsi_cmd1 = "$PAIS,013,,,[[KanDLe]]*10\r\n";
                Logger.i("dsgdsg:" + mmsi_cmd1);
                UART1Tx(mmsi_cmd1);
//                Logger.i("dsgsgsgsgg");
            } else {
                SharedPreferences sp2 = getSharedPreferences("Phone", MODE_PRIVATE);
//                String phone = sp2.getString("sosphone", "0591968195");
                String phone = (String) SPUtils.get(mActivity,"sosphone", "0591968195");

//                SharedPreferences.Editor editor1 = sp.edit();

             *//*
                editor1.putInt("isAisSetting", 0);
                editor1.commit();*//*
//                正在发ais设置指令
                SPUtils.putAndApply(mActivity,"isAisSetting", 0);
                DBManager db = new DBManager(this);
                ShipBean my_ship3 = db.getMyShip();
                if (my_ship3 != null) {
                    String b = xml_Operate.WriteXml(my_ship3, "0", phone);
                    xml_Operate.Write(b, pathSettingXml);
                }
            }*/
        }

    ////////////////////////////////////////////////////
        /* else {
            xml_Operate = new Xml_Operate(MainActivity.this, pathSettingXml);
            SharedPreferences sp2 = getSharedPreferences("Phone", MODE_PRIVATE);
            String phone = sp2.getString("sosphone", "0591968195");
            SharedPreferences.Editor editor1 = sp.edit();

                *//*
                正在发ais设置指令
                 *//*
            editor1.putInt("isAisSetting", 0);
            editor1.commit();
            DBManager db = new DBManager(this);
            ShipBean my_ship3 = db.getMyShip();
            if (my_ship3 != null) {
                String b = xml_Operate.WriteXml(my_ship3, "0", phone);
                xml_Operate.Write(b, pathSettingXml);
            }
        }
        Logger.i(log.toString() + "DGGF" + pathSettingXml);*/

        /*
       电子围栏 配置文件路径
         */

        /*pathDZWL = inPath + "/chartmap/" + "dzwl.txt";
        getDianZiWeiLan();

        hangXianPath = inPath + "/chartmap/" + "hangxian.xml";
        hangDianPath = inPath + "/chartmap/" + "hangdian.xml";
        *//*
        获得航点航线数据
         *//*
        getHangDianHangXian();


        Logger.i("zzsgdfh" + Environment.getExternalStorageDirectory().getPath() + "exter" + ExtenSdCard.getSecondExterPath());
        if (ExtenSdCard.getSecondExterPath() != null) {
            Logger.i("dsg" + ExtenSdCard.isSecondSDcardMounted());
        }
    }*/


    String hangXianPath;
    String hangDianPath;
    String gpsPath;
    String aisPath;
    String aisPath2;

    @Override
    protected void onStart() {
        super.onStart();
      /*  //开启位置监听
        AlxLocationManager.onCreateGPS(getApplication());

        final Handler handler = new Handler();
        //每隔2s更新一下经纬度结果
        new Timer().scheduleAtFixedRate(new TimerTask() {//每秒钟检查一下当前位置
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Logger.init("ZERO").reset();
                        Logger.i(String.valueOf(MyLocation.getInstance().latitude));
                        Logger.i(String.valueOf(MyLocation.getInstance().longitude));
                        Logger.i(String.valueOf(MyLocation.getInstance().accuracy));
                        if(MyLocation.getInstance().updateTime != 0)Logger.i(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(MyLocation.getInstance().updateTime)));
                    }
                });
            }
        },0,5000);*/
    }



    /*
        获得航点航线
         */
    public void getHangDianHangXian() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 /*
        航线
         */
                File file3 = new File(hangXianPath);
                if (file3.exists()) {
                    List<CollectPointBean> ls = PersonService.readHangxian(hangXianPath);
                    String sign = PersonService.getHangxianSign();
                    if (sign != null && !TextUtils.isEmpty(sign)) {
                        if (sign.equals("1")) {
                            if (ls != null && ls.size() > 0) {
                                DBManager db = new DBManager(MainActivity11111.this);
                                db.deleteAllCourseCollectBean();
                                for (CollectPointBean cpb : ls) {
                                    if (cpb.getType() == 1) {
                                        db.addCollectPoint(cpb);
                                    }
                                }
                                DBManager db2 = new DBManager(MainActivity11111.this);

                                List<String> ls2 = db2.getAllCourseName();
                                if (ls2 != null && ls2.size() > 0) {
                                    PersonService.writeHangxian(MainActivity11111.this, hangXianPath, ls2, "0");
                                }

                            }
                        } else {
                            DBManager db = new DBManager(MainActivity11111.this);

                            List<String> ls2 = db.getAllCourseName();
                            if (ls2 != null && ls2.size() > 0) {
                                PersonService.writeHangxian(MainActivity11111.this, hangXianPath, ls2, "0");
                            }
                        }
                    }

                } else {
                    DBManager db = new DBManager(MainActivity11111.this);

                    List<String> ls = db.getAllCourseName();
                    if (ls != null && ls.size() > 0) {
                        PersonService.writeHangxian(MainActivity11111.this, hangXianPath, ls, "0");
                    }
                }
        /*
        航点
         */
                File file4 = new File(hangDianPath);
                if (file4.exists()) {
                    List<CollectPointBean> ls = PersonService.readHangDian(hangDianPath);
                    String sign = PersonService.getHangdianSign();
                    if (sign != null && !TextUtils.isEmpty(sign)) {
                        if (sign.equals("1")) {
                            if (ls != null && ls.size() > 0) {
                                DBManager db = new DBManager(MainActivity11111.this);
                                db.deleteAllCollectBean();
                                for (CollectPointBean cpb : ls) {
                                    if (cpb.getType() == 0) {
                                        db.addCollectPoint(cpb);
                                    }
                                }

                            }
                        } else {
                            DBManager db = new DBManager(MainActivity11111.this);

                            List<CollectPointBean> ls2 = db.getCollects();
                            if (ls2 != null && ls2.size() > 0) {
                                PersonService.writeHangdian(hangDianPath, ls2, "0");
                            }
                        }
                    }
                } else {
                    DBManager db = new DBManager(MainActivity11111.this);

                    List<CollectPointBean> ls2 = db.getCollects();
                    if (ls2 != null && ls2.size() > 0) {
                        PersonService.writeHangdian(hangDianPath, ls2, "0");
                    }
                }
            }
        }).start();
    }

    public void getDianZiWeiLan() {
        /*
       获取电子围栏数据
        */
        new Thread(new Runnable() {
            @Override
            public void run() {
                File a = new File(pathDZWL);
                String str = null;
                if (!a.exists()) {
                    str = TxtFileUtile.getAssertTxtFile(MainActivity11111.this, "dzwl.txt");
                } else {
                    str = TxtFileUtile.getTxtFile(pathDZWL);
                    Logger.i("dsjsfff" + str);
                }
                if (str != null && !TextUtils.isEmpty(str)) {
                    String[] z = str.split(":");
                    Logger.i("dsjs" + str);
                    SharePrefenerArrary.setSharedPreference(MainActivity11111.this, "txt_dzwl", z);
                }
            }
        }).start();

    }

    /*
   校验和
    */
    private String getXORCheck(char[] b) {

        char x = 0;
        for (int i = 0; i < b.length; i++)
            x ^= b[i];

        return String.format("%x", (int) x);
    }
/*
    @Override
    void receiver(String str) {
        if (null != my_ship) {
            if (str.contains("$DUAIR,1,013*")) {
                String a2 = "PAIS,010,,," + my_ship.getMMSI();
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

                String mmsi_cmd = "$PAIS,010,,," + my_ship.getMMSI() + "*" + c + "\r\n";
                Logger.i("dsgdsg:" + mmsi_cmd);
                UART1Tx(mmsi_cmd);
            } else if (str.contains("$DUAIR,1,010*")) {
*//*
修改船舶属性
 *//*
//        String a3="AISSD,A--A,B--B,C,D,E,F,G,H*hh"+mmsi;
                String huhao = my_ship.getHuhao();
                if (my_ship.getHuhao().length() < 7) {
                    for (int i = 0; i < 7 - my_ship.getHuhao().length(); i++) {
                        huhao += " ";
                    }
                }
                String name = my_ship.getEnglishName();
                if (name.length() < 20) {
                    for (int i = 0; i < 20 - name.length(); i++) {
                        name += " ";
                    }
                }
                String A = "" + my_ship.getDimBow();
                if (A.length() < 3) {
                    for (int i = 0; i < 3 - A.length(); i++) {
                        A = "0" + A;
                    }
                }
                String B = "" + my_ship.getDimStern();
                if (B.length() < 3) {
                    for (int i = 0; i < 3 - B.length(); i++) {
                        B = "0" + B;
                    }
                }
                String C = "" + my_ship.getDimPort();
                if (C.length() < 2) {
                    for (int i = 0; i < 2 - C.length(); i++) {
                        C = "0" + C;
                    }
                }
                String D = "" + my_ship.getDimStarboard();
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
 *//*
        配置航行静态数据
        *//*
                String type = "" + my_ship.getType();
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
                if (my_ship != null) {
                    SharedPreferences sp2 = getSharedPreferences("Phone", MODE_PRIVATE);
                    Logger.i("sgsgzzzz" + my_ship.getOtherName() + "sgs");
                    String phone = sp2.getString("sosphone", "0591968195");
                    String a = xml_Operate.WriteXml(my_ship, "0", phone);
                    DBManager db = new DBManager(MainActivity.this);
                    my_ship.setMyShip(true);
                    db.addShipBean(my_ship);
                    xml_Operate.Write(a, pathSettingXml);
                }
//            Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
            } else if (str.contains("$DUAIR,0,013*")) {
//            Toast.makeText(MainActivity.this, "设置MMSI失败", Toast.LENGTH_SHORT).show();
            } else if (str.contains("$DUAIR,0,SSD*")) {
//            Toast.makeText(MainActivity.this, "设置SSD失败", Toast.LENGTH_SHORT).show();
            } else if (str.contains("$DUAIR,0,VSD*")) {
//            Toast.makeText(MainActivity.this, "设置VSD失败", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

   /* @Override
    void gpsreceiver(Gpsbean gpsbean) {
        if (gpsbean != null) {
            String jingdu = convertUtils.Longitude(gpsbean.getGpsLongitude());
            String weidu = convertUtils.Latitude(gpsbean.getGpsLatitude());
            DBManager dbManager = new DBManager(this);
            if (dbManager != null) {
                int hs = dbManager.getMyShip().getSog();
                int hx = dbManager.getMyShip().getCog();
                if (!(hx >= 3600) && hx != -1) {
                    tv4.setText("航向: " + hx / (10.0) + "°");
                }
                DecimalFormat df = new DecimalFormat("##0.0");
                if (hs != 1023 && hs != -1) {
                    tv3.setText("航速: " + df.format(hs / (10.0)) + " Kn");
                }
            }
            tv1.setText("经度：" + jingdu);
            tv2.setText("纬度：" + weidu);
        }

//        sp = getSharedPreferences("sp", MODE_PRIVATE);
//        int sfdw = sp.getInt("sfdw", 1);
     *//*   if (sfdw == 1) {
            dw.setText("未定位");
            dw.setTextColor(Color.RED);
        } else if (sfdw == 2) {
            dw.setText("已定位");
            dw.setTextColor(Color.GREEN);
        }*//*
    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }

*/
    /*
       停止三秒传一次
        */
/*    public void stopPush() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }*/
/*

    */
/*
    开始
     *//*

    public void startPush() {
        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    initMap();
                    Logger.i("aaaaaa1kr21212苛刻4");
                    handler.postDelayed(this, 15000);
                }
            };
            handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.
        }
    }
*/

/*    @Override
    protected void onStop() {
        stopPush();
        super.onStop();
    }*/

    @Override
    protected void onResume() {
        BitmapFactory.Options options = new BitmapFactory.Options();
//
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        options.inSampleSize = 2;

      /*  if (ship_a == null) {
            ship_a = readBitmap(MainActivity.this, R.drawable.aleichuan);
        }
        if (ship_b == null) {
            ship_b = readBitmap(MainActivity.this, R.drawable.bleichuan);
        }*/
        super.onResume();
        tileView.resume();
//        startPush();
    }

 /*   @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }
*/

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tileView.destroy();
        tileView = null;

        AlxLocationManager.stopGPS();
    }

    // 点击返回 回到home界面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);

            home.addCategory(Intent.CATEGORY_HOME);

            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

 /*   @Override
    public void onBackPress(View view) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);

        startActivity(home);
    }*/
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 打开或关闭drawer
        if (menuToggle.onOptionsItemSelected(item)) {
            return true;
        }

//		switch (item.getItemId()) {
////		case R.id.action_search:
////			Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();
////			return true;
//		default:
        return super.onOptionsItemSelected(item);
//		}
    }
*/

/*
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        if (position != 0) {
            menuLayout.closeDrawer(menuElementsList);
        }
        if (position == 1) {
            Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
            intent.putExtra("extra", "1");
            startActivity(intent);
        }
        if (position == 2) {
            Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
            intent.putExtra("extra", "2");
            startActivity(intent);
        }
        if (position == 3) {
            Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
            intent.putExtra("extra", "3");
            startActivity(intent);
        }
        if (position == 4) {
            Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
            intent.putExtra("extra", "4");
            startActivity(intent);
        }
        if (position == 5) {
            System.exit(0);//正常退出App
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        activityTitle = title;
        getActionBar().setTitle(activityTitle);
    }

    */
/**
     * 点击菜单按钮，打开或关闭menu
     *//*


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        menuToggle.syncState();

    }

    //双击事件
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        menuToggle.onConfigurationChanged(newConfig);
    }


    class onDoubleClick implements View.OnTouchListener {
        // 计算点击的次数
        private int count = 0;
        // 第一次点击的时间 long型
        private long firstClick = 0;
        // 最后一次点击的时间
        private long lastClick = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                if (firstClick != 0 && System.currentTimeMillis() - firstClick > 500) {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                } else if (count == 2) {
                    lastClick = System.currentTimeMillis();
                    // 两次点击小于500ms 也就是连续点击
                    if (lastClick - firstClick < 500) {
                        if (x % 2 == 1) {
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);//导入上面所述的包
                            linearLayout.setLayoutParams(layoutParams);
                            layoutParams.gravity = Gravity.CENTER;
                            linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            x++;
                            tv1.setTextSize(37);
                            tv2.setTextSize(37);
                            tv3.setTextSize(37);
                            tv4.setTextSize(37);
                            dw.setTextSize(37);
                            nl.setTextSize(37);
                            sj.setTextSize(37);
                            dw.setVisibility(View.VISIBLE);
                            nl.setVisibility(View.VISIBLE);
                            sj.setVisibility(View.VISIBLE);
                            tv_gongli.setVisibility(View.GONE);
                            cj_lla_blc.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);
                            ll3.setVisibility(View.GONE);
                            ll4.setVisibility(View.GONE);
                            ll5.setVisibility(View.GONE);
                        } else {
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(420, 280);//导入上面所述的包
                            layoutParams.gravity = Gravity.RIGHT;
                            linearLayout.setLayoutParams(layoutParams);
                            linearLayout.setBackgroundResource(R.drawable.corners_bg);
//                            linearLayout.setBackgroundColor(Color.parseColor("#3fcebcbc"));
                            tv1.setTextSize(18);
                            tv2.setTextSize(18);
                            tv3.setTextSize(18);
                            tv4.setTextSize(18);
                            dw.setVisibility(View.GONE);
                            nl.setVisibility(View.GONE);
                            sj.setVisibility(View.GONE);
                            tv_gongli.setVisibility(View.VISIBLE);
                            cj_lla_blc.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.VISIBLE);
                            ll3.setVisibility(View.VISIBLE);
                            ll4.setVisibility(View.VISIBLE);
                            ll5.setVisibility(View.VISIBLE);
                            x++;
                        }
//                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(100,100));
//                        Toast.makeText(MainActivity.this, "双击了" + width + height, Toast.LENGTH_SHORT).show();
                    }
                    clear();
                }
            }

            return false;
        }


        // 清空状态

        private void clear() {
            count = 0;
            firstClick = 0;
            lastClick = 0;
        }
    }
*/
   /* *//**
     * 坐标位置监听
     *//*
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

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(25000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler2.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    static long TemTime = -1;
    *//*
    定位时间
     *//*
    static long locationTime = -1;
    *//*
    时间差
     *//*
    static long timeSub = 0;

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }*/
/*
    //在主线程里面处理消息并更新UI界面
    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int sfdw = sp.getInt("sfdw", 1);
                    if (sfdw == 1) {
                        TemTime = System.currentTimeMillis();
                        if ((TemTime - locationTime - timeSub) > 1 * 60 * 1000) {
                            dw.setText("未定位");
                            dw.setTextColor(Color.RED);
//                               locationTime=-1;

                            long sysTime = System.currentTimeMillis();
                            CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);
                            sj.setText("时间：" + sysTimeStr); //更新时间
                            Logger.i(sysTimeStr + "");
                        }
                    } else if (sfdw == 2) {
                        TemTime = System.currentTimeMillis();

                        dw.setText("已定位");
                        dw.setTextColor(Color.GREEN);
                        sp = getSharedPreferences("sp", MODE_PRIVATE);
                        String time = sp.getString("gpsTime", "");
                        if (time != null && !TextUtils.isEmpty(time)) {

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date date = null;
                            try {
                                date = sdf.parse(time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar ca = Calendar.getInstance();
                            ca.setTime(date);
                            ca.add(Calendar.HOUR_OF_DAY, 8);

                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                            String sysTimeStr = sdf2.format(ca.getTime());
                            locationTime = dateToLong(ca.getTime());
                            timeSub = TemTime - locationTime;
                            sj.setText("时间：" + sysTimeStr); //更新时间
                            Logger.i(sysTimeStr + "");
                        } else {
                            locationTime = -1;
                        }
                    }

                    Calendar calendar = Calendar.getInstance();
                    Lunar lunar = new Lunar(calendar);
                    nl.setText("农历：" + lunar.toString());
                    sp = getSharedPreferences("sp", MODE_PRIVATE);
                    String dl = sp.getString("kg", "关");
                    if (dl.equals("开")) {
                        dzwl.setImageResource(R.drawable.dzwl2);
                    } else {
                        dzwl.setImageResource(R.drawable.dzwl1);
                    }
                    String mos = sp.getString("mode", "标准模式");
                    if (mos.equals("标准模式")) {
                        ms.setImageResource(R.drawable.bzms);
                    } else {
                        ms.setImageResource(R.drawable.znms);
                    }
                    break;
                default:
                    break;

            }
        }
    };

    private void juDgeState(final int flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case 1:
                        dw.setText("未定位");
                        dw.setTextColor(Color.RED);
                        break;
                    case 2:
                        dw.setText("定位");
//                        pdoptv.setText("位置精度：" + pdop);
                        dw.setTextColor(Color.GREEN);
                        break;
                }
            }
        });
    }*/
}
