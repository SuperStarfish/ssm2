package com.sem.ssm2.server.database.query;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Inserts a new event host into the game.
 */
public class RequestHostCode extends Query {
    /**
     * The highest number for host keys.
     */
    private static final int MAXCODE = 10000;
    /**
     * The ip of the host.
     */
    protected String cIp;
    /**
     * The port to connect to.
     */
    protected int cPort;

    /**
     * Creates a new host.
     *
     * @param ip The ip of the host.
     * @param port The port to use.
     */
    public RequestHostCode(final String ip, final int port) {
        cIp = ip;
        cPort = port;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        boolean exists;
        Random rnd = new Random();
        Integer code;

        String preparedQuery = "SELECT * FROM Event_Hosts WHERE Code = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            do {
                code = rnd.nextInt(MAXCODE);
                statement.setInt(1, code);

                try (ResultSet resultSet = statement.executeQuery()) {
                    exists = resultSet.next();
                }
            }
            while (exists);
        }

        preparedQuery = "INSERT INTO Event_Hosts (Code, Ip, Port) VALUES (?, ?, ?)";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            setValues(statement, code, cIp, cPort);
            statement.executeUpdate();
        }

        return code;
    }
}