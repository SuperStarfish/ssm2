package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Removes the given collectible from the server.
 */
public class DeleteCollectible extends Query {

    /**
     * The group of the collectible.
     */
    protected final String cGroupId;
    /**
     * The collectible to remove.
     */
    protected final Collectible cCollectible;

    /**
     * Removes the given collectible from the server.
     *
     * @param collectible The collectible to remove.
     * @param groupId     The group of the collectible.
     */
    protected DeleteCollectible(final Collectible collectible, final String groupId) {
        cCollectible = collectible;
        cGroupId = groupId;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {

        String preparedQuery = "DELETE FROM Collectible WHERE OwnerId = ? AND Type = ? AND Hue = ? AND "
                + "Date = ? AND GroupId = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            setValues(statement,
                    cCollectible.getOwnerId(),
                    cCollectible.getClass().getSimpleName(),
                    cCollectible.getHue(),
                    cCollectible.getDateAsString(),
                    cGroupId);
            statement.executeUpdate();
        }

        return null;
    }
}