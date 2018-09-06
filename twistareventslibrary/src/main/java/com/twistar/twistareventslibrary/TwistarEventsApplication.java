package com.twistar.twistareventslibrary;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.serialport.SerialPort;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class TwistarEventsApplication  {

    private static final String TAG = TwistarEventsApplication.class.getSimpleName();
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private TwisterEventsCallback mTwisterEventsCallback;
    private SerialPort mSerialPort = null;
    private long mFirstTimeStamp;
    private static TwistarEventsApplication instance = null;



    public void onCreate(Context context) {
       // super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "onCreate: ");
        instance = this;
        runSerialThread();

    }


    public static TwistarEventsApplication getContext() {
        return instance;
    }

    /**
     * Set interface to get callback form thread
     *
     * @param twisterEventsCallback
     */
    public void setTwisterEventsListner(TwisterEventsCallback twisterEventsCallback) {
        mTwisterEventsCallback = twisterEventsCallback;
    }

    public void runSerialThread() {
        try {
            mSerialPort = getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            /* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            Log.e("Serial Interface :- ", "Security error");
        } catch (IOException e) {
            Log.e("Serial Interface :- ", "Unknown error");
        } catch (InvalidParameterException e) {
            Log.e("Serial Interface :- ", "Configuration error");
        }
    }

    public SerialPort getSerialPort()
            throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */

            // TODO
        /*    String packageName = getPackageName();
            SharedPreferences sp = getSharedPreferences(packageName + "_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

			*//* Check parameters *//*
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }*/

            /* Open the serial port */
            // @manish PORT and DEVICE are STATIC
            mSerialPort = new SerialPort(new File("/dev/ttyMT1"), 115200, 0);
        }
        return mSerialPort;
    }



    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();

            if (instance == null) {
                interrupt();
//                closeSerialPort();
            }

            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            int data = buffer[i];
                            // stupid Java thinks that bytes are signed
                            data &= 0xff;
                            Log.d(TAG, "run: ");
                            if (data < 32 || data > 127) {
                                int requestCode = Integer.parseInt(Integer.toHexString(data));
                                if (mTwisterEventsCallback != null) {
                                    if (requestCode == 1) {
                                        mFirstTimeStamp = System.currentTimeMillis();
                                    }
                                    if (requestCode == 2 && (System.currentTimeMillis() - mFirstTimeStamp) >= 1500) {
                                        if (mTwisterEventsCallback != null)
                                            mTwisterEventsCallback.onEventReceived(Constants.TWISTER_HARDWARE_EVENTS.BACK_PRESS);
                                    } else {
                                        if (mTwisterEventsCallback != null) {
                                            mTwisterEventsCallback.onEventReceived(requestCode);
                                        }
                                    }
                                }
                            } else {
                                // Unknown EVENT
                                if (mTwisterEventsCallback != null)
                                    mTwisterEventsCallback.onEventReceived(Constants.TWISTER_HARDWARE_EVENTS.UNKNOWN_EVENT);
                            }
                        }
                    } else {
                        // Unknown EVENT
                        if (mTwisterEventsCallback != null)
                            mTwisterEventsCallback.onEventReceived(Constants.TWISTER_HARDWARE_EVENTS.UNKNOWN_EVENT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
