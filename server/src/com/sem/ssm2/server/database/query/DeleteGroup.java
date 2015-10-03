package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteGroup extends Query {

    protected GroupData groupData;

    public DeleteGroup(GroupData groupData) {
        this.groupData = groupData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "delete from group_members where groupId = ?";

        try(PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setInt(1, groupData.getGroupId());
            statement.execute();
        }

        query = "delete from groups where id = ?";

        try(PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setInt(1, groupData.getGroupId());
            statement.execute();
        }

        return null;
    }
}
