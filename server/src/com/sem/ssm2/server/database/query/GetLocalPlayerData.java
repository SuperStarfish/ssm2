package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetLocalPlayerData extends Query {

    public String id;

    public GetLocalPlayerData(String id) {
        this.id = id;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String insertEntry = "insert or ignore into player (id) values (?);";

        try (PreparedStatement statement = databaseConnection.prepareStatement(insertEntry)) {
            statement.setString(1, id);
            statement.execute();
        }

        String getEntry = "select * from player where id = ?;";

        PlayerData playerData = null;

        try (PreparedStatement statement = databaseConnection.prepareStatement(getEntry)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    playerData = new PlayerData(
                            resultSet.getString("id"),
                            resultSet.getString("username"),
                            resultSet.getLong("last_stroll"),
                            resultSet.getLong("walking_time"),
                            resultSet.getLong("running_time"),
                            resultSet.getInt("number_of_strolls")
                    );
                }
            }
        }
        return playerData;
    }
}
