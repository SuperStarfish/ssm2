package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddRemoteCollectible extends Query {

    protected Collectible collectible;
    protected String playerId;
    protected int groupId;


    public AddRemoteCollectible(Collectible collectible, String playerId, int groupId) {
        this.collectible = collectible;
        this.playerId = playerId;
        this.groupId = groupId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String query = "insert or replace into collections (ownerId, groupId, type, hue, amount, last_entry) " +
                "values (?, ?, ?, ?, ? + ifnull((select amount from collections where ownerId = ? and groupId = ? and " +
                "type = ? and hue = ?), 0), datetime('now'))";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            setValues(statement,
                    playerId,
                    groupId,
                    collectible.getClass().getSimpleName(),
                    collectible.getHue(),
                    collectible.getAmount(),
                    playerId,
                    groupId,
                    collectible.getClass().getSimpleName(),
                    collectible.getHue()
                    );
            statement.execute();
        }

        query = "update collections set amount = amount - 1 where ownerId = ? and type = ? and hue = ?";

        String getQuery = "select amount from collections where ownerId = ? and type = ? and hue = ?";

        String deleteQuery = "delete from collections where ownerId = ? and type = ? and hue = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, playerId);
            statement.setString(2, collectible.getClass().getSimpleName());
            statement.setFloat(3, collectible.getHue());
            statement.execute();
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(getQuery)) {
                preparedStatement.setString(1, playerId);
                preparedStatement.setString(2, collectible.getClass().getSimpleName());
                preparedStatement.setFloat(3, collectible.getHue());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        int amount = resultSet.getInt("amount");
                        System.out.println("AMount: " + amount);
                        if(amount <= 0) {
                            try (PreparedStatement statement1 = databaseConnection.prepareStatement(deleteQuery)) {
                                statement1.setString(1, playerId);
                                statement1.setString(2, collectible.getClass().getSimpleName());
                                statement1.setFloat(3, collectible.getHue());
                                statement1.execute();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
