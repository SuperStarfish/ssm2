package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates an given collectibles amount in the server.
 */
public class SetCollectibleAmount extends Query {

    /**
     * The collectible.
     */
    protected Collectible cCollectible;
    /**
     * The group id.
     */
    protected String cGroupId;
    /**
     * The new amount of the collectible.
     */
    protected Integer cAmount;

    /**
     * Updates the amount of the collectible to the database.
     *
     * @param collectible The collectible to update.
     * @param groupId     The group it belongs to.
     * @param amount      The amount already set in teh server.
     **/
    protected SetCollectibleAmount(final Collectible collectible, final String groupId, final Integer amount) {
        cCollectible = collectible;
        cGroupId = groupId;
        cAmount = amount;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        if (cAmount <= 0) {
            new DeleteCollectible(cCollectible, cGroupId).query(databaseConnection);
        } else {
            String preparedQuery = "UPDATE Collectible SET Amount = ? WHERE OwnerId = ? AND Type = ? AND Hue = ? AND "
                    + "Date = ? AND GroupId = ?";

            try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
                setValues(statement,
                        cAmount,
                        cCollectible.getOwnerId(),
                        cCollectible.getClass().getSimpleName(),
                        cCollectible.getHue(),
                        cCollectible.getDateAsString(),
                        cGroupId);
                statement.executeUpdate();
            }
        }
        return null;
    }
}