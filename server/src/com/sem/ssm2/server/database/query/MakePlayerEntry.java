package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Object that will retrieve user data from the server with the given id.
 */
public class MakePlayerEntry extends Query {

    /**
     * The PlayerData constructed from given id. Will be filled and returned.
     */
    protected String cId;

    /**
     * Constructor for the request.
     *
     * @param id The id of the player to be retrieved.
     */
    public MakePlayerEntry(final String id) {
        cId = id;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        String preparedStatement = "INSERT OR IGNORE INTO User (ID) VALUES (?)";
        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedStatement)) {
            statement.setString(1, cId);
            statement.executeUpdate();
            statement.close();
        }
        return null;
    }

}
