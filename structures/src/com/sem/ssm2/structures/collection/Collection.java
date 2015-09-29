package com.sem.ssm2.structures.collection;

import com.sem.ssm2.structures.Subject;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Collection which can hold collectibles.
 * A collectible exists out of an shape and colour.
 * Only one of each collectibles can exist in one collection.
 */
public class Collection extends HashSet<Collectible> implements Serializable {

    /**
     * Identifier of the collection.
     */
    protected String cId;

    /**
     * Subject that gets notified when something is added to the HashMap.
     */
    protected Subject cChangeAddSubject;


    /**
     * Constructs a HashSet collection to store collectibles gained by the player.
     *
     * @param id Identifier of the collection.
     */
    public Collection(final String id) {
        super();
        cId = id;
        cChangeAddSubject = new Subject();
    }

    /**
     * Returns the identifier of this collection.
     *
     * @return The identifier.
     */
    public String getId() {
        return cId;
    }

    /**
     * Sets the id of this collection.
     *
     * @param groupId The id the collection belongs to.
     */
    public void setId(final String groupId) {
        cId = groupId;
    }

    /**
     * Sorts a list based on a given comparator.
     *
     * @param comparator The comparator.
     * @return The sorted list.
     */
    public ArrayList<Collectible> sort(final Comparator<Collectible> comparator) {
        ArrayList<Collectible> list = new ArrayList<Collectible>(this);
        Collections.sort(list, comparator);
        return list;
    }

    @Override
    public boolean addAll(final java.util.Collection<? extends Collectible> c) {
        boolean result = super.addAll(c);
        cChangeAddSubject.update(c);
        return result;
    }

    /**
     * String representation of collection.
     *
     * @return String representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection<");

        int counter = 0;
        for (Collectible c : this) {
            counter++;
            sb.append(c.toString());
            if (counter < this.size()) {
                sb.append(", ");
            }
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Resets the current collection. In other words, deletes all the
     * collectibles in this collection.
     */
    public void resetCollection() {
        super.clear();
    }

    /**
     * Getter for the change add subject. Gets notified when something is added to the HashMap.
     *
     * @return The change add subject.
     */
    public Subject getChangeAddSubject() {
        return cChangeAddSubject;
    }


}