package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class AddRemoteCollection extends Query {

    protected Collection collection;

    public AddRemoteCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        for (Collectible collectible : collection) {
            new AddRemoteCollectible(collectible, -1).query(databaseConnection);
        }

        return null;
    }
}
