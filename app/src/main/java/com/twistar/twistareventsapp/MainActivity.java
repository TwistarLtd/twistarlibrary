package com.twistar.twistareventsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.twistar.twistareventslibrary.TwistarEventsApplication;
import com.twistar.twistareventslibrary.TwisterEventsCallback;

public class MainActivity extends AppCompatActivity implements TwisterEventsCallback {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TwistarEventsApplication twistarEventsApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twistarEventsApplication = new TwistarEventsApplication(this);
        twistarEventsApplication.setmTwisterEventsCallback(this);
    }

    @Override
    public void onTwistLeftEventReceived() {
        Log.d(TAG, "onTwistLeftEventReceived: ");
    }

    @Override
    public void onTwistRightEventReceived() {
        Log.d(TAG, "onTwistRightEventReceived: ");
    }

    @Override
    public void onButtonPressEventReceived() {
        Log.d(TAG, "onButtonPressEventReceived: ");
    }

    @Override
    public void onButtonReleaseEventReceived() {
        Log.d(TAG, "onButtonReleaseEventReceived: ");
    }

    @Override
    public void onUnknownEventReceived() {
        Log.d(TAG, "onUnknownEventReceived: ");
    }
}
