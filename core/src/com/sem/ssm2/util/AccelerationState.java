package com.sem.ssm2.util;

/**
 * States of acceleration.
 * Originally defined by the Accel Library Android authors.
 */
public enum AccelerationState {

    /**
     * State which defines the accelerometer movement registered while resting.
     */
    RESTING(0, 0),

    /**
     * State which defines the accelerometer movement registered while walking.
     */
    WALKING(0.05f, 2),
//    WALKING(0.05f, 0.2f),

    /**
     * State which defines the accelerometer movement registered while running.
     */
    RUNNING(0.1f, 4),
//    RUNNING(0.1f, 0.4f),

    /**
     * State which defines the accelerometer movement registered while moving too fast.
     */
    CHEATING(0, 0);

    private float rotationSpeed;
    private float eventChance;

    AccelerationState(float rotationSpeed, float eventChance){
        this.rotationSpeed = rotationSpeed;
        this.eventChance = eventChance;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public float getEventChance() {
        return eventChance;
    }

    public boolean isMoving() {
        return rotationSpeed > 0f;
    }
}