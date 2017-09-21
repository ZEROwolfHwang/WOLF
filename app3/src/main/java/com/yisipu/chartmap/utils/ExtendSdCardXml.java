package com.yisipu.chartmap.utils;

import android.content.Context;
import android.util.Xml;

import com.yisipu.chartmap.bean.FilePathBean;
import com.yisipu.chartmap.bean.ShipBean;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class ExtendSdCardXml
{
	private Context context;

    private String fileName;

	private ShipBean my_ship=new ShipBean();
	public ExtendSdCardXml(Context context, String fileName)
	{

		this.context = context;
		this.fileName=fileName;
	}



	public String WriteXml(FilePathBean filePathBean)
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter strWriter = new StringWriter();
		try
		{
			serializer.setOutput(strWriter);

			// <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
			serializer.startDocument("UTF-8", true);

			// <Author name="Tony">
			serializer.startTag("", "PathSetting");
			serializer.attribute("", "PathSetting", "路径配置");

			// <message date="2013.12.13">
			serializer.startTag("", "path");
			serializer.text(String.valueOf(filePathBean.getPath()));
			serializer.endTag("", "path");

			serializer.startTag("", "isExten");
			serializer.text(String.valueOf(filePathBean.getIsExten()));
			serializer.endTag("", "isExten");

			// </Author>
			serializer.endTag("", "PathSetting");
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


}
