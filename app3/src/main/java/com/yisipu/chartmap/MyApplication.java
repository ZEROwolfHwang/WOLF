package com.yisipu.chartmap;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class MyApplication extends Application {
    public static MyApplication sApplication;
    private static final String TAG = "YuChuan3";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Logger.init(TAG).logLevel(LogLevel.FULL);
        sApplication = this;
    }

    public static MyApplication getApplication() {
        // TODO Auto-generated method stub
        return sApplication;
    }
  /*  @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}
