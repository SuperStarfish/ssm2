package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that will retrieve user data from the server with the given id.
 */
public class RequestGroupId extends Query {

    /**
     * The PlayerData constructed from given id. Will be filled and returned.
     */
    protected String cId;

    /**
     * Constructor for the request.
     *
     * @param id The id of the player to be retrieved.
     */
    public RequestGroupId(final String id) {
        cId = id;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        String result = null;

        String preparedQuery = "SELECT GroupId FROM User WHERE ID = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            statement.setString(1, cId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString("GroupId");
                }
            }
            statement.close();
        }

        return result;
    }
}