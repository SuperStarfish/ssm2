package com.sem.ssm2.structures.mp_fishingboat;

import java.io.Serializable;

/**
 * Boat of the mp event.
 */
public class BoatData implements Serializable {
    /**
     * Current location of the boat.
     */
    protected Coordinate cLocation;

    /**
     * Current rotation of the boat.
     */
    protected double cRotation;

    /**
     * Constructs a new boat data object, containing information about the boat.
     *
     * @param location of the boat.
     * @param rotation of the boat.
     */
    public BoatData(Coordinate location, double rotation) {
        cLocation = location;
        cRotation = rotation;
    }

    /**
     * Returns the location of the boat.
     *
     * @return Coordinate representing the location of the boat.
     */
    public Coordinate getcLocation() {
        return cLocation;
    }

    /**
     * Sets the location of the boat.
     *
     * @param location new location of the boat.
     */
    public void setcLocation(Coordinate location) {
        this.cLocation = location;
    }

    /**
     * Returns the rotation of the boat.
     *
     * @return rotation of the boat in radians.
     */
    public double getcRotation() {
        return cRotation;
    }

    /**
     * Sets the rotation of the boat.
     *
     * @param rotation of the boat in radians.
     */
    public void setcRotation(double rotation) {
        this.cRotation = rotation;
    }
}
