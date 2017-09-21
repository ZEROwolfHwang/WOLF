package com.yisipu.chartmap.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class MapDataBaseXmlBean implements Serializable {
   // 是否存在至少一个数据库
    boolean sign=false;
    int levelMin=10;
    int levelMax=15;
    List<MapDateBaseItem> lm=new ArrayList<>();

    public MapDataBaseXmlBean() {
    }


    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public int getLevelMin() {
        return levelMin;
    }

    public void setLevelMin(int levelMin) {
        this.levelMin = levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }

    public void setLevelMax(int levelMax) {
        this.levelMax = levelMax;
    }


    public List<MapDateBaseItem> getLm() {
        return lm;
    }

    public void setLm(List<MapDateBaseItem> lm) {
        this.lm = lm;
    }

    @Override
    public String toString() {
        return "MapDataBaseXmlBean{" +
                "sign=" + sign +
                ", levelMin=" + levelMin +
                ", levelMax=" + levelMax +
                ", lm=" + lm +
                '}';
    }
}
