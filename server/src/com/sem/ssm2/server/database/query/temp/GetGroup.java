//package com.sem.ssm2.server.database.query.temp;
//
//import com.sem.ssm2.server.database.query.Query;
//import com.sem.ssm2.structures.groups.GroupData;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * Retrieves a group from the server based on the id.
// */
//public class GetGroup extends Query {
//    /**
//     * The groups id.
//     */
//    protected final String cGroupId;
//
//    /**
//     * Constructs a group with the given group id.
//     *
//     * @param groupId The given group id.
//     */
//    public GetGroup(final String groupId) {
//        cGroupId = groupId;
//    }
//
//    @Override
//    public GroupData query(final Connection databaseConnection) throws SQLException {
//        String query = "SELECT G.Key AS GroupId, Name, OwnerId, Username, U.Id "
//                + "FROM 'Group' G INNER JOIN User U ON OwnerId = Id WHERE G.Key = ? ";
//
//        GroupData groupData;
//        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
//            statement.setString(1, cGroupId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    groupData = new GroupData(
//                            resultSet.getInt("GroupId"),
//                            resultSet.getBoolean("")
//                            resultSet.getString("Name"),
//                            resultSet.getString("OwnerId"),
//                            resultSet.getString("Username")
//                    );
//                    resultSet.close();
//                } else {
//                    groupData = null;
//                }
//            }
//            statement.close();
//        }
//
//        return groupData;
//    }
//}