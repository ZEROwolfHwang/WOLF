package com.yisipu.chartmap.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CollectPointBean  implements Serializable {
   int id;
    /*
    纬度
     */
    double latitude=-1;
    /*
    经度
     */
    double longitude=-1;
    /*
    类型  0为航点，1为航线
     */
    int type=-1;
    /*
    顺序
     */
    int index=-1;
    /*
    航点名称
     */
    String name="";
    /*
     航线名称
      */
    String course_name="";
    /*
 图片资源顺序
  */
    int image=-1;
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
