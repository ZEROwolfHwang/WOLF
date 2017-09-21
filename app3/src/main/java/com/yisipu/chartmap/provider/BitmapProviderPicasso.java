package com.yisipu.chartmap.provider;

import android.content.Context;
import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * @author Mike Dunn, 2/19/16.
 */
public class BitmapProviderPicasso implements BitmapProvider {
  public Bitmap getBitmap( Tile tile, Context context ) {
    Object data = tile.getData();
    if( data instanceof String ) {
      String unformattedFileName = (String) tile.getData();
//        String formattedFileName = String.format( unformattedFileName, 3, 3 );
      String formattedFileName = String.format( unformattedFileName, tile.getColumn(), tile.getRow() );
     Logger.i("unf"+unformattedFileName+"for"+formattedFileName+"getColumn"+tile.getColumn()+"Row"+tile.getRow());
      try {
        return Picasso.with( context ).load( formattedFileName ).memoryPolicy( MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE ).get();
      } catch( Throwable t ) {
        // probably couldn't find the file, maybe OOME
      }
    }
    return null;
  }
}