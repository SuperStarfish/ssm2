package com.sem.ssm2.structures.mp_fishingboat;

import java.io.Serializable;
import java.util.Random;

/**
 * Coordinates of an object that can be sent to the other player.
 */
public class Coordinate implements Serializable {

    protected static Random random = new Random();

    public static Coordinate randomCoordinate() {
        return new Coordinate(random.nextFloat() * 1080 - 32, random.nextFloat() * 1920 - 32);
    }


    /**
     * X and Y locations.
     * Between [0-1] where 1 is the screen width/height.
     */
    protected float cX, cY;

    /**
     * Constructs a new Coordinate object with the given X and Y locations.
     * X and Y between [0-1] where 1 is the screen width/height.
     *
     * @param x X location.
     * @param y Y location.
     */
    public Coordinate(float x, float y) {
        cX = x;
        cY = y;
    }

    /**
     * Returns the X location.
     *
     * @return float between [0-1] where 1 is the screen width.
     */
    public float getX() {
        return cX;
    }

    /**
     * Sets the X location.
     *
     * @param x float between [0-1] where 1 is the screen width.
     */
    public void setX(float x) {
        cX = x;
    }

    /**
     * Returns the Y location.
     *
     * @return float between [0-1] where 1 is the screen height.
     */
    public float getY() {
        return cY;
    }

    /**
     * Sets the Y location.
     *
     * @param y x float between [0-1] where 1 is the screen height.
     */
    public void setY(float y) {
        cY = y;
    }

    /**
     * Returns a string representation of the coordinate.
     *
     * @return string representing the coordinate.
     */
    public String toString() {
        return "(" + cX + ", " + cY + ")";
    }
}