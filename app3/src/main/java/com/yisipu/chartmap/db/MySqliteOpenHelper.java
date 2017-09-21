package com.yisipu.chartmap.db;

/**
 * Created by Administrator on 2016/8/9.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private static MySqliteOpenHelper mInstance = null;

    public synchronized static MySqliteOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySqliteOpenHelper(context);
        }
        return mInstance;
    };
    public MySqliteOpenHelper(Context context) {

        //context :上下文   ， name：数据库文件的名称    factory：用来创建cursor对象，默认为null
        //version:数据库的版本号，从1开始，如果发生改变，onUpgrade方法将会调用,4.0之后只能升不能将
        super(context, "chartmap.db", null,6);
    }

    //oncreate方法是数据库第一次创建的时候会被调用;  特别适合做表结构的初始化,需要执行sql语句；SQLiteDatabase db可以用来执行sql语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        //通过SQLiteDatabase执行一个创建表的sql语句
        db.execSQL("create table ship(_id integer primary key autoincrement,mmsi integer,sog integer,cog integer,fangwei double,distance double," +
                "latitude double,longitude double,precision integer,status integer,huhao varchar(20),chuanshou integer," +
                "rot double,englishname varchar(20),ChineseName varchar(50),country integer," +
                "type integer,dimBow integer,dimStern integer,dimPort integer,dimStarboard integer,updateTime integer,isMyShip integer,classType integer,otherName varchar(50),chinese_name_change  varchar(50))");
        db.execSQL("create table collect(_id integer primary key autoincrement,latitude double,longitude double,type integer,index_course integer,name varchar(50),course_name varchar(50),image integer)");
        /*
        接收报文类
         */
        db.execSQL("create table receiveMessage(_id integer primary key autoincrement,getMessage varchar(200),updateTime integer)");
    }

    //onUpgrade数据库版本号发生改变时才会执行； 特别适合做表结构的修改
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //添加一个phone字段
//        db.execSQL("alter table info add phone varchar(11)");
        if(oldVersion<2) {
            db.execSQL("create table collect(_id integer primary key autoincrement,latitude double,longitude double,type integer,index_course integer,name varchar(50),course_name varchar(50),image integer)");
        }
//        if(oldVersion<4) {
////            db.beginTransaction();
////            try {
//                upgradeDatabaseToVersion1(db);
////                db.setTransactionSuccessful();
////            } catch (Throwable ex) {
//////                Logger.e(TAG, ex.getMessage(), ex);
//////                break;
////            } finally {
////                db.endTransaction();
////            }
//        }
        if (oldVersion < 5) {
            upgradeDatabaseToVersion2(db);
        }
        if(oldVersion<6){
            db.execSQL("create table receiveMessage(_id integer primary key autoincrement,getMessage varchar(200),updateTime integer)");
        }
        }


    //定义升级函数
    private void upgradeDatabaseToVersion2(SQLiteDatabase db) {
        // Add 'new' column to mytable table.
        db.execSQL("ALTER TABLE ship RENAME TO __temp__Subscription2");
        db.execSQL("create table ship(_id integer primary key autoincrement,mmsi integer,sog integer,cog integer,fangwei double,distance double," +
                "latitude double,longitude double,precision integer,status integer,huhao varchar(20),chuanshou integer," +
                "rot double,englishname varchar(20),ChineseName varchar(50),country integer," +
                "type integer,dimBow integer,dimStern integer,dimPort integer,dimStarboard integer,updateTime integer,isMyShip integer,classType integer,otherName varchar(50),chinese_name_change  varchar(50))");
//        db.execSQL("ALTER TABLE ship ADD COLUMN otherName varchar(50)");
//       db.execSQL("INSERT INTO TABLE ship SELECT _id,otherName FROM __temp__Subscription");
        db.execSQL("DROP TABLE __temp__Subscription2");

    }
    //定义升级函数
    private void upgradeDatabaseToVersion1(SQLiteDatabase db) {
        // Add 'new' column to mytable table.
        db.execSQL("ALTER TABLE ship RENAME TO __temp__Subscription");
        db.execSQL("create table ship(_id integer primary key autoincrement,mmsi integer,sog integer,cog integer,fangwei double,distance double," +
                "latitude double,longitude double,precision integer,status integer,huhao varchar(20),chuanshou integer," +
                "rot double,englishname varchar(20),ChineseName varchar(50),country integer," +
                "type integer,dimBow integer,dimStern integer,dimPort integer,dimStarboard integer,updateTime integer,isMyShip integer,classType integer,otherName varchar(50))");
//        db.execSQL("ALTER TABLE ship ADD COLUMN otherName varchar(50)");
//       db.execSQL("INSERT INTO TABLE ship SELECT _id,otherName FROM __temp__Subscription");
     db.execSQL("DROP TABLE __temp__Subscription");

    }
}