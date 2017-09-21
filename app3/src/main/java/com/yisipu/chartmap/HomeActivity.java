package com.yisipu.chartmap;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yisipu.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * HomeActivity
 * Created by Administrator on 2016/7/29.
 */
public class HomeActivity extends Activity {
    //Ais为0
    private final String UART0_DEVICE_NODE = "/dev/ttyS1";
    //AIS为2的时候我们收到模拟信息
    private final String UART1_DEVICE_NODE = "/dev/ttyS0";

    private final String AIS_SW_DEVICE_NODE = "/sys/customer/ais_en";
    //
//    private final int     UART0_BAUDRATE = 9600;
    private final int UART0_BAUDRATE = 9600;
    private final int UART1_BAUDRATE = 38400;
    private SerialPort mUART0;
    private SerialPort mUART1;

    private InputStream mUART0InputStream;
    private OutputStream mUART0OutputStream;
    private InputStream mUART1InputStream;
    private OutputStream mUART1OutputStream;

    private mUART0ReadThread mUART0mReadThread;
    private mUART1ReadThread mUART1mReadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init_UART0();
    }

    public class mUART0ReadThread extends Thread {
        @Override
        public void run() {
//            super.run();


            while(!isInterrupted()) {
                Logger.i("ddgg3");
            }
            int size;
//                try {
//                    byte[] buffer = new byte[256];
//
//                    if (mUART0InputStream == null) return;
//                        size = mUART0InputStream.read(buffer);
//                        if (size > 0) {
//                            String str = new String(buffer, 0, size, "UTF-8");
//                            Logger.d("ddsgg", "_1:" + str);
//                            if (str.contains("$DUAIR")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                            if (str.contains("$PAIS")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                            if (str.contains("$GP")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                        }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return;
//                }
//            }
        }
    }

    public class mUART1ReadThread extends Thread {
        @Override
        public void run() {
//            super.run();
            Logger.i("ddgg");
//            while(!isInterrupted()) {

//                int size;
//                try {
//                    byte[] buffer = new byte[256];
//
//                    if (mUART1InputStream == null) return;
//                        size = mUART1InputStream.read(buffer);
//                        Logger.i("ddgg" + size);
//                        if (size > 0) {
//                            String str = new String(buffer, 0, size, "UTF-8");
//                            Logger.d("ddsgg", "_1:" + str);
//                            if (str.contains("$DUAIR")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                            if (str.contains("$PAIS")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                            if (str.contains("$GP")) {
//                                Logger.d(TAG, "_1:" + str);
//                            }
//                        }
//
//                } catch (IOException e) {
//                    Logger.i("ddggdgggg" +e);
//                    e.printStackTrace();
//                    return;
//                }
//            }
        }
        }


        public void init_UART0() {
            try {
//                mUART0 = new SerialPort(new File(UART0_DEVICE_NODE), UART0_BAUDRATE);
//
//                mUART0InputStream = mUART0.getInputStream();
//                mUART0OutputStream = mUART0.getOutputStream();
                Logger.i("dddgsg4");
                mUART0mReadThread = new mUART0ReadThread();
                mUART0mReadThread.start();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                Logger.i("dddgsg" + e.toString());
                e.printStackTrace();
            }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                Logger.i("dddgsg2");
//            }
        }

        public void init_UART1() {
            try {
                mUART1 = new SerialPort(new File(UART1_DEVICE_NODE), UART1_BAUDRATE);
                mUART1InputStream = mUART1.getInputStream();
                mUART1OutputStream = mUART1.getOutputStream();

                mUART1mReadThread = new mUART1ReadThread();
                mUART1mReadThread.start();
                //        Logger.d(TAG, getNodeString(AIS_SW_DEVICE_NODE));
                //         setNodeString(AIS_SW_DEVICE_NODE,"1");
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

