package com.yisipu.chartmap.utils;

/**
 * 渔船类型工具类
 */
public class ShipTypeUtils {

    public static String getShipTypeString(int type){
        String a="";
        if(type==20){
            a="地效翼船";

        }else if(type>=21&&type<=24){
            a="危险翼船";
        }else if(type==30){
            a="渔船";
        }else if(type==31){
            a="拖带船";
        }else if(type==32){
            a="大拖带船";
        }else if(type==33){
            a="疏浚船";
        }else if(type==34){
            a="潜水船";
        }else if(type==35){
            a="军事船";
        }else if(type==36){
            a="帆船";
        }else if(type==37){
            a="游艇";
        }else if(type==40){
            a="高速船";
        }else if(type>=41&&type<=44){
            a="危险高速";
        }else if(type==50){
            a="引航船";
        }else if(type==51){
            a="搜救船";
        }else if(type==52){
            a="拖轮";
        }else if(type==53){
            a="港口供应";
        }else if(type==54){
            a="防污船舶";
        }else if(type==55){
            a="执法船";
        }else if(type==58){
            a="医疗船";
        }else if(type==60){
            a="客船";
        }else if(type>=61&&type<=64){
            a="危险客船";
        }else if(type==70){
            a="货船";
        }else if(type>=71&&type<=74){
            a="危险货船";
        }else if(type==80){
            a="油轮";
        }else if(type>=81&&type<=84){
            a="危险油轮";
        }else if(type==90){
            a="其他类";
        }else if(type>=91&&type<=94){
            a="危险船";
        }


        return  a;
    }
}
