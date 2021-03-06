package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GetLocalCollection extends Query {

    protected boolean random;
    protected int limit = 10;

    public GetLocalCollection(boolean random) {
        this.random = random;
    }

    public GetLocalCollection(boolean random, int limit) {
        this.random = random;
        this.limit = limit;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        Collection collection = new Collection();

        String preparedQuery = "SELECT * FROM collection";
        if(random) preparedQuery += " ORDER BY RANDOM() LIMIT " + limit;

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
