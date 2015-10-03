package com.sem.ssm2.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.sem.ssm2.client.connection.Connection;
import com.sem.ssm2.client.connection.UnConnected;
import com.sem.ssm2.server.LocalStorageResolver;
import com.sem.ssm2.server.Server;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.*;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.Subject;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.groups.GroupData;

import java.util.ArrayList;

public class Client {

    protected Server internalServer;
    protected LocalStorageResolver localStorageResolver;

    protected UserIDResolver userIDResolver;
    protected Connection remoteConnection, localConnection;
    protected Subject remoteStateChange;
    protected ArrayList<Runnable> postRunnables;

    protected String remoteIP, defaultRemoteIP = "82.169.19.191";
    protected int remotePort, defaultRemotePort = 56789;
    protected PlayerData playerData;

    protected boolean retryRemoteConnection = false;

    public Client(LocalStorageResolver storageResolver) {
        this(new DummyUserIdResolver(), storageResolver);
    }

    public Client(UserIDResolver userIDResolver, LocalStorageResolver localStorageResolver) {
        this.userIDResolver = userIDResolver;
        this.localStorageResolver = localStorageResolver;
        remoteConnection = new UnConnected(this);
        localConnection = new UnConnected(this);
        remoteStateChange = new Subject();
        postRunnables = new ArrayList<>();
        remoteIP = getIPSettings();
        remotePort = getPortSettings();

        internalServer = new Server(localStorageResolver);
        internalServer.start();
        localConnection.connect(null, internalServer.getSocketPort());

        localConnection.send(new GetLocalPlayerData(userIDResolver.getID()), new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                playerData = (PlayerData) response.getData();
            }
        });

    }


    /*-------
    |*********
    |*********
    |*********
    |*********
    |*********
    |*********
    |*********
    \*/
    public void getLocalCollection(ResponseHandler responseHandler) {
        localConnection.send(new GetLocalCollection(false), responseHandler);
    }

    public void getLocalCollection(boolean random, int limit, ResponseHandler responseHandler) {
        localConnection.send(new GetLocalCollection(random, limit), responseHandler);
    }

    public void getPlayerData(ResponseHandler responseHandler) {
        localConnection.send(new GetLocalPlayerData(userIDResolver.getID()), responseHandler);
    }

    public void updateStrollTime(long millis, ResponseHandler responseHandler) {
        localConnection.send(new SetStrollTime(millis), responseHandler);
    }

    public void updateRemotePlayerData(PlayerData playerData, ResponseHandler responseHandler) {
        remoteConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
    }

    public void createGroup(GroupData groupData, ResponseHandler responseHandler) {
        remoteConnection.send(new CreateNewGroup(groupData, playerData), responseHandler);
    }

    public void getGroups(ResponseHandler responseHandler) {
        remoteConnection.send(new GetPlayerGroups(playerData.getId()), responseHandler);
    }

    public void leaveGroup(GroupData groupData, ResponseHandler responseHandler) {
        remoteConnection.send(new LeaveGroup(groupData, playerData), responseHandler);
    }

    public void getPublicGroups(ResponseHandler responseHandler) {
        remoteConnection.send(new GetPublicGroups(playerData), responseHandler);
    }

    public void joinGroup(GroupData groupData, ResponseHandler responseHandler) {
        remoteConnection.send(new JoinGroup(groupData, playerData), responseHandler);
    }

    public void getGroupData(int groupId, ResponseHandler responseHandler) {
        remoteConnection.send(new GetGroupData(groupId), responseHandler);
    }

    public void synchronizeLocalCollection(ResponseHandler responseHandler) {
        final Collection collection = new Collection();
        localConnection.send(new GetUnsyncedCollection(playerData), new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                collection.addAll((Collection) response.getData());
            }
        });
        if(collection.size() > 0) {
            remoteConnection.send(new AddCollection(collection), responseHandler);
        }
    }

    /*-------
    |*********
    |*********
    |*********
    |*********
    |*********
    |*********
    |*********
    \*/

    public void setRemoteConnection(Connection connection) {
        remoteConnection = connection;
        remoteStateChange.update(connection.isConnected());
    }

    public boolean isRemoteConnected() {
        return remoteConnection.isConnected();
    }

    public void connectToRemoteServer() {
        remoteConnection.connect(remoteIP, remotePort);
    }

    public void setLocalConnection(Connection connection) {
        localConnection = connection;
    }

    public void addPostRunnable(Runnable runnable) {
        postRunnables.add(runnable);
    }

    public ArrayList<Runnable> getPostRunnables() {
        return postRunnables;
    }

    public void resetPostRunnables() {
        postRunnables.clear();
    }

    public Subject getRemoteStateChange() {
        return remoteStateChange;
    }

    public String getIPSettings() {
        String result = defaultRemoteIP;
        Preferences preferences = Gdx.app.getPreferences("IP_CONFIG");
        if (preferences != null && preferences.contains("ssm-ip")) {
            result = preferences.getString("ssm-ip");
        }

        return result;
    }

    public int getPortSettings() {
        int result = defaultRemotePort;
        Preferences preferences = Gdx.app.getPreferences("IP_CONFIG");

        if(preferences != null && preferences.contains("ssm-port")) {
            result = preferences.getInteger("ssm-port");
        }

        return result;
    }

    public boolean setRemoteIP(String ip) {
        if(isValidIP(ip)) {
            remoteIP = ip;
            Preferences preferences = Gdx.app.getPreferences("IP_CONFIG");
            preferences.putString("ssm-ip", ip);
            preferences.flush();
            return true;
        } else {
            return false;
        }
    }

    public boolean setRemotePort(int port) {
        if(isValidPort(port)) {
            remotePort = port;
            Preferences preferences = Gdx.app.getPreferences("IP_CONFIG");
            preferences.putInteger("ssm-port", port);
            preferences.flush();
            return true;
        } else {
            return false;
        }
    }

    public boolean retryRemoteConnection() {
        return retryRemoteConnection;
    }

    /**
     * http://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
     *
     * @param ip input string
     * @return boolean if the input string is a valid IP.
     */
    public static boolean isValidIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");

        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidPort(int port) {
        return port > 0 && port <= 65535;
    }

    public void pingRemote(ResponseHandler handler) {
        remoteConnection.send(new Ping(), handler);
    }
}

