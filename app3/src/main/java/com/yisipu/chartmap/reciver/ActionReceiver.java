package com.yisipu.chartmap.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.servicer.MyDataServicer;
import com.yisipu.chartmap.utils.ServiceIsUser;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ActionReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

           SharedPreferences sp=context.getSharedPreferences("sp",Context.MODE_PRIVATE);
       //接受到关闭服务广播
      boolean isClose=  sp.getBoolean("close_service",false);
            boolean isUse = ServiceIsUser.isServiceWork(context, "com.yisipu.chartmap.servicer.MyDataServicer");
            if (!isUse&&!isClose) {
                Intent service = new Intent(context, MyDataServicer.class);
                context.startService(service);
            }
        }
    }
}
