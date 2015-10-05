package com.sem.ssm2.structures.mp_fishingboat;

/**
 * Rotation of the fishing boat.
 */
public class Rotation {

    /**
     * Rotation in radians.
     */
    protected float cRotation;

    /**
     * Constructs a new rotation object.
     *
     * @param rotation rotation in radians.
     */
    public Rotation(float rotation) {
        cRotation = rotation;
    }

    /**
     * Returns the rotation in radians.
     *
     * @return rotation in radians.
     */
    public float getRotation() {
        return cRotation;
    }

    /**
     * Sets the current rotation to the given rotation.
     *
     * @param rotation new rotation in radians.
     */
    public void setRotation(float rotation) {
        cRotation = rotation;
    }
}