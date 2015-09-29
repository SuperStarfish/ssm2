package com.sem.ssm2.server.database.query;

import com.sem.ssm2.server.database.query.Query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class Ping extends Query {
    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        return "Pong";
    }
}
