package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Adds a new collectible to the server.
 */
public class InsertCollectible extends Query {

    /**
     * Id of the group the collectible belongs to.
     */
    protected final String cGroupId;
    /**
     * The collectible to add.
     */
    protected Collectible cCollectible;

    /**
     * Inserts a new Collectible in the database.
     *
     * @param collectible The collectible to insert.
     * @param groupId     Id of the group the collectible belongs to.
     */
    protected InsertCollectible(final Collectible collectible, final String groupId) {
        cCollectible = collectible;
        cGroupId = groupId;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        String preparedQuery = "INSERT INTO Collectible (OwnerId, Type, Hue, Amount, Date, GroupId) VALUES "
                + "(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            setValues(statement,
                    cCollectible.getOwnerId(),
                    cCollectible.getClass().getSimpleName(),
                    cCollectible.getHue(),
                    cCollectible.getAmount(),
                    cCollectible.getDateAsString(),
                    cGroupId);
            statement.executeUpdate();
        }

        return null;
    }
}