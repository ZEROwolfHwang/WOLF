package com.yisipu.chartmap;

import com.orhanobut.logger.Logger;
import com.yisipu.serialport.SerialPort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class UatrTest {
    @SuppressWarnings("unused")
    private static final String TAG = UatrTest.class.getSimpleName();

//    private final String  UART0_DEVICE_NODE = "/dev/ttyHSL0";
//    private final String  UART1_DEVICE_NODE = "/dev/ttyHSL1";
    //
    private final String  UART0_DEVICE_NODE = "/dev/ttyS1";
    //AIS为2的时候我们收到模拟信息
    private final String  UART1_DEVICE_NODE = "/dev/ttyS0";

    private final String  AIS_SW_DEVICE_NODE = "/sys/customer/ais_en";
//
//    private final int     UART0_BAUDRATE = 9600;
    private final int     UART0_BAUDRATE = 9600;
    private final int     UART1_BAUDRATE = 38400;

//    private final int     UART0_BAUDRATE = 38400;
//    private final int     UART1_BAUDRATE = 38400;
    private SerialPort    mUART0;
    private SerialPort    mUART1;

    private InputStream   mUART0InputStream;
    private OutputStream  mUART0OutputStream;
    private InputStream   mUART1InputStream;
    private OutputStream  mUART1OutputStream;

    private mUART0ReadThread mUART0mReadThread;
    private mUART1ReadThread mUART1mReadThread;

    private class mUART0ReadThread extends Thread {
        @Override
        public void run() {
//            super.run();

            while(!isInterrupted()) {
                Logger.i("ddgg3");
                int size;
                try {
                    byte[] buffer = new byte[256];

                    if (mUART0InputStream == null) return;
                        size = mUART0InputStream.read(buffer);
                        if (size > 0) {
                            String str = new String(buffer, 0, size, "UTF-8");
                            Logger.d("ddsgg", "_1:" + str);
                            if (str.contains("$DUAIR")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                            if (str.contains("$PAIS")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                            if (str.contains("$GP")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class mUART1ReadThread extends Thread {
        @Override
        public void run() {
//            super.run();

            while(!isInterrupted()) {
                Logger.i("ddgg");
                int size;
                try {
                    byte[] buffer = new byte[256];

                    if (mUART1InputStream == null) return;
                        size = mUART1InputStream.read(buffer);
                        Logger.i("ddgg" + size);
                        if (size > 0) {
                            String str = new String(buffer, 0, size, "UTF-8");
                            Logger.d("ddsgg", "_1:" + str);
                            if (str.contains("$DUAIR")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                            if (str.contains("$PAIS")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                            if (str.contains("$GP")) {
                                Logger.d(TAG, "_1:" + str);
                            }
                        }

                } catch (IOException e) {
                    Logger.i("ddggdgggg" +e);
                    e.printStackTrace();
                    return;
                }
            }
        }
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
        Logger.d(TAG, "getNodeString:" + path + " " + prop);
        return prop;
    }
    /**
     * 写节点
     */
    private static void setNodeString(String path,String val) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bufWriter = new BufferedWriter(fw);
            bufWriter.write(val);  // 写操作
            fw.flush();
            bufWriter.close();
            fw.close();
            Logger.d(TAG, "setNodeString:" + path + " " + val +" "+ getNodeString(path));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(TAG, "can't write the " + path);
        }
    }

    public void init_UART0() {
        try
        {
            mUART0 = new SerialPort(new File(UART0_DEVICE_NODE), UART0_BAUDRATE);

            mUART0InputStream  = mUART0.getInputStream();
            mUART0OutputStream = mUART0.getOutputStream();

            mUART0mReadThread = new mUART0ReadThread();
            mUART0mReadThread.start();
        } catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void init_UART1() {
        try
        {
            mUART1 = new SerialPort(new File(UART1_DEVICE_NODE), UART1_BAUDRATE);
            mUART1InputStream  = mUART1.getInputStream();
            mUART1OutputStream = mUART1.getOutputStream();

            mUART1mReadThread = new mUART1ReadThread();
            mUART1mReadThread.start();
    //        Logger.d(TAG, getNodeString(AIS_SW_DEVICE_NODE));
   //         setNodeString(AIS_SW_DEVICE_NODE,"1");
        } catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public byte[] getUart0RxBuffer() {
        byte[] mRxBuffer = new byte[2];

        try
        {
            if(mUART0InputStream != null)
            {
                mUART0InputStream.read(mRxBuffer);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mRxBuffer;
    }
    
    public byte[] getUart1RxBuffer() {
        byte[] mRxBuffer = new byte[2];

        try
        {
            if(mUART1InputStream != null)
            {
                mUART1InputStream.read(mRxBuffer);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return mRxBuffer;
    }
    
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void UART1Tx(String cmd) {
        byte[] mTxBuffer = cmd.getBytes();
        Logger.d("UART1Tx",cmd);
        try
        {
            if(mUART1OutputStream != null)
            {
                mUART1OutputStream.write(mTxBuffer);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void close_UART0() {
        try
        {
            if(mUART0InputStream != null)
            {
                mUART0InputStream.close();
            }
            if(mUART0OutputStream != null)
            {
                mUART0OutputStream.close();
            }

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(mUART0 != null)
        {
            mUART0.close();
        }
    }
    
    public void close_UART1() {
        try
        {
            if(mUART1InputStream != null)
            {
                mUART1InputStream.close();
            }
            if(mUART1OutputStream != null)
            {
                mUART1OutputStream.close();
            }

            if (mUART1mReadThread != null)
            {
                mUART1mReadThread.interrupt();
            }

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(mUART1 != null)
        {
            mUART1.close();
        }
    }
}
