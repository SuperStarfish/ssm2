package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Query's the database of the server.
 */
public abstract class Query implements Serializable {

    /**
     * Query's the given database connection and returns the result that the server should return.
     * This Query object is made by the client, sent to the server and executed on the database.
     * The result of this query is sent back to the client.
     *
     * @param databaseConnection The database connection with which can be queried.
     * @return The result of this query.
     * @throws SQLException Something went wrong with the query.
     */
    public abstract Serializable query(Connection databaseConnection) throws SQLException;

    /**
     * Inserts all the objects in the prepared statement, in order.
     *
     * @param preparedStatement The statement on which to add the objects.
     * @param values            The Objects to add to the statement.
     * @throws SQLException If something went wrong while preparing the statement.
     */
    protected void setValues(final PreparedStatement preparedStatement, final Object... values) throws SQLException {
        for (int i = 1; i <= values.length; i++) {
            preparedStatement.setObject(i, values[i - 1]);
        }
    }
}
