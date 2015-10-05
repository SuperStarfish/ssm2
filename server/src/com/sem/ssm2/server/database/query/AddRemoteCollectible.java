package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddRemoteCollectible extends Query {

    protected Collectible collectible;
    protected int groupId;


    public AddRemoteCollectible(Collectible collectible, int groupId) {
        this.collectible = collectible;
        this.groupId = groupId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String query = "insert or replace into collections (ownerId, groupId, type, hue, amount, last_entry) " +
                "values (?, ?, ?, ?, ? + ifnull((select amount from collections where ownerId = ? and groupId = ? and " +
                "type = ? and hue = ?), 0), datetime('now'))";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            setValues(statement,
                    collectible.getOwnerId(),
                    groupId,
                    collectible.getClass().getSimpleName(),
                    collectible.getHue(),
                    collectible.getAmount(),
                    collectible.getOwnerId(),
                    groupId,
                    collectible.getClass().getSimpleName(),
                    collectible.getHue()
                    );
            statement.execute();
        }
        return null;
    }
}
