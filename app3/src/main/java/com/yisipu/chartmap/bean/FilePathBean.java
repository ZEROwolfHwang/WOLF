package com.yisipu.chartmap.bean;

/**
 * Created by Administrator on 2016/11/18.
 */
public class FilePathBean {
    /*
    当isExten为1时数据库和 ais gps 都从这个位置放和取
     */
    String path="";
    /*
    是否外置sd卡路径,0为内置 1为外置
     */
    int  isExten=0;

    public FilePathBean() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIsExten() {
        return isExten;
    }

    public void setIsExten(int isExten) {
        this.isExten = isExten;
    }
}
