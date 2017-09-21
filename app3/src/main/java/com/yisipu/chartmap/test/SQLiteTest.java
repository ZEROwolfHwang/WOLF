package com.yisipu.chartmap.test;


import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.db.MySqliteOpenHelper;

import java.util.List;


public class SQLiteTest extends InstrumentationTestCase {
    //1.测试创建数据库的方法
    public void testCreateDB(){
        //1.创建数据库
        MySqliteOpenHelper  helper = new MySqliteOpenHelper(getInstrumentation().getContext());
        //2.得到相应的数据库
        SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("INSERT INTO account(_id,name, balance) VALUES(1,'fuck', 10000)");
        Logger.i("哈哈4555dfsfewewew");

    }

    //2.测试添加数据的方法
//    public void testAdd(){
//       DBManager dao = new DBManager(getInstrumentation().getContext());// Context android.test.AndroidTestCase.getContext()
//        ShipBean sb=null;
//        for(int i=1;i<100;i++){
//           sb=new ShipBean();
//            sb.setMMSI(i);
//            sb.setCog(i+1);
//           dao.addShipBean(sb);
//        }
//    }
//    public void
    //3.测试删除数据的方法
    public void testDelete(){
        DBManager dao = new DBManager(getInstrumentation().getContext());
        ShipBean sb=new ShipBean();
        sb.setMMSI(1);
        for(int i=100;i<200;i++){
            dao.deleteShipBean(sb);
        }
    }
    //4.测试修改数据的方法
//    public void testUpdate(){
//        AccountDao dao = new AccountDao(getContext());
//        Account a = new Account(1,"fuck you!!!~",999);
//        dao.update(a);
//    }
    //5.测试查询数据的方法，根据id查询数据库

    public void testQuery(){
        DBManager dao = new DBManager(getInstrumentation().getContext());
        Logger.i("zzzzz"+dao.getEditShipBean(1));
        Logger.i("zzzzz"+dao.getEditShipBean(3));
        Logger.i("zzzzz"+dao.getEditShipBean(4));
    }
    //6.测试查询所有的方法
    public void testQueryAll(){
        Logger.i("哈哈4555dfsfewewew");
        DBManager dao = new DBManager(getInstrumentation().getContext());

        List<ShipBean> list =null;
        list = dao.getShipBeans();
        for (ShipBean Sb : list) {
            Logger.i("哈哈4555"+Sb.toString());
        }
    }
    //6.测试查询所有的方法
    public void testQueryMy(){
        Logger.i("哈哈4555dfsfewewew");
        DBManager dao = new DBManager(getInstrumentation().getContext());
        Logger.i("zzzzz"+dao.getMyShip());

    }
    //7.测试分布查询的方法
//    public void testQueryPage(){
//        AccountDao dao = new AccountDao(getContext());
//        List<Account> list = null;
//        list = dao.queryPage(2, 10);
//        for (Account account : list) {
//            Logger.i(account);
//        }
//    }
    //8.测试查询总记录条数的方法
//    public void testQueryCount(){
//        AccountDao dao = new AccountDao(getContext());
//        int count = dao.queryCount();
//        Logger.i(count);
//    }
}