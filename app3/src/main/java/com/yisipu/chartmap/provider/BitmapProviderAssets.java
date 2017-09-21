package com.yisipu.chartmap.provider;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.detail.DetailLevel;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;

import java.io.InputStream;

/**
 * This is a very simple implementation of BitmapProvider, using a formatted string to find
 * an asset by filename, and built-in methods to decode the bitmap data.
 *
 * Feel free to use your own implementation here, where you might implement a favorite library like
 * Picasso, or add your own disk-caching scheme, etc.
 */

public class BitmapProviderAssets implements BitmapProvider {

  private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();

  static {
    OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
  }

  @Override
  public Bitmap getBitmap( Tile tile, Context context ) {
    Object data = tile.getData();
    if( data instanceof String ) {

      DetailLevel dl=tile.getDetailLevel();
      Logger.i("scale34325"+dl.getScale()+"ds"+dl.getRelativeScale()+"fl"+dl.getData().toString()+"f4"+dl.getDetailLevelManager().getCurrentDetailLevel());
      String unformattedFileName = (String) tile.getData();
//      String formattedFileName = String.format( unformattedFileName, (int)(tile.getColumn()), (int)(tile.getRow()) );
      String formattedFileName = String.format( unformattedFileName, (int)(tile.getColumn()+dl.getScale()*25), (int)(tile.getRow()+dl.getScale()*11) );
      Logger.i("scale36:"+formattedFileName);
      Logger.i("scale36:"+formattedFileName);
      Logger.i("scale36:"+formattedFileName+"dfh:"+(tile.getColumn()+dl.getScale()*16*25)+"row:"+tile.getRow()+"colum:"+tile.getColumn()+"row22"+tile.getRow()+11*dl.getScale());
      AssetManager assetManager = context.getAssets();
      try {
        InputStream inputStream = assetManager.open( formattedFileName );
        if( inputStream != null ) {
          try {
            return BitmapFactory.decodeStream( inputStream, null, OPTIONS );
          } catch( OutOfMemoryError | Exception e ) {
            // this is probably an out of memory error - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
          }
        }
      } catch( Exception e ) {
        // this is probably an IOException, meaning the file can't be found
      }
    }
    return null;
  }


}
