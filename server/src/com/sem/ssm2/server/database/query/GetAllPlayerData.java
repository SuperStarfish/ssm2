package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Gets all the group data.
 */
public class GetAllPlayerData extends Query {
    @Override
    public ArrayList<PlayerData> query(final Connection databaseConnection) throws SQLException {
        ArrayList<PlayerData> list = new ArrayList<PlayerData>();
        String query = "SELECT Id,Username FROM User";
        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PlayerData playerData = new PlayerData(resultSet.getString("Id"));
                    playerData.setUsername(resultSet.getString("Username"));
                    list.add(playerData);
                }
                resultSet.close();
            }
            statement.close();
        }
        return list;
    }
}