package com.sem.ssm2.structures;

import java.io.Serializable;
import java.util.Observable;

/**
 * Subject that can be subscribed to for changes.
 */
public class Subject extends Observable implements Serializable {

    /**
     * Notifies the subscribers.
     */
    public final void update() {
        update(null);
    }

    /**
     * Notifies the subscribers.
     *
     * @param item Passes the object to its subscribers with the update.
     */
    public final void update(final Object item) {
        setChanged();
        notifyObservers(item);
    }
}