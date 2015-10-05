package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class GetRemoteCollection extends Query {

    protected int groupId;

    public GetRemoteCollection(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String preparedQuery = "select * from collections where groupId = ?";

        Collection collection = new Collection();

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            statement.setInt(1, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                CollectibleFactory factory = new CollectibleFactory();
                while (resultSet.next()) {
                    collection.add(factory.generateCollectible(
                            resultSet.getString("type"),
                            resultSet.getFloat("hue"),
                            resultSet.getInt("amount"),
                            new Date(resultSet.getLong("last_entry"))
                    ));
                }
            }
        }

        return collection;
    }
}
