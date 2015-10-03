package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GetPublicGroups extends Query {

    protected PlayerData playerData;

    public GetPublicGroups(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "select * from groups where id not in (select groupId from group_members where playerId = ?) " +
                "and is_public = 1;";

        ArrayList<GroupData> groups;

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setString(1, playerData.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                groups = new ArrayList<>();
                while(resultSet.next()) {
                    groups.add(new GroupData(
                            resultSet.getInt("id"),
                            true,
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
