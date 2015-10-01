package com.sem.ssm2.util;

/**
 * States of acceleration.
 * Originally defined by the Accel Library Android authors.
 */
public enum AccelerationState {

    /**
     * State which defines the accelerometer movement registered while resting.
     */
    RESTING(0),

    /**
     * State which defines the accelerometer movement registered while walking.
     */
    WALKING(0.05f),

    /**
     * State which defines the accelerometer movement registered while running.
     */
    RUNNING(0.1f),

    /**
     * State which defines the accelerometer movement registered while moving too fast.
     */
    CHEATING(0);

    private float rotationSpeed;

    AccelerationState(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public boolean isMoving() {
        return rotationSpeed > 0f;
    }
}