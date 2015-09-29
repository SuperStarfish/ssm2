package com.sem.ssm2.server;

import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.query.Query;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * The ServerThread interacts with the Client.
 */
public final class ServerThread implements Runnable {
    /**
     * Default java logging functionality.
     */
    protected static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());

    /**
     * The connection with the Client.
     */
    protected Socket cConnection;

    /**
     * The ObjectOutputStream for outgoing messages.
     */
    protected ObjectOutputStream cOutputStream;

    /**
     * The ObjectInputStream for incoming messages.
     */
    protected ObjectInputStream cInputStream;

    /**
     * Determines if the connection needs to be kept alive.
     */
    protected boolean cKeepAlive = true;

    /**
     * Connection with the database. Provided by the LocalStorageResolver.
     */
    protected Connection cDatabaseConnection;

    /**
     * The LocalStorageResolver containing if the server is remote or local and the connection to the database.
     */
    protected LocalStorageResolver cLocalStorageResolver;

    /**
     * Creates a new ServerThread for communication with the server and the client.
     *
     * @param connection           The connection with the Client.
     * @param localStorageResolver The LocalStorage resolver containing database connection and if it is remote
     *                             or local.
     */
    public ServerThread(final Socket connection, final LocalStorageResolver localStorageResolver) {
        cConnection = connection;
        cLocalStorageResolver = localStorageResolver;
        cDatabaseConnection = cLocalStorageResolver.getConnection();
        LOGGER.info("Established a connection with: " + cConnection.getInetAddress().getHostName());
    }

    @Override
    public void run() {
        try {
            createStreams();
            interactWithClient();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                cleanUp();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Creates the ObjectInput- and ObjectOutputStreams.
     *
     * @throws IOException IOException
     */
    protected void createStreams() throws IOException {
        cOutputStream = new ObjectOutputStream(cConnection.getOutputStream());
        cOutputStream.flush();
        cInputStream = new ObjectInputStream(cConnection.getInputStream());
    }

    /**
     * This method is used for incoming messages from the client.
     */
    protected void interactWithClient() {
        do {
            try {
                Object input = cInputStream.readObject();
                reply(queryDatabase((Query) input));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                LOGGER.severe("Lost connection with: " + cConnection.getInetAddress().getHostName());
                cKeepAlive = false;
            } catch (SocketException e) {
                LOGGER.info("Lost connection with: " + cConnection.getInetAddress().getHostName());
                cKeepAlive = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (cKeepAlive);
    }

    /**
     * Closes the ObjectInput- and ObjectOutputStream as well as the connection with the Client.
     *
     * @throws IOException IOException
     */
    protected void cleanUp() throws IOException {
        String hostName = cConnection.getInetAddress().getHostName();
        cOutputStream.close();
        cInputStream.close();
        cConnection.close();
        LOGGER.info("Closed connection with: " + hostName);
    }

    /**
     * Sends a reply that contains the data that is requested.
     *
     * @param response The data to reply to the client.
     */
    protected void reply(final Response response) {
        try {
            cOutputStream.writeObject(response);
            cOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a database connection and calls the given query.
     * The result of the query is put into a response.
     * The response also indicates whether the query was successful.
     *
     * @param query The query to be executed on the database.
     * @return The response to be sent back to the client.
     */
    protected Response queryDatabase(final Query query) {
        Serializable serializable = null;
        boolean success = false;

        try {
            serializable = query.query(cDatabaseConnection);
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Response(success, serializable);
    }

}