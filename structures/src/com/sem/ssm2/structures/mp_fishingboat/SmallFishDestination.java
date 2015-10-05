package com.sem.ssm2.structures.mp_fishingboat;

import java.io.Serializable;

public class SmallFishDestination implements Serializable {

    /**
     * Id of the smallfish to whom this destination belongs.
     */
    protected int cId;

    /**
     * Destination which the smallfish tries to reach.
     */
    protected Coordinate cNewDestination;

    /**
     * Creates a new SmallFishDestination object for the smallfish with the given id.
     *
     * @param id          of the smallfish to to whom this destination belongs.
     * @param destination coordinate object representing the destination for the smallfish.
     */
    public SmallFishDestination(int id, Coordinate destination) {
        cId = id;
        cNewDestination = destination;
    }

    /**
     * Returns the id of the smallfish to whom this destination belongs.
     *
     * @return int id of the smallfish.
     */
    public int getcId() {
        return cId;
    }

    /**
     * Changes the smallfish to whom this destination belongs.
     *
     * @param id id of the new owner smallfish
     */
    public void setcId(int id) {
        this.cId = id;
    }

    /**
     * Returns the destination coordinates.
     *
     * @return coordinate object representing the location of the destination.
     */
    public Coordinate getcNewDestination() {
        return cNewDestination;
    }

    /**
     * Sets the destination coordinates to the given coordinates.
     *
     * @param newDestination new coordinates.
     */
    public void setcNewDestination(Coordinate newDestination) {
        this.cNewDestination = newDestination;
    }
}