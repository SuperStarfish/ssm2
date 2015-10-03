package com.sem.ssm2.server.database.query;


import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetPlayerGroups extends Query {
    protected String id;

    public GetPlayerGroups(String id) {
        this.id = id;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "select * from group_members as M JOIN groups as G on M.groupId=G.id where M.playerId = ?;";

        ArrayList<GroupData> groups;

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                groups = new ArrayList<>();
                while(resultSet.next()) {
                    groups.add(new GroupData(
                            resultSet.getInt("groupId"),
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
