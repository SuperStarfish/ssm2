package com.sem.ssm2.structures.mp_fishingboat;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class SmallFishData implements Serializable {

    protected float speed = 5f;

    /**
     * cPosition: current position of the smallfish.
     * cDestination: coordinates of the destination of the smallfish.
     */
    protected Coordinate position, destination;

    public SmallFishData() {
        this.position = Coordinate.randomCoordinate();
        this.destination = Coordinate.randomCoordinate();
    }

    /**
     * Returns the position of the smallfish.
     *
     * @return Coordinate object representing the position of the smallfish on the screen.
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * Sets the position of the smallfish to the given coordinate.
     *
     * @param position new position for the smallfish.
     */
    public void setPosition(Coordinate position) {
        this.position = position;
    }

    /**
     * Returns the destination of the smallfish.
     *
     * @return Coordinate object representing the current destination of the smallfish.
     */
    public Coordinate getDestination() {
        return destination;
    }

    /**
     * Sets the destination of the smallfish to the given coordinate.
     *
     * @param destination new destination
     */
    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    /**
     * Checks if the destination of the smallfish has been reached, in other words,
     * checks if the current location is the same as the destination.
     *
     * @return boolean representing whether the destination has been reached.
     */
    public boolean destinationReached() {
        boolean xCheck = destination.getX() <= position.getX() + 32
                && destination.getX() >= position.getX();

        boolean yCheck = destination.getY() <= position.getY() + 32
                && destination.getY() >= position.getY();
        return xCheck && yCheck;
    }

    /**
     * Moves the smallfish closer to its destination.
     */
    public void move() {
        float diffX = destination.getX() - getCenterX();
        float diffY = destination.getY() - getCenterY();

        float newX = position.getX() + speed * Math.signum(diffX);
        float newY = position.getY() + speed * Math.signum(diffY);

        position.setX(newX);
        position.setY(newY);
    }

    /**
     * Returns the X location of the center point of the smallfish. (With regards to its sprite).
     *
     * @return float between [0-1] where 1 is the screen width.
     */
    public float getCenterX() {
        return position.getX() + (float) 16 / 2;
    }

    /**
     * Returns the Y location of the center point of the smallfish. (With regards to its sprite).
     *
     * @return float between [0-1] where 1 is the screen height.
     */
    public float getCenterY() {
        return position.getY() + (float) 16 / 2;
    }

    /**
     * Checks whether the given boundaries overlap with the smallfish.
     * This only works for rectangles!
     *
     * @param xMin Left most position of the rectangle
     * @param xMax Right most position of the rectangle
     * @param yMin Bottom most position of the rectangle
     * @param yMax Top most position of the rectangle
     * @return boolean whether or not the rectangle and the smallfish overlap.
     */
    public boolean intersects(double xMin, double xMax, double yMin, double yMax) {
        double fishXMin = position.getX();
        double fishYMin = position.getY();
        double fishXMax = fishXMin + 16;
        double fishYMax = fishYMin + 16;

        double xOverlap = Math.max(0, Math.min(xMax, fishXMax) - Math.max(xMin, fishXMin));
        double yOverlap = Math.max(0, Math.min(yMax, fishYMax) - Math.max(yMin, fishYMin));

        return (xOverlap * yOverlap) > 0d;
    }

}