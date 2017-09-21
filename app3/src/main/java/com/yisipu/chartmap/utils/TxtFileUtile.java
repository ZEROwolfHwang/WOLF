package com.yisipu.chartmap.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/14.
 */
public class TxtFileUtile {

        /*
        文件末尾添加
         */
        public static void fileOutputStream(String str,String file,boolean isAppend) throws IOException {
           /*
           换行
            */
            str+="\r\n";
//            String temp="Hello world!\n";

            FileOutputStream fos = new FileOutputStream(file,isAppend);//true表示在文件末尾追加

            fos.write(str.getBytes());
            ;
            fos.close();//流要及时关闭

        }
    /*
    获得txt
     */
         public static String  getTxtFile(String file) {

             String str = "";
             try {
                 File urlFile = new File(file);
                 InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
                 BufferedReader br = new BufferedReader(isr);

                 String mimeTypeLine = null;

                 while ((mimeTypeLine = br.readLine()) != null) {
                     str = str + ":"+mimeTypeLine;
                 }
                 return  str;
             } catch (Exception e) {
                 e.printStackTrace();
                 return  str;
             }

         }
    /*
   assert获得txt
    */
    public static String  getAssertTxtFile(Context context, String file) {

        String str = "";
        try {
//            File urlFile = new File(file);
//            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
           InputStream in= context.getAssets().open(file);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

            String mimeTypeLine = null;

            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + ":"+mimeTypeLine;
            }
            return  str;
        } catch (Exception e) {
            e.printStackTrace();
            return  str;
        }

    }

}
