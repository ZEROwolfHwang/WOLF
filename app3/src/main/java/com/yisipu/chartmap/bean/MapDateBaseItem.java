package com.yisipu.chartmap.bean;

/**
 * Created by Administrator on 2016/11/25.
 */
public class MapDateBaseItem {
    int id=-1;
    String filename="";
//    10级别该地图最小x,10级别最大x,10级别最小Y,10级别最大Y
    int minX=-1;
    int maxX=-1;
    int minY=-1;
    int maxY=-1;
   // 是否启用
    boolean isUse=false;
    //是否存在-->
    boolean isExist=false;

    public MapDateBaseItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    @Override
    public String toString() {
        return "MapDateBaseItem{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", isUse=" + isUse +
                ", isExist=" + isExist +
                '}';
    }
}
