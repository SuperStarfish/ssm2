package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.Group;
import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeaveGroup extends Query {

    protected GroupData groupData;
    protected PlayerData playerData;

    public LeaveGroup(GroupData groupData, PlayerData playerData) {
        this.groupData = groupData;
        this.playerData = playerData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        if(groupData.getOwner().equals(playerData.getId())) {
            return (new DeleteGroup(groupData)).query(databaseConnection);
        } else {
            String query = "delete from group_members where groupId = ? and playerId = ?";

            try(PreparedStatement statement = databaseConnection.prepareStatement(query)) {
                statement.setInt(1, groupData.getGroupId());
                statement.setString(2, playerData.getId());
                statement.execute();
            }

            return null;
        }
    }
}
