package com.yisipu.chartmap.servicer;


import android.Manifest;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UEventObserver;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.fjhtxl.sdk.WorkService;
import com.fjhtxl.sdk.listener.IMultipleListener;
import com.fjhtxl.sdk.net.GmsConfig;
import com.fjhtxl.sdk.protocol.ConstCmd;
import com.fjhtxl.sdk.protocol.entity.CmdParam;
import com.fjhtxl.sdk.protocol.entity.DevInfo;
import com.fjhtxl.sdk.protocol.entity.GenralReply;
import com.fjhtxl.sdk.protocol.entity.GpsInfo;
import com.fjhtxl.sdk.protocol.entity.MsgInfo;
import com.fjhtxl.sdk.protocol.entity.ReplyPosReq;
import com.fjhtxl.sdk.protocol.entity.TextMsg;
import com.fjhtxl.sdk.task.SendObjTask;
import com.fjhtxl.sdk.util.ConfigUtil;
import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.MyApplication;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.ViewPagerActivity;
import com.yisipu.chartmap.bean.FilePathBean;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.SatelliteBean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.fragment.FragmentAisLctivity;
import com.yisipu.chartmap.fragment.FragmentGPS;
import com.yisipu.chartmap.utils.ExtenSdCard;
import com.yisipu.chartmap.utils.ExtendSdCardXml;
import com.yisipu.chartmap.utils.LangUtils;
import com.yisipu.chartmap.utils.LocationUtils;
import com.yisipu.chartmap.utils.ScopeUtils;
import com.yisipu.chartmap.utils.SharePrefenerArrary;
import com.yisipu.chartmap.utils.TxtFileUtile;
import com.yisipu.serialport.SerialPort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage18;
import dk.dma.ais.message.AisMessage19;
import dk.dma.ais.message.AisMessage24;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessage8;
import dk.dma.ais.sentence.Vdm;


/**
 * Created by Administrator on 2016/8/9.
 */
public class MyDataServicer extends Service {
    private String phone;
    private AisMessage message4;
    private DBManager db;
    private SharedPreferences sp2;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;
    private boolean bl = false;
    private boolean bl2 = false;
    private LocationUtils locationUtils;
    private Runnable sendable;
    private Matcher matcher;
    Thread gpsThread;
    int index2 = 1;
    boolean lppd = false;
    int x = 0;
    int x2 = 0;
    private Pattern pattern;
    public static final int STATE_WATER = 0;
    public static final int STATE_NORMAL = 1;
    private int mCurrentState = STATE_NORMAL;
    private static final String STATE_PATCH = "/sys/class/switch/water_det_switch/state";
    private int lsflg = 1;
    private static final String BJ = "/sys/bus/i2c/devices/3-0053/flash_feature";
    private static Vibrator vibrator = null;
    private String lpfwkg;
    private String gjts;
    private Float fw;
    private static Float Temfw = -1f;
    int zdbs = 1;
    int zdbs2 = 1;
    private static MediaPlayer mPlayer = null;
    private static boolean isRelease = true;   //判断是否MediaPlayer是否释放的标志
    int y = 0;

    /*
    取消报警提示
     */
    public static void cancelBaojing(Context context) {
        isRelease = true;
        setNodeString(BJ, "0");
        if (vibrator != null) {
//           vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (FragmentAisLctivity.BaoJingLs != null && FragmentAisLctivity.BaoJingLs.size() > 0) {
            FragmentAisLctivity.BaoJingLs.clear();

        }
        FragmentAisLctivity.isRunningBaojing = 0;

    }

    //
    public void call() {
        Toast toast = null;
        phone = sp.getString("sosphone", "0591968195");
        // 取得输入的电话号码串
        if (phone != null && phone != "") {
            Intent phoneIntent = new Intent(
                    "android.intent.action.CALL", Uri.parse("tel:"
                    + phone));
            phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 启动
            startActivity(phoneIntent);
            //自定义Toast
//            toastCommom.ToastShow(MainActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "紧急警报");
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.toast_xml,
                    null);
            ImageView imageView = (ImageView) layout.findViewById(R.id.sos_iv);
            imageView.setVisibility(View.VISIBLE);
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);


