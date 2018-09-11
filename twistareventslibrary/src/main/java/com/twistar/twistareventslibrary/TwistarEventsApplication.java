package com.twistar.twistareventslibrary;

import android.content.Context;
import android.serialport.SerialPort;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class TwistarEventsApplication {

    private static final String TAG = TwistarEventsApplication.class.getSimpleName();
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private TwisterEventsCallback mTwisterEventsCallback;
    private SerialPort mSerialPort = null;
    private Context instance = null;


    public TwistarEventsApplication(Context context) {
        instance = context;

        runSerialThread();
    }

    public void setmTwisterEventsCallback(TwisterEventsCallback mTwisterEventsCallback){
        this.mTwisterEventsCallback = mTwisterEventsCallback;
    }

    /**
     * Here creating the serial port and
     * stating the thread to read the serial data.
     */
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

    /**
     * Here opening the serial port at certain baudrate.
     *
     * @return SerialPort It is the object of SerialPort class to read the data from the device/
     */
    public SerialPort getSerialPort()
            throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            mSerialPort = new SerialPort(new File("/dev/ttyMT1"), 115200, 0);
        }
        return mSerialPort;
    }

    /**
     * This thread is used to read the serial data from the device and parsing it to call the events.
     */
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();

            if (instance == null) {
                interrupt();
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
                                    sendEventsByRequestCode(requestCode);
                                }
                            } else {
                                // Unknown EVENT
                                if (mTwisterEventsCallback != null)
                                    mTwisterEventsCallback.onUnknownEventReceived();
                            }
                        }
                    } else {
                        // Unknown EVENT
                        if (mTwisterEventsCallback != null)
                            mTwisterEventsCallback.onUnknownEventReceived();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * This function can manage the events depending upon the request code.
     *
     * @param requestCode It is the first argument and used to send callbacks depending upon its value.
     */
    private void sendEventsByRequestCode(int requestCode) {
        if (mTwisterEventsCallback != null) {
            switch (requestCode) {
                case Constants.TWISTER_HARDWARE_EVENTS.TWIST_LEFT:
                    mTwisterEventsCallback.onTwistLeftEventReceived();
                    break;
                case Constants.TWISTER_HARDWARE_EVENTS.TWIST_RIGHT:
                    mTwisterEventsCallback.onTwistRightEventReceived();
                    break;
                case Constants.TWISTER_HARDWARE_EVENTS.BUTTON_PRESS:
                    mTwisterEventsCallback.onButtonPressEventReceived();
                    break;
                case Constants.TWISTER_HARDWARE_EVENTS.BUTTON_RELEASE:
                    mTwisterEventsCallback.onButtonReleaseEventReceived();
                    break;
                case Constants.TWISTER_HARDWARE_EVENTS.UNKNOWN_EVENT:
                    mTwisterEventsCallback.onUnknownEventReceived();
                    break;

            }
        }
    }

}
