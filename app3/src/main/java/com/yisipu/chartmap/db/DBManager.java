package com.yisipu.chartmap.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.MessageHaiYuBean;
import com.yisipu.chartmap.bean.ShipBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *
 */
public class DBManager {
    private MySqliteOpenHelper helper;
    private SQLiteDatabase db;


    public DBManager(Context context) {
        helper = MySqliteOpenHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    /*
    查询是否存在指定mmsi的ship,不存在则为null
     */
    public ShipBean getEditShipBean(int mmsi) {
        ShipBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from ship where mmsi=?", new String[]{String.valueOf(mmsi)});

        while (cursor.moveToNext()) {
            editBean = new ShipBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setMMSI(cursor.getInt(cursor
                    .getColumnIndex("mmsi")));
            editBean.setReal_sudu(cursor.getInt(cursor
                    .getColumnIndex("chuanshou")));
            editBean.setSog(cursor.getInt(cursor
                    .getColumnIndex("sog")));
            editBean.setCog(cursor.getInt(cursor
                    .getColumnIndex("cog")));
            editBean.setFangwei(cursor.getDouble(cursor
                    .getColumnIndex("fangwei")));
            editBean.setDistance(cursor.getDouble(cursor
                    .getColumnIndex("distance")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setPrecision(cursor.getInt(cursor
                    .getColumnIndex("precision")));
            editBean.setStatus(cursor.getInt(cursor
                    .getColumnIndex("status")));
            editBean.setHuhao(cursor.getString(cursor
                    .getColumnIndex("huhao")));
            editBean.setRot(cursor.getDouble(cursor
                    .getColumnIndex("rot")));
            editBean.setEnglishName(cursor.getString(cursor
                    .getColumnIndex("englishname")));
            editBean.setChineseName(cursor.getString(cursor
                    .getColumnIndex("ChineseName")));
            editBean.setCountry(cursor.getInt(cursor
                    .getColumnIndex("country")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setDimBow(cursor.getInt(cursor
                    .getColumnIndex("dimBow")));
            editBean.setDimStern(cursor.getInt(cursor
                    .getColumnIndex("dimStern")));
            editBean.setDimPort(cursor.getInt(cursor
                    .getColumnIndex("dimPort")));
            editBean.setDimStarboard(cursor.getInt(cursor
                    .getColumnIndex("dimStarboard")));
            editBean.setClassType(cursor.getInt(cursor
                    .getColumnIndex("classType")));
            editBean.setOtherName(cursor.getString(cursor
                    .getColumnIndex("otherName")));
            editBean.setChineseNameChange(cursor.getString(cursor
                    .getColumnIndex("chinese_name_change")));
            int isMyship = cursor.getInt(cursor.getColumnIndex("isMyShip"));
            if (isMyship == 1) {
                editBean.setMyShip(true);
            } else {
                editBean.setMyShip(false);
            }
            editBean.setUpdateTime(cursor.getInt(cursor
                    .getColumnIndex("updateTime")));
        }
        cursor.close();

        return editBean;
    }

    public ShipBean getMyShip() {
        ShipBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from ship where isMyShip=?", new String[]{String.valueOf(1)});
//		Cursor cursor = db.rawQuery(
//				"select * from ship where isMyShip=?", new String[] {String.valueOf(LocationUtils.gps2d(1,2,3,4))});
        while (cursor.moveToNext()) {
            editBean = new ShipBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setMMSI(cursor.getInt(cursor
                    .getColumnIndex("mmsi")));
            editBean.setMyShip(true);
            editBean.setReal_sudu(cursor.getInt(cursor
                    .getColumnIndex("chuanshou")));
            editBean.setSog(cursor.getInt(cursor
                    .getColumnIndex("sog")));
            editBean.setCog(cursor.getInt(cursor
                    .getColumnIndex("cog")));
            editBean.setFangwei(cursor.getDouble(cursor
                    .getColumnIndex("fangwei")));
            editBean.setDistance(cursor.getDouble(cursor
                    .getColumnIndex("distance")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setPrecision(cursor.getInt(cursor
                    .getColumnIndex("precision")));
            editBean.setStatus(cursor.getInt(cursor
                    .getColumnIndex("status")));
            editBean.setHuhao(cursor.getString(cursor
                    .getColumnIndex("huhao")));
            editBean.setRot(cursor.getDouble(cursor
                    .getColumnIndex("rot")));
            editBean.setEnglishName(cursor.getString(cursor
                    .getColumnIndex("englishname")));
            editBean.setChineseName(cursor.getString(cursor
                    .getColumnIndex("ChineseName")));
            editBean.setCountry(cursor.getInt(cursor
                    .getColumnIndex("country")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setDimBow(cursor.getInt(cursor
                    .getColumnIndex("dimBow")));
            editBean.setDimStern(cursor.getInt(cursor
                    .getColumnIndex("dimStern")));
            editBean.setDimPort(cursor.getInt(cursor
                    .getColumnIndex("dimPort")));
            editBean.setDimStarboard(cursor.getInt(cursor
                    .getColumnIndex("dimStarboard")));
            editBean.setUpdateTime(cursor.getInt(cursor
                    .getColumnIndex("updateTime")));
            editBean.setClassType(cursor.getInt(cursor
                    .getColumnIndex("classType")));
            editBean.setOtherName(cursor.getString(cursor
                    .getColumnIndex("otherName")));
            editBean.setChineseNameChange(cursor.getString(cursor
                    .getColumnIndex("chinese_name_change")));
        }
        cursor.close();

        return editBean;
    }

    /**
     * 用途：添加一个船信息
     */
    public void addShipBean(ShipBean shipBean) {
        if (shipBean == null) {

            return;
        }
        Date dt = new Date();
        Long time = dt.getTime();
        db.beginTransaction();

        if (shipBean.getMyShip()) {
            ShipBean temMy = getMyShip();
            if (temMy != null) {
                ContentValues contentValues = new ContentValues();
                // 先把所有的变成1，不是默认
//			contentValues.put("mmsi",shipBean.getMMSI());
                if (shipBean.getSog() != -1) {
                    contentValues.put("sog", shipBean.getSog());
                }
                if (shipBean.getCog() != -1) {
                    contentValues.put("cog", shipBean.getCog());
                }
                contentValues.put("mmsi", shipBean.getMMSI());
                contentValues.put("fangwei", shipBean.getFangwei());
                contentValues.put("distance", shipBean.getDistance());
                if (shipBean.getLatitude() != -1 && shipBean.getLongitude() != -1) {
                    contentValues.put("latitude", shipBean.getLatitude());
                    contentValues.put("longitude", shipBean.getLongitude());
                }
                if (shipBean.getPrecision() != -1) {
                    contentValues.put("precision", shipBean.getPrecision());
                }
                if (shipBean.getStatus() != -1) {
                    contentValues.put("status", shipBean.getStatus());
                }
                if (shipBean.getHuhao() != null) {
                    contentValues.put("huhao", shipBean.getHuhao());
                }
                if (shipBean.getReal_sudu() != -1) {
                    contentValues.put("chuanshou", shipBean.getReal_sudu());
                }
                if (shipBean.getRot() != -1) {
                    contentValues.put("rot", shipBean.getRot());
                }
                if (!shipBean.getEnglishName().equals("")) {
                    contentValues.put("englishname", shipBean.getEnglishName());
                }
                if (!shipBean.getChineseName().equals("")) {
                    contentValues.put("ChineseName", shipBean.getChineseName());
                }
                if (shipBean.getCountry() != -1) {
                    contentValues.put("country", shipBean.getCountry());
                }
                if (shipBean.getType() != -1) {
                    contentValues.put("type", shipBean.getType());
                }
                if (shipBean.getDimBow() != -1) {

                    contentValues.put("dimBow", shipBean.getDimBow());
                }
                if (shipBean.getDimStern() != -1) {
                    contentValues.put("dimStern", shipBean.getDimStern());
                }
                if (shipBean.getDimPort() != -1) {

                    contentValues.put("dimPort", shipBean.getDimPort());
                }
                if (shipBean.getDimStarboard() != -1) {
                    contentValues.put("dimStarboard", shipBean.getDimStarboard());
                }
                if (shipBean.getClassType() != -1) {
                    contentValues.put("classType", shipBean.getClassType());

                }
                if (!shipBean.getOtherName().equals("")) {
                    contentValues.put("otherName", shipBean.getOtherName());

                }
                if (!shipBean.getChineseNameChange().equals("")) {
                    contentValues.put("chinese_name_change", shipBean.getChineseNameChange());

                }
                contentValues.put("updateTime", time);
                contentValues.put("isMyShip", 1);
                db.update("ship", contentValues, " isMyShip= ?",
                        new String[]{String.valueOf(1)});
            } else {
                db.execSQL(
                        "insert into ship values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{null,
                                shipBean.getMMSI(),
                                shipBean.getSog(),
                                shipBean.getCog(),
                                shipBean.getFangwei(),
                                shipBean.getDistance(),
                                shipBean.getLatitude(),
                                shipBean.getLongitude(),
                                shipBean.getPrecision(),
                                shipBean.getStatus(),
                                shipBean.getHuhao(),
                                shipBean.getReal_sudu(),
                                shipBean.getRot(),
                                shipBean.getEnglishName(),
                                shipBean.getChineseName(),
                                shipBean.getCountry(),
                                shipBean.getType(),
                                shipBean.getDimBow(),
                                shipBean.getDimStern(),
                                shipBean.getDimPort(),
                                shipBean.getDimStarboard(),
                                time,
                                1,
                                shipBean.getClassType(),
                                shipBean.getOtherName(),
                                shipBean.getChineseNameChange()
                        });

            }

            db.setTransactionSuccessful();
            db.endTransaction();
            return;
        }
        ShipBean tempBean = getEditShipBean(shipBean.getMMSI());

        //0为非本船，1为本船
        int isMyShip = 0;
        if (shipBean.getMyShip()) {
            isMyShip = 1;
        }
        if (tempBean != null) {
            ContentValues contentValues = new ContentValues();
            // 先把所有的变成1，不是默认
//			contentValues.put("mmsi",shipBean.getMMSI());
            if (shipBean.getSog() != -1) {
                contentValues.put("sog", shipBean.getSog());
            }
            if (shipBean.getCog() != -1) {
                contentValues.put("cog", shipBean.getCog());
            }
            contentValues.put("fangwei", shipBean.getFangwei());
            contentValues.put("distance", shipBean.getDistance());
            if (shipBean.getLatitude() != -1 && shipBean.getLongitude() != -1) {
                contentValues.put("latitude", shipBean.getLatitude());
                contentValues.put("longitude", shipBean.getLongitude());
            }
            if (shipBean.getPrecision() != -1) {
                contentValues.put("precision", shipBean.getPrecision());
            }
            if (shipBean.getStatus() != -1) {
                contentValues.put("status", shipBean.getStatus());
            }
            if (shipBean.getHuhao() != null) {
                contentValues.put("huhao", shipBean.getHuhao());
            }
            if (shipBean.getReal_sudu() != -1) {
                contentValues.put("chuanshou", shipBean.getReal_sudu());
            }
            if (shipBean.getRot() != -1) {
                contentValues.put("rot", shipBean.getRot());
            }
            if (!shipBean.getEnglishName().equals("")) {
                contentValues.put("englishname", shipBean.getEnglishName());
            }
            if (!shipBean.getChineseName().equals("")) {
                contentValues.put("ChineseName", shipBean.getChineseName());
            }
            if (shipBean.getCountry() != -1) {
                contentValues.put("country", shipBean.getCountry());
            }
            if (shipBean.getType() != -1) {
                contentValues.put("type", shipBean.getType());
            }
            if (shipBean.getDimBow() != -1) {

                contentValues.put("dimBow", shipBean.getDimBow());
            }
            if (shipBean.getDimStern() != -1) {
                contentValues.put("dimStern", shipBean.getDimStern());
            }
            if (shipBean.getDimPort() != -1) {

                contentValues.put("dimPort", shipBean.getDimPort());
            }
            if (shipBean.getDimStarboard() != -1) {
                contentValues.put("dimStarboard", shipBean.getDimStarboard());
            }
            if (shipBean.getClassType() != -1) {
                contentValues.put("classType", shipBean.getClassType());

            }
            if (!shipBean.getOtherName().equals("")) {
                contentValues.put("otherName", shipBean.getOtherName());

            }
            if (!shipBean.getChineseNameChange().equals("")) {
                contentValues.put("chinese_name_change", shipBean.getChineseNameChange());

            }
            contentValues.put("updateTime", time);
            contentValues.put("isMyShip", isMyShip);
            db.update("ship", contentValues, " mmsi= ?",
                    new String[]{String.valueOf(shipBean.getMMSI())});
        } else {
            db.execSQL(
                    "insert into ship values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{null,
                            shipBean.getMMSI(),
                            shipBean.getSog(),
                            shipBean.getCog(),
                            shipBean.getFangwei(),
                            shipBean.getDistance(),
                            shipBean.getLatitude(),
                            shipBean.getLongitude(),
                            shipBean.getPrecision(),
                            shipBean.getStatus(),
                            shipBean.getHuhao(),
                            shipBean.getReal_sudu(),
                            shipBean.getRot(),
                            shipBean.getEnglishName(),
                            shipBean.getChineseName(),
                            shipBean.getCountry(),
                            shipBean.getType(),
                            shipBean.getDimBow(),
                            shipBean.getDimStern(),
                            shipBean.getDimPort(),
                            shipBean.getDimStarboard(),
                            time,
                            isMyShip,
                            shipBean.getClassType(),
                            shipBean.getOtherName(),
                            shipBean.getChineseNameChange()
                    });
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    /**
     * 更新一条数据
     */
    public void updateShipBean(ShipBean shipBean) {
        ShipBean tempBean = getEditShipBean(shipBean.getMMSI());
        Date dt = new Date();
        Long time = dt.getTime();
        //0为非本船，1为本船
        int isMyShip = 0;
        if (shipBean.getMyShip()) {
            isMyShip = 1;
        }

        ContentValues contentValues = new ContentValues();

        // 先把所有的变成1，不是默认
//			contentValues.put("mmsi",shipBean.getMMSI());
        contentValues.put("sog", shipBean.getSog());
        contentValues.put("cog", shipBean.getCog());
        contentValues.put("fangwei", shipBean.getFangwei());
        contentValues.put("distance", shipBean.getDistance());
        contentValues.put("latitude", shipBean.getLatitude());
        contentValues.put("longitude", shipBean.getLongitude());
        contentValues.put("precision", shipBean.getPrecision());
        contentValues.put("status", shipBean.getStatus());
        contentValues.put("huhao", shipBean.getHuhao());
        contentValues.put("chuanshou", shipBean.getReal_sudu());
        contentValues.put("rot", shipBean.getRot());
        contentValues.put("englishname", shipBean.getEnglishName());
        contentValues.put("ChineseName", shipBean.getChineseName());
        contentValues.put("country", shipBean.getCountry());
        contentValues.put("type", shipBean.getType());
        contentValues.put("dimBow", shipBean.getDimBow());
        contentValues.put("dimStern", shipBean.getDimStern());
        contentValues.put("dimPort", shipBean.getDimPort());
        contentValues.put("dimStarboard", shipBean.getDimStarboard());
        contentValues.put("updateTime", time);
        contentValues.put("isMyShip", isMyShip);
        contentValues.put("otherName", shipBean.getOtherName());
       contentValues.put("chinese_name_change", shipBean.getChineseNameChange());

        db.update("ship", contentValues, "mmsi = ?",
                new String[]{String.valueOf(shipBean.getMMSI())});

    }

    /**
     * 删除一个数据 用途：
     */
    public void deleteShipBean(ShipBean editShipBean) {
        db.delete("ship", "mmsi = ?",
                new String[]{String.valueOf(editShipBean.getMMSI())});

    }

    /**
     * 删除一个数据根据时间10分钟不更新 用途：
     */
    public void deleteTimeShipBean(long nowTime) {
        db.execSQL("delete from ship where isMyShip=0 And (?-updateTime)>600000", new Object[]{nowTime});
//		db.execSQL("delete from ship where isMyShip=0 And (?-updateTime)>5000",new Object[]{nowTime});


    }

    /**
     * 删除数据：
     */
    public void deleteShipBean() {
        db.execSQL("delete from ship where isMyShip=0");
//		db.execSQL("delete from ship where isMyShip=0 And (?-updateTime)>5000",new Object[]{nowTime});


    }

    /**
     * 删除一个数据 用途：
     */
    public void deleteAllShipBean() {

        db.delete("ship", null, null);

    }

    /**
     * 取出所有数据 用途：
     */
    public List<ShipBean> getShipBeans() {
        List<ShipBean> editShipBeans = new ArrayList<ShipBean>();
        ShipBean editBean = null;
        Cursor cursor = db.rawQuery("select * from ship", null);
        while (cursor.moveToNext()) {
            editBean = new ShipBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setMMSI(cursor.getInt(cursor
                    .getColumnIndex("mmsi")));
            editBean.setReal_sudu(cursor.getInt(cursor
                    .getColumnIndex("chuanshou")));
            editBean.setSog(cursor.getInt(cursor
                    .getColumnIndex("sog")));
            editBean.setCog(cursor.getInt(cursor
                    .getColumnIndex("cog")));
            editBean.setFangwei(cursor.getDouble(cursor
                    .getColumnIndex("fangwei")));
            editBean.setDistance(cursor.getDouble(cursor
                    .getColumnIndex("distance")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setPrecision(cursor.getInt(cursor
                    .getColumnIndex("precision")));
            editBean.setStatus(cursor.getInt(cursor
                    .getColumnIndex("status")));
            editBean.setHuhao(cursor.getString(cursor
                    .getColumnIndex("huhao")));
            editBean.setRot(cursor.getDouble(cursor
                    .getColumnIndex("rot")));
            editBean.setEnglishName(cursor.getString(cursor
                    .getColumnIndex("englishname")));
            editBean.setChineseName(cursor.getString(cursor
                    .getColumnIndex("ChineseName")));
            editBean.setCountry(cursor.getInt(cursor
                    .getColumnIndex("country")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setDimBow(cursor.getInt(cursor
                    .getColumnIndex("dimBow")));
            editBean.setDimStern(cursor.getInt(cursor
                    .getColumnIndex("dimStern")));
            editBean.setDimPort(cursor.getInt(cursor
                    .getColumnIndex("dimPort")));
            editBean.setDimStarboard(cursor.getInt(cursor
                    .getColumnIndex("dimStarboard")));
            editBean.setUpdateTime(cursor.getInt(cursor
                    .getColumnIndex("updateTime")));
            editBean.setOtherName(cursor.getString(cursor
                    .getColumnIndex("otherName")));
            editBean.setChineseNameChange(cursor.getString(cursor
                    .getColumnIndex("chinese_name_change")));
            int isMyship = cursor.getInt(cursor.getColumnIndex("isMyShip"));
            if (isMyship == 1) {
                editBean.setMyShip(true);
            } else {
                editBean.setMyShip(false);
            }
            editBean.setClassType(cursor.getInt(cursor
                    .getColumnIndex("classType")));

            editShipBeans.add(editBean);
        }
        cursor.close();

        return editShipBeans;
    }




    //
//
    public void closeDb() {
        db.close();
    }

    /**
     * 根据航速查询：
     */
    public List<ShipBean> getSog(int Sog) {
        List<ShipBean> editShipBeans = new ArrayList<ShipBean>();
        ShipBean editBean = null;
        Cursor cursor = db.rawQuery("select * from ship where Sog>?", new String[]{String.valueOf(Sog)});
        while (cursor.moveToNext()) {
            editBean = new ShipBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setMMSI(cursor.getInt(cursor
                    .getColumnIndex("mmsi")));
            editBean.setReal_sudu(cursor.getInt(cursor
                    .getColumnIndex("chuanshou")));
            editBean.setSog(cursor.getInt(cursor
                    .getColumnIndex("sog")));
            editBean.setCog(cursor.getInt(cursor
                    .getColumnIndex("cog")));
            editBean.setFangwei(cursor.getDouble(cursor
                    .getColumnIndex("fangwei")));
            editBean.setDistance(cursor.getDouble(cursor
                    .getColumnIndex("distance")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setPrecision(cursor.getInt(cursor
                    .getColumnIndex("precision")));
            editBean.setStatus(cursor.getInt(cursor
                    .getColumnIndex("status")));
            editBean.setHuhao(cursor.getString(cursor
                    .getColumnIndex("huhao")));
            editBean.setRot(cursor.getDouble(cursor
                    .getColumnIndex("rot")));
            editBean.setEnglishName(cursor.getString(cursor
                    .getColumnIndex("englishname")));
            editBean.setChineseName(cursor.getString(cursor
                    .getColumnIndex("ChineseName")));
            editBean.setCountry(cursor.getInt(cursor
                    .getColumnIndex("country")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setDimBow(cursor.getInt(cursor
                    .getColumnIndex("dimBow")));
            editBean.setDimStern(cursor.getInt(cursor
                    .getColumnIndex("dimStern")));
            editBean.setDimPort(cursor.getInt(cursor
                    .getColumnIndex("dimPort")));
            editBean.setDimStarboard(cursor.getInt(cursor
                    .getColumnIndex("dimStarboard")));
            editBean.setUpdateTime(cursor.getInt(cursor
                    .getColumnIndex("updateTime")));
            editBean.setOtherName(cursor.getString(cursor
                    .getColumnIndex("otherName")));
            editBean.setChineseNameChange(cursor.getString(cursor
                    .getColumnIndex("chinese_name_change")));
            int isMyship = cursor.getInt(cursor.getColumnIndex("isMyShip"));
            if (isMyship == 1) {
                editBean.setMyShip(true);
            } else {
                editBean.setMyShip(false);
            }
            editBean.setClassType(cursor.getInt(cursor
                    .getColumnIndex("classType")));

            editShipBeans.add(editBean);
        }
        cursor.close();

        return editShipBeans;
    }

    /*
    通过名称查询收藏点
     */

    public CollectPointBean getCollect(String name) {
        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where name=? and type=?", new String[]{name, String.valueOf(0)});

        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));


        }
        cursor.close();

        return editBean;
    }

    /*
    添加收藏点
     */
    public void addCollectPoint(CollectPointBean collectPointBean) {
        if (collectPointBean == null) {

            return;
        }

        db.beginTransaction();

        db.execSQL(
                "insert into collect values(?,?,?,?,?,?,?,?)",
                new Object[]{null,
                        collectPointBean.getLatitude(),
                        collectPointBean.getLongitude(),
                        collectPointBean.getType(),
                        collectPointBean.getIndex(),
                        collectPointBean.getName(),
                        collectPointBean.getCourse_name(),
                        collectPointBean.getImage()
                });


        db.setTransactionSuccessful();
        db.endTransaction();
        return;
    }

    /*
    修改通过id修改收藏点
     */
    public void updateCollectBean(CollectPointBean collectPointBean) {
        CollectPointBean tempBean = getCollectById(collectPointBean.getId());
        if (tempBean == null) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        // 先把所有的变成1，不是默认
//			contentValues.put("mmsi",shipBean.getMMSI());
        contentValues.put("name", collectPointBean.getName());
        contentValues.put("latitude", collectPointBean.getLatitude());
        contentValues.put("longitude", collectPointBean.getLongitude());
        contentValues.put("type", collectPointBean.getType());
        contentValues.put("index_course", collectPointBean.getIndex());
        contentValues.put("course_name", collectPointBean.getCourse_name());
        contentValues.put("image", collectPointBean.getImage());


        db.update("collect", contentValues, "_id= ?",
                new String[]{String.valueOf(tempBean.getId())});

    }

    /*
    根据id 查询收藏点
     */
    public CollectPointBean getCollectById(int id) {
        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where _id=?", new String[]{String.valueOf(id)});

        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));

        }
        cursor.close();

        return editBean;
    }

    /*
    查询所有航点 （不是航线上的点，type=0）
     */
    public List<CollectPointBean> getCollects() {
        List<CollectPointBean> ls = new ArrayList<>();

        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where type=?", new String[]{String.valueOf(0)});
        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));

            ls.add(editBean);
        }
        cursor.close();

        return ls;

    }
    /*
    删除收藏的航点
     */

    /**
     * 删除一个数据 用途：
     */
    public void deleteCollectBean(String name) {
        db.delete("collect", "name=? and type=0",
                new String[]{String.valueOf(name)});

    }

    /**
     * 删除所有航点数据
     */
    public void deleteAllCollectBean() {
        db.delete("collect",  "type=0",
                null);

    }
    /**
     * 删除所有航线数据
     */
    public void deleteAllCourseCollectBean() {
        db.delete("collect",  "type=1",
                null);

    }
    /*
    删除航线(所有的航线上的航点)
     */
    public void deleteCourse(String course_name) {
        db.delete("collect", "course_name=? and type=1",
                new String[]{String.valueOf(course_name)});

    }

    /*
增加航线(所有的航线上的航点)
 */
    public void addCourse(List<CollectPointBean> ls) {
        if (ls == null || ls.size() == 0) {

            return;
        }

        db.beginTransaction();
        for (CollectPointBean collectPointBean : ls) {
            db.execSQL(
                    "insert into collect values(?,?,?,?,?,?,?,?)",
                    new Object[]{null,
                            collectPointBean.getLatitude(),
                            collectPointBean.getLongitude(),
                            collectPointBean.getType(),
                            collectPointBean.getIndex(),
                            collectPointBean.getName(),
                            collectPointBean.getCourse_name(),
                            collectPointBean.getImage()
                    });


        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return;
    }

    /*
    获得所有航线名无重复
     */
    public List<String> getAllCourseName() {
        List<String> a = new ArrayList<>();
        String editBean = null;
        Cursor cursor = db.query(true, "collect", new String[]{"_id", "course_name"}, null, null, "course_name", null, null, null);

//        Cursor cursor = db.rawQuery(
//                "select course_name from collect where type=1",null);
        while (cursor.moveToNext()) {
            if (!TextUtils.isEmpty(cursor.getString(cursor
                    .getColumnIndex("course_name")))) {
                editBean = cursor.getString(cursor
                        .getColumnIndex("course_name"));
                a.add(editBean);
            }
        }
        cursor.close();
        return a;
    }
     /*
    通过航线名称和index查询航线上的点
     */

    public CollectPointBean getCourse(String Course_name, int index) {
        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where course_name=? and type=? and index_course=?", new String[]{Course_name, String.valueOf(1), String.valueOf(index)});

        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));


        }
        cursor.close();

        return editBean;
    }
    /*
    判断是否存在该航线名称
     */

    public CollectPointBean getCourseName(String Course_name) {
        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where course_name=? and type=? ", new String[]{Course_name, String.valueOf(1)});

        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));

            break;


        }
        cursor.close();

        return editBean;
    }
    /*
    通过航线名 搜索所有的在该航线的航点名
     */

    public List<CollectPointBean> getCoursePoints(String course_name) {
        List<CollectPointBean> ls = new ArrayList<>();

        CollectPointBean editBean = null;
        Cursor cursor = db.rawQuery(
                "select * from collect where type=? and course_name=? order by index_course asc", new String[]{String.valueOf(1), course_name});
        while (cursor.moveToNext()) {
            editBean = new CollectPointBean();
            editBean.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            editBean.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            editBean.setType(cursor.getInt(cursor
                    .getColumnIndex("type")));
            editBean.setIndex(cursor.getInt(cursor
                    .getColumnIndex("index_course")));
            editBean.setName(cursor.getString(cursor
                    .getColumnIndex("name")));
            editBean.setImage(cursor.getInt(cursor
                    .getColumnIndex("image")));
            editBean.setCourse_name(cursor.getString(cursor
                    .getColumnIndex("course_name")));
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));

            ls.add(editBean);
        }
        cursor.close();

        return ls;

    }


    /*
    海渔平台接受到消息
     */
 /*
    查询所有收到的报文
     */
    public List<MessageHaiYuBean> getMessageHaiYu() {
        List<MessageHaiYuBean> ls = new ArrayList<>();

        MessageHaiYuBean editBean = null;

        Cursor cursor = db.rawQuery(
                "select * from receiveMessage order by updateTime desc",null);
        while (cursor.moveToNext()) {
            editBean = new MessageHaiYuBean();
            editBean.setGetMessage(cursor.getString(cursor
                    .getColumnIndex("getMessage")));
            editBean.setId(cursor.getInt(cursor
                    .getColumnIndex("_id")));
            editBean.setUpdateTime(cursor.getInt(cursor
                    .getColumnIndex("updateTime")));


            ls.add(editBean);
        }
        cursor.close();

        return ls;

    }
    /*
   添加报文
    */
    public void addMessageHaiYu(MessageHaiYuBean collectPointBean) {
        if (collectPointBean == null) {

            return;
        }

        db.beginTransaction();

        db.execSQL(
                "insert into receiveMessage values(?,?,?)",
                new Object[]{null,
                        collectPointBean.getGetMessage(),
                        collectPointBean.getUpdateTime(),
                });


        db.setTransactionSuccessful();
        db.endTransaction();
        return;
    }
}
