package com.sem.ssm2.server;

import com.sem.ssm2.server.database.query.temp.EventHostCleaner;
import com.sem.ssm2.server.database.query.Query;
import com.sem.ssm2.util.IpResolver;
import com.sem.ssm2.util.StaticsCaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Server has a main method that starts a Server instance. The server looks up the local IP and the external IP.
 * It creates a ServerSocket on the specified (default) port and then accepts connections. Incoming connections are
 * handled in new Threads. This is done with an ExecutorService that defines the maximum number of Threads.
 */
public class Server {
    /**
     * Default Logger in Java used for the purpose of logging changes in the Server.
     */
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    /**
     * The largest port number that can exist.
     */
    protected static final int cMaxPortNumber = 65535;
    /**
     * The default port to be used for the ServerSocket.
     */
    protected static final int cDefaultPort = 56789;
    /**
     * The maximum number of threads that the ExecutorService can have.
     */
    protected static final int cMaxThreads = 50;
    /**
     * Ip of the server, either local or remote.
     */
    protected String cIp;
    /**
     * The ServerSocket used by this Server to accept incoming connections.
     */
    protected ServerSocket cServerSocket;
    /**
     * The ExecutorPool that limits the number of Threads spawned.
     */
    protected ExecutorService cPool;
    /**
     * Service that cleans the tables on regular intervals.
     */
    protected ScheduledExecutorService cCleaner;
    /**
     * The task that will clean the database tables.
     */
    protected final Runnable cCleanTask = new Runnable() {
        protected final Query cQuery = new EventHostCleaner();
        @Override
        public void run() {
            try {
                cQuery.query(cLocalStorageResolver.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Interval with which the database is cleaned (in minutes).
     */
    protected static final int cCleanupInterval = 30;
    /**
     * Used to make calls to static methods. Primarily used for mocking in tests.
     */
    protected StaticsCaller cStaticsCaller;
    /**
     * This defines the database connection and if the server is remote or local.
     */
    protected LocalStorageResolver cLocalStorageResolver;

    /**
     * Creates a server using the settings provided by the LocalStorageResolver.
     *
     * @param storageResolver Container of settings and database connection.
     */
    public Server(final LocalStorageResolver storageResolver) {
        cLocalStorageResolver = storageResolver;
        cStaticsCaller = new StaticsCaller();
    }

    /**
     * Starts a server.
     *
     * @param args Not used, but default for main method.
     */
    public static void main(final String[] args) {
        Server server = new Server(new RemoteStorageResolver());
        LOGGER.setLevel(Level.INFO);
        server.start();
    }

    /**
     * Starts the Server. This can be either an internal or external server, defined by isRemote.
     * If the server is internal it will not attempt to check its external IP.
     * Uses default port if remote, otherwise uses 0 (random) port.
     */
    public final void start() {
        createLocalIP();

        if (cLocalStorageResolver.isLocal()) {
            createServerSocket(0);
        } else {
            createServerSocket(cDefaultPort);
            createExternalIP();
            validateExternalConnection();

            initCleaner();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Accepting incoming connections");
                while (true) {
                    acceptConnections();
                }
            }
        }).start();
    }

    /**
     * This initializes the ExecutorService that set intervals cleans database tables.
     */
    public void initCleaner() {
        cCleaner = Executors.newSingleThreadScheduledExecutor();
        cCleaner.scheduleAtFixedRate(cCleanTask, 0, cCleanupInterval, TimeUnit.MINUTES);
    }

    /**
     * Attempts to get the local IP address. If it fails it assigns as UnknownHost.
     */
    protected final void createLocalIP() {
        try {
            cIp = cStaticsCaller.getLocalHost().getHostAddress();
            LOGGER.info("Local IP: " + cIp);
        } catch (UnknownHostException e) {
            LOGGER.severe("Could not set up local IP!");
        }
    }

    /**
     * Attempts to get the External IP address. If it fails it assigns as UnknownHost.
     */
    protected final void createExternalIP() {
        IpResolver ipResolver = new IpResolver();
        try {
            cIp = cStaticsCaller.getByName(ipResolver.getExternalIP()).getHostAddress();
            LOGGER.info("External IP: " + cIp);
        } catch (UnknownHostException e) {
            LOGGER.severe("Could not set up local IP!");
        }
    }

    /**
     * Attempts to create a ServerSocket using the port given. If it fails, it will ask for a new port.
     *
     * @param port The port to use for the ServerSocket.
     */
    protected final void createServerSocket(final int port) {
        try {
            cServerSocket = new ServerSocket(port);
            LOGGER.info("Successfully bound to port " + cServerSocket.getLocalPort() + ".");
        } catch (IOException e) {
            LOGGER.severe("Port " + port + " is already in use!");
            createServerSocket(ServerUtils.askForPort());
        }
    }

    /**
     * Checks to see if the Server is accessible from the outside world.
     */
    protected final void validateExternalConnection() {
        try {
            LOGGER.info("Checking if the port is open.");
            Socket socket = new Socket(cIp, cDefaultPort);
            cServerSocket.accept();
            LOGGER.info("Port " + cServerSocket.getLocalPort() + " is open. Managed to connect over external IP");
            socket.close();
        } catch (ConnectException e) {
            LOGGER.severe("Port " + cServerSocket.getLocalPort() + " does not appear to be open! "
                    + "Can't connect over external IP. Use local IP to connect to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for a new incoming connection and then adds it in a new ServerThread in the pool.
     */
    protected final void acceptConnections() {
        try {
            cPool = Executors.newFixedThreadPool(cMaxThreads);
            Socket client = cServerSocket.accept();
            Runnable task = new ServerThread(client, cLocalStorageResolver);
            cPool.submit(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final String toString() {
        String result = "";
        String lineSeparator = System.getProperty("line.separator");
        result += "IP: " + cIp + lineSeparator;
        return result;
    }

    /**
     * Returns the socket on which the server lives. This is used to create a local connection with the server.
     *
     * @return The port the socket lives on.
     */
    public int getSocketPort() {
        return cServerSocket.getLocalPort();
    }

    public static class ServerUtils {
        /**
         * Asks the user for a port. It validates if the port is within proper range.
         *
         * @return The port that the user provided.
         */
        public static int askForPort() {
            int port = -1;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
            do {
                LOGGER.info("Please enter a port number:");
                try {
                    String line = bufferedReader.readLine();
                    port = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    LOGGER.severe("Bad input. Try again.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!isValidPort(port));
            return port;
        }

        /**
         * Returns if the port is within proper range.
         *
         * @param port The port to check.
         * @return Returns if the port is within valid range.
         */
        protected static boolean isValidPort(final int port) {
            return port >= 0 && port <= cMaxPortNumber;
        }
    }
}