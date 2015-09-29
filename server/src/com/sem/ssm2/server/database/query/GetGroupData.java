package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.groups.GroupData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Gets all the group data.
 */
public class GetGroupData extends Query {

    @Override
    public ArrayList<GroupData> query(final Connection databaseConnection) throws SQLException {
        ArrayList<GroupData> list = new ArrayList<GroupData>();

        String query = "SELECT G.Key AS GroupId, G.Name AS Name, G.OwnerId AS OwnerId, U.Username AS Username, U.Id "
                + "FROM 'Group' G INNER JOIN User U ON G.OwnerId = U.Id";

        try (Statement statement = databaseConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                list.add(new GroupData(
                        resultSet.getInt("GroupId"),
                        resultSet.getString("Name"),
                        resultSet.getString("OwnerId"),
                        resultSet.getString("Username")
                ));
            }
        }
        return list;
    }
}