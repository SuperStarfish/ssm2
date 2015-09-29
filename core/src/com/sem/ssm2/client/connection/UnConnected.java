package com.sem.ssm2.client.connection;

import com.sem.ssm2.client.Client;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.Query;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * A state where the client is not connected to a server.
 */
public class UnConnected implements Connection {
    /**
     * Default java logging functionality.
     */
    protected static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
    /**
     * Localhost that can be used to connect to the local server.
     */
    protected static final String LOCALHOST = "127.0.0.1";
    /**
     * Boolean whether this connection is already trying to connect.
     */
    protected boolean cConnecting;

    protected int reconnectDelay = 2000;

    protected Client client;

    /**
     * Creates a new state where the Client is Unconnected.
     */
    public UnConnected(Client client) {
        this.client = client;
        cConnecting = false;
    }

    /**
     * Connects to either a local or a remote server. Connects to localhost if ip is 'null'.
     * @param ip   The IP to connect to.
     * @param port The port to connect to.
     */
    @Override
    public void connect(final String ip, final int port) {
        if (!cConnecting) {
            if (ip == null) {
                localConnect(LOCALHOST, port);
            } else {
                remoteConnect(ip, port);
            }
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void send(final Query query, final ResponseHandler responseHandler) {
        if (responseHandler != null) {
            responseHandler.handleResponse(new Response(false, null));
        }
    }

    /**
     * Attempts to connect to the local server.
     * @param ip Localhost.
     * @param port Port provided.
     */
    protected void localConnect(final String ip, final int port) {
        cConnecting = true;
        try {
            LOGGER.info("Trying to connect to the local server");
            Connection connection = new LocalConnection(client, ip, port);
            client.setLocalConnection(connection);
        } catch (IOException e) {
            LOGGER.info("Connection failed!");
        }
        cConnecting = false;
    }

    /**
     * Connects to a remote server. Connection is done in a separate Thread as not to block the game.
     * @param ip The IP to connect to.
     * @param port The port to connect to.
     */
    protected void remoteConnect(final String ip, final int port) {
        cConnecting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info("Trying to connect to the remote server");
                    final Connection connection = new RemoteConnection(client, ip, port);
                    client.addPostRunnable(new Runnable() {
                        @Override
                        public void run() {
                            client.setRemoteConnection(connection);
                            cConnecting = false;
                        }
                    });
                } catch (IOException e) {
                    cConnecting = false;
                    LOGGER.info("Failed to connect to remote server (" + ip + ":" + port + ").");

                    if(client.retryRemoteConnection()){
                        try {
                            Thread.sleep(reconnectDelay);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        client.addPostRunnable(new Runnable() {
                            @Override
                            public void run() {
                                client.connectToRemoteServer();
                            }
                        });
                    }


                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect: Not even connected");
    }
}