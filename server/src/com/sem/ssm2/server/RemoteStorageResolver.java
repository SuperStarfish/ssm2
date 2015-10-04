package com.sem.ssm2.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Definition on how to properly connect to the database for a remote server. Remote servers are run as standalone
 * Java applications and do not require LibGDX to function.
 */
public class RemoteStorageResolver extends LocalStorageResolver {

    protected final String remotePlayersTable = "CREATE TABLE IF NOT EXISTS 'players' (id text primary key not null," +
            "username varchar default 'Anonymous', last_stroll bigint, walking_time bigint, running_time bigint, " +
            "number_of_strolls integer);";

    protected final String groupTable = "create table if not exists groups (id integer primary key autoincrement, " +
            "is_public boolean not null, password text, name text not null, admin text not null);";

    protected final String groupMembersTable = "create table if not exists group_members (groupId integer not null, " +
            "playerId text not null);";

    protected final String collectionsTable = "create table if not exists collections (ownerId text not null, " +
            "groupId text, type text not null, hue float not null, amount int not null default 0, " +
            "last_entry text not null, unique(ownerId, groupId, type, hue));";

    /**
     * Query that creates an 'Event_Hosts' table, if it does not exist. This table is used primarily for remote servers
     * to connect two clients with each other.
     */
    protected final String eventHostsTable = "CREATE TABLE IF NOT EXISTS 'Event_Hosts' (Code SMALLINT PRIMARY KEY NOT NULL, "
            + "Ip TEXT NOT NULL, Port INTEGER NOT NULL, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";

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
        return new String[]{remotePlayersTable, groupTable, groupMembersTable, collectionsTable, eventHostsTable};
    }
}