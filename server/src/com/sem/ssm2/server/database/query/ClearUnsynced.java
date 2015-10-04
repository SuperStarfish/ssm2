package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearUnsynced extends Query {
    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        try (PreparedStatement statement = databaseConnection.prepareStatement("Delete from unsynced_collection")) {
            statement.execute();
        }

        return null;
    }
}