            toast.setView(layout);
            toast.show();
        }


    }

    //落水和sos不用判断条件
    public void bjgs2() {
//        sp2 = getSharedPreferences("sp", MODE_PRIVATE);
//        String gaoj = sp2.getString("gjts", "开");
//        if (gaoj.equals("开")) {
//            String shanguang = sp2.getString("sg", "开");
//            String zhendong = sp2.getString("zd", "开");
//            String yinpin = sp2.getString("yp", "开");
//            if (shanguang.equals("开")) {

        setNodeString(BJ, "1");

//                setNodeString(BJ, "9");
//            }
//            if (zhendong.equals("开")) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                long [] pattern = {300,1000,300,1000}; // 停止 开启 停止 开启
        vibrator.vibrate(new long[]{1000, 2000, 1000, 2000, 1000}, 0);
//                vibrator.vibrate(pattern,2); //重复两次上面的pattern 如果只想震动一次，index设为-1
//            }
//            if (yinpin.equals("开")) {
        if (isRelease) {
            mPlayer = MediaPlayer.create(this, R.raw.bjyp);
            isRelease = false;
        }
        mPlayer.start();   //开始播放
        mPlayer.setLooping(true);
//            }
//        }
    }

    //告警
    public void bjgs() {
        sp2 = getSharedPreferences("sp", MODE_PRIVATE);
        String gaoj = sp2.getString("gjts", "开");
        if (gaoj.equals("开")) {
            String shanguang = sp2.getString("sg", "开");
            String zhendong = sp2.getString("zd", "开");
            String yinpin = sp2.getString("yp", "开");
            if (shanguang.equals("开")) {

                setNodeString(BJ, "1");

//                setNodeString(BJ, "9");
            }
            if (zhendong.equals("开")) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                long [] pattern = {300,1000,300,1000}; // 停止 开启 停止 开启
                vibrator.vibrate(new long[]{1000, 2000, 1000, 2000, 1000}, 0);
//                vibrator.vibrate(pattern,2); //重复两次上面的pattern 如果只想震动一次，index设为-1
            }
            if (yinpin.equals("开")) {
                if (isRelease) {
                    mPlayer = MediaPlayer.create(this, R.raw.bjyp);
                    isRelease = false;
                }
                mPlayer.start();   //开始播放
                mPlayer.setLooping(true);
            }
        }
    }

    /*
    防碰告警
     */
    public void fpbjgs() {
        sp2 = getSharedPreferences("sp", MODE_PRIVATE);
        String gaoj = sp2.getString("gjts", "开");
        if (gaoj.equals("开")) {
            String shanguang = sp2.getString("fpsg", "开");
            String zhendong = sp2.getString("fpzd", "开");
            String yinpin = sp2.getString("fpyp", "开");
            if (shanguang.equals("开")) {

                setNodeString(BJ, "1");

//                setNodeString(BJ, "9");
            }
            if (zhendong.equals("开")) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                long [] pattern = {300,1000,300,1000}; // 停止 开启 停止 开启
                vibrator.vibrate(new long[]{1000, 2000, 1000, 2000, 1000}, 0);
//                vibrator.vibrate(pattern,2); //重复两次上面的pattern 如果只想震动一次，index设为-1
            }
            if (yinpin.equals("开")) {
                if (isRelease) {
                    mPlayer = MediaPlayer.create(this, R.raw.bjyp);
                    isRelease = false;
                }
                mPlayer.start();   //开始播放
                mPlayer.setLooping(true);
            }
        }
    }

    String LOCK_TAG = "df";
    Handler mTimeHandler = new Handler();

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    /*
    设置媒体音量到最大
     */
    public void setVolume() {
        AudioManager mAudioManager = (AudioManager) MyApplication.getApplication()
                .getSystemService(Context.AUDIO_SERVICE);

//通话音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        //设置媒体音量为最大值，当然也可以设置媒体音量为其他给定的值
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, max, 0);
//        Logger.d(“VIOCE_CALL”, “max : ” + max + ” current : ” + current);
//系统音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, max, 0);
//        Logger.d(“SYSTEM”, “max : ” + max + ” current : ” + current);
//铃声音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, max, 0);
//        Logger.d(“RING”, “max : ” + max + ” current : ” + current);
//音乐音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
//        Logger.d(“MUSIC”, “max : ” + max + ” current : ” + current);
//提示声音音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, max, 0);

    }

    /**
     * 按键广播接收器
     *
     * @author len
     */
    public BroadcastReceiver keyBroadcast = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //开机服务判断是否初始化
            if (Constant.KAIJIQIDONG.equals(action)) {
                try {
                    pdkj();
                    Logger.d("main广播初始化", "main广播初始化main广播初始化");
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
            //SOS按钮
            if (Constant.SOS_ACTION.equals(action) || Constant.SOS_LONG_ACTION.equals(action)) {
                String cmd2 = "!AIBBM,1,1,,0,14,DluC85=?Dh,4*55\r\n";
//        Logger.i("aaa:"+cmd2);
                sp = getSharedPreferences("sp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("fuwu", 1);//keyname是储存数据的键值名，同一个对象可以保存多个键值
                editor.commit();//提交保存修改
                setVolume();
                try {
                    pdkj();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UART1Tx(cmd2);
                sp = getSharedPreferences("Phone", MODE_PRIVATE);
                //获取本地的报警电话，默认为：110
                phone = sp.getString("sosphone", "0591968195");
                bjgs2();
                Logger.i("进入了SOS的报警" + lppd);
                call();

            }
            //AIS键
            if (Constant.AIS_LONG_PRESS.equals(action) || Constant.AIS_SHORT_PRESS.equals(action)) {
                Intent intent1 = new Intent(getApplicationContext(), ViewPagerActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("jinrufg", "1");
                sp = getSharedPreferences("sp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("fuwu", 1);//keyname是储存数据的键值名，同一个对象可以保存多个键值
                editor.commit();//提交保存修改
                try {
                    pdkj();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(LOCK_TAG);
                keyguardLock.disableKeyguard();
                //亮屏幕
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                boolean screen = pm.isScreenOn();
              /*
              屏幕off
               */
                if (screen == false) {
                    final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOCK_TAG);
//                    final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
                    wakeLock.acquire();

                    mTimeHandler.postDelayed(new Runnable() {
                        public void run() {
                            wakeLock.release();
                        }
                    }, 10 * 1000);

                }
                //
                if (!FragmentAisLctivity.isShow()) {
                    if ("com.yisipu.chartmap.ViewPagerActivity".equals(getTopActivityName(getApplicationContext()))) {

                              /*
                    pager 跳转页面
                     */
                        Intent intent16 = new Intent();
                        intent16.setAction(Constant.Aisaction);
                        intent16.putExtra("toPager", "ais");
                        sendBroadcast(intent16);
                    } else {
                        startActivity(intent1);
                    }
                } else {
                    /*
                    pager 跳转页面
                     */
                    Intent intent16 = new Intent();
                    intent16.setAction(Constant.Aisaction);
                    intent16.putExtra("toPager", "ais");
                    sendBroadcast(intent16);
                }

            }
            //GPS键
            if (Constant.GPS_LONG_PRESS.equals(action) || Constant.GPS_SHORT_PRESS.equals(action)) {
                Intent intent1 = new Intent(getApplicationContext(), ViewPagerActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("jinrufg", "2");
                sp = getSharedPreferences("sp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("fuwu", 1);//keyname是储存数据的键值名，同一个对象可以保存多个键值
                editor.commit();//提交保存修改
                try {
                    pdkj();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(LOCK_TAG);
                keyguardLock.disableKeyguard();
                //亮屏幕
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                boolean screen = pm.isScreenOn();
              /*
              屏幕off
               */
                if (screen == false) {
                    final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOCK_TAG);

//                    final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
                    wakeLock.acquire();

                    mTimeHandler.postDelayed(new Runnable() {
                        public void run() {
                            wakeLock.release();
                        }
                    }, 10 * 1000);

                }
                if (!FragmentGPS.isShow()) {
                    if ("com.yisipu.chartmap.ViewPagerActivity".equals(getTopActivityName(getApplicationContext()))) {

                              /*
                    pager 跳转页面
                     */
                        Intent intent16 = new Intent();
                        intent16.setAction(Constant.Aisaction);
                        intent16.putExtra("toPager", "gps");
                        sendBroadcast(intent16);
                    } else {
                        startActivity(intent1);
                    }
                } else {
                    /*
                    pager 跳转页面
                     */
                    Intent intent16 = new Intent();
                    intent16.setAction(Constant.Aisaction);
                    intent16.putExtra("toPager", "gps");
                    sendBroadcast(intent16);
                }


            }
            //快捷键1
            if (Constant.SHORTCUT_1_LONG_PRESS.equals(action) || Constant.SHORTCUT_1_SHORT_PRESS.equals(action)) {

                sp2 = getSharedPreferences("sp", MODE_PRIVATE);
                if (sp2.getString("keyboard", "").equals("闹钟")) {
                    Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
                    alarmas.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(alarmas);
                }
                if (sp2.getString("keyboard", "").equals("计算器")) {
                    Intent mIntent = new Intent();
                    mIntent.setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                }
                if (sp2.getString("keyboard", "").equals("关闭告警")) {
                    isRelease = true;
                    setNodeString(BJ, "0");
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.cancel();
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = null;
                    }
                    if (FragmentAisLctivity.BaoJingLs != null && FragmentAisLctivity.BaoJingLs.size() > 0) {
                        FragmentAisLctivity.BaoJingLs.clear();

                    }
                    FragmentAisLctivity.isRunningBaojing = 0;
                }
            }
            if (Constant.CLOSE_SERVICE.equals(action)) {

                SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("close_service", true);
                editor.commit();
                Intent intent14 = new Intent(getApplicationContext(), MyDataServicer.class);
                getApplicationContext().stopService(intent14);
            }
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {

                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int status = intent.getIntExtra("status", -1);
                int health = intent.getIntExtra("health", -1);
                int level = -1; // percentage, or -1 for unknown
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }

                if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
                    Logger.i("'s battery feels very hot!");
                } else {
                    switch (status) {
                        case BatteryManager.BATTERY_STATUS_UNKNOWN:

                            Logger.i("'s battery feels very hot!2");
                            break;
                          /*
                            充电状态
                             */
                        case BatteryManager.BATTERY_STATUS_CHARGING:
                            ;
                            Logger.i("'s battery feels very hot!" + level);
                            if (level <= 33) {

                            } else if (level <= 84) {

                            } else {

                            }

                            break;
                        case BatteryManager.BATTERY_STATUS_DISCHARGING:
                            /*
                            没充电状态
                             */
                        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                            if (level == 0) {
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("levelBarry", "-1");
                                editor.commit();
                            } else if (level == 15) {
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("levelBarry", "15");
                                editor.commit();
                            } else if (level == 5) {
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("levelBarry", "5");
                                editor.commit();
                            } else {
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("levelBarry", "-1");
                                editor.commit();
                            }
                            break;
                        case BatteryManager.BATTERY_STATUS_FULL:
                            Logger.i("'s battery feels very hot!8" + level);
                            break;
                        default:
                            break;
                    }
                }

            }
            //快捷键2
            if (Constant.SHORTCUT_2_LONG_PRESS.equals(action) || Constant.SHORTCUT_2_SHORT_PRESS.equals(action)) {
                sp2 = getSharedPreferences("sp", MODE_PRIVATE);
                if (sp2.getString("keyboard2", "").equals("闹钟")) {
                    Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
                    alarmas.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(alarmas);
                }
                if (sp2.getString("keyboard2", "").equals("计算器")) {
                    Intent mIntent = new Intent();
                    mIntent.setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                }
                if (sp2.getString("keyboard2", "").equals("AIS开关")) {

                }

                if (sp2.getString("keyboard2", "").equals("关闭告警")) {
                    isRelease = true;
                    setNodeString(BJ, "0");
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.cancel();
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = null;
                    }
                    if (FragmentAisLctivity.BaoJingLs != null && FragmentAisLctivity.BaoJingLs.size() > 0) {
                        FragmentAisLctivity.BaoJingLs.clear();

                    }
                    FragmentAisLctivity.isRunningBaojing = 0;
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {  //这是Service必须要实现的方法，目前这里面什么都没有做
//只是在onCreate()方法中打印了一个log便于测试
        return null;
    }


    public AisSettingListener getAis() {
        return ais;
    }

    public void setAis(AisSettingListener ais) {
        this.ais = ais;
    }

    public interface AisSettingListener {
        public void onReceiverAis(String str);
    }

    private AisSettingListener ais;
    /*

     */
    long nowTime;
    private Runnable runnable;

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.SOS_ACTION);
        intentFilter.addAction(Constant.SOS_LONG_ACTION);
        intentFilter.addAction(Constant.AIS_LONG_PRESS);
        intentFilter.addAction(Constant.AIS_SHORT_PRESS);
        intentFilter.addAction(Constant.GPS_LONG_PRESS);
        intentFilter.addAction(Constant.GPS_SHORT_PRESS);
        intentFilter.addAction(Constant.SHORTCUT_1_LONG_PRESS);
        intentFilter.addAction(Constant.SHORTCUT_1_SHORT_PRESS);
        intentFilter.addAction(Constant.SHORTCUT_2_LONG_PRESS);
        intentFilter.addAction(Constant.SHORTCUT_2_SHORT_PRESS);
        intentFilter.addAction(Constant.KAIJIQIDONG);

        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        intentFilter.addAction(Constant.CLOSE_SERVICE);
        registerReceiver(keyBroadcast, intentFilter);// 注册接受消息广播

    }

    @Override
    public void onStart(Intent intent, int startId) {
        File dir2 = new File(getPath());
        if (!dir2.exists())
            dir2.mkdir();
        File dir = new File(getPath() + "/map_database");
        if (!dir.exists())
            dir.mkdir();
        File dir4 = new File(Environment.getExternalStorageDirectory().getPath() + "/chartmap/");
        if (!dir4.exists())
            dir4.mkdir();
        getPath1();
        //动态注册广播接收器
        registerBroadcast();
        sp2 = getSharedPreferences("sp", MODE_PRIVATE);
        String ms = sp2.getString("mode", "标准模式");
        if (ms.equals("标准模式")) {
            setNodeString(AIS_SW_DEVICE_NODE, "1");
            setNodeString(AIS_SW_DEVICE_ELEC5V, "1");

        }
        if (ms.equals("智能模式")) {
            setNodeString(AIS_SW_DEVICE_NODE, "0");
            setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
        }
        try {
            pdkj();
            Logger.d("初始化", "初始化locationManager");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(dbDelete).start();
        new Thread(lsrunnable).start();

    }

    /*
    true表示在电子围栏中不发送gps ais,表示可以
     */
    public boolean isInDZWL = false;
    public boolean isInDZWL2 = false;
    public String isOpenDZWL = "";
    /*
    海渔Service
     */
    WorkService workService = null;
    //    List<BaoJingShip> baoJingShipList=new ArrayList<>();
    static List<ShipBean> shipListTemp = new ArrayList<>();
    int i7 = 0;
    Runnable dbDelete = new Runnable() {
        Toast toast = null;

        //延时操作
        @Override
        public void run() {
            long time1 = System.currentTimeMillis();
            while (true) {
//                long time2=System.currentTimeMillis();
//                if(time2-time1==6000){
//                    time1=System.currentTimeMillis();
                DBManager db = new DBManager(getApplicationContext());
                Date dt = new Date();
                Long time = dt.getTime();
                db.deleteTimeShipBean(time);
                ShipBean sb = new ShipBean();
                sb = db.getMyShip();
                int haili = sp2.getInt("hl", 1);
                float hl = (float) haili;
                if (sb != null) {
                    if (sb.getLatitude() != 91 && sb.getLongitude() != 181) {
                        bl = locationUtils.lpbj(getApplicationContext(), hl, db, sb.getLatitude(), sb.getLongitude());
                        Logger.d("确认经纬度执行了bl" + bl + sb.getLatitude() + "   " + sb.getLongitude());
                    }
                } else {
                    bl = false;
                    Logger.d("没执行了bl" + bl);
                }
                //罗盘的安全范围报警
                if (null != sb) {
                    lpfwkg = sp2.getString("lpaqfw", "开");
                    gjts = sp2.getString("gjts", "关");
                    if (lpfwkg.equals("开") && gjts.equals("开")) {
                        fw = sp2.getFloat("baojing", 4);

                        float sdgl = sp2.getFloat("sdgl", 0.5f);
                        Date data = new Date();
                        long time2 = data.getTime();
//                        lppd = LocationUtils.lpbj(getApplicationContext(), fw, db, sb.getLatitude(), sb.getLongitude());
                        List<ShipBean> ls2 = LocationUtils.lpbjList(getApplicationContext(), fw, db, sb.getLatitude(), sb.getLongitude());
                        boolean baojing = false;
                        if (ls2.size() == 0) {
                            shipListTemp.clear();
                            Logger.i("判断klkk0");
                        }
                        if (!String.valueOf(Temfw).equals(String.valueOf(fw))) {
                            shipListTemp.clear();
                            Logger.i("判断klkk1" + fw + "sdd" + Temfw);
                            Temfw = fw;

                        }
                        i7 = 0;
                        for (ShipBean sp : ls2) {
                            for (ShipBean s7 : shipListTemp) {
                                if (s7.getMMSI() == sp.getMMSI()) {
                                    i7++;
                                    break;
                                }
                            }
                        }
                        Logger.i("hgk" + i7 + "gkghk" + ls2.size() + "hjj" + shipListTemp.size() + "dsd" + Temfw + "dfd" + fw);
                        if (ls2.size() > 0 && shipListTemp.size() > 0 && i7 != ls2.size()) {
                            baojing = true;
                            shipListTemp.clear();
                            shipListTemp.addAll(ls2);
                            Logger.i("判断klkk2");
                        }
                        if (ls2.size() > 0 && shipListTemp.size() == 0) {
                            baojing = true;
                            shipListTemp.addAll(ls2);
                            Logger.i("判断klkk3");
                        }
                        Logger.i("判断返回" + LocationUtils.lpbj(getApplicationContext(), fw, db, sb.getLatitude(), sb.getLongitude()));
                        Logger.d(TAG, "执行了线程" + lppd);
                        Logger.i("距离距离" + lppd);
                        if (baojing && ls2 != null && ls2.size() > 0 && gjts.equals("开") && lpfwkg.equals("开")) {
//                            if (zdbs == 1) {
                            bjgs();

                            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(LOCK_TAG);
                            keyguardLock.disableKeyguard();
                            //亮屏幕
                            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                            boolean screen = pm.isScreenOn();
              /*
              屏幕off
               */
                            if (screen == false) {
                                final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOCK_TAG);
//                    final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
                                wakeLock.acquire();

                                mTimeHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        wakeLock.release();
                                    }
                                }, 10 * 1000);

                            }
                            Logger.i("kkkdg" + FragmentAisLctivity.isShow());
                            if (!FragmentAisLctivity.isShow()) {

                                if ("com.yisipu.chartmap.ViewPagerActivity".equals(getTopActivityName(getApplicationContext()))) {
                                    Logger.i("kkkdg333");
                              /*
                    pager 跳转页面
                     */
                                    Intent intent16 = new Intent();
                                    intent16.setAction(Constant.Aisaction);
                                    intent16.putExtra("toPager", "ais");
                                    intent16.putExtra("baoJingShip", (Serializable) ls2);
                                    intent16.putExtra("isRuingBaoJing", 1);
                                    FragmentAisLctivity.BaoJingLs = ls2;
                                    FragmentAisLctivity.isRunningBaojing = 1;
                                    sendBroadcast(intent16);

                                } else {
                                    Logger.i("kkkdg322");
                                    Intent intent1 = new Intent(getApplicationContext(), ViewPagerActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.putExtra("jinrufg", "1");
                                    intent1.putExtra("baoJingShip", (Serializable) ls2);
                                    intent1.putExtra("isRuingBaoJing", 1);
                                    FragmentAisLctivity.BaoJingLs = ls2;
                                    FragmentAisLctivity.isRunningBaojing = 1;
                                    startActivity(intent1);
                                }
                            } else {
                    /*
                    pager 跳转页面
                     */
                                Logger.i("kkkdg344");
                                Intent intent16 = new Intent();
                                intent16.setAction(Constant.Aisaction);
                                intent16.putExtra("toPager", "ais");
                                intent16.putExtra("baoJingShip", (Serializable) ls2);
                                intent16.putExtra("isRuingBaoJing", 1);
                                FragmentAisLctivity.BaoJingLs = ls2;
                                FragmentAisLctivity.isRunningBaojing = 1;
                                sendBroadcast(intent16);
                            }
                            Logger.i("进入了罗盘的报警" + lppd);
                            zdbs = 0;
                            y = 1;
                        }
                    }
                }
             /*
                电子围栏
                 */
                DBManager dbManager = new DBManager(getApplication());

                ShipBean sp = dbManager.getMyShip();
                String kg = sp2.getString("kg", "关");
                ArrayList<Double> x1 = new ArrayList<>();
                ArrayList<Double> y1 = new ArrayList<>();

                if (kg.equals("开")) {

                    x1.clear();
                    y1.clear();
                    String[] dzwl = SharePrefenerArrary.getSharedPreference(getApplicationContext(), "txt_dzwl");


                    for (String z1 : dzwl) {
                        String[] a = z1.split(",");

                        if (a.length >= 2) {
                            if (a[0].contains("E") || a[0].contains("W")) {
                                double z = ScopeUtils.getJW(a[0]);

                                if (z != -1) {
                                    x1.add(z);
                                }
                                if (a[1].contains("E") || a[1].contains("W")) {
                                    double z2 = ScopeUtils.getJW(a[1]);
                                    if (z != -1) {
                                        x1.add(z2);
                                    }
                                    continue;
                                } else if (a[1].contains("N") || a[1].contains("S")) {
                                    double z2 = ScopeUtils.getJW(a[1]);

                                    if (z != -1) {
                                        y1.add(z2);
                                    }
                                    continue;
                                }
                            } else if (a[0].contains("N") || a[0].contains("S")) {
                                double z = ScopeUtils.getJW(a[0]);

                                if (z != -1) {
                                    y1.add(z);
                                }
                                if (a[1].contains("E") || a[1].contains("W")) {
                                    double z2 = ScopeUtils.getJW(a[1]);
                                    if (z != -1) {
                                        x1.add(z2);
                                    }
                                    continue;
                                } else if (a[1].contains("N") || a[1].contains("S")) {
                                    double z2 = ScopeUtils.getJW(a[1]);

                                    if (z != -1) {
                                        y1.add(z2);
                                    }
                                    continue;
                                }
                            }

                        }
                    }

                }
                if (kg.equals("开") && sp != null && sp.getLatitude() != -1 && sp.getLongitude() != -1 && sp.getLatitude() <= 90 && sp.getLongitude() <= 180) {
                    boolean isIn = ScopeUtils.containsPoint(sp.getLongitude(), sp.getLatitude(), x1, y1);
                    if (x1.size() > 0 && y1.size() > 0) {
//                        Logger.i("fffff" + x1.size() + "xcc" + x1.get(0) + "GDF" + y1.get(0) + "dkg" + isIn);
                    }
                    if (isIn) {
                        isInDZWL = true;
                        if (isInDZWL2 == false) {
                            setNodeString(AIS_SW_DEVICE_NODE, "0");
                            setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
                            isInDZWL2 = true;
                        }

                    } else {

                        isInDZWL = false;
                        if (isInDZWL2 == true) {
                            setNodeString(AIS_SW_DEVICE_NODE, "1");
                            setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                            isInDZWL2 = false;
                        }

                    }
                } else {
                    isInDZWL = false;
                    if (isInDZWL2 == true) {
                        setNodeString(AIS_SW_DEVICE_NODE, "1");
                        setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                        isInDZWL2 = false;
                    }
                }
                if (kg.equals("开")) {
                    isOpenDZWL = "开";
                }
                if (kg.equals("关")) {
                    isInDZWL = false;
                    if (isOpenDZWL.equals("开") || TextUtils.isEmpty(isOpenDZWL)) {
                        setNodeString(AIS_SW_DEVICE_NODE, "1");
                        setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                        isOpenDZWL = "关";
                    }
                }
                 /*
                 海渔平台
                  */
                SharedPreferences sp2 = getApplicationContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
                GmsConfig config = new GmsConfig();
                config.setHost("218.85.80.122");
                config.setPort(8110);
                //配置设备信息
                final DevInfo dev = new DevInfo();
                dev.setColor((byte) 2);
                String vin = String.valueOf(sp2.getInt("my_mmsi", -1));
                if (!vin.equals("-1")) {
                    dev.setVin(vin);
                }
                final String SimNo = sp2.getString("SimNo", "013459140383");
                dev.setSimNo(SimNo);//018511646665
//                dev.setTokenCode("EC2827649");
                ConfigUtil.initialize(config, dev);//初始通信及设备对象

                IMultipleListener iMultipleListener = new IMultipleListener() {
                    @Override
                    public void OnConnect(DevInfo devInfo, boolean b, short i) {

//                        Logger.i("saafzzzz" + b + "sgs" + i + "sf" + devInfo.getTokenCode() + "dgs" + devInfo.getClientId() +
//                                "ddj" + devInfo.getRemark() + "sg" + devInfo.getSimNo() + "dsg" + devInfo.getVin() + "dsg" + devInfo.getProvinceId() +
//                                "see" + devInfo.describeContents() + "ddd" + devInfo.getSeqId());

                    }

                    @Override
                    public void OnRecData(MsgInfo msgInfo, CmdParam cmdParam) {

//                        Logger.i("saafddddd" + "dfgf" + msgInfo.getDataArr() + "dsg" +
//                                "dgsd" + cmdParam.getCmdId() + "dsg" + cmdParam.getSeqId() + "dg" + cmdParam.getSimNo());

                        if (cmdParam instanceof TextMsg) {

                            TextMsg tm = (TextMsg) cmdParam;

                            Logger.i("ssggs" + tm.getContent() + "dsg" + tm.getSimNo() + "dg" + tm.getFlag());
                        }
                        if (cmdParam instanceof ReplyPosReq) {

                            GpsInfo gps = new GpsInfo();
                            String SimNo = "013459140383";

                            gps.setSimNo(SimNo);
                            gps.setLat(25);
                            gps.setLng(119);
                            Date date = new Date();
                            gps.setUpTime(date);
                            gps.setState(1);
                            gps.setAlarm(0);
                            Logger.i("dfkk哈哈哈哈");
                            SendObjTask.putData(gps);
                        }
                        if (cmdParam instanceof GenralReply) {
                            GenralReply tm = (GenralReply) cmdParam;
                            if (tm.getReplyCmd() == ConstCmd.CMD_TEXT_MSG) {
//                                Logger.i("ssggs555" + tm.getSimNo() + "dsg" + tm.getReplyCmd() + "dg" + tm.getResult() + "sgsg" + tm.getReplyId() + "sg" + ConstCmd.CMD_TEXT_MSG);
                            }
                        }
                    }

                    @Override
                    public void OnSendData(CmdParam cmdParam, MsgInfo msgInfo, boolean b) {
                        if (cmdParam instanceof GpsInfo) {
//                            Logger.i("saaf777SDSGff" + "dfgf" + msgInfo.getDataArr() + "dsg" +
//                                    "dgsd" + cmdParam.getCmdId() + "dsg" + cmdParam.getSeqId() + "dg" + cmdParam.getSimNo() + "sgsg" + b);
                        }
                        if (cmdParam instanceof TextMsg) {
//                            Logger.i("saaf33333" + "dfgf" + msgInfo.getDataArr() + "dsg" +
//                                    "dgsd" + cmdParam.getCmdId() + "dsg" + cmdParam.getSeqId() + "dg" + cmdParam.getSimNo() + "sgsg" + b);
                        }
                    }
                };


                workService = new WorkService(iMultipleListener);


                String strfence = sp2.getString("hypt", "关");
                if (strfence.equals("关")) {

                    if (workService.isWorking()) {
                        Logger.i("dsssg" + workService.isWorking());
                        workService.stop();
                    }
                }

                if (strfence.equals("开")) {

                    if (!workService.isWorking()) {
                        Logger.i("dsssg" + workService.isWorking());
                        workService.start();
                    }
                }
               /*
               配置数据库放置位置xml
                */
                getPath1();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
    SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);
    }

    private static final String TAG = "MyDataServicer";

    /*
 真机读取的gps端口
  */
    private final String UART0_DEVICE_NODE = "/dev/ttyHSL0";
    /*sb.getLatitude(), sb.getLongitude()
    真机读取的Ais端口
     */
    private final String SERIALPORT_DEVICE_NODE = "/dev/ttyHSL1";

    private InputStream mUART0InputStream;
    private OutputStream mUART0OutputStream;
    private SerialPort mUART0;
    //    private static Runnable runnable;
     /*
     读取Ais端口模拟器
      */
//    private final String  SERIALPORT_DEVICE_NODE = "/dev/ttyS0";
    private final int SERIALPORT_BAUDRATE = 38400;
    //gps  9600
    private final int UART0_BAUDRATE = 9600;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private final String AIS_SW_DEVICE_NODE = "/sys/customer/ais_en";
    private final String AIS_SW_DEVICE_ELEC5V = "/sys/customer/uart_elec5v";//AIS控制发射
    String y2 = "";
    int index = 1;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                sp = getSharedPreferences("sp", MODE_PRIVATE);//指定读取的对象
                int fuwu = sp.getInt("fuwu", 0);
                if (fuwu == 1) {
                    try {
                        BufferedReader in2 = new BufferedReader(new InputStreamReader(mInputStream));

                        String y = "";

                        while ((y = in2.readLine()) != null) {//一行一行读
                            if (y.length() > 0) {
                                if (y.length() > 7 && y.trim().charAt(7) == '2') {
                                    if (index == 1) {
                                        y2 = y;
                                        index++;
                                        Logger.i("message1:hhhhh" + y);
                                    } else if (index == 2) {
                                        y = y2 + y;
                                        index = 1;
                                        y2 = "";
                                        onDataReceived(y.getBytes(), y.length());
                                        Logger.i("message:hhhhh" + y);
                                    }

                                } else {
                                    onDataReceived(y.getBytes(), y.length());
                                    Logger.i(y);
                                }
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public static Handler handler = new Handler();
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            phone = sp.getString("sosphone", "0591968195");
            // 取得输入的电话号码串
            if (phone != null && phone != "") {
                setVolume();
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + phone));
                phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 启动
                startActivity(phoneIntent);
                //自定义Toast
//            toastCommom.ToastShow(MainActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "紧急警报");
                Toast toast = null;
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.toast_xml,
                        null);
                TextView text = (TextView) layout.findViewById(R.id.toast_tv);
                ImageView imageView = (ImageView) layout.findViewById(R.id.sos_iv);
                imageView.setVisibility(View.VISIBLE);
                text.setText("落水危险");
                toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }
    };


    /**
     * 获取节点
     */
    private static String getNodeString(String path) {
        String prop = "0";// 默认值
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            prop = reader.readLine();
            reader.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.d(TAG, "getNodeString:" + path + " " + prop);
        return prop;
    }

    /**
     * 写节点
     */
    private static void setNodeString(String path, String val) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bufWriter = new BufferedWriter(fw);
            bufWriter.write(val);  // 写操作
            fw.flush();
            bufWriter.close();
            fw.close();
            Logger.d(TAG, "setNodeString:" + path + " " + val + " " + getNodeString(path));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(TAG, "can't write the " + path);
        }
    }


    /*
    往ais端口写数据
     */
    public void UART1Tx(String cmd) {
        byte[] mTxBuffer = cmd.getBytes();
        Logger.d("UART1Tx", cmd);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mTxBuffer);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
 往gps端口写数据
 */
    public void UART0Tx(String cmd) {
        byte[] mTxBuffer = cmd.getBytes();
        try {
            if (mUART0OutputStream != null) {
                mUART0OutputStream.write(mTxBuffer);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //初始化gps端口
    public void init_UART0() {
        try {
            mUART0 = new SerialPort(new File(UART0_DEVICE_NODE), UART0_BAUDRATE);

            mUART0InputStream = mUART0.getInputStream();
            mUART0OutputStream = mUART0.getOutputStream();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int i = 0;
    String tem;
    List<ShipBean> ls = new ArrayList<>();

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getInnerSDCardPath() {

        return Environment.getExternalStorageDirectory().getPath();
    }

    /*
    配置获取路径
     */
    public String getPath() {

        if (ExtenSdCard.getSecondExterPath() != null) {
            if (ExtenSdCard.isSecondSDcardMounted()) {
                return ExtenSdCard.getSecondExterPath() + "/chartmap/";
            }
        }

        return Environment.getExternalStorageDirectory().getPath() + "/chartmap/";
    }

    /*
    内置卡和外置卡配置路径
     */
    String path_setting = getInnerSDCardPath() + "/chartmap/" + "path_setting.xml";

    /*
    配置xml文件配置路径
     */
    public String getPath1() {
        ExtendSdCardXml xml = new ExtendSdCardXml(getApplicationContext(), path_setting);

        if (ExtenSdCard.getSecondExterPath() != null) {
            if (ExtenSdCard.isSecondSDcardMounted()) {
                FilePathBean fpb = new FilePathBean();
                fpb.setIsExten(1);
                fpb.setPath(ExtenSdCard.getSecondExterPath());
                String a = xml.WriteXml(fpb);
                xml.Write(a, path_setting);
                return ExtenSdCard.getSecondExterPath() + "/chartmap/";
            }
        }
        FilePathBean fpb = new FilePathBean();
        fpb.setIsExten(0);
        fpb.setPath(Environment.getExternalStorageDirectory().getPath());
        String a = xml.WriteXml(fpb);
        xml.Write(a, path_setting);
        return Environment.getExternalStorageDirectory().getPath() + "/chartmap/";
    }

    /*
   写ais信息到文件中
     */
    String inPath = getInnerSDCardPath();
    String aisPath;

    public void WriteFileToAis(final String str) {

        aisPath = getPath() + "/" + "ais.txt";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (str != null && !TextUtils.isEmpty(str)) {
                        TxtFileUtile.fileOutputStream(str, aisPath, true);
                    }
                } catch (Exception exc) {

                }
            }
        }).start();
    }

    /*
 写ais信息到文件中2
   */
//    String inPath = getInnerSDCardPath();
    String aisPath2;

    public void WriteFileToAis2(final String str) {
        aisPath2 = getPath() + "/" + "ais2.txt";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (str != null && !TextUtils.isEmpty(str)) {
                        TxtFileUtile.fileOutputStream(str, aisPath2, true);
                    }
                } catch (Exception exc) {

                }
            }
        }).start();
    }

    /*
    写gps到文件
     */
    private String gpsPath;

    public void WriteFileToGps(final String str) {
        gpsPath = getPath() + "/" + "gps.txt";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (str != null && !TextUtils.isEmpty(str)) {
                        TxtFileUtile.fileOutputStream(str, gpsPath, true);
                    }
                } catch (Exception exc) {

                }
            }
        }).start();
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        Logger.i("zzzzzz334jkkked" + new String(buffer, 0, size));
        DBManager db = new DBManager(this);

        String tem5;
        tem5 = new String(buffer, 0, size);
        tem = tem5.trim();
        WriteFileToAis(tem);
        Vdm vdm = new Vdm();

        if (tem.contains("!AIVDO")) {
            ShipBean tem3 = db.getMyShip();
            ShipBean s = null;
            if (tem3 != null) {
                s = tem3;
            } else {
                //本号
                s = new ShipBean();
            }
            try {
                         /*
                         取2
                          */
                if (tem.charAt(7) == '2') {
                    Logger.i(tem.charAt(7) + "dffddhhdhd");
//
                    int index2 = tem.indexOf("!AIVDO", 1);
                    if (index2 != -1) {
                        String z = tem.substring(0, index2) + "\r\n";
                        String z2 = tem.substring(index2, tem.length());


                        int a = vdm.parse(z);

                        a = vdm.parse(z2);
//
                        Logger.i("sdgggdgddg" + vdm.getMsgId());
//

//
                    }
                } else {
                    vdm.parse(tem);
                    vdm.getMsgId();
                }
                switch (vdm.getMsgId()) {
                    case 1:
                            /*
                            本船
                             */
                        WriteFileToAis2(tem);
                        AisMessage message = AisMessage.getInstance(vdm);
//                            Toast.makeText(MainActivity.this, message.getMsgId() + "", Toast.LENGTH_SHORT).show();
                        Logger.i(message.getMsgId() + "sgdsfg");
                        if (message instanceof AisMessage1) {
                            AisMessage1 msg1 = (AisMessage1) message;


                            s.setMMSI(msg1.getUserId());

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            SharedPreferences.Editor etor = sp.edit();
                            if ((msg1.getPos().getLatitude() / (600000.0)) <= 90 && msg1.getPos().getLongitude() / (600000.0) <= 180) {
                                s.setLatitude(msg1.getPos().getLatitude() / (600000.0));

                                s.setLongitude(msg1.getPos().getLongitude() / (600000.0));

                                etor.putFloat("my_latitude", (float) (msg1.getPos().getLatitude() / (600000.0)));
                                etor.putFloat("my_longitude", (float) (msg1.getPos().getLongitude() / (600000.0)));
                            }
                            etor.putInt("my_mmsi", msg1.getUserId());
                            etor.commit();

                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());
                            s.setStatus(msg1.getNavStatus());
                            s.setMyShip(true);
                            s.setClassType(2);
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }

                        break;
                    case 5:
                        WriteFileToAis2(tem);
                        AisMessage message1 = AisMessage.getInstance(vdm);
                        Logger.i(message1.getMsgId() + "22sgdsfg");
                        if (message1 instanceof AisMessage5) {
                            AisMessage5 msg1 = (AisMessage5) message1;

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }
                            s.setHuhao(msg1.getCallsign());
                            if (msg1.getShipType() != 0) {
                                s.setType(msg1.getShipType());
                            }

                            s.setDimBow(msg1.getDimBow());
                            s.setIMO(msg1.getImo());
                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(true);
                            s.setClassType(2);
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }

                        break;
                    case 18:
                        WriteFileToAis2(tem);
                        AisMessage message2 = AisMessage.getInstance(vdm);
                        Logger.i(message2.getMsgId() + "22sgdsfg");
                        if (message2 instanceof AisMessage18) {
                            AisMessage18 msg1 = (AisMessage18) message2;


                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            SharedPreferences.Editor etor = sp.edit();
                            if ((msg1.getPos().getLatitude() / (600000.0)) <= 90 && msg1.getPos().getLongitude() / (600000.0) <= 180) {
                                s.setLatitude(msg1.getPos().getLatitude() / (600000.0));
                                s.setLongitude(msg1.getPos().getLongitude() / (600000.0));

                                etor.putFloat("my_latitude", (float) (msg1.getPos().getLatitude() / (600000.0)));
                                etor.putFloat("my_longitude", (float) (msg1.getPos().getLongitude() / (600000.0)));
                            }
                            etor.putInt("my_mmsi", msg1.getUserId());
                            etor.commit();
                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());

                            s.setMyShip(true);

                            s.setClassType(2);

                            if (s.getMMSI() != 0)
                                db.addShipBean(s);

                        }


                        break;
                    case 19:
                        WriteFileToAis2(tem);
                        AisMessage message3 = AisMessage.getInstance(vdm);
                        Logger.i(message3.getMsgId() + "22sgdsfg");

                        if (message3 instanceof AisMessage19) {
                            AisMessage19 msg1 = (AisMessage19) message3;

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            SharedPreferences.Editor etor = sp.edit();
                            if ((msg1.getPos().getLatitude() / (600000.0)) <= 90 && msg1.getPos().getLongitude() / (600000.0) <= 180) {
                                s.setLatitude(msg1.getPos().getLatitude() / (600000.0));
                                s.setLongitude(msg1.getPos().getLongitude() / (600000.0));

                                etor.putFloat("my_latitude", (float) (msg1.getPos().getLatitude() / (600000.0)));
                                etor.putFloat("my_longitude", (float) (msg1.getPos().getLongitude() / (600000.0)));
                            }
                            etor.putInt("my_mmsi", msg1.getUserId());
                            etor.commit();
                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());
                            if (msg1.getShipType() != 0) {
                                s.setType(msg1.getShipType());
                            }
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }


                            s.setDimBow(msg1.getDimBow());

                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(true);
                            s.setClassType(2);

                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }


                        break;
                    case 24:
                        WriteFileToAis2(tem);
                        AisMessage message24 = AisMessage.getInstance(vdm);
                        Logger.i(message24.getMsgId() + "22sgdsfg");

                        if (message24 instanceof AisMessage24) {
                            AisMessage24 msg1 = (AisMessage24) message24;

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }
                            s.setDimBow(msg1.getDimBow());

                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(true);

                            SharedPreferences.Editor etor = sp.edit();

                            etor.putInt("my_mmsi", msg1.getUserId());
                            etor.commit();
                            if (msg1.getShipType() != 0) {
                                s.setType(msg1.getShipType());
                            }

                            s.setClassType(2);

                            if (s.getMMSI() != 0)
                                db.addShipBean(s);

                        }


                        break;


                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (tem.contains("!AIVDM")) {

            //其他船
            try {

                if (tem.charAt(7) == '2') {
                    Logger.i(tem.charAt(7) + "dffddhhdhd");

                    int index2 = tem.indexOf("!AIVDM", 1);
                    if (index2 != -1) {
                        String z = tem.substring(0, index2) + "\r\n";
                        String z2 = tem.substring(index2, tem.length());


                        int a = vdm.parse(z);

                        a = vdm.parse(z2);
                        Logger.i("sdgggdgddg" + vdm.getMsgId());
                    }
                } else {
                    vdm.parse(tem);
                    vdm.getMsgId();
                }


                switch (vdm.getMsgId()) {
                    case 1:
                        WriteFileToAis2(tem);
                        AisMessage message = AisMessage.getInstance(vdm);

//                            Toast.makeText(MainActivity.this, message.getMsgId() + "", Toast.LENGTH_SHORT).show();
                        Logger.i(message.getMsgId() + "sgdsfg");
                        if (message instanceof AisMessage1) {
                            AisMessage1 msg1 = (AisMessage1) message;

                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }
                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }


                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            s.setLatitude(msg1.getPos().getLatitude() / (600000.0));
                            s.setLongitude(msg1.getPos().getLongitude() / (600000.0));


                            double a = sp.getFloat("my_latitude", -1);
                            double b = sp.getFloat("my_longitude", -1);
                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());
                            s.setStatus(msg1.getNavStatus());
                            s.setMyShip(false);
//                            if (tem.charAt(12) == 'A') {
                            s.setClassType(1);

//                            } else if (tem.charAt(12) == 'B') {
//                                s.setClassType(2);
//                            }
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);

