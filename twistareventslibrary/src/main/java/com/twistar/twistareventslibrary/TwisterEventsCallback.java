package com.twistar.twistareventslibrary;

/**
 * This is the interface to get the callback of Twistar device events
 */
public interface TwisterEventsCallback {

    void onTwistLeftEventReceived();
    void onTwistRightEventReceived();
    void onButtonPressEventReceived();
    void onButtonReleaseEventReceived();
    void onUnknownEventReceived();
}
