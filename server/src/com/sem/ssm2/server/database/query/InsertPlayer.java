package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertPlayer extends Query {
    public String id;

    public InsertPlayer(String id) {
        this.id = id;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "insert into player (id) values (?)";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }

        return (new GetPlayerData(id)).query(databaseConnection);
    }
}
