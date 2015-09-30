package com.sem.ssm2.structures;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Contains all the data of the user.
 */
public class PlayerData implements Serializable {

    /**
     * The id belonging to this user.
     */
    protected String cId;

    /**
     * The username for this user.
     */
    protected String cUsername;

    /**
     * The last time a stroll was started.
     */
    protected long cStrollTimestamp;

    /**
     * The last time an interval was started.
     */
    protected long cIntervalTimestamp;

    /**
     * The id of the group the member is from.
     */
    protected String cGroupId;

    /**
     * A new UserData object containing the id for the current user.
     *
     * @param id The ID that belongs to the user.
     */
    public PlayerData(final String id) {
        cId = id;
    }

    public PlayerData(String id, String username, long strollTimeStamp) {
        cId = id;
        cUsername = username;
        cStrollTimestamp = strollTimeStamp;
    }

    /**
     * Gets the current user id.
     *
     * @return The ID that belongs to the user.
     */
    public String getId() {
        return cId;
    }

    /**
     * Sets the userID to the new id.
     *
     * @param id The new id.
     */
    public void setId(final String id) {
        cId = id;
    }

    /**
     * Gets the current username.
     *
     * @return The username that belongs to the user.
     */
    @Override
    public String toString() {
        return cUsername;
    }

    /**
     * Sets the username to the new username.
     *
     * @param username The new username.
     */
    public void setUsername(final String username) {
        cUsername = username;
    }

    /**
     * Gets the current users last stroll time.
     *
     * @return The timestamp for the stroll.
     */
    public long getStrollTimestamp() {
        return cStrollTimestamp;
    }

    /**
     * Sets the stroll timestamp to the new timestamp.
     *
     * @param strollTimestamp The new timestamp;
     */
    public void setStrollTimestamp(final long strollTimestamp) {
        cStrollTimestamp = strollTimestamp;
    }

    /**
     * Gets the current users last interval time.
     *
     * @return The timestamp for the interval.
     */
    public long getIntervalTimestamp() {
        return cIntervalTimestamp;
    }

    /**
     * Sets the interval timestamp to the new timestamp.
     *
     * @param intervalTimestamp The new timestamp.
     */
    public void setIntervalTimestamp(final long intervalTimestamp) {
        cIntervalTimestamp = intervalTimestamp;
    }

    /**
     * Gets the group id.
     *
     * @return The group id
     */
    public String getGroupId() {
        return cGroupId;
    }

    public String getUsername() {
        return cUsername;
    }

    /**
     * Time since last stroll
     * @return
     */
    public int getElapsedTime() {
        return (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cStrollTimestamp);
    }

    /**
     * Sets the group id.
     *
     * @param groupId The group id.
     */
    public void setGroupId(final String groupId) {
        cGroupId = groupId;
    }

}