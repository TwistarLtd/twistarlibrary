package com.twistar.twistareventsapp;

import android.serialport.SerialPort;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.twistar.twistareventslibrary.TwistarEventsApplication;
import com.twistar.twistareventslibrary.TwisterEventsCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity implements TwisterEventsCallback{
    private static final String TAG = MainActivity.class.getSimpleName();


//    private TwisterEventsCallback mTwisterEventsCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwistarEventsApplication twistarEventsApplication = new TwistarEventsApplication();
        twistarEventsApplication.onCreate(this);
        twistarEventsApplication.setTwisterEventsListner(this);
    }


    @Override
    public void onEventReceived(int requestCode) {
        Log.d(TAG, "onEventReceived: "+requestCode);
    }
}