//                                    db.closeDb();
//

                        }
                        ;

                        break;
                    case 5:
                        WriteFileToAis2(tem);
                        AisMessage message1 = AisMessage.getInstance(vdm);
//                             AisMessage message1= AisMessage.getApplication(vdm);
//
                        if (message1 instanceof AisMessage5) {
                            AisMessage5 msg1 = (AisMessage5) message1;
                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }
                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }


                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }
                            Logger.i("name" + msg1.getName());
                            s.setHuhao(msg1.getCallsign());
                            if (msg1.getShipType() != 0) {
                                s.setType(msg1.getShipType());
                            }

                            s.setDimBow(msg1.getDimBow());
                            s.setIMO(msg1.getImo());
                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(false);
                            s.setClassType(1);
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }
                        ;


                        break;
                    case 8:
                        WriteFileToAis2(tem);
                        AisMessage message8 = AisMessage.getInstance(vdm);
                        if (message8 instanceof AisMessage8) {
                            AisMessage8 msg1 = (AisMessage8) message8;

                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }
                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }
                            s.setMyShip(false);
                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }


                        break;
                    case 18:
                        WriteFileToAis2(tem);
                        AisMessage message2 = AisMessage.getInstance(vdm);
                        Logger.i(message2.getMsgId() + "22sgdsfg");
                        if (message2 instanceof AisMessage18) {

                            AisMessage18 msg1 = (AisMessage18) message2;
                            //解决一个小bug
                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }
                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            s.setLatitude(msg1.getPos().getLatitude() / (600000.0));
                            s.setLongitude(msg1.getPos().getLongitude() / (600000.0));
                            double a = sp.getFloat("my_latitude", -1);
                            double b = sp.getFloat("my_longitude", -1);

                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());

                            s.setMyShip(false);
                            s.setClassType(2);
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }


                        break;
                    case 19:
                        WriteFileToAis2(tem);
                        AisMessage message3 = AisMessage.getInstance(vdm);
                        Logger.i(message3.getMsgId() + "22sgdsfg");


                        if (message3 instanceof AisMessage19) {
                            AisMessage19 msg1 = (AisMessage19) message3;
                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }

                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }

                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }
                            s.setLatitude(msg1.getPos().getLatitude() / (600000.0));
                            s.setLongitude(msg1.getPos().getLongitude() / (600000.0));
                            double a = sp.getFloat("my_latitude", -1);
                            double b = sp.getFloat("my_longitude", -1);

                            s.setSog(msg1.getSog());
                            s.setPrecision(msg1.getPosAcc());
                            s.setCog(msg1.getCog());
                            s.setReal_sudu(msg1.getTrueHeading());
                            if (msg1.getShipType() != 0) {
                                s.setType(msg1.getShipType());
                            }
                            s.setDimBow(msg1.getDimBow());

                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(false);

                            s.setClassType(2);
                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }

                        break;
                    case 24:
                        WriteFileToAis2(tem);
                        AisMessage message24 = AisMessage.getInstance(vdm);
                        Logger.i(message24.getMsgId() + "22sgdsfg");

                        if (message24 instanceof AisMessage24) {
                            AisMessage24 msg1 = (AisMessage24) message24;
                            if (sp.getInt("my_mmsi", -1) != -1 && msg1.getUserId() == sp.getInt("my_mmsi", -1)) {
                                break;
                            }

                            ShipBean tem3 = db.getEditShipBean(msg1.getUserId());
                            ShipBean s = null;
                            if (tem3 != null) {
                                s = tem3;
                            } else {
                                //本号
                                s = new ShipBean();
                            }
                            int coutry = Integer.valueOf(String.valueOf(msg1.getUserId()).substring(0, 3));
                            s.setCountry(coutry);
                            s.setMMSI(msg1.getUserId());
                            int index = msg1.getName().indexOf("@");
                            if (index != -1) {
                                s.setEnglishName(msg1.getName().substring(0, index));
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            } else {
                                s.setEnglishName(msg1.getName());
                                if (s.getCountry() == 412 || s.getCountry() == 413) {
                                    s.setChineseName(LangUtils.getInstance().parseEnglist(s.getEnglishName()));

                                }
                            }
                            s.setDimBow(msg1.getDimBow());

                            s.setDimPort(msg1.getDimPort());
                            s.setDimStarboard(msg1.getDimStarboard());
                            s.setDimStern(msg1.getDimStern());
                            s.setMyShip(false);

                            if (msg1.getShipType() != 0) {

                                s.setType(msg1.getShipType());
                            }
                            s.setClassType(2);

                            if (s.getMMSI() != 0)
                                db.addShipBean(s);
                        }
                        ;


                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (tem.contains("$PAIS,010,,,")) {
            Intent intent2 = new Intent();
            intent2.setAction(Constant.Aisaction);
            intent2.putExtra("selectMMsi", tem);
            sendBroadcast(intent2);
            //查询本船mmsi
            Logger.i("dskdsgkgl" + tem);
            String number = tem.trim().substring(12, 21);
            Logger.i("dskdsgkgl" + number);
            //判断是否是纯数字
            pattern = Pattern.compile("[0-9]{1,}");
            matcher = pattern.matcher((CharSequence) number);
            boolean result = matcher.matches();
            if (result == true) {
                int a = Integer.valueOf(number);

                if (a != 0) {
                    ShipBean sb = new ShipBean();
                    sb.setMMSI(a);
                    SharedPreferences.Editor editor = sp.edit();


                    editor.putInt("my_mmsi", a);
                    editor.commit();
                    double a1 = sp.getFloat("my_latitude", -1);
                    double b1 = sp.getFloat("my_longitude", -1);
                    if (a1 == -1 || b1 == -1 || a1 == 91 || b1 == 181) {

                        if (sb.getClassType() == -1) {
                            sb.setClassType(2);
                        }

                        sb.setMyShip(true);
                    } else {
                        if (sb.getClassType() == -1) {
                            sb.setClassType(2);
                        }
                        sb.setLatitude(a1);
                        sb.setLongitude(b1);
                        sb.setMyShip(true);
                    }
                    DBManager d = new DBManager(this);
                    d.addShipBean(sb);
                }
            }
        } else if (tem.contains("$AISSD")) {
            Intent intent2 = new Intent();
            intent2.setAction(Constant.Aisaction);
            intent2.putExtra("selectMMsi", tem);
            sendBroadcast(intent2);


        } else if (tem.contains("$AIVSD")) {
            Intent intent2 = new Intent();
            intent2.setAction(Constant.Aisaction);
            intent2.putExtra("selectMMsi", tem);
            sendBroadcast(intent2);
        } else if (tem.contains("$DUAIR")) {
            Intent intent2 = new Intent();
            intent2.setAction(Constant.Aisaction);
            intent2.putExtra("selectMMsi", tem);
            sendBroadcast(intent2);

        }
    }

    @Override
    public void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
        }
        mReadThread = null;
        mSerialPort = null;

        if (mUART0 != null) {
            mUART0.close();
        }
