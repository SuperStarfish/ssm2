package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateLocalPlayerData extends Query {

    protected PlayerData playerData;

    public UpdateLocalPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "update player set username = ?, walking_time = ?, running_time = ?, number_of_strolls = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, playerData.getUsername());
            statement.setLong(2, playerData.getWalkingTime());
            statement.setLong(3, playerData.getRunningTime());
            statement.setInt(4, playerData.getNumberOfStrolls());
            statement.execute();
        }
        return null;
    }
}
