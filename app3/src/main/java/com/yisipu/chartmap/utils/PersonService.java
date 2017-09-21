package com.yisipu.chartmap.utils;

/**
 * Created by Administrator on 2016/11/18.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.HangdianXmlBean;
import com.yisipu.chartmap.db.DBManager;

public class PersonService {

    private static String HangdianSign=null;
    private static String HangxianSign=null;
    /*
    航点
     */
    public static List<CollectPointBean> getCollectPoint(InputStream xml) throws Exception {
        List<CollectPointBean> collectPointBeanList = null;
        CollectPointBean collectPointBean = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    collectPointBeanList = new ArrayList<CollectPointBean>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("sign".equals(pullParser.getName())){
                        HangdianSign= pullParser.nextText();
                    }
                    if ("collectPointBean".equals(pullParser.getName())) {

                        collectPointBean = new CollectPointBean();

                    }


                    if ("id".equals(pullParser.getName())) {
                        String id = pullParser.nextText();
                        if (id != null && !TextUtils.isEmpty(id)) {
                            collectPointBean.setId(Integer.parseInt(id));
                        }
                        ;
                    }
                    if ("latitude".equals(pullParser.getName())) {
                        String latitude = pullParser.nextText();
                        if (latitude != null && !TextUtils.isEmpty(latitude)) {
                            collectPointBean.setLatitude(Double.parseDouble(latitude));
                        }
                        ;
                    }
                    if ("longitude".equals(pullParser.getName())) {
                        String longitude = pullParser.nextText();
                        if (longitude != null && !TextUtils.isEmpty(longitude)) {
                            collectPointBean.setLongitude(Double.parseDouble(longitude));
                        }
                        ;
                    }
                    if ("type".equals(pullParser.getName())) {
                        String type = pullParser.nextText();
                        if (type != null && !TextUtils.isEmpty(type)) {
                            collectPointBean.setType(Integer.parseInt(type));
                        }
                        ;
                    }
                    if ("index".equals(pullParser.getName())) {
                        String index = pullParser.nextText();
                        if (index != null && !TextUtils.isEmpty(index)) {
                            collectPointBean.setIndex(Integer.parseInt(index));
                        }
                        ;
                    }
                    if ("name".equals(pullParser.getName())) {
                        String name = pullParser.nextText();
                        if (name != null && !TextUtils.isEmpty(name)) {
                            collectPointBean.setName(name);
                        }
                        ;
                    }
                    if ("course_name".equals(pullParser.getName())) {
                        String course_name = pullParser.nextText();
                        if (course_name != null && !TextUtils.isEmpty(course_name)) {
                            collectPointBean.setCourse_name(course_name);
                        }
                        ;
                    }
                    if ("image".equals(pullParser.getName())) {
                        String image = pullParser.nextText();
                        if (image != null && !TextUtils.isEmpty(image)) {
                            collectPointBean.setImage(Integer.parseInt(image));
                        }
                        ;
                    }
//                    if ("age".equals(pullParser.getName())){
//                        int age = Integer.valueOf(pullParser.nextText());
//                       collectPointBean.setAge(age);
//                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("collectPointBean".equals(pullParser.getName())) {
                        collectPointBeanList.add(collectPointBean);
                        collectPointBean = null;
                    }
                    break;

            }

            event = pullParser.next();
        }


        return collectPointBeanList;
    }

    /**
     * 保存航点数据到xml文件中
     *
     * @param
     * @param out
     * @throws Exception
     */
    public static void saveCollectPoint(List<CollectPointBean> collectPointBeanList, OutputStream out,String sign) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "collectPointBeanList");
        serializer.startTag("", "sign");
        serializer.text(String.valueOf(sign));
        serializer.endTag("", "sign");
        for (CollectPointBean collectPointBean : collectPointBeanList) {
            serializer.startTag(null, "collectPointBean");
            serializer.startTag(null, "id");
            serializer.text(String.valueOf(collectPointBean.getId()));
            serializer.endTag(null, "id");
            serializer.startTag(null, "latitude");
            serializer.text(String.valueOf(collectPointBean.getLatitude()));
            serializer.endTag(null, "latitude");
            serializer.startTag(null, "longitude");
            serializer.text(String.valueOf(collectPointBean.getLongitude()));
            serializer.endTag(null, "longitude");
            serializer.startTag(null, "type");
            serializer.text(String.valueOf(collectPointBean.getType()));
            serializer.endTag(null, "type");
            serializer.startTag(null, "index");
            serializer.text(String.valueOf(collectPointBean.getIndex()));
            serializer.endTag(null, "index");
            serializer.startTag(null, "name");
            serializer.text(String.valueOf(collectPointBean.getName()));
            serializer.endTag(null, "name");
            serializer.startTag(null, "course_name");
            serializer.text(String.valueOf(collectPointBean.getCourse_name()));
            serializer.endTag(null, "course_name");
            serializer.startTag(null, "image");
            serializer.text(String.valueOf(collectPointBean.getImage()));
            serializer.endTag(null, "image");


            serializer.endTag(null, "collectPointBean");
        }
        serializer.endTag(null, "collectPointBeanList");
        serializer.endDocument();
        out.flush();
        out.close();
    }

    /*
    输入航点数据到xml文件
     */
    public static void writeHangdian(String fileName, List<CollectPointBean> collectPointBeanList,String sign) {
        if (collectPointBeanList == null || collectPointBeanList.size() <= 0) {
            return;
        }
        try {
            File file = new File(fileName);


            OutputStream oStream = new FileOutputStream(file);//这种方式将会把要写入的文件（name）保存到
            saveCollectPoint(collectPointBeanList, oStream,sign);
        } catch (Exception ecp) {

        }
    }

    /*
    输入航线数据到xml文件
     */
    public static void writeHangxian(Context context, String fileName, List<String> courseList,String sign) {
        if (courseList == null || courseList.size() <= 0) {
            return;
        }
        try {
            File file = new File(fileName);


            OutputStream oStream = new FileOutputStream(file);//这种方式将会把要写入的文件（name）保存到
            saveCoursePoint(context, courseList, oStream,sign);
        } catch (Exception ecp) {

        }
    }

    /*
读航点数据到手机
 */
    public static List<CollectPointBean> readHangDian(String fileName) {

        try {
            File file = new File(fileName);

            FileInputStream fis = new FileInputStream(file);

            List<CollectPointBean> ls = getCollectPoint(fis);
            return ls;
        } catch (Exception ecp) {
            return null;
        }
    }

    /*
读航线数据到手机
*/
    public static List<CollectPointBean> readHangxian(String fileName) {

        try {
            File file = new File(fileName);

            FileInputStream fis = new FileInputStream(file);

            List<CollectPointBean> ls = getCoursePoint(fis);
            return ls;
        } catch (Exception ecp) {
            return null;
        }
    }

    /**
     * 保存航线数据到xml文件中
     *
     * @param
     * @param out
     * @throws Exception
     */
    public static void saveCoursePoint(Context context, List<String> courseList, OutputStream out,String sign) throws Exception {
        DBManager db = new DBManager(context);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        List<CollectPointBean> ls = null;
        serializer.startTag(null, "coursePointBeanList");
        serializer.startTag("", "sign");
        serializer.text(String.valueOf(sign));
        serializer.endTag("", "sign");
        for (String courseName : courseList) {
            if (courseName != null && !TextUtils.isEmpty(courseName)) {
                ls = db.getCoursePoints(courseName);
                if (ls != null) {
                    serializer.startTag(null, "coursePointBean");
                    serializer.attribute(null, "航线名", courseName);
                    for (CollectPointBean collectPointBean : ls) {
                        serializer.startTag(null, "collectPointBean");
                        serializer.startTag(null, "id");
                        serializer.text(String.valueOf(collectPointBean.getId()));
                        serializer.endTag(null, "id");
                        serializer.startTag(null, "latitude");
                        serializer.text(String.valueOf(collectPointBean.getLatitude()));
                        serializer.endTag(null, "latitude");
                        serializer.startTag(null, "longitude");
                        serializer.text(String.valueOf(collectPointBean.getLongitude()));
                        serializer.endTag(null, "longitude");
                        serializer.startTag(null, "type");
                        serializer.text(String.valueOf(collectPointBean.getType()));
                        serializer.endTag(null, "type");
                        serializer.startTag(null, "index");
                        serializer.text(String.valueOf(collectPointBean.getIndex()));
                        serializer.endTag(null, "index");
                        serializer.startTag(null, "name");
                        serializer.text(String.valueOf(collectPointBean.getName()));
                        serializer.endTag(null, "name");
                        serializer.startTag(null, "course_name");
                        serializer.text(String.valueOf(collectPointBean.getCourse_name()));
                        serializer.endTag(null, "course_name");
                        serializer.startTag(null, "image");
                        serializer.text(String.valueOf(collectPointBean.getImage()));
                        serializer.endTag(null, "image");


                        serializer.endTag(null, "collectPointBean");
                    }
                    serializer.endTag(null, "coursePointBean");
                }
            }
        }
        serializer.endTag(null, "coursePointBeanList");
        serializer.endDocument();
        out.flush();
        out.close();
    }

    /*
  获取航线的航点
   */
    public static List<CollectPointBean> getCoursePoint(InputStream xml) throws Exception {
        List<CollectPointBean> collectPointBeanList = null;
        CollectPointBean collectPointBean = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    collectPointBeanList = new ArrayList<CollectPointBean>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("sign".equals(pullParser.getName())){
                        HangxianSign= pullParser.nextText();
                    }
                    if ("collectPointBean".equals(pullParser.getName())) {

                        collectPointBean = new CollectPointBean();

                    }


                    if ("id".equals(pullParser.getName())) {
                        String id = pullParser.nextText();
                        if (id != null && !TextUtils.isEmpty(id)) {
                            collectPointBean.setId(Integer.parseInt(id));
                        }
                        ;
                    }
                    if ("latitude".equals(pullParser.getName())) {
                        String latitude = pullParser.nextText();
                        if (latitude != null && !TextUtils.isEmpty(latitude)) {
                            collectPointBean.setLatitude(Double.parseDouble(latitude));
                        }
                        ;
                    }
                    if ("longitude".equals(pullParser.getName())) {
                        String longitude = pullParser.nextText();
                        if (longitude != null && !TextUtils.isEmpty(longitude)) {
                            collectPointBean.setLongitude(Double.parseDouble(longitude));
                        }
                        ;
                    }
                    if ("type".equals(pullParser.getName())) {
                        String type = pullParser.nextText();
                        if (type != null && !TextUtils.isEmpty(type)) {
                            collectPointBean.setType(Integer.parseInt(type));
                        }
                        ;
                    }
                    if ("index".equals(pullParser.getName())) {
                        String index = pullParser.nextText();
                        if (index != null && !TextUtils.isEmpty(index)) {
                            collectPointBean.setIndex(Integer.parseInt(index));
                        }
                        ;
                    }
                    if ("name".equals(pullParser.getName())) {
                        String name = pullParser.nextText();
                        if (name != null && !TextUtils.isEmpty(name)) {
                            collectPointBean.setName(name);
                        }
                        ;
                    }
                    if ("course_name".equals(pullParser.getName())) {
                        String course_name = pullParser.nextText();
                        if (course_name != null && !TextUtils.isEmpty(course_name)) {
                            collectPointBean.setCourse_name(course_name);
                        }
                        ;
                    }
                    if ("image".equals(pullParser.getName())) {
                        String image = pullParser.nextText();
                        if (image != null && !TextUtils.isEmpty(image)) {
                            collectPointBean.setImage(Integer.parseInt(image));
                        }
                        ;
                    }
//                    if ("age".equals(pullParser.getName())){
//                        int age = Integer.valueOf(pullParser.nextText());
//                       collectPointBean.setAge(age);
//                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("collectPointBean".equals(pullParser.getName())) {
                        collectPointBeanList.add(collectPointBean);
                        collectPointBean = null;
                    }
                    break;

            }

            event = pullParser.next();
        }
        return collectPointBeanList;
    }

    public static String getHangdianSign() {
        return HangdianSign;
    }

    public static void setHangdianSign(String hangdianSign) {
        HangdianSign = hangdianSign;
    }

    public static String getHangxianSign() {
        return HangxianSign;
    }

    public  static void  setHangxianSign(String hangxianSign) {
        HangxianSign = hangxianSign;
    }
}