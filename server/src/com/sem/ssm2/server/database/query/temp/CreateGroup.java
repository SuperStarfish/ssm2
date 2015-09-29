package com.sem.ssm2.server.database.query.temp;

import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.Group;
import com.sem.ssm2.structures.groups.GroupData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Query used to create a new group.
 */
public class CreateGroup extends Query {
    /**
     * The name for the group.
     */
    protected final String cGroupName;
    /**
     * The ownerID for the group.
     */
    protected final String cOwnerId;

    /**
     * Creates a new CreateGroup Query using the supplied group name and ownerID.
     *
     * @param groupName The group name.
     * @param ownerId   The owner ID.
     */
    public CreateGroup(final String groupName, final String ownerId) {
        cGroupName = groupName;
        cOwnerId = ownerId;
    }

    @Override
    public Group query(final Connection databaseConnection) throws SQLException {
        String preparedStatement = "INSERT INTO 'Group' (OwnerId, Name) VALUES (?, ?)";

        Group group;

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedStatement)) {
            statement.setString(1, cOwnerId);
            statement.setString(2, cGroupName);
            statement.executeUpdate();
        }

        String preparedQuery = "SELECT G.Key AS GroupId, G.Name AS Name, G.OwnerId As OwnerId, U.Username AS Username, "
                + "U.Id FROM 'Group' G INNER JOIN User U ON G.OwnerId = U.Id WHERE G.Name = ? AND "
                + "G.OwnerId = ? LIMIT 1";

        try (PreparedStatement statement = databaseConnection.prepareStatement(preparedQuery)) {
            statement.setString(1, cGroupName);
            statement.setString(2, cOwnerId);

            GroupData groupData;

            int groupId;

            try (ResultSet resultSet = statement.executeQuery()) {
                groupId = resultSet.getInt("GroupId");

                groupData = new GroupData(
                        groupId,
                        cGroupName,
                        cOwnerId,
                        resultSet.getString("Username")
                );
            }

            group = new Group(Integer.toString(groupId), groupData);

            PlayerData playerData = new PlayerData(cOwnerId);
            playerData.setGroupId(Integer.toString(groupId));
            new UpdatePlayerData(playerData).query(databaseConnection);
        }

        return group;
    }
}