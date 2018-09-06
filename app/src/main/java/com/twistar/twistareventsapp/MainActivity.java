package com.twistar.twistareventsapp;

import android.serialport.SerialPort;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
//    private TwisterEventsCallback mTwisterEventsCallback;
    private SerialPort mSerialPort = null;
    private long mFirstTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runSerialThread();
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

            if (getApplicationContext() == null) {
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

                                if (requestCode == 1) {
                                    mFirstTimeStamp = System.currentTimeMillis();
                                }
                                if (requestCode == 2 && (System.currentTimeMillis() - mFirstTimeStamp) >= 1500) {
                                 //   if (mTwisterEventsCallback != null)
                                    Log.d(TAG, "run: backpress");
                                       // mTwisterEventsCallback.onEventReceived(AppConstants.TWISTER_HARDWARE_EVENTS.BACK_PRESS);
                                } else {
                                    Log.d(TAG, "run: "+ requestCode);
/*
                                    if (mTwisterEventsCallback != null) {
                                        mTwisterEventsCallback.onEventReceived(requestCode);
                                    }
*/
                                }
                            } else {
                                // Unknown EVENT
                                Log.d(TAG, "run: unknown 0");
//                                    mTwisterEventsCallback.onEventReceived(AppConstants.TWISTER_HARDWARE_EVENTS.UNKNOWN_EVENT);
                            }
                        }
                    } else {
                        // Unknown EVENT
                        Log.d(TAG, "run: unknown 1");
//                            mTwisterEventsCallback.onEventReceived(AppConstants.TWISTER_HARDWARE_EVENTS.UNKNOWN_EVENT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


}
