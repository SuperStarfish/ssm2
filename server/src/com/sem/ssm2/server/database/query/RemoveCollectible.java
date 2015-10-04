package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveCollectible extends Query{

    protected Collectible collectible;

    public RemoveCollectible(Collectible collectible) {
        this.collectible = collectible;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "update collection set amount = amount - 1 where type = ? and hue = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, collectible.getClass().getSimpleName());
            statement.setFloat(2, collectible.getHue());
            statement.execute();
        }
        return null;
    }
}
