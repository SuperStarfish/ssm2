package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetAllGroups extends Query {

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        ArrayList<GroupData> groups;

        String preparedQuery = "SELECT * FROM groups";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                groups = new ArrayList<>();
                while(resultSet.next()) {
                    groups.add(new GroupData(
                            resultSet.getInt("id"),
                            resultSet.getBoolean("is_public"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("admin")
                    ));
                }
            }
        }

        return groups;
    }
}
