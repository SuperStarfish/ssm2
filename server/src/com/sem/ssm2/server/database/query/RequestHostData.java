package com.sem.ssm2.server.database.query;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.HostData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Request the host ip from the server belonging to the given code.
 */
public class RequestHostData extends Query {

    /**
     * Code belonging to the host ip.
     */
    protected String cCode;

    /**
     * Makes a new Query to retrieve the host ip belonging to the given code.
     *
     * @param code The given code.
     */
    public RequestHostData(final String code) {
        cCode = code;
    }

    @Override
    public Serializable query(final Connection databaseConnection) throws SQLException {
        String preparedQuery = "SELECT Ip, Port FROM Event_Hosts WHERE Code = ?";

        HostData hostData = null;

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            statement.setString(1, cCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String ip = resultSet.getString("Ip");
                    int port = resultSet.getInt("Port");
                    hostData = new HostData(ip, port);
                }
            }
        }

        return hostData;
    }
}