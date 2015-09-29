package com.sem.ssm2.structures.groups;

import java.io.Serializable;

/**
 * Contains the data of a group.
 */
public class GroupData implements Serializable {
    /**
     * Defines the id of a group.
     * Used to uniquely identify the group.
     */
    protected int cGroupId;

    /**
     * Defines the name of a group.
     * This name is not necessarily unique.
     */
    protected String cName;

    /**
     * Defines the owner of the group.
     * The owner of the group is based on a player id, which is unique.
     */
    protected String cOwnerId;

    /**
     * Name of the owner.
     */
    protected String cOwnerName;

    /**
     * Constructs a new group data object.
     */
    public GroupData() {
    }

    /**
     * Constructs a new group data object and sets the following parameters.
     *
     * @param groupId   integer representing the id of the group.
     * @param groupName string representing the name of the group.
     * @param ownerId   string representing the group owner's id.
     * @param ownerName string representing the name of the group's owner.
     */
    public GroupData(int groupId, String groupName, String ownerId, String ownerName) {
        cGroupId = groupId;
        cName = groupName;
        cOwnerId = ownerId;
        cOwnerName = ownerName;
    }

    /**
     * Returns the group's id as a string.
     *
     * @return string representing the group's id.
     */
    public String getGroupId() {
        return Integer.toString(cGroupId);
    }

    /**
     * Sets the group's id to the given input.
     *
     * @param groupId new group id.
     */
    public void setGroupId(final int groupId) {
        cGroupId = groupId;
    }

    @Override
    public String toString() {
        return cName;
    }

    /**
     * Sets the name of the group to the given input.
     *
     * @param name new group name
     */
    public void setName(final String name) {
        cName = name;
    }
}