package com.sem.ssm2.desktop.util;

import com.sem.ssm2.server.LocalStorageResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquariumStorageResolver extends LocalStorageResolver {
    @Override
    protected boolean setLocal() {
        return true;
    }

    @Override
    protected Connection createDatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:sqlite:local.sqlite");

    }

    @Override
    protected String[] createDatabases() {
        return new String[0];
    }
}
