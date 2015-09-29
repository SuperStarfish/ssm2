package com.sem.ssm2.util;

/**
 * States of acceleration.
 * Originally defined by the Accel Library Android authors.
 */
public enum AccelerationState {
    /**
     * State which defines the accelerometer movement registered while resting.
     */
    RESTING,

    /**
     * State which defines the accelerometer movement registered while walking.
     */
    WALKING,

    /**
     * State which defines the accelerometer movement registered while running.
     */
    RUNNING,

    /**
     * State which defines the accelerometer movement registered while moving too fast.
     */
    CHEATING
}