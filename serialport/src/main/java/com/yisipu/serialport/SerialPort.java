package com.yisipu.serialport;

import java.io.*;

import android.util.Log;


public class SerialPort {
    private static final String TAG = SerialPort.class.getSimpleName();
    
    private FileDescriptor   mFd;
    private FileInputStream  mFileInputStream;
    private FileOutputStream mFileOutputStream;
    
    static {
        try
        {
            System.loadLibrary("serialport_jni");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load libjni_serialport.so!");
        }
    }
    

    public SerialPort(File device, int baudrate) throws SecurityException, IOException {
/* Check access permission
* 不是真机的时候要，是真机的时候不要
* */

//        if (!device.canRead() || !device.canWrite()) {
//            try {
//				/* Missing read/write permission, trying to chmod the file */
//                Process su;
//                su = Runtime.getRuntime().exec("/system/bin/su");
//                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
//                        + "exit\n";
//                su.getOutputStream().write(cmd.getBytes());
//                if ((su.waitFor() != 0) || !device.canRead()
//                        || !device.canWrite()) {
//                    throw new SecurityException();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new SecurityException();
//            }
//        }

        mFd = open(device.getAbsolutePath(), baudrate);
        if (mFd == null) {  
            Log.e(TAG, "native open returns null");  
            throw new IOException();  
        }  
        mFileInputStream = new FileInputStream(mFd);  
        mFileOutputStream = new FileOutputStream(mFd);  
    }  
  
    public InputStream getInputStream() {  
        return mFileInputStream;  
    }  
  
    public OutputStream getOutputStream() {  
        return mFileOutputStream;  
    }

    
    //JNI interface
    private native FileDescriptor open(String path, int baudrate);
    public  native int close();
}
