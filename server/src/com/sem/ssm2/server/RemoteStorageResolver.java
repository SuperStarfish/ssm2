package com.sem.ssm2.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Definition on how to properly connect to the database for a remote server. Remote servers are run as standalone
 * Java applications and do not require LibGDX to function.
 */
public class RemoteStorageResolver extends LocalStorageResolver {

    protected final String cRemoteUserTable = "CREATE TABLE IF NOT EXISTS 'User' (Key INTEGER PRIMARY KEY NOT NULL UNIQUE, "
            + "Id TEXT NOT NULL UNIQUE, Username TEXT DEFAULT 'Unknown',"
            + " Interval INTEGER, Stroll INTEGER, GroupId TEXT NULL);";

    /**
     * Query that creates a 'Group' table, if it does not exist.
     */
    protected final String cGroupTable = "CREATE TABLE IF NOT EXISTS 'Group' (Key INTEGER PRIMARY KEY NOT NULL, "
            + "OwnerId TEXT NOT NULL, Name TEXT NOT NULL);";

    /**
     * Query that creates an 'Event_Hosts' table, if it does not exist. This table is used primarily for remote servers
     * to connect two clients with each other.
     */
    protected final String cEventHostsTable = "CREATE TABLE IF NOT EXISTS 'Event_Hosts' (Code SMALLINT PRIMARY KEY NOT NULL, "
            + "Ip TEXT NOT NULL, Port INTEGER NOT NULL, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

    @Override
    protected boolean setLocal() {
        return false;
    }

    @Override
    public Connection createDatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:remote.sqlite");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] createDatabases() {
        return new String[]{cRemoteUserTable, cEventHostsTable, cGroupTable};
    }
}