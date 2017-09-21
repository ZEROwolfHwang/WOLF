package com.yisipu.chartmap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.yisipu.chartmap.bean.ShipBean;

public class Xml_Operate
{
	private Context context;
	private String str1;
	private String str2;
    private String fileName;
	private String sign;
	private  String SOSMobile;
	private ShipBean my_ship=new ShipBean();
	public Xml_Operate(Context context,String fileName)
	{

		this.context = context;
		this.fileName=fileName;
	}

	public String getStr1()
	{
		return str1;
	}

	public String getStr2()
	{
		return str2;
	}

	public void ReadXml()
	{
		try
		{
			File file = new File(fileName);

			FileInputStream fis =new FileInputStream(file);
			XmlPullParser xParser = Xml.newPullParser();

			xParser.setInput(fis, "UTF-8");
			int eventCode = xParser.getEventType();
			while (eventCode != XmlPullParser.END_DOCUMENT)
			{
				switch (eventCode)
				{
				case XmlPullParser.START_DOCUMENT:// 0文档开始事件
					break;
				case XmlPullParser.START_TAG:// 2开始元素

					if ("MMSI".equals(xParser.getName()))
					{

						my_ship.setMMSI(Integer.valueOf(xParser.nextText()));
//						Logger.e("", str1);
					} else if ("type".equals(xParser.getName()))
					{
						my_ship.setType(Integer.valueOf(xParser.nextText()));
					}
					if ("shipChName".equals(xParser.getName()))
					{
						my_ship.setChineseName(xParser.nextText());
//						Logger.e("", str1);
					} else if ("shipEnName".equals(xParser.getName()))
					{
						my_ship.setEnglishName(xParser.nextText());
					}
					if ("callName".equals(xParser.getName()))
					{
						my_ship.setHuhao(xParser.nextText());
//						Logger.e("", str1);
					} else if ("A".equals(xParser.getName()))
					{
						my_ship.setDimBow(Integer.valueOf(xParser.nextText()));
					}
					if ("B".equals(xParser.getName()))
					{
						my_ship.setDimStern(Integer.valueOf(xParser.nextText()));
//						Logger.e("", str1);
					} else if ("C".equals(xParser.getName()))
					{
						my_ship.setDimPort(Integer.valueOf(xParser.nextText()));
					}else if ("D".equals(xParser.getName()))
				{
					my_ship.setDimStarboard(Integer.valueOf(xParser.nextText()));
//					Logger.e("", str1);
				} else if ("sign".equals(xParser.getName()))
				{
					sign= xParser.nextText();
				}else if ("SOSMobile".equals(xParser.getName()))
					{
						SOSMobile= xParser.nextText();
					}
					else if ("nickname".equals(xParser.getName()))
					{

						my_ship.setOtherName(String.valueOf(xParser.nextText()));
					}

					break;
				}
				eventCode = xParser.next();// 指针转到下一个位置
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String WriteXml(ShipBean shipBean , String sign,String sosPhone)
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter strWriter = new StringWriter();
		try
		{
			serializer.setOutput(strWriter);

			// <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
			serializer.startDocument("UTF-8", true);

			// <Author name="Tony">
			serializer.startTag("", "info");
			serializer.attribute("", "Name", "AIS参数");

			// <message date="2013.12.13">
			serializer.startTag("", "MMSI");
			serializer.text(String.valueOf(shipBean.getMMSI()));
			serializer.endTag("", "MMSI");

			serializer.startTag("", "shipChName");
			serializer.text(String.valueOf(shipBean.getChineseName()));
			serializer.endTag("", "shipChName");

			serializer.startTag("", "shipEnName");
			serializer.text(String.valueOf(shipBean.getEnglishName()));
			serializer.endTag("", "shipEnName");

			serializer.startTag("", "callName");
			serializer.text(String.valueOf(shipBean.getHuhao()));
			serializer.endTag("", "callName");

			serializer.startTag("", "type");
			serializer.text(String.valueOf(shipBean.getType()));
			serializer.endTag("", "type");

			serializer.startTag("", "A");
			serializer.text(String.valueOf(shipBean.getDimBow()));
			serializer.endTag("", "A");

			serializer.startTag("", "B");
			serializer.text(String.valueOf(shipBean.getDimStern()));
			serializer.endTag("", "B");

			serializer.startTag("", "C");
			serializer.text(String.valueOf(shipBean.getDimPort()));
			serializer.endTag("", "C");

			serializer.startTag("", "D");
			serializer.text(String.valueOf(shipBean.getDimStarboard()));
			serializer.endTag("", "D");

			serializer.startTag("", "SOSMobile");
			serializer.text(String.valueOf(sosPhone));
			serializer.endTag("", "SOSMobile");

			serializer.startTag("", "sign");
			serializer.text(String.valueOf(sign));
			serializer.endTag("", "sign");

			serializer.startTag("", "nickname");
			serializer.text(String.valueOf(shipBean.getOtherName()));
			serializer.endTag("", "nickname");





			// </Author>
			serializer.endTag("", "info");
			serializer.endDocument();

			return strWriter.toString();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean Write(String writeString, String name)
	{
		try
		{

			File file = new File(name);


			OutputStream oStream = new FileOutputStream(file);//这种方式将会把要写入的文件（name）保存到
			OutputStreamWriter osw = new OutputStreamWriter(oStream);				  //“/data/data/'package name'/file”目录下

			osw.write(writeString);//writeString是要写入的我们刚刚写好的“String WriteXml”
			osw.close();
			oStream.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public ShipBean getMy_ship() {
		return my_ship;
	}

	public void setMy_ship(ShipBean my_ship) {
		this.my_ship = my_ship;
	}

	public String getSOSMobile() {
		return SOSMobile;
	}

	public void setSOSMobile(String SOSMobile) {
		this.SOSMobile = SOSMobile;
	}
}
