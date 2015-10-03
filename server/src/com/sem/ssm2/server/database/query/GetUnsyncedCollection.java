package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GetUnsyncedCollection extends Query {

    PlayerData playerData;

    public GetUnsyncedCollection(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        Collection collection = new Collection();

        String preparedQuery = "SELECT * FROM unsynced_collection";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
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
