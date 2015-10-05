package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveRemoteCollectible extends Query{

    protected Collectible collectible;
    protected int groupId;

    public RemoveRemoteCollectible(Collectible collectible, int groupId) {
        this.collectible = collectible;
        this.groupId = groupId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "update collections set amount = amount - 1 where ownerId = ? and groupId = ? and type = ? and hue = ?";

        String getQuery = "select amount from collections where ownerId = ? and groupId = ? and type = ? and hue = ?";

        String deleteQuery = "delete from collections where ownerId = ? and groupId = ? and type = ? and hue = ? and ";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, collectible.getOwnerId());
            statement.setInt(2, groupId);
            statement.setString(3, collectible.getClass().getSimpleName());
            statement.setFloat(4, collectible.getHue());
            statement.execute();
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(getQuery)) {
                preparedStatement.setString(1, collectible.getOwnerId());
                preparedStatement.setInt(2, groupId);
                preparedStatement.setString(3, collectible.getClass().getSimpleName());
                preparedStatement.setFloat(4, collectible.getHue());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        int amount = resultSet.getInt("amount");
                        System.out.println("AMount: " + amount);
                        if(amount <= 0) {
                            try (PreparedStatement statement1 = databaseConnection.prepareStatement(deleteQuery)) {
                                statement1.setString(1, collectible.getOwnerId());
                                statement1.setInt(2, groupId);
                                statement1.setString(3, collectible.getClass().getSimpleName());
                                statement1.setFloat(4, collectible.getHue());
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
