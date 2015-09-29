package com.sem.ssm2.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * This class is used for different devices to create a local or remote server. The child classes
 * determine the settings for the server, but the creation of tables is streamlined.
 */
public abstract class LocalStorageResolver {
    /**
     * Default Java logging utility.
     */
    protected static final Logger LOGGER = Logger.getLogger(LocalStorageResolver.class.getSimpleName());

    /**
     * Query that creates a 'User' table, if it does not exist.
     */
    protected String cUserTable = "CREATE TABLE IF NOT EXISTS 'User' (Key INTEGER PRIMARY KEY NOT NULL UNIQUE, "
            + "Id TEXT NOT NULL UNIQUE, Username TEXT DEFAULT 'Unknown',"
            + " Interval INTEGER, Stroll INTEGER, GroupId TEXT NULL);";

    /**
     * Query that creates a 'Collectible' table, if it does not exist.
     */
    protected String cCollectibleTable = "CREATE TABLE IF NOT EXISTS 'Collectible' (Key INTEGER PRIMARY KEY NOT NULL, "
            + "OwnerId TEXT, Type TEXT NOT NULL, Hue REAL NOT NULL, Amount INTEGER NOT NULL, "
            + "Date DATE NOT NULL, GroupId INTEGER);";

    /**
     * The database connection. This can be used to make queries on. Child class determines how this connection
     * is created.
     */
    protected Connection cConnection;

    /**
     * Boolean to determine if the server is remote or local. Remote servers behave slightly different than
     * local servers in the way they setup the connection.
     */
    protected boolean cIsLocal;

    /**
     * Clears all the databases. !!!!! ONLY USE FOR DEVELOPMENT !!!!!
     */
    protected boolean cResetDBs = false;

    /**
     * Creates the database connection using the child definition. Also creates the databases defined by the child.
     */
    public LocalStorageResolver() {
        cIsLocal = setLocal();
        try {
            cConnection = createDatabaseConnection();
            cConnection.setAutoCommit(true);
            LOGGER.info("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cResetDBs) {
            dropDatabase("User", "Collectible", "Group", "Event_Hosts");
        }

        for (String table : createDatabases()) {
            createDatabase(table);
        }
    }

    /**
     * Child has to define if the server is remote or local. This is needed for some minor setup differences in
     * the two implementations.
     *
     * @return If the database is on a remote or local server.
     */
    protected abstract boolean setLocal();

    /**
     * Child class has to properly create the database connection using the methods appropriate to its platform.
     *
     * @return The connection with the database
     * @throws SQLException If the database connection could not be established.
     */
    protected abstract Connection createDatabaseConnection() throws SQLException;

    /**
     * Drops all the supplied databases.  !!!!! ONLY USE FOR DEVELOPMENT !!!!!
     *
     * @param dbs Databases to drop.
     */
    protected void dropDatabase(final String... dbs) {
        for (String database : dbs) {
            try (PreparedStatement statement = cConnection.prepareStatement("DROP TABLE IF EXISTS " + database)) {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Child class has to define which databases are needed to function properly.
     *
     * @return An array of queries that construct databases.
     */
    protected abstract String[] createDatabases();

    /**
     * Method that creates a database by executing the supplied query.
     *
     * @param query The query to create the database.
     */
    protected void createDatabase(final String query) {
        try {
            try (Statement statement = cConnection.createStatement()) {
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Connection with the database.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        return cConnection;
    }

    /**
     * Returns if the database exists on a remote or local server.
     *
     * @return If the database is on a remote or local server.
     */
    boolean isLocal() {
        return cIsLocal;
    }
}