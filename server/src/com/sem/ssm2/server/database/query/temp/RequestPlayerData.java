package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that will retrieve user data from the server with the given id.
 */
public class RequestPlayerData extends Query {

    /**
     * The PlayerData constructed from given id. Will be filled and returned.
     */
    protected PlayerData cPlayerData;

    /**
     * Constructor for the request.
     *
     * @param id The id of the player to be retrieved.
     */
    public RequestPlayerData(final String id) {
        cPlayerData = new PlayerData(id);
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        new MakePlayerEntry(cPlayerData.getId()).query(databaseConnection);

        String preparedQuery = "SELECT * FROM User WHERE ID = ? LIMIT 1";
        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            statement.setString(1, cPlayerData.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cPlayerData.setUsername(resultSet.getString("Username"));
                    cPlayerData.setIntervalTimestamp(resultSet.getInt("Interval"));
                    cPlayerData.setStrollTimestamp(resultSet.getInt("Stroll"));
                }
            }
            statement.close();
        }
        return cPlayerData;
    }
}