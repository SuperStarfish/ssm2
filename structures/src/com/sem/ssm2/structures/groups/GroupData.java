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

    protected boolean isPublic;

    protected String password;

    /**
     * Constructs a new group data object.
     */
    public GroupData() {
    }

    public GroupData(boolean isPublic, String password, String name) {
        this.isPublic = isPublic;
        this.password = password;
        this.cName = name;
    }

    public GroupData(int groupId, boolean isPublic, String password, String name, String admin) {
        this.cGroupId = groupId;
        this.isPublic = isPublic;
        this.password = password;
        this.cName = name;
        cOwnerId = admin;
    }

    /**
     * Returns the group's id as a string.
     *
     * @return string representing the group's id.
     */
    public int getGroupId() {
        return cGroupId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return cName;
    }

    public String getOwner() {
        return cOwnerId;
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