package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Adds a new collectible to the server.
 */
public class AddLocalCollectible extends Query {
    /**
     * The collectible to add.
     */
    protected Collectible collectible;

    /**
     * Adds a Collectible in the database.
     *
     * @param collectible The collectible to update.
     */
    public AddLocalCollectible(final Collectible collectible) {
        this.collectible = collectible;
    }

    @Override
    public Serializable query(final Connection connection) throws SQLException {
        System.out.println(collectible.getHue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "insert or replace into collection (type, hue, amount, last_entry) " +
                        "values (?, ?, ? + ifnull((select amount from collection where type = ? and hue = ?), 0), datetime('now'))";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, collectible.getClass().getSimpleName());
                    statement.setFloat(2, collectible.getHue());
                    statement.setInt(3, collectible.getAmount());
                    statement.setString(4, collectible.getClass().getSimpleName());
                    statement.setFloat(5, collectible.getHue());
                    statement.execute();
                }catch (SQLException e) {
                    e.printStackTrace();
                }

                String query2 = "insert or replace into unsynced_collection (type, hue, amount, last_entry) " +
                        "values (?, ?, ? + ifnull((select amount from collection where type = ? and hue = ?), 0), datetime('now'))";

                try (PreparedStatement statement = connection.prepareStatement(query2)) {
                    statement.setString(1, collectible.getClass().getSimpleName());
                    statement.setFloat(2, collectible.getHue());
                    statement.setInt(3, collectible.getAmount());
                    statement.setString(4, collectible.getClass().getSimpleName());
                    statement.setFloat(5, collectible.getHue());
                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                            }
        }).start();


        return null;
    }
}