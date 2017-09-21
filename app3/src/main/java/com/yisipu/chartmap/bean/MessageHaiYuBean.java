package com.yisipu.chartmap.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */
public class MessageHaiYuBean implements Serializable {
    int id=-1;
    long updateTime=-1;
    String getMessage="";

    public MessageHaiYuBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getGetMessage() {
        return getMessage;
    }

    public void setGetMessage(String getMessage) {
        this.getMessage = getMessage;
    }

    @Override
    public String toString() {
        return "MessageHaiYuBean{" +
                "id=" + id +
                ", updateTime=" + updateTime +
                ", getMessage='" + getMessage + '\'' +
                '}';
    }
}