//
//    /**
//     * Gets the userdata from the server. Uses UserIDResolver to get the data. Behaviour depends on the state.
//     *
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void getPlayerData(final ResponseHandler responseHandler) {
//        cLocalConnection.send(new RequestPlayerData(cUserIDResolver.getID()), responseHandler);
//    }
//
//    // --------------- Only queries follow below.--------------
//
//    /**
//     * Gets the userdata from the server. Uses UserIDResolver to get the data. Behaviour depends on the state.
//     *
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void getGroupId(final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new RequestGroupId(cUserIDResolver.getID()), responseHandler);
//    }
//
//    /**
//     * Updates the username on both the local server.
//     *
//     * @param username        The username.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void updateLocalUsername(final String username, final ResponseHandler responseHandler) {
//        PlayerData playerData = new PlayerData(getUserID());
//        playerData.setUsername(username);
//
//        cLocalConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
//    }
//
//    /**
//     * Returns the user ID from the supplied UserIDResolver.
//     *
//     * @return The ID belonging to the current player.
//     */
//    public String getUserID() {
//        return cUserIDResolver.getID();
//    }
//
//    /**
//     * Updates the username on both the remote server.
//     *
//     * @param username        The username.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void updateRemoteUsername(final String username, final ResponseHandler responseHandler) {
//        PlayerData playerData = new PlayerData(getUserID());
//        playerData.setUsername(username);
//
//        cRemoteConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
//    }
//
//    /**
//     * Updates the stroll timestamp on the local server.
//     *
//     * @param strollTimestamp The timestamp the stroll timer should end.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void updateStrollTimestamp(final Long strollTimestamp, final ResponseHandler responseHandler) {
//        PlayerData playerData = new PlayerData(getUserID());
//        playerData.setStrollTimestamp(strollTimestamp);
//        cLocalConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
//    }
//
//    /**
//     * Updates the interval timestamp on the local server.
//     *
//     * @param intervalTimestamp The timestamp the interval timer should end.
//     * @param responseHandler   The task to execute once a reply is received.
//     */
//    public void updateIntervalTimestamp(final Long intervalTimestamp, final ResponseHandler responseHandler) {
//        PlayerData playerData = new PlayerData(getUserID());
//        playerData.setIntervalTimestamp(intervalTimestamp);
//        cLocalConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
//    }
//
//    /**
//     * Resets the player data.
//     *
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void deletePlayerData(final ResponseHandler responseHandler) {
//        MultiResponseHandler multiResponseHandler = new MultiResponseHandler(responseHandler, 2);
//        cLocalConnection.send(new DeletePlayerData(cUserIDResolver.getID()), multiResponseHandler);
//        cRemoteConnection.send(new DeletePlayerData(cUserIDResolver.getID()), multiResponseHandler);
//    }
//
//    /**
//     * Gets the collection belonging to the player.
//     *
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void getPlayerCollection(final ResponseHandler responseHandler) {
//        cLocalConnection.send(new RequestCollection(getUserID()), responseHandler);
//    }
//
//    /**
//     * Updates the collection belonging to the player.
//     *
//     * @param collection      The collection with which will be updated.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void updatePlayerCollection(final Collection collection, final ResponseHandler responseHandler) {
//        collection.setId(getUserID());
//        cLocalConnection.send(new AddCollection(collection), responseHandler);
//    }
//
//    /**
//     * Adds the player to the specified group.
//     *
//     * @param groupId         The group to join.
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void joinGroup(final String groupId, final ResponseHandler responseHandler) {
//        PlayerData playerData = new PlayerData(getUserID());
//        playerData.setGroupId(groupId);
//        cRemoteConnection.send(new UpdateRemotePlayerData(playerData), responseHandler);
//    }
//
//    /**
//     * Donates a collectible from the server.
//     *
//     * @param collectible     The collectible to be donated.
//     * @param groupId         The group to which the collectible should be donated.
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void donateCollectible(final Collectible collectible, final String groupId,
//                                  final ResponseHandler responseHandler) {
//        MultiResponseHandler multiResponseHandler = new MultiResponseHandler(responseHandler, 2);
//        cLocalConnection.send(new RemoveCollectible(collectible, getUserID()), multiResponseHandler);
//        cRemoteConnection.send(new AddCollectible(collectible, groupId), multiResponseHandler);
//    }
//
//    /**
//     * Returns if connected to the remote server or not.
//     *
//     * @return Is connected or not.
//     */
//    public boolean isRemoteConnected() {
//        return cRemoteConnection.isConnected();
//    }
//
//    /**
//     * Gets the collection belonging to the specified group.
//     *
//     * @param groupId         The group to get the collection from.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void getGroupCollection(final String groupId, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new RequestCollection(groupId), responseHandler);
//    }
//
//    /**
//     * Gets the group data from the server. Behaviour depends on the state.
//     *
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void getGroupData(final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new GetGroupData(), responseHandler);
//    }
//
//    /**
//     * Retrieves the data of the given group id.
//     *
//     * @param groupId         The group id.
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void getGroup(final String groupId, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new GetGroup(groupId), responseHandler);
//    }
//
//    /**
//     * Creates a CreateGroup Query that will be send to the server.
//     *
//     * @param groupId         The group if to create.
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void createGroup(final String groupId, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new CreateGroup(groupId, cUserIDResolver.getID()), responseHandler);
//    }
//
//    /**
//     * Retrieves the usernames of all the members of the given group.
//     *
//     * @param groupId         The group to fetch the members from.
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void getMembers(final String groupId, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new GetMembers(groupId), responseHandler);
//    }
//
//    /**
//     * Retrieves the usernames of all the players.
//     *
//     * @param responseHandler The task to execute once a reply is received.
//     */
//    public void getAllPlayerData(final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new GetAllPlayerData(), responseHandler);
//    }
//
//    /**
//     * Stores the host ip on the server with a generated code that it will return to let the client connect.
//     *
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void hostEvent(int port, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new RequestHostCode(getIPAddress(true), port), responseHandler);
//    }
//
//    /**
//     * Found implementation.
//     * Android returns 127.0.0.1 for Inet4Address.getLocalHost().getHostName(), so using a method found at:
//     * http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device
//     *
//     * @param useIPv4 boolean whether or not to use IPv4.
//     *                +    * @return String representing the IP address.
//     */
//    public static String getIPAddress(boolean useIPv4) {
//        try {
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface intf : interfaces) {
//                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
//                for (InetAddress addr : addrs) {
//                    if (!addr.isLoopbackAddress()) {
//                        String sAddr = addr.getHostAddress().toUpperCase();
//                        boolean isIPv4 = validIP(sAddr);
//                        if (useIPv4) {
//                            if (isIPv4) {
//                                return sAddr;
//                            }
//                        } else {
//                            if (!isIPv4) {
//                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
//                                String ip;
//                                if (delim < 0) {
//                                    ip = sAddr;
//                                } else {
//                                    ip = sAddr.substring(0, delim);
//                                }
//                                return ip;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//        } // for now eat exceptions
//        return "";
//    }
//
//    /**
//     * http://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
//     *
//     * @param ip input string
//     * @return boolean if the input string is a valid IP.
//     */
//    public static boolean validIP(String ip) {
//        try {
//            if (ip == null || ip.isEmpty()) {
//                return false;
//            }
//
//            String[] parts = ip.split("\\.");
//            if (parts.length != 4) {
//                return false;
//            }
//
//            for (String s : parts) {
//                int i = Integer.parseInt(s);
//                if ((i < 0) || (i > 255)) {
//                    return false;
//                }
//            }
//            return !ip.endsWith(".");
//
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//    }
//
//    /**
//     * Retrieves the ip of the host.
//     *
//     * @param code            code of the connection.
//     * @param responseHandler The task to execute once a reply is received completed.
//     */
//    public void getHost(final Integer code, final ResponseHandler responseHandler) {
//        cRemoteConnection.send(new RequestHostIp(code), responseHandler);
//    }
//
