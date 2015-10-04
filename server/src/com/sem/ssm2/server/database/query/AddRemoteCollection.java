package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class AddRemoteCollection extends Query {

    protected Collection collection;
    protected String playerId;

    public AddRemoteCollection(Collection collection, String playerId) {
        this.collection = collection;
        this.playerId = playerId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        for (Collectible collectible : collection) {
            new AddRemoteCollectible(collectible, playerId, -1).query(databaseConnection);
        }

        return null;
    }
}
