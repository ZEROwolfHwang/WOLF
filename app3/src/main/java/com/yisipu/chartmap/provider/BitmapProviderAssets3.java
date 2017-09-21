package com.yisipu.chartmap.provider;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.detail.DetailLevel;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.yisipu.chartmap.bean.CachImageBean;
import com.yisipu.chartmap.bean.MapDataBaseXmlBean;
import com.yisipu.chartmap.bean.MapDateBaseItem;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.HaiTuSqliteDb;
import com.yisipu.chartmap.utils.BitmapUtil;
import com.yisipu.chartmap.utils.ExtenSdCard;
import com.yisipu.chartmap.utils.MapDataBaseXmlUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a very simple implementation of BitmapProvider, using a formatted string to find
 * an asset by filename, and built-in methods to decode the bitmap data.
 *
 * Feel free to use your own implementation here, where you might implement a favorite library like
 * Picasso, or add your own disk-caching scheme, etc.
 */

public class BitmapProviderAssets3 implements BitmapProvider {

  private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
  public static MapDataBaseXmlBean mdbx=null;
    public static List<CachImageBean> cibList=new ArrayList<>();
    public  static List<MapDateBaseItem> mdbi =new ArrayList<>() ;
    public  static  Bitmap noBitmap=null;
  static {
    OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;

      OPTIONS.outWidth=512;
      OPTIONS.outHeight=512;





  }
    /*
    空的时候加载默认空图
     */
    public  Bitmap initDefaultMap(Context context){
//        if(noBitmap!=null){
//            return noBitmap;
//        }
        if(BitmapUtil.getBitmapFromMemCache("23_11")!=null) {
        return BitmapUtil.getBitmapFromMemCache("23_11");
        }else {
            String formattedFileName = "tiles/map2/5/23_11_5.png";

            AssetManager assetManager = context.getAssets();
            try {
                InputStream inputStream = assetManager.open(formattedFileName);
                if (inputStream != null) {
                    try {
//
                        Bitmap a = BitmapFactory.decodeStream(inputStream, null, OPTIONS);
                        if (a != null) {
                            BitmapUtil.addBitmapToMemCache("23_11", a);
                        }
                        return a;


                    } catch (OutOfMemoryError | Exception e) {
                        // this is probably an out of memory error - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
                    }
                }
            } catch (Exception e) {
                // this is probably an IOException, meaning the file can't be found
            }
        }
        return null;
    }

