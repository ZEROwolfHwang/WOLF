package com.yisipu.chartmap.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.detail.DetailLevel;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.yisipu.chartmap.db.SQLdm;

import java.util.ArrayList;



/**
 * This is a very simple implementation of BitmapProvider, using a formatted string to find
 * an asset by filename, and built-in methods to decode the bitmap data.
 *
 * Feel free to use your own implementation here, where you might implement a favorite library like
 * Picasso, or add your own disk-caching scheme, etc.
 */

public class BitmapProviderAssets2 implements BitmapProvider {

  private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();

  static {
    OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
  }
  private Bitmap getDrawable(Context context,int Column,int Row,float scale) {
    Bitmap bitmap =null;
    SQLdm s = new SQLdm();
    SQLiteDatabase db =s.openDatabase(context.getApplicationContext());


    ArrayList<Drawable> drawables = new ArrayList<Drawable>();
   int xR=(int)(Column+(842*scale));
    int yR=(int)(Row+(442*scale));

    //查询数据库
    Cursor c = db.query("tiles", null, "x=? and y=?", new String[]{String.valueOf(xR),String .valueOf(yR)}, null, null, null);

    //遍历数据
    if(c != null && c.getCount() != 0) {
      while(c.moveToNext()) {
        //获取数据
        byte[] b = c.getBlob(c.getColumnIndexOrThrow("image"));
        //将获取的数据转换成drawable
        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//        Drawable drawable = bitmapDrawable;
//        drawables.add(drawable);
        c.close();
        return  bitmap;
      }
    }
    c.close();
    db.close();
    return bitmap;
  }
  @Override
  public Bitmap getBitmap( Tile tile, Context context ) {
    Object data = tile.getData();
    DetailLevel dl=tile.getDetailLevel();
    Logger.i("scale34325"+dl.getScale()+"ds"+dl.getRelativeScale()+"fl"+dl.getData().toString()+"f4"+dl.getDetailLevelManager().getCurrentDetailLevel());
    if( data instanceof String ) {
//      String unformattedFileName = (String) tile.getData();
//      String formattedFileName = String.format( unformattedFileName, 1,2 );
      //打开数据库输出流
      return getDrawable(context,tile.getColumn(),tile.getRow(),dl.getScale());
//      SQLdm s = new SQLdm();
//      SQLiteDatabase db =s.openDatabase(context.getApplicationContext());
//
//
//      //查询数据库中testid=1的数据
//      Cursor cursor = db.rawQuery("select * from testbiao where testid=?", new String[]{"1"});
//      String name = null;
//      if(cursor.moveToFirst()){
//        name = cursor.getString(cursor.getColumnIndex("name"));
//      }
//      String formattedFileName = String.format( unformattedFileName, tile.getColumn(), tile.getRow() );
//      AssetManager assetManager = context.getAssets();
//      try {
//        InputStream inputStream = assetManager.open( formattedFileName );
//        if( inputStream != null ) {
//          try {
//            return BitmapFactory.decodeStream( inputStream, null, OPTIONS );
//          } catch( OutOfMemoryError | Exception e ) {
//            // this is probably an out of memory error - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
//          }
//        }
//      } catch( Exception e ) {
//        // this is probably an IOException, meaning the file can't be found
//      }
    }
    return null;
  }


}
