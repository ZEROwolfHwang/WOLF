package com.yisipu.chartmap.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShipBean implements Serializable,Comparable<ShipBean>{
   /*
   _id
    */
 private  int id=-1;
    /*
    是否本船
     */
     private Boolean IsMyShip=false;
    /*
     id MMSI唯一标识
     */
   private int MMSI;
    /*
    sog 航速
   */
    private int Sog=-1;
    /*
    航向
     */
    private int Cog=-1;
    /*
    方位
     */
    private  double fangwei=-1;
    /*
    距离
     */
    private double distance=-1;
        /*
    经度
     */
    private double Latitude=-1;
    /*
    纬度
     */
    private double Longitude=-1;
    /*
    位置精确度
     */
    private int precision=-1;
    /*
    导航状态
     */
    private int status=-1;
    /*
    呼号
     */
    private String huhao;
    /*
    IMO编号
     */
    private Long IMO;
    /*
    船艏   既实际航向
    */
    private int real_sudu=-1;
    /*
    转向 旋转速率
     */
    private double rot=-1;
    /*
    英文名
     */
    private String EnglishName="";
    /*
  中文名
   */
    private String ChineseName="";
 /*
 修改过的中文名
  */
 private String ChineseNameChange="";

 /*
别名
*/
 private String OtherName="";
    /*
    国籍  MMSI前三个数字
     */
   private int country=-1;
 /*
 是否范围碰撞,0为无报警，1为范围报警，2为碰撞报警
  */
private int isCollision=0;
 public int getClassType() {
  return classType;
 }

 public void setClassType(int classType) {
  this.classType = classType;
 }

 /*
  船A 或者船B A为1 ，B 为2  默认1
   */
 private  int classType=-1;
   /*
   船舶类型
    */
    private int type=-1;
 /*
   A
    */
 private int DimBow=-1 ;
 /*
   B
    */
 private int DimStern=-1;
 /*
  C
   */
 private int DimPort=-1;
 /*
  D
   */
 private int DimStarboard=-1;


/*
更新时间
 */
 private int updateTime;
 public ShipBean() {
 }

 /*
 0为相等，1为大于，-1为小于
  */
 @Override
 public int compareTo(ShipBean another) {

  if(this.IsMyShip==true){

   return -1;
  }else {

   if(this.getLatitude()>90||this.getLongitude()>180||this.getLatitude()==-1||this.getLongitude()==-1){
    return 1;
   }else if((!another.IsMyShip)&&(another.getLatitude()>90||another.getLongitude()>180||another.getLatitude()==-1||another.getLongitude()==-1)){
    return -1;
   }
   if(this.distance==-1){
    return  1;
   }else if((!another.IsMyShip)&&another.distance==-1){
    return -1;
   }
   if ((this.distance > another.distance )) {
    return 1;
   } else if ((this.distance == another.distance)) {
    return 0;
   } else if ((this.distance < another.distance)){
    return -1;
   }else {
    return  1;
   }
  }
 }

 public Boolean getMyShip() {
  return IsMyShip;
 }

 public void setMyShip(Boolean myShip) {
  IsMyShip = myShip;
 }

 public int getMMSI() {
  return MMSI;
 }

 public void setMMSI(int MMSI) {
  this.MMSI = MMSI;
 }

 public int getSog() {
  return Sog;
 }

 public void setSog(int sog) {
  Sog = sog;
 }

 public int getCog() {
  return Cog;
 }

 public void setCog(int cog) {
  Cog = cog;
 }

 public double getFangwei() {
  return fangwei;
 }

 public void setFangwei(double fangwei) {
  this.fangwei = fangwei;
 }

 public double getDistance() {
  return distance;
 }

 public void setDistance(double distance) {
  this.distance = distance;
 }

 public double getLatitude() {
  return Latitude;
 }

 public void setLatitude(double latitude) {
  Latitude = latitude;
 }

 public double getLongitude() {
  return Longitude;
 }

 public void setLongitude(double longitude) {
  Longitude = longitude;
 }

 public int getPrecision() {
  return precision;
 }

 public void setPrecision(int precision) {
  this.precision = precision;
 }

 public int getStatus() {
  return status;
 }

 public void setStatus(int status) {
  this.status = status;
 }

 public String getHuhao() {
  return huhao;
 }

 public void setHuhao(String huhao) {
  this.huhao = huhao;
 }

 public Long getIMO() {
  return IMO;
 }

 public void setIMO(Long IMO) {
  this.IMO = IMO;
 }

 public int getReal_sudu() {
  return real_sudu;
 }

 public void setReal_sudu(int real_sudu) {
  this.real_sudu = real_sudu;
 }

 public double getRot() {
  return rot;
 }

 public void setRot(double rot) {
  this.rot = rot;
 }

 public String getEnglishName() {
  return EnglishName;
 }

 public void setEnglishName(String englishName) {
  EnglishName = englishName;
 }

 public String getChineseName() {
  return ChineseName;
 }

 public void setChineseName(String chineseName) {
  ChineseName = chineseName;
 }

 public int getCountry() {
  return country;
 }

 public void setCountry(int country) {
  this.country = country;
 }

 public int getType() {
  return type;
 }

 public void setType(int type) {
  this.type = type;
 }

 public int getDimBow() {
  return DimBow;
 }

 public void setDimBow(int dimBow) {
  DimBow = dimBow;
 }

 public int getDimStern() {
  return DimStern;
 }

 public void setDimStern(int dimStern) {
  DimStern = dimStern;
 }

 public int getDimPort() {
  return DimPort;
 }

 public void setDimPort(int dimPort) {
  DimPort = dimPort;
 }

 public int getDimStarboard() {
  return DimStarboard;
 }

 public void setDimStarboard(int dimStarboard) {
  DimStarboard = dimStarboard;
 }


 public int getUpdateTime() {
  return updateTime;
 }

 public void setUpdateTime(int updateTime) {
  this.updateTime = updateTime;
 }

 @Override
 public String toString() {
  return "ShipBean{" +
          "IsMyShip=" + IsMyShip +
          ", MMSI=" + MMSI +
          ", Sog=" + Sog +
          ", Cog=" + Cog +
          ", fangwei=" + fangwei +
          ", distance=" + distance +
          ", Latitude=" + Latitude +
          ", Longitude=" + Longitude +
          ", precision=" + precision +
          ", status=" + status +
          ", huhao='" + huhao + '\'' +
          ", IMO=" + IMO +
          ", real_sudu=" + real_sudu +
          ", rot=" + rot +
          ", EnglishName='" + EnglishName + '\'' +
          ", ChineseName='" + ChineseName + '\'' +
          ", country=" + country +
          ", type=" + type +
          ", DimBow=" + DimBow +
          ", DimStern=" + DimStern +
          ", DimPort=" + DimPort +
          ", DimStarboard=" + DimStarboard +
          ", updateTime=" + updateTime +
          '}';
 }

 public String getOtherName() {
  return OtherName;
 }

 public void setOtherName(String otherName) {
  OtherName = otherName;
 }

 public String getChineseNameChange() {
  return ChineseNameChange;
 }

 public void setChineseNameChange(String chineseNameChange) {
  ChineseNameChange = chineseNameChange;
 }

 public int getIsCollision() {
  return isCollision;
 }

 public void setIsCollision(int isCollision) {
  this.isCollision = isCollision;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }
}