  private  long duringTime=-1;
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
    private static final String DATABASE_PATH = getPath()+"/chartmap/map_database";
//    String pathDataBaseXml=DATABASE_PATH+"/"+"map_setting.xml";
    private Bitmap getDrawable(Context context,int Column,int Row,float scale,String fileName) {

        Bitmap bitmap =null;
        HaiTuSqliteDb s = new HaiTuSqliteDb();
//        SQLiteDatabase db =s.openDatabase(context.getApplicationContext());

        SQLiteDatabase db =s.openDatabase(fileName);
        if(db!=null) {
            ArrayList<Drawable> drawables = new ArrayList<Drawable>();
            int xR = (int) (Column + (Constant.TimesNeed*Constant.minX5 * scale));
            int yR = (int) (Row + (Constant.TimesNeed*Constant.minY5 * scale));
              Logger.i("kkkkk"+xR+"iiiiii"+yR);
            Date date=new Date();
           long time1= date.getTime();

            //查询数据库
            Cursor c = db.query("tiles", null,  "x=? and y=?", new String[]{String.valueOf(xR), String.valueOf(yR)}, null, null, null);

            //遍历数据
            if (c != null && c.getCount() != 0) {
                while (c.moveToNext()) {
                    Date date2=new Date();
                    long time2= date2.getTime();
                    duringTime=time2-time1;
                    Logger.i("x=? and y=?"+xR+"dsg"+ yR+"timeUse"+duringTime);
                    //获取数据
                    byte[] b = c.getBlob(c.getColumnIndexOrThrow("image"));
                    Date date3=new Date();
                    long time3=date3.getTime();
                    duringTime=time3-time2;
                    Logger.i("x=? and y=?"+xR+"dsg"+ yR+"timeUseBBB"+duringTime);
                        //将获取的数据转换成drawable
                        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,   OPTIONS);
                    if ( bitmap != null) {
                        BitmapUtil.addBitmapToMemCache(xR+"_"+yR, bitmap);
                    }
                    Date date6=new Date();
                    long time6=date6.getTime();
                    duringTime=time6-time3;
                    Logger.i("x=? and y=?"+xR+"dsg"+ yR+"timeUseBBB666"+duringTime);
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//        Drawable drawable = bitmapDrawable;
//        drawables.add(drawable);
//                    if(bitmap!=null){
//                        CachImageBean cib=new CachImageBean();
//                        cib.setX( xR);
//                        cib.setY(yR);
//                        cib.setBitmap(bitmap);
//                        cibList.add(cib);
//                    }
                    c.close();
                    db.close();
                    return   bitmap;
                }
            }

            c.close();
            db.close();
        }
        if(bitmap==null){
            return   initDefaultMap(context);
        }
        return bitmap;
    }
  @Override
  public Bitmap getBitmap( Tile tile, Context context ) {
      Date date1=new Date();
      long time1=date1.getTime();

    Object data = tile.getData();
    if( data instanceof String ) {

        DetailLevel dl = tile.getDetailLevel();
       int x= (int) (tile.getColumn() +Constant.TimesNeed* dl.getScale() * Constant.minX5);
        int y=(int) (tile.getRow() +Constant.TimesNeed*dl.getScale() * Constant.minY5);
        Date date2=new Date();
        long time2=date2.getTime();
        duringTime=time2-time1;
        Logger.i("x=? and y=?"+x+"dsg"+ y+"timeUseBBBZ"+duringTime);
        if(BitmapUtil.getBitmapFromMemCache(x+"_"+y)!=null) {
            return BitmapUtil.getBitmapFromMemCache(x+"_"+y);
        }else {
//        for(CachImageBean cachImageBean:cibList){
//            if(cachImageBean.getX()== (int) (tile.getColumn() + dl.getScale() * Constant.minX5)&&cachImageBean.getY()==(int) (tile.getRow() + dl.getScale() * Constant.minY5)){
//                return cachImageBean.getBitmap();
//            }
//
//        }

            if (dl.getScale() <= 8) {

                Logger.i("scale34325" + dl.getScale() + "ds" + dl.getRelativeScale() + "sfs" + dl.getDetailLevelManager().getCurrentDetailLevel().getScale() + "sgse" + tile.getDetailLevel().getRelativeScale());
                String unformattedFileName = (String) tile.getData();
//      String formattedFileName = String.format( unformattedFileName, (int)(tile.getColumn()), (int)(tile.getRow()) );
                String formattedFileName = String.format(unformattedFileName, (int) (tile.getColumn() + Constant.TimesNeed * dl.getScale() * Constant.minX5), (int) (tile.getRow() + Constant.TimesNeed * dl.getScale() * Constant.minY5));
                Logger.i("scale36:" + formattedFileName);
                Logger.i("scale36:" + formattedFileName);
                Logger.i("scale36:" + formattedFileName + "dfh:" + (tile.getColumn() + Constant.TimesNeed * dl.getScale() * 16 * Constant.minX5) + "row:" + tile.getRow() + "colum:" + tile.getColumn() + "row22" + tile.getRow() + Constant.minY5 * dl.getScale());
                AssetManager assetManager = context.getAssets();
                try {
                    InputStream inputStream = assetManager.open(formattedFileName);
                    if (inputStream != null) {
                        try {
//                        Bitmap bit = BitmapFactory.decodeStream(inputStream, null, OPTIONS);
//                        int width = bit.getWidth();
//                        int height = bit.getHeight();
//                        int newWidth = 512;
//                        int newHeight = 512;
//                        float scaleWidth = ((float) newWidth) / width;
//                        float scaleHeight = ((float) newHeight) / height;
//                        Matrix matrix = new Matrix();
//                        matrix.postScale(scaleWidth, scaleHeight);
//                        bit = Bitmap.createBitmap(bit, 0, 0, width, height, matrix, true);
//                        int z = bit.getWidth();
//                        int zh = bit.getHeight();
////                        Logger.i("dfs" + width + "dgss" + height + "gf" + z + "ser" + zh);
////
//return  bit;
                            Bitmap a = BitmapFactory.decodeStream(inputStream, null, OPTIONS);
                            if (a!= null) {
                                BitmapUtil.addBitmapToMemCache(x+"_"+y, a);
                            }
                            Date date3=new Date();
                            long time3=date3.getTime();
                            duringTime=time3-time2;
                            Logger.i("x=? and y=?"+x+"dsg"+ y+"timeUseBBBZHH"+duringTime);
//                              if(a!=null){
//                                  CachImageBean cib=new CachImageBean();
//                                  cib.setX( (int) (tile.getColumn() + dl.getScale() * Constant.minX5));
//                                  cib.setY((int) (tile.getRow() + dl.getScale() * Constant.minY5));
//                                  cib.setBitmap(a);
//                                  cibList.add(cib);
//                              }
                            return a;


                        } catch (OutOfMemoryError | Exception e) {
                            // this is probably an out of memory error - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
                        }
                    }
                } catch (Exception e) {
                    // this is probably an IOException, meaning the file can't be found
                }
            } else if (dl.getScale() > 8) {

                String pathDataBaseXml = DATABASE_PATH + "/" + "map_setting.xml";
                try {
                    if (mdbx == null) {
                        File dir = new File(DATABASE_PATH);
                        if (!dir.exists())
                            dir.mkdir();

                        mdbx = MapDataBaseXmlUtil.readMapDataBase(pathDataBaseXml);
                        if (mdbx != null) {
                            mdbi = mdbx.getLm();
                            MapDateBaseItem mbTem = null;
                            int xR = (int) (tile.getColumn() + (Constant.minX5 * (Constant.TimesNeed * dl.getScale())));
                            int yR = (int) (tile.getRow() + (Constant.minY5 * Constant.TimesNeed * dl.getScale()));
                            int xR2 = (int) (xR / (Constant.TimesNeed * dl.getScale() / 32.0));
                            int yR2 = (int) (yR / (Constant.TimesNeed * dl.getScale() / 32.0));
                            Logger.i("dss" + xR + "dsdg" + yR + "fg" + xR2 + "ds" + yR2);
                            boolean isFind = false;
                            if (mdbi != null) {
                                for (MapDateBaseItem mb : mdbi) {
                                    Logger.i("dsgkkk" + mb.toString());
                                    if (mb.isUse() && mb.isExist() && xR2 >= mb.getMinX() && xR2 < mb.getMaxX() && yR2 >= mb.getMinY() && yR2 <= mb.getMaxY()) {
                                        mbTem = mb;
                                        isFind = true;
                                        break;
                                    }
                                }
                            } else {
                                return initDefaultMap(context);
//                                  return  null;
                            }

                            Logger.i("dsd" + isFind);
                            if (isFind == false) {
                                return initDefaultMap(context);
//                                  return  null;
                            } else {
                                Date date3=new Date();
                                long time3=date3.getTime();
                                duringTime=time3-time1;
                                Logger.i("x=? and y=?"+x+"dsg"+ y+"timeUseBBBZHHDG"+duringTime);
                                return getDrawable(context, tile.getColumn(), tile.getRow(), dl.getScale(), mbTem.getFilename());
                            }

                        } else {
                            return initDefaultMap(context);
//                              return null;
                        }
                    } else {
                        mdbi = mdbx.getLm();
                        MapDateBaseItem mbTem = null;
                        int xR = (int) (tile.getColumn() + (Constant.minX5 * (2 * dl.getScale())));
                        int yR = (int) (tile.getRow() + (Constant.minY5 * Constant.TimesNeed * dl.getScale()));
                        int xR2 = (int) (xR / (Constant.TimesNeed * dl.getScale() / 32.0));
                        int yR2 = (int) (yR / (Constant.TimesNeed * dl.getScale() / 32.0));
                        Logger.i("dss" + xR + "dsdg" + yR + "fg" + xR2 + "ds" + yR2);
                        boolean isFind = false;
                        if (mdbi != null) {
                            for (MapDateBaseItem mb : mdbi) {
                                Logger.i("dsgkkk" + mb.toString());
                                if (mb.isUse() && mb.isExist() && xR2 >= mb.getMinX() && xR2 < mb.getMaxX() && yR2 >= mb.getMinY() && yR2 <= mb.getMaxY()) {
                                    mbTem = mb;
                                    isFind = true;
                                    break;
                                }
                            }
                        } else {
                            return initDefaultMap(context);
//                              return  null;
                        }

                        Logger.i("dsd" + isFind);
                        if (isFind == false) {
                            return initDefaultMap(context);
//                              return  null;
                        } else {
                            Date date3=new Date();
                            long time3=date3.getTime();
                            duringTime=time3-time1;
                            Logger.i("x=? and y=?"+x+"dsg"+ y+"timeUseBBBZHHDA"+duringTime);
                            return getDrawable(context, tile.getColumn(), tile.getRow(), dl.getScale(), mbTem.getFilename());
                        }

                    }


                } catch (Exception e) {
                    return initDefaultMap(context);
//                    return null;
                }
            }

        }
    }
      return   initDefaultMap(context);
//    return null;
  }


}
