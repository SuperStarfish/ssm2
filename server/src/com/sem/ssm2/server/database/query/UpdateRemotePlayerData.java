package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateRemotePlayerData extends Query {

    PlayerData playerData;

    public UpdateRemotePlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "insert or replace into players (id, username, last_stroll, walking_time, " +
                "running_time, number_of_strolls) values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            setValues(statement,
                    playerData.getId(),
                    playerData.getUsername(),
                    playerData.getStrollTimestamp(),
                    playerData.getWalkingTime(),
                    playerData.getRunningTime(),
                    playerData.getNumberOfStrolls()
                    );
            statement.execute();
        }
        return null;
    }
}
