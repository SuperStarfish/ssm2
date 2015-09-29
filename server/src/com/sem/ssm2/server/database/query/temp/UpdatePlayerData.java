package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates the player data on the server.
 */
public class UpdatePlayerData extends Query {

    /**
     * Player data containing he changes to be made to the server. The players id should be the same.
     */
    protected final PlayerData cPlayerData;
    /**
     * Connection to the database to run the query on.
     */
    protected Connection cDatabaseConnection;

    /**
     * Constructs a new query based on the player data to be changed.
     *
     * @param playerData The changes.
     */
    public UpdatePlayerData(final PlayerData playerData) {
        cPlayerData = playerData;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        cDatabaseConnection = databaseConnection;

        new MakePlayerEntry(cPlayerData.getId()).query(databaseConnection);

        if (cPlayerData.toString() != null) {
            updateData("Username", cPlayerData.toString());
        }

        if (cPlayerData.getIntervalTimestamp() != 0) {
            updateData("Interval", cPlayerData.getIntervalTimestamp());
        }

        if (cPlayerData.getStrollTimestamp() != 0) {
            updateData("Stroll", cPlayerData.getStrollTimestamp());
        }

        if (cPlayerData.getGroupId() != null) {
            updateData("GroupId", cPlayerData.getGroupId());
        }

        return null;
    }

    /**
     * Updates the players data.
     *
     * @param column   The column to update.
     * @param newValue The new value.
     * @throws SQLException Throws if something went wrong while updating.
     */
    protected void updateData(final String column, final Object newValue) throws SQLException {
        try (PreparedStatement statement = cDatabaseConnection.prepareStatement("UPDATE USER SET " + column + " = ? "
                + "WHERE Id = ?")) {
            statement.setObject(1, newValue);
            statement.setObject(2, cPlayerData.getId());
            statement.executeUpdate();
            statement.close();
        }
    }

}