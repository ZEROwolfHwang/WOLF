package com.yisipu.chartmap.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/9.
 */
public class CachImageBean  implements Serializable {
    int x;
    int y;

    public CachImageBean() {
    }

    Bitmap bitmap=null;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
