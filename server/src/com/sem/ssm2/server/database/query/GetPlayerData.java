package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetPlayerData extends Query {

    public String id;

    public GetPlayerData(String id) {
        this.id = id;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String query = "SELECT * FROM player";

        PlayerData playerData = null;

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    System.out.println(resultSet.getString("last_stroll"));
//                    playerData = new PlayerData(
//                            resultSet.getString("id"),
//                            resultSet.getString("username"),
//                            new DateTime(resultSet.getString("last_stroll"))
//                    );
                } else {
                    return (new InsertPlayer(id)).query(databaseConnection);
                }
                resultSet.close();
            }
            statement.close();
        }
        return playerData;

    }
}
