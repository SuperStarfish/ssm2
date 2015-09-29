package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Adds a new collectible to the server.
 */
public class RemoveCollectible extends Query {

    /**
     * Id of the group the collectible belongs to.
     */
    protected final String cGroupId;
    /**
     * The collectible to add.
     */
    protected Collectible cCollectible;

    /**
     * Adds a Collectible in the database.
     *
     * @param collectible The collectible to update.
     * @param groupId     Id of the group the collectible belongs to.
     */
    public RemoveCollectible(final Collectible collectible, final String groupId) {
        cCollectible = collectible;
        cGroupId = groupId;
    }

    @Override
    public Serializable query(final Connection connection) throws SQLException {
        int amount = new GetCollectibleAmount(cCollectible, cGroupId).query(connection);
        new SetCollectibleAmount(cCollectible, cGroupId, amount - cCollectible.getAmount()).query(connection);

        return null;
    }
}