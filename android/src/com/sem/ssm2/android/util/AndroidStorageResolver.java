package com.sem.ssm2.android.util;

import com.sem.ssm2.server.LocalStorageResolver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Storage resolver for android devices.
 */
public class AndroidStorageResolver extends LocalStorageResolver {

    @Override
    protected boolean setLocal() {
        return true;
    }

    @Override
    protected Connection createDatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqldroid.SQLDroidDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new org.sqldroid.SQLDroidDriver()
                .connect("jdbc:sqldroid:/data/data/com.sem.ssm2.android/databases/local.db", new Properties());
    }

    @Override
    protected String[] createDatabases() {
        return new String[]{playerTable, collectionTable, unsyncedCollectionTable};
    }
}