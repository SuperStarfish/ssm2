package com.sem.ssm2.server.database.query;

import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.GroupData;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetGroupMembers extends Query {

    protected GroupData groupData;
    public GetGroupMembers(GroupData groupData) {
        this.groupData = groupData;
    }

    @Override
    public Serializable query(Connection databaseConnection) throws SQLException {
        String query = "select * from group_members as M INNER JOIN players as P on M.playerId=P.id where M.groupId = ?;";

        ArrayList<PlayerData> players;

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setInt(1, groupData.getGroupId());
            try (ResultSet resultSet = statement.executeQuery()) {
                players = new ArrayList<>();
                while(resultSet.next()) {
                    players.add(new PlayerData(
                            resultSet.getString("playerId"),
                            resultSet.getString("username"),
                            resultSet.getLong("last_stroll"),
                            resultSet.getLong("walking_time"),
                            resultSet.getLong("running_time"),
                            resultSet.getInt("number_of_strolls")
                    ));
                }
            }
        }


        return players;
    }
}
