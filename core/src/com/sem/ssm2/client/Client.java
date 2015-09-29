package com.sem.ssm2.client;

import com.sem.ssm2.client.connection.Connection;
import com.sem.ssm2.client.connection.UnConnected;
import com.sem.ssm2.server.database.MultiResponseHandler;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.*;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.Subject;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Client that connects to the server.
 */
public final class Client {
    /**
     * Default Logger in Java used for the purpose of logging changes in the Server.
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    /**
     * The Client connection with the internal storage server.
     * FindBugs: cg.group4.client.Client.cLocalInstance should be package protected
     */
    protected static Client cInstance;
    /**
     * The default IP to connect to.
     */
    protected String cCurrentIp = defaultIp();
    /**
     * The default port to connect to.
     */
    protected int cCurrentPort = defaultPort();
    /**
     * The local connection state with the server.
     */
    protected Connection cLocalConnection;
    /**
     * The remote connection state with the server.
     */
    protected Connection cRemoteConnection;
    /**
     * The user id. Android Phone ID or Desktop name.
     */
    protected UserIDResolver cUserIDResolver;
    /**
     * Notifies all listeners that the server has either connected or disconnected.
     */
    protected Subject cRemoteChangeSubject;
    /**
     * A list of Runnable that have to be run at the end of a render cycle.
     * These are added to the Gdx.app.postRunnable().
     */
    protected ArrayList<Runnable> cPostRunnables;


    /**
     * Constructs a new Client and sets the state to unconnected.
     */
    public Client() {
        cLocalConnection = new UnConnected();
        cRemoteConnection = new UnConnected();
        cUserIDResolver = new DummyUserIdResolver();
        cRemoteChangeSubject = new Subject();
        cPostRunnables = new ArrayList<>();
    }

    /**
     * Gets the Singleton instance.
     *
     * @return The Client.
     */
    public static Client getInstance() {
        if (cInstance == null) {
            cInstance = new Client();
        }
        return cInstance;
    }

    /**
     * Connects to the default remote server. Behaviour depends on the state.
     */
    public void connectToRemoteServer() {
        cRemoteConnection.connect(cCurrentIp, cCurrentPort);
    }

    /**
     * Closes the connection with the remote server.
     */
    public void closeRemoteConnection() {
        cRemoteConnection.disconnect();
    }

    /**
     * Connects to a server given an IP and port.
     *
     * @param port Port to connect to.
     */
    public void connectToLocalServer(final int port) {
        cLocalConnection.connect(null, port);
    }

    /**
     * Returns the list of Runnable that need to be processed in the Gdx.app.postRunnable().
     *
     * @return ArrayList with the Runnables.
     */
    public ArrayList<Runnable> getPostRunnables() {
        return cPostRunnables;
    }

    /**
     * Adds another Runnable to the PostRunnable list.
     *
     * @param runnable The task to run.
     */
    public void addPostRunnables(final Runnable runnable) {
        cPostRunnables.add(runnable);
    }

    /**
     * Clears the PostRunnable ArrayList.
     */
    public void resetPostRunnables() {
        cPostRunnables.clear();
    }

    /**
     * Returns the RemoteChangeSubject that notifies whenever a change in the remote connection status occurs.
     *
     * @return Subject that can be subscribed on.
     */
    public Subject getRemoteChangeSubject() {
        return cRemoteChangeSubject;
    }

    /**
     * Sets the local connection to the new connection.
     *
     * @param connection The connection that needs to be set.
     */
    public void setLocalConnection(final Connection connection) {
        cLocalConnection = connection;
        LOGGER.info("Managed to locally connect: " + connection.isConnected());
    }

    /**
     * Sets the remote connection to the new connection.
     *
     * @param connection The connection that needs to be set.
     */
    public void setRemoteConnection(final Connection connection) {
        cRemoteConnection = connection;
        cRemoteChangeSubject.update(connection.isConnected());
        LOGGER.info("Remote connection running: " + connection.isConnected());
    }

    /**
     * Sets the userid using the device ID.
     *
     * @param idResolver Tool used for getting the proper device ID.
     */
    public void setUserIDResolver(final UserIDResolver idResolver) {
        cUserIDResolver = idResolver;
    }

    /**
     * Gets the userdata from the server. Uses UserIDResolver to get the data. Behaviour depends on the state.
     *
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void getPlayerData(final ResponseHandler responseHandler) {
        cLocalConnection.send(new RequestPlayerData(cUserIDResolver.getID()), responseHandler);
    }

    // --------------- Only queries follow below.--------------

    /**
     * Gets the userdata from the server. Uses UserIDResolver to get the data. Behaviour depends on the state.
     *
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void getGroupId(final ResponseHandler responseHandler) {
        cRemoteConnection.send(new RequestGroupId(cUserIDResolver.getID()), responseHandler);
    }

    /**
     * Updates the username on both the local server.
     *
     * @param username        The username.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void updateLocalUsername(final String username, final ResponseHandler responseHandler) {
        PlayerData playerData = new PlayerData(getUserID());
        playerData.setUsername(username);

        cLocalConnection.send(new UpdatePlayerData(playerData), responseHandler);
    }

    /**
     * Returns the user ID from the supplied UserIDResolver.
     *
     * @return The ID belonging to the current player.
     */
    public String getUserID() {
        return cUserIDResolver.getID();
    }

    /**
     * Updates the username on both the remote server.
     *
     * @param username        The username.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void updateRemoteUsername(final String username, final ResponseHandler responseHandler) {
        PlayerData playerData = new PlayerData(getUserID());
        playerData.setUsername(username);

        cRemoteConnection.send(new UpdatePlayerData(playerData), responseHandler);
    }

    /**
     * Updates the stroll timestamp on the local server.
     *
     * @param strollTimestamp The timestamp the stroll timer should end.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void updateStrollTimestamp(final Long strollTimestamp, final ResponseHandler responseHandler) {
        PlayerData playerData = new PlayerData(getUserID());
        playerData.setStrollTimestamp(strollTimestamp);
        cLocalConnection.send(new UpdatePlayerData(playerData), responseHandler);
    }

    /**
     * Updates the interval timestamp on the local server.
     *
     * @param intervalTimestamp The timestamp the interval timer should end.
     * @param responseHandler   The task to execute once a reply is received.
     */
    public void updateIntervalTimestamp(final Long intervalTimestamp, final ResponseHandler responseHandler) {
        PlayerData playerData = new PlayerData(getUserID());
        playerData.setIntervalTimestamp(intervalTimestamp);
        cLocalConnection.send(new UpdatePlayerData(playerData), responseHandler);
    }

    /**
     * Resets the player data.
     *
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void deletePlayerData(final ResponseHandler responseHandler) {
        MultiResponseHandler multiResponseHandler = new MultiResponseHandler(responseHandler, 2);
        cLocalConnection.send(new DeletePlayerData(cUserIDResolver.getID()), multiResponseHandler);
        cRemoteConnection.send(new DeletePlayerData(cUserIDResolver.getID()), multiResponseHandler);
    }

    /**
     * Gets the collection belonging to the player.
     *
     * @param responseHandler The task to execute once a reply is received.
     */
    public void getPlayerCollection(final ResponseHandler responseHandler) {
        cLocalConnection.send(new RequestCollection(getUserID()), responseHandler);
    }

    /**
     * Updates the collection belonging to the player.
     *
     * @param collection      The collection with which will be updated.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void updatePlayerCollection(final Collection collection, final ResponseHandler responseHandler) {
        collection.setId(getUserID());
        cLocalConnection.send(new AddCollection(collection), responseHandler);
    }

    /**
     * Adds the player to the specified group.
     *
     * @param groupId         The group to join.
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void joinGroup(final String groupId, final ResponseHandler responseHandler) {
        PlayerData playerData = new PlayerData(getUserID());
        playerData.setGroupId(groupId);
        cRemoteConnection.send(new UpdatePlayerData(playerData), responseHandler);
    }

    /**
     * Donates a collectible from the server.
     *
     * @param collectible     The collectible to be donated.
     * @param groupId         The group to which the collectible should be donated.
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void donateCollectible(final Collectible collectible, final String groupId,
                                  final ResponseHandler responseHandler) {
        MultiResponseHandler multiResponseHandler = new MultiResponseHandler(responseHandler, 2);
        cLocalConnection.send(new RemoveCollectible(collectible, getUserID()), multiResponseHandler);
        cRemoteConnection.send(new AddCollectible(collectible, groupId), multiResponseHandler);
    }

    /**
     * Returns if connected to the remote server or not.
     *
     * @return Is connected or not.
     */
    public boolean isRemoteConnected() {
        return cRemoteConnection.isConnected();
    }

