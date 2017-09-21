package com.yisipu.chartmap.utils;

import android.text.TextUtils;
import android.util.Xml;

import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.MapDataBaseXmlBean;
import com.yisipu.chartmap.bean.MapDateBaseItem;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图扩展方式
 */
public class MapDataBaseXmlUtil {
    /*
读航线数据到手机
*/
    public static MapDataBaseXmlBean readMapDataBase(String fileName) {

        try {
            File file = new File(fileName);

            FileInputStream fis = new FileInputStream(file);

            MapDataBaseXmlBean ls = getMapDataBaseXmlBean(fis);
            return ls;
        } catch (Exception ecp) {
            return null;
        }
    }
    /*
   获取扩展数据库列表
    */
    public static MapDataBaseXmlBean getMapDataBaseXmlBean(InputStream xml) throws Exception {
        MapDataBaseXmlBean mapDataBaseXmlBean=null;
        List<MapDateBaseItem> mapDateBaseItemList= null;
        MapDateBaseItem mapDateBaseItem = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    mapDataBaseXmlBean=new MapDataBaseXmlBean();


                    break;
                case XmlPullParser.START_TAG:
                    if ("sign".equals(pullParser.getName())){
                     String sign = pullParser.nextText();
                        if(sign!=null&&!TextUtils.isEmpty(sign.trim())){
                            mapDataBaseXmlBean.setSign(Boolean.parseBoolean(sign.trim()));
                        }
                    }
                    if ("levelMin".equals(pullParser.getName())){
                        String levelMin = pullParser.nextText();
                        if(levelMin!=null&&!TextUtils.isEmpty(levelMin.trim())){
                            mapDataBaseXmlBean.setLevelMin(Integer.parseInt(levelMin.trim()));
                        }
                    }
                    if ("levelMax".equals(pullParser.getName())){
                        String levelMax = pullParser.nextText();
                        if(levelMax!=null&&!TextUtils.isEmpty(levelMax.trim())){
                            mapDataBaseXmlBean.setLevelMax(Integer.parseInt(levelMax.trim()));
                        }
                    }
                    if ("databaseList".equals(pullParser.getName())) {

                        mapDateBaseItemList=new ArrayList<>();

                    }
                    if ("database".equals(pullParser.getName())) {

                        mapDateBaseItem=new MapDateBaseItem();

                    }

                    if ("id".equals(pullParser.getName())) {
                        String id = pullParser.nextText();
                        if (id != null && !TextUtils.isEmpty(id)) {
                            mapDateBaseItem.setId(Integer.parseInt(id));
                        }
                        ;
                    }
                    if ("filename".equals(pullParser.getName())) {
                        String filename = pullParser.nextText();
                        if (filename != null && !TextUtils.isEmpty(filename)) {
                            mapDateBaseItem.setFilename(filename);
                        }
                        ;
                    }
                    if ("minX".equals(pullParser.getName())) {
                        String minX = pullParser.nextText();
                        if (minX != null && !TextUtils.isEmpty(minX)) {
                           mapDateBaseItem.setMinX(Integer.parseInt(minX));
                        }
                        ;
                    }
                    if ("maxX".equals(pullParser.getName())) {
                        String maxX= pullParser.nextText();
                        if (maxX != null && !TextUtils.isEmpty(maxX)) {
                            mapDateBaseItem.setMaxX(Integer.parseInt(maxX));
                        }
                        ;
                    }
                    if ("minY".equals(pullParser.getName())) {
                        String minY= pullParser.nextText();
                        if (minY != null && !TextUtils.isEmpty(minY)) {
                            mapDateBaseItem.setMinY(Integer.parseInt(minY));
                        }
                        ;
                    }
                    if ("maxY".equals(pullParser.getName())) {
                        String maxY= pullParser.nextText();
                        if (maxY!= null && !TextUtils.isEmpty(maxY)) {
                            mapDateBaseItem.setMaxY(Integer.parseInt(maxY));
                        }
                        ;
                    }
                    if ("isUse".equals(pullParser.getName())) {
                        String isUse= pullParser.nextText();
                        if (isUse!= null && !TextUtils.isEmpty(isUse)) {
                            mapDateBaseItem.setUse(Boolean.parseBoolean(isUse));
                        }
                        ;
                    }
                    if ("isExist".equals(pullParser.getName())) {
                        String isExist= pullParser.nextText();
                        if (isExist!= null && !TextUtils.isEmpty(isExist)) {
                            mapDateBaseItem.setExist(Boolean.parseBoolean(isExist));
                        }
                        ;
                    }


//                    if ("age".equals(pullParser.getName())){
//                        int age = Integer.valueOf(pullParser.nextText());
//                       collectPointBean.setAge(age);
//                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("database".equals(pullParser.getName())) {
                        mapDateBaseItemList.add(mapDateBaseItem);
                     mapDateBaseItem = null;
                    }
                    if ("databaseList".equals(pullParser.getName())) {
                       mapDataBaseXmlBean.setLm(mapDateBaseItemList);
                        mapDateBaseItemList = null;
                    }
                    break;

            }

            event = pullParser.next();
        }
        return mapDataBaseXmlBean;
    }
}