//        if(dbDelete!=null){
//            dbDelete=null;
//        }
        mUART0 = null;
        //注销广播
        unregisterReceiver(keyBroadcast);// 注销接受消息广播
        super.onDestroy();
    }

    ShipBean ship2Tem;
    String ms2 = "标准模式";
    List<SatelliteBean> sateList = new ArrayList<>();
    List<Integer> lsSate = new ArrayList<>();

    /*
       获取GPS
        */
    public void getgps() {
        Logger.i("getgps");
        locationManager = (LocationManager) MyApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
//        locationManager=(LocationManager)this.getApplicationContext.getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location loc) {
                if (loc == null) {
                    Logger.i("dsg==null");
                }
                /*
                上报海渔平台
                 */
                String strfence = sp2.getString("hypt", "关");
                if (strfence.equals("开")) {
                    if (loc != null) {

                        GpsInfo gps = new GpsInfo();
                        String SimNo = sp2.getString("SimNo", "013459140383");

                        gps.setSimNo(SimNo);
                        gps.setLat(loc.getLatitude());
                        gps.setLng(loc.getLongitude());
                        gps.setSpeed(loc.getSpeed());
                        gps.setHigh((int) loc.getAltitude());
                        Date date = new Date();
                        gps.setUpTime(date);
                        gps.setState(1);
                        gps.setAlarm(0);
                        Logger.i("dfkk哈哈哈哈");
                        SendObjTask.putData(gps);


                    }
                }
                index2++;
                SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                int x = 1;
                editor.putInt("sfdw", 1);
                if (loc != null) {
                    x = 2;
                    editor.putInt("sfdw", 2);
                }
                editor.commit();
                // TODO Auto-generated method stub
                //定位資料更新時會回呼
                Logger.d("GPS-NMEA1111", loc.getLatitude() + "," + loc.getLongitude());
                Logger.d("GPS-NMEA111", loc.getLatitude() + "," + loc.getLongitude());
                DBManager d = new DBManager(getApplicationContext());
                Boolean istrue = false;
                ShipBean sb2;
                Intent intent2 = new Intent();
                // intent2.putExtra("gps",gps)
                intent2.setAction(Constant.Aisaction);
//                String str="纬度："+loc.getLongitude()+"经度："+loc.getLatitude();
                Gpsbean gpsbean = new Gpsbean();
                gpsbean.setGpsLongitude(loc.getLongitude());
                gpsbean.setGpsLatitude(loc.getLatitude());
                intent2.putExtra("gps", gpsbean);
                sendBroadcast(intent2);
                for (ShipBean sb : ls) {
                    if (sb.getMyShip() == true) {
                        sb2 = sb;
                        if (loc.getLongitude() <= 180 && loc.getLatitude() <= 90) {
                            sb2.setLongitude(loc.getLongitude());
                            sb2.setLatitude(loc.getLatitude());
                        }
                        istrue = true;
                        d.addShipBean(sb2);
                    }
                }
                if (!istrue) {
                    String z = "$DUAIQ,010*55\r\n";
                    UART1Tx(z);
                }
                SharedPreferences.Editor etor = sp.edit();
                if (loc.getLongitude() <= 180 && loc.getLatitude() <= 90) {
                    etor.putFloat("my_latitude", (float) (loc.getLatitude()));
                    etor.putFloat("my_longitude", (float) (loc.getLongitude()));
                    etor.commit();
                }
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
                //写gps到文件
                WriteFileToGps(nmea);


                lsSate.clear();
                Logger.d("GPS-NMEA", nmea);
                Logger.d("wdbs ", nmea);

                if (nmea != null && nmea.contains("$GPGSA")) {
                    Logger.d("wdbs22", nmea);
                    String[] str = nmea.split(",");
                    int ss = str.length;
                    for (int i = 3; i < str.length - 4; i++) {
                        if (!TextUtils.isEmpty(str[i])) {
                            try {
                                int a = Integer.parseInt(str[i]);
                                lsSate.add(a);
                            } catch (Exception ecp) {
                                continue;
                            }
                        }
                    }
                    Logger.d("定位的信息", "" + ss);
                    Intent intent2 = new Intent();
                    Jdbean jdbean = new Jdbean();
                    jdbean.setSateInList(lsSate);
                    jdbean.setHdop(str[str.length - 2]);
                    jdbean.setPdop(str[str.length - 3]);
                    jdbean.setSfdw(str[2]);
                    SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                    if (TextUtils.isEmpty(str[2]) || str[2].equals("1")) {
                        editor.putInt("sfdw", 1);
                    } else {

                        editor.putInt("sfdw", 2);
                    }
                    editor.commit();
                    intent2.setAction(Constant.Aisaction);
                    intent2.putExtra("pdop", jdbean);
                    Logger.i(jdbean.toString());
                    sendBroadcast(intent2);
                    Logger.i("saaaaaf" + str + "有进来" + jdbean.getHdop() + "位置精度" + jdbean.getPdop());
                    Logger.i("GPGSA的长度" + ss);
                }
                if (nmea != null && nmea.contains("$GPRMC")) {
                    String[] str = nmea.split(",");
                    int ss = str.length;
                    if (ss > 1 && !TextUtils.isEmpty(str[1])) {
                        SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                        editor.putString("gpsTime", str[1]);
                        editor.commit();
                    }
                    if (ss > 2 && !TextUtils.isEmpty(str[2])) {
                        SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                        if ("A".equals(str[2])) {
                            editor.putInt("sfdw", 2);
                        } else if ("V".equals(str[2])) {
                            editor.putInt("sfdw", 1);
                        }
                        editor.commit();
                    }
                }

                int bg = sp2.getInt("fuwu", 0);
                DBManager dbManager = new DBManager(getApplication());

                ShipBean sp = dbManager.getMyShip();

                String kg = sp2.getString("kg", "关");

                if ((kg.equals("开") && !isInDZWL) || kg.equals("关")) {


                    if (sp != null && String.valueOf(sp.getMMSI()).equals("999999999")) {
                        if (ship2Tem != null && String.valueOf(ship2Tem.getMMSI()).equals("999999999")) {

                        } else {
                            setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
                            String cmd4 = "$PAIS,TXG,0*70\\r\\n";
                            UART1Tx(cmd4);
                            ship2Tem = sp;

                        }
                    }
                    if (sp != null && !String.valueOf(sp.getMMSI()).equals("999999999")) {
                        if (bg == 1) {
                            //发送gps消息到gps端口
                            String ms = sp2.getString("mode", "标准模式");
                            if (ms2.equals(ms) && ms.equals("智能模式") && bl != bl2) {
                                bl2 = bl;
                                if (bl == true) {

                                    setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                                    String cmd4 = "$PAIS,TXG,1*71\\r\\n";
                                    UART1Tx(cmd4);
                                } else {
                                    setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
                                    String cmd4 = "$PAIS,TXG,0*70\\r\\n";
                                    UART1Tx(cmd4);
                                }
                            }

                            if (!ms2.equals(ms)) {
                                ms2 = ms;
                                if (ms.equals("标准模式")) {
                                    setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                                    String cmd4 = "$PAIS,TXG,1*71\\r\\n";
                                    UART1Tx(cmd4);

                                } else if (ms.equals("智能模式") && bl == true) {
                                    setNodeString(AIS_SW_DEVICE_ELEC5V, "1");

                                    String cmd4 = "$PAIS,TXG,1*71\\r\\n";
                                    UART1Tx(cmd4);
                                } else if ((ms.equals("智能模式") && bl == false)) {
                                    setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
                                    String cmd4 = "$PAIS,TXG,0*70\\r\\n";
                                    UART1Tx(cmd4);
                                }
                            }

//                String zt = sp2.getString("aiszt", "开");
                            if (ms.equals("标准模式")) {
//                        setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                                UART0Tx(nmea + '\n');
                            }
                            //(ms.equals("智能模式") && bl && zt.equals("开"))
                            if (ms.equals("智能模式") && bl == true) {
//                        setNodeString(AIS_SW_DEVICE_ELEC5V, "1");
                                UART0Tx(nmea + '\n');
                            } else if ((ms.equals("智能模式") && bl == false)) {
//                        setNodeString(AIS_SW_DEVICE_ELEC5V, "0");
                            }
                        }
                    }
                }
            }
        };

