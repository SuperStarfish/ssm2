package com.sem.ssm2.structures.groups;

import com.sem.ssm2.structures.collection.Collection;

import java.io.Serializable;

/**
 * Representation of a group.
 */
public class Group implements Serializable {

    /**
     * group data.
     */
    protected GroupData cGroupData;

    /**
     * Group collection.
     */
    protected Collection cCollection;

    /**
     * Creates a group collection with the given id.
     *
     * @param id String representing the collection id.
     */
    public Group(final String id) {
        cCollection = new Collection(id);
    }

    /**
     * Creates a group collection with the given id and group data.
     *
     * @param id        String representing the collection id.
     * @param groupData Groupdata object containing data about the group. (owner, etc.)
     */
    public Group(final String id, final GroupData groupData) {
        cCollection = new Collection(id);
        cGroupData = groupData;
    }


    // group data

    /**
     * Gets the current group data.
     *
     * @return Group Data object that is currently being used.
     */
    public GroupData getGroupData() {
        return cGroupData;
    }

    /**
     * Sets the group data to the given group data.
     *
     * @param groupData object to set as the new group data object.
     */
    public void setGroupData(final GroupData groupData) {
        cGroupData = groupData;
    }

    // collections

    /**
     * Gets the current collection.
     *
     * @return Collection current collection.
     */
    public Collection getCollection() {
        return cCollection;
    }

    /**
     * Sets the collection to the given collection.
     *
     * @param collection Collection to replace the current collection with.
     */
    public void setCollection(final Collection collection) {
        cCollection = collection;
    }


}