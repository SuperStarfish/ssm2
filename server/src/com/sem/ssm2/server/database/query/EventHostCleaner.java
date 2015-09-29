package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cleans the database once every while.
 */
public class EventHostCleaner extends Query {

    /**
     * Query that deletes every row in the Event_Hosts table where time is longer than 30 minutes ago.
     */
    protected String cQuery = "DELETE FROM 'Event_Hosts' WHERE Timestamp <= datetime('now','-30 minutes')";

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        try (Statement statement = databaseConnection.createStatement()) {
            statement.executeUpdate(cQuery);
        }
        return null;
    }
}