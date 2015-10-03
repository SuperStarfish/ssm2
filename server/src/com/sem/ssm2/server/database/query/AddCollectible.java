//package com.sem.ssm2.server.database.query;
//
//import com.sem.ssm2.server.database.query.Query;
//import com.sem.ssm2.server.database.query.temp.GetCollectibleAmount;
//import com.sem.ssm2.server.database.query.temp.InsertCollectible;
//import com.sem.ssm2.server.database.query.temp.SetCollectibleAmount;
//import com.sem.ssm2.structures.collection.collectibles.Collectible;
//
//import java.io.Serializable;
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * Adds a new collectible to the server.
// */
//public class AddCollectible extends Query {
//
//    /**
//     * Id of the group the collectible belongs to.
//     */
//    protected final String cGroupId;
//    /**
//     * The collectible to add.
//     */
//    protected Collectible cCollectible;
//
//    /**
//     * Adds a Collectible in the database.
//     *
//     * @param collectible The collectible to update.
//     * @param groupId     Id of the group the collectible belongs to.
//     */
//    public AddCollectible(final Collectible collectible, final String groupId) {
//        cCollectible = collectible;
//        cGroupId = groupId;
//    }
//
//    @Override
//    public Serializable query(final Connection connection) throws SQLException {
//        int amount = new GetCollectibleAmount(cCollectible, cGroupId).query(connection);
//        if (amount == 0) {
//            new InsertCollectible(cCollectible, cGroupId).query(connection);
//        } else {
//            amount += cCollectible.getAmount();
//            new SetCollectibleAmount(cCollectible, cGroupId, amount).query(connection);
//        }
//
//        return null;
//    }
//}