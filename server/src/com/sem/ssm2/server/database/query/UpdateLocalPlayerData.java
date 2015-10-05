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
        String query = "update player set username = ? ";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {

        }
        return null;
    }
}
