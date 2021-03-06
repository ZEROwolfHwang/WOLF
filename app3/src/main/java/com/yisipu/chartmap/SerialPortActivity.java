package com.yisipu.chartmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.yisipu.serialport.SerialPort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/7/30.
 */
public abstract class SerialPortActivity extends BaseActivity{
    private static final String TAG = SerialPortActivity.class.getSimpleName();

   /*
真机读取的gps端口
 */
    private final String  UART0_DEVICE_NODE = "/dev/ttyHSL0";
/*
真机读取的Ais端口
 */
    private final String SERIALPORT_DEVICE_NODE = "/dev/ttyHSL1";

    private InputStream   mUART0InputStream;
    private OutputStream  mUART0OutputStream;
    private SerialPort    mUART0;
//    private static Runnable runnable;
     /*
     读取Ais端口模拟器
      */
//    private final String  SERIALPORT_DEVICE_NODE = "/dev/ttyS0";
    private final int     SERIALPORT_BAUDRATE = 38400;
    //gps  9600
    private final int     UART0_BAUDRATE = 9600;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private final String  AIS_SW_DEVICE_NODE = "/sys/customer/ais_en";
    String y2="";
    int index=1;
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    BufferedReader in2=new BufferedReader(new InputStreamReader(mInputStream ));

                    String y="";

                    while((y=in2.readLine())!=null){//一行一行读
                        if (y.length() > 0) {
                            if(y.length()>7&&y.trim().charAt(7)=='2'){
                                if(index==1){
                                    y2=y;
                                    index++;
                                    Logger.i("message1:hhhhh"+y);
                                }else if(index==2){
                                    y=y2+y;
                                    index=1;
                                    y2="";
                                    onDataReceived(y.getBytes(), y.length());
                                    Logger.i("message:hhhhh"+y);
                                }
                            }else {
                                onDataReceived(y.getBytes(), y.length());
                                Logger.i(y);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    public static Handler handler=new Handler();
    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SerialPortActivity.this.finish();
            }
        });
        b.show();
    }
    /**
     * 获取节点
     */
    private static String getNodeString(String path) {
        String prop = "0";// 默认值
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            prop = reader.readLine();
            reader.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.d("getNodeString:" + path + " " + prop);
        return prop;
    }
    /**
     * 写节点
     */
    public static void setNodeString(String path,String val) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bufWriter = new BufferedWriter(fw);
            bufWriter.write(val);  // 写操作
            fw.flush();
            bufWriter.close();
            fw.close();
            Logger.d("setNodeString:" + path + " " + val +" "+ getNodeString(path));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(TAG, "can't write the " + path);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            mSerialPort = new SerialPort(new File(SERIALPORT_DEVICE_NODE), SERIALPORT_BAUDRATE);
            mInputStream  = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();

//            mReadThread = new ReadThread();
//            mReadThread.start();
            /*
            真机调试要取消这边注释
             */
            Logger.d(TAG, getNodeString(AIS_SW_DEVICE_NODE));
                     setNodeString(AIS_SW_DEVICE_NODE,"1");
        } catch (SecurityException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        //初始化gps端口
        init_UART0();
    }
    private final String AIS_SW_DEVICE_ELEC5V = "/sys/customer/uart_elec5v";//AIS控制发射
    /*
    往ais端口写数据
     */
    public void UART1Tx(String cmd) {

        byte[] mTxBuffer = cmd.getBytes();
        Logger.d("UART1Tx",cmd);
        try
        {
            if(mOutputStream != null)
            {
                mOutputStream.write(mTxBuffer);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*
 往gps端口写数据
 */
public void UART0Tx(String cmd) {
    byte[] mTxBuffer = cmd.getBytes();
    try
    {
        if(mUART0OutputStream != null)
        {
            mUART0OutputStream.write(mTxBuffer);
        }
    } catch (IOException e)
    {
        e.printStackTrace();
    }
}
    //初始化gps端口
    public void init_UART0(){
        try
        {
            mUART0 = new SerialPort(new File(UART0_DEVICE_NODE), UART0_BAUDRATE);

            mUART0InputStream  = mUART0.getInputStream();
            mUART0OutputStream = mUART0.getOutputStream();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected abstract void onDataReceived(final byte[] buffer, final int size);

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if(mSerialPort != null){
            mSerialPort.close();
        }
        mReadThread = null;
        mSerialPort = null;

        if(mUART0 != null){
            mUART0.close();
        }

        mUART0 = null;

        super.onDestroy();
    }
}
