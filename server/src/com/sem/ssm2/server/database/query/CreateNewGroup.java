package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.*;


public class CreateNewGroup extends Query {

    protected GroupData groupData;
    protected PlayerData playerData;

    public CreateNewGroup(GroupData groupData, PlayerData playerData) {
        this.groupData = groupData;
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {

        String createGroup = "insert into groups (is_public, password, name, admin) values (?, ?, ?, ?)";
        String insertAdmin = "insert into group_members (groupId, playerId) values (?, ?)";

        try (PreparedStatement statement = databaseConnection.prepareStatement(createGroup, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1, groupData.isPublic());
            statement.setString(2, groupData.getPassword());
            statement.setString(3, groupData.getName());
            statement.setString(4, playerData.getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertAdmin)) {
                    preparedStatement.setInt(1, resultSet.getInt("last_insert_rowid()"));
                    preparedStatement.setString(2, playerData.getId());
                    preparedStatement.execute();
                }
            }
        }
        return null;
    }
}
