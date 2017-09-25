package com.yisipu.chartmap.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class Jdbean implements Serializable {
    private String pdop;//位置精度
    private String hdop;//水平精度
    private String sfdw;//判断是否定位
    private List<Integer> sateInList=new ArrayList<>();//卫星随机码
    public String getSfdw() {
        return sfdw;
    }

    public void setSfdw(String sfdw) {
        this.sfdw = sfdw;
    }



    public String getHdop() {
        return hdop;
    }

    public void setHdop(String gdop) {
        this.hdop = gdop;
    }

    public String getPdop() {
        return pdop;
    }

    public void setPdop(String pdop) {
        this.pdop = pdop;
    }


    public List<Integer> getSateInList() {
        return sateInList;
    }

    public void setSateInList(List<Integer> sateInList) {
        this.sateInList = sateInList;
    }

    @Override
    public String toString() {
        return "Jdbean{" +
                "pdop='" + pdop + '\'' +
                ", hdop='" + hdop + '\'' +
                ", sfdw='" + sfdw + '\'' +
                ", sateInList=" + sateInList +
                '}';
    }
}
