package com.sem.ssm2.structures;

import com.sem.ssm2.structures.groups.GroupData;

/**
 * Represents a selection that can be made.
 */
public class Selection {

    /**
     * The name of the selection.
     */
    protected String cName;
    /**
     * The value of the selection.
     */
    protected String cValue;

    /**
     * An object representing a selection.
     *
     * @param groupData The group data the selection is representing.
     */
    public Selection(GroupData groupData) {
        cName = groupData.toString();
        cValue = groupData.getGroupId();
    }

    /**
     * An object representing a selection.
     *
     * @param playerData The player data the selection is representing.
     */
    public Selection(PlayerData playerData) {
        cName = playerData.toString();
        cValue = playerData.getId();
    }

    /**
     * Getter for the value of the slection.
     *
     * @return The value.
     */
    public String getValue() {
        return cValue;
    }

    /**
     * To string method for the selection.
     *
     * @return The name.
     */
    public String toString() {
        return cName;
    }
}