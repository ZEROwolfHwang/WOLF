package com.yisipu.chartmap.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.servicer.MyDataServicer;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/10.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent service = new Intent(context, MyDataServicer.class);
            sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);//创建对象，Datadefault是储存数据的对象名
            SharedPreferences.Editor editor = sp.edit();//获取编辑对象
            editor.putInt("fuwu",0);//keyname是储存数据的键值名，同一个对象可以保存多个键值
            editor.commit();//提交保存修改
            SharedPreferences sp=context.getSharedPreferences("sp",Context.MODE_PRIVATE);
            //接受到关闭服务广播
//            boolean isClose=  sp.getBoolean("close_service",false);
//            if(!isClose) {

            SharedPreferences.Editor editor2=sp.edit();

            editor2.putBoolean("close_service",false);

            editor.commit();//提交保存修改
                context.startService(service);
//            }
            Logger.d("TAG1", "开机自启动服务");
            DBManager db=new DBManager(context);
            Logger.d("TAG2", "开机自启动服务");
            Date dt = new Date();
            Logger.d("TAG3", "开机自启动服务");
            Long time = dt.getTime();
//            db.deleteTimeShipBean(time);
         db.deleteShipBean();
//            Intent startapp=new Intent(context, MainActivity.class);
//            startapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(startapp);
            Logger.d("TAG4", "开机自启动服务");
        }
    }
}