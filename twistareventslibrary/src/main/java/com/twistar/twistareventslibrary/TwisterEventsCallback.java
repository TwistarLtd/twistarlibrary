package com.twistar.twistareventslibrary;

/**
 * This is the interface to get the callback of Twistar device events
 */
public interface TwisterEventsCallback {

    /**
     * To get the twist left event from the device.
     */
    void onTwistLeftEventReceived();

    /**
     * To get the twist right event from the device.
     */
    void onTwistRightEventReceived();

    /**
     * To get the button press event from the device.
     */
    void onButtonPressEventReceived();

    /**
     * To get the button release event from the device.
     */
    void onButtonReleaseEventReceived();

    /**
     * Whenever there is any unknown event. This method will have the callback.
     */
    void onUnknownEventReceived();
}
