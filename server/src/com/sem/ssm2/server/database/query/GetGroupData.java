package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetGroupData extends Query {

    protected int groupId;

    public GetGroupData(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        GroupData groupData = null;

        String query = "select * from groups where id = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    groupData = new GroupData(
                            groupId,
                            resultSet.getBoolean("is_public"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("admin")
                    );
                }
            }
        }

        return groupData;
    }
}
