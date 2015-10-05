package com.sem.ssm2.util;

import com.badlogic.gdx.math.Vector3;

/**
 * Configurable object that reads and returns input from the accelerometer the device.
 */
public class Accelerometer {

    /**
     * Keeps tracks if the gravity needs to be filtered.
     */
    protected boolean cFilterGravity;

    /**
     * Vector of the previous update. Needed for gravity filtering.
     */
    protected Vector3 cBaseVector;

    /**
     * Readings under this level will be discarded.
     */
    protected float cNoiseThreshold;

    /**
     * Reader that reads the sensor values from the device.
     */
    protected SensorReader cReader;

    /**
     * Whether to filter the noise per axis or on all three.
     */
    protected boolean cFilterPerAxis;

    /**
     * The default filter to use for the accelerometer.
     */
    protected float cDefaultNoiseFilter = 1.5f;

    /**
     * Gravity filter used in the accelerometer.
     */
    protected float cGravity = 9.5f;

    /**
     * Constructs an accelerometer which is used to read the accelerometer data
     * from the device.
     *
     * @param reader SensorReader
     */
    public Accelerometer(final SensorReader reader) {
        cFilterGravity = false;
        cNoiseThreshold = cDefaultNoiseFilter;
        cReader = reader;
        cBaseVector = cReader.readAccelerometer();
        cFilterPerAxis = true;
    }

    /**
     * Method that reads and filters the current accelerometer readings.
     *
     * @return Current accelerometer readings.
     */
    public Vector3 update() {
        Vector3 readings = cReader.readAccelerometer();
        Vector3 resultVector = readings.cpy();

        if (cFilterGravity) {
            resultVector.set(
                    resultVector.x - cBaseVector.x,
                    resultVector.y - cBaseVector.y,
                    resultVector.z - cBaseVector.z);
        }

        if (cFilterPerAxis) {
            resultVector.set(
                    filterNoise(resultVector.x),
                    filterNoise(resultVector.y),
                    filterNoise(resultVector.z));
        } else {
            resultVector = filterNoise(resultVector);
        }

        cBaseVector = readings;
        return resultVector;
    }

    /**
     * Helper method that should not be called outside of this class.
     * Returns the input if the absolute input is higher than the noise threshold. 0 Otherwise.
     *
     * @param scalar Accelerometer component
     * @return Input if scalar > noise threshold. 0 Otherwise.
     */
    protected final float filterNoise(final float scalar) {
        float result = 0f;
        if (Math.abs(scalar) > cNoiseThreshold) {
            result = scalar;
        }
        return result;
    }

    /**
     * Filters the vector of noise.
     *
     * @param vector The vector to filter.
     * @return The filtered vector.
     */
    protected Vector3 filterNoise(final Vector3 vector) {
        if (vector.epsilonEquals(cBaseVector, cNoiseThreshold)) {
            return cBaseVector;
        }
        return vector;
    }

    /**
     * Method that determines the highest (absolute) acceleration of the input.
     *
     * @param accelData Input data from which to determine the highest acceleration component.
     * @return Highest acceleration of the input.
     */
    public float highestAccelerationComponent(final Vector3 accelData) {
        float highestComponent = Math.abs(accelData.x);

        float tempAccelStorage = Math.abs(accelData.y);
        if (tempAccelStorage > highestComponent) {
            highestComponent = tempAccelStorage;
        }
        tempAccelStorage = Math.abs(accelData.z);
        if (tempAccelStorage > highestComponent) {
            highestComponent = tempAccelStorage;
        }

        return highestComponent;
    }

    /**
     * Sets the filtering of the gravity from the accelerometer on/off.
     *
     * @param mode Default is off.
     */
    public void filterGravity(final boolean mode) {
        cFilterGravity = mode;
    }


    /**
     * Helper method that should not be called outside of this class.
     * Checks whether a given acceleration is the gravity or not.
     *
     * @param scalar Acceleration
     * @return Boolean whether the input acceleration is the gravity or not.
     */
    protected final boolean isGravity(final float scalar) {
        float absoluteValue = Math.abs(scalar);
        return (absoluteValue >= cGravity);
    }

    /**
     * Sets the noise threshold.
     * A higher threshold will result in only higher accelerations being registered by the accelerometer.
     *
     * @param threshold Default is 1.5f
     */
    public final void setNoiseThreshold(final float threshold) {
        this.cNoiseThreshold = threshold;
    }

    /**
     * Setter for filters noise per axis.
     *
     * @param filterPerAxis The boolean.
     */
    public void setFilterPerAxis(boolean filterPerAxis) {
        cFilterPerAxis = filterPerAxis;
    }
}