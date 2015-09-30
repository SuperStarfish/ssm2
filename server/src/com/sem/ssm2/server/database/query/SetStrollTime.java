package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetStrollTime extends Query {

    protected long millis;

    public SetStrollTime(long millis) {
        this.millis = millis;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String query = "update player set last_stroll = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setLong(1, millis);
            statement.execute();
        }

        return null;
    }
}
