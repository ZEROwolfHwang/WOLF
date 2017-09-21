package com.yisipu.chartmap.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class BaoJingShip  implements Serializable {
 /*
 进入报警范围的船只
  */
  ShipBean shipBean=null;
    /*
    报警时间
     */
    long baojingTime=-1;


    public ShipBean getShipBean() {
        return shipBean;
    }

    public void setShipBean(ShipBean shipBean) {
        this.shipBean = shipBean;
    }

    public long getBaojingTime() {
        return baojingTime;
    }

    public void setBaojingTime(long baojingTime) {
        this.baojingTime = baojingTime;
    }
}