    /**
     * Gets the collection belonging to the specified group.
     *
     * @param groupId         The group to get the collection from.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void getGroupCollection(final String groupId, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new RequestCollection(groupId), responseHandler);
    }

    /**
     * Gets the group data from the server. Behaviour depends on the state.
     *
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void getGroupData(final ResponseHandler responseHandler) {
        cRemoteConnection.send(new GetGroupData(), responseHandler);
    }

    /**
     * Retrieves the data of the given group id.
     *
     * @param groupId         The group id.
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void getGroup(final String groupId, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new GetGroup(groupId), responseHandler);
    }

    /**
     * Creates a CreateGroup Query that will be send to the server.
     *
     * @param groupId         The group if to create.
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void createGroup(final String groupId, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new CreateGroup(groupId, cUserIDResolver.getID()), responseHandler);
    }

    /**
     * Retrieves the usernames of all the members of the given group.
     *
     * @param groupId         The group to fetch the members from.
     * @param responseHandler The task to execute once a reply is received.
     */
    public void getMembers(final String groupId, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new GetMembers(groupId), responseHandler);
    }

    /**
     * Retrieves the usernames of all the players.
     *
     * @param responseHandler The task to execute once a reply is received.
     */
    public void getAllPlayerData(final ResponseHandler responseHandler) {
        cRemoteConnection.send(new GetAllPlayerData(), responseHandler);
    }

    /**
     * Stores the host ip on the server with a generated code that it will return to let the client connect.
     *
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void hostEvent(int port, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new RequestHostCode(getIPAddress(true), port), responseHandler);
    }

    /**
     * Found implementation.
     * Android returns 127.0.0.1 for Inet4Address.getLocalHost().getHostName(), so using a method found at:
     * http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device
     *
     * @param useIPv4 boolean whether or not to use IPv4.
     *                +    * @return String representing the IP address.
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = validIP(sAddr);
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                String ip;
                                if (delim < 0) {
                                    ip = sAddr;
                                } else {
                                    ip = sAddr.substring(0, delim);
                                }
                                return ip;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * http://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
     *
     * @param ip input string
     * @return boolean if the input string is a valid IP.
     */
    public static boolean validIP(String ip) {
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

    /**
     * Retrieves the ip of the host.
     *
     * @param code            code of the connection.
     * @param responseHandler The task to execute once a reply is received completed.
     */
    public void getHost(final Integer code, final ResponseHandler responseHandler) {
        cRemoteConnection.send(new RequestHostIp(code), responseHandler);
    }

    // --------------- Helper methods follow below.--------------


    /**
     * Changes the host IP address.
     * @param ip new default ip
     */
    public void setIp(String ip) {
        this.cCurrentIp = ip;
    }

    /**
     * Changes the host port
     * @param port new port
     */
    public void setPort(int port) {
        this.cCurrentPort = port;
    }

    /**
     * Gets the host IP address.
     * @return ip
     */
    public String getIp() {
        return cCurrentIp;
    }

    /**
     * Gets the host port.
     * @return host port number
     */
    public int getPort() {
        return cCurrentPort;
    }

    /**
     * Returns the default ip. This method is used as storage for the defaults in case overwritten by
     * user preferences.
     * @return default ip
     */
    public String defaultIp() {
        return "127.0.0.1";
    }

    /**
     * Returns the default port. This method is used as storage for the defaults in case overwritten by
     * user preferences.
     * @return default port
     */
    public int defaultPort() {
        return 56789;
    }
}