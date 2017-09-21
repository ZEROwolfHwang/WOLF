package com.yisipu.chartmap.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.utils.ExtenSdCard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */
public class HaiTuSqliteDb {
    /*
 配置获取路径
  */
    public static String getPath(){

        if(ExtenSdCard.getSecondExterPath()!=null){
            if(ExtenSdCard.isSecondSDcardMounted()){
                return ExtenSdCard.getSecondExterPath();
            }
        }

        return Environment.getExternalStorageDirectory().getPath();
    }
    private static String DatabaseFilename=null;
    SQLiteDatabase db;
    //    private final String DATABASE_PATH = android.os.Environment
//            .getExternalStorageDirectory().getAbsolutePath() + "/vote";
    private static final String DATABASE_PATH =  getPath()+"/chartmap/map_database";
//    private static String DATABASE_FILENAME = "map.sqlitedb";


    // 初始化数据库
    public static SQLiteDatabase openDatabase(String mdatabaseFilename) {
        DatabaseFilename= mdatabaseFilename;
        SQLiteDatabase db = null;
        File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();

        try {
            String databaseFilename = DATABASE_PATH + "/" + DatabaseFilename;
            File file = new File(databaseFilename);
            if (!file.exists()) {
                return null;
            }
//            File dir = new File(DATABASE_PATH);
//            if (!dir.exists())
//                dir.mkdir();
//            if (!(new File(databaseFilename)).exists()) {
//                InputStream is = getResources().openRawResource(R.raw.db_vote);
//                FileOutputStream fos = new FileOutputStream(databaseFilename);
//                byte[] buffer = new byte[8192];
//                int count = 0;
//                while ((count = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, count);
//                }
//                fos.close();
//                is.close();
//            }
            db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

//    public static List<Integer> getZoomList() {
//        HaiTuSqliteDb s = new HaiTuSqliteDb();
////        SQLiteDatabase db =s.openDatabase(context.getApplicationContext());
//
//        SQLiteDatabase db = s.openDatabase();
//        List<Integer> zList = new ArrayList<>();
//        zList.clear();
//        if (db != null) {
//            Cursor cursor = db.query(true, "tiles", new String[]{"z"}, null, null, "z", null, null, null);
//
//            while (cursor.moveToNext()) {
//
//                int z = cursor.getInt(cursor
//                        .getColumnIndex("z"));
//
//                zList.add(z);
//            }
//            cursor.close();
//
//            db.close();
//            return zList;
//
//        }
//
//        return null;
//    }

    public String getDatabaseFilename() {
        return DatabaseFilename;
    }

    public void setDatabaseFilename(String databaseFilename) {
        DatabaseFilename = databaseFilename;
    }
}