/*
        sendable = new Runnable() {
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
        };
        new Thread(sendable).start();*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.addNmeaListener(nmeaListener);
    }

    //监控落水
    public static int checkHallState() {
        if (new File(STATE_PATCH).exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(
                        STATE_PATCH));
                String readLine = reader.readLine();
                Logger.i(TAG, "readLine =" + readLine);
                if (readLine != null && readLine.trim().equals("0")) {//close
                    return STATE_WATER;
                } else {
                    return STATE_NORMAL;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        } else {
            Logger.i("not exit");
        }
        return STATE_NORMAL;

    }


    private UEventObserver uEventObserver = new UEventObserver() {

        @Override
        public void onUEvent(UEvent event) {
            Logger.i("UEventObserver", "uEventObserver onUEvent()   " + event);
            int state = "0".equals(event.get("SWITCH_STATE")) ? STATE_WATER
                    : STATE_NORMAL;
            Logger.i(TAG, "uEventObserver onUEvent() state  " + state);
            if (mCurrentState != state) {
                mCurrentState = state;

            }
        }
    };

    private boolean phoneIsInUse() {
        boolean phoneInUse = false;
        try {
            ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
            if (phone != null) phoneInUse = !phone.isIdle();
        } catch (RemoteException e) {
            Log.w(TAG, "phone.isIdle() failed", e);
        }
        return phoneInUse;
    }

    Runnable lsrunnable = new Runnable() {
        Toast toast = null;

        @Override
        public void run() {
            while (true) {

                mCurrentState = checkHallState();
                if (new File(STATE_PATCH).exists()) {
                    uEventObserver.startObserving("DEVPATH=/devices/virtual/switch/water_det_switch");
                    Logger.i(TAG, "uEventObserver start Oberving  water_det_switch");

                }
                if (lsflg == 1) {
                    if (mCurrentState == 0 && !phoneIsInUse()) {
                        Message msg = new Message();
                        bjgs2();
                        Logger.i("进入了落水的报警" + lppd);
                        handler1.sendMessage(msg);
                        lsflg = 0;
                    }
                }
                if (mCurrentState == 1) {
                    lsflg = 1;
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * 初始化AIS，GPS
     *
     * @throws IOException
     */
    public void pdkj() throws IOException {
        sp2 = getSharedPreferences("sp", MODE_PRIVATE);
        int bg = sp2.getInt("fuwu", 0);
        if (bg == 1) {
            if (mSerialPort == null) {
                mSerialPort = new SerialPort(new File(SERIALPORT_DEVICE_NODE), SERIALPORT_BAUDRATE);
                Logger.d("初始化", "初始化mSerialPort");
            }
            if (mInputStream == null) {
                mInputStream = mSerialPort.getInputStream();
                Logger.d("初始化", "初始化mInputStream");
            }
            if (mOutputStream == null) {
                mOutputStream = mSerialPort.getOutputStream();
                Logger.d("初始化", "初始化mOutputStream");
            }
            if (db == null) {
                db = new DBManager(getApplicationContext());
                Logger.d("初始化", "初始化DBManager");
            }
            if (mReadThread == null) {
                Logger.d("初始化", "初始化mReadThread");
                mReadThread = new ReadThread();
                mReadThread.start();

            }
            setNodeString(AIS_SW_DEVICE_NODE, "1");
            Logger.d(TAG, getNodeString(AIS_SW_DEVICE_NODE));
            //初始化gps端口\
            if (mUART0 == null) {
                Logger.d("初始化", "初始化mUART0");
                init_UART0();
            }
            if (locationManager == null) {
                Logger.d("初始化", "初始化locationManager");
                getgps();
            }
        }
    }
}