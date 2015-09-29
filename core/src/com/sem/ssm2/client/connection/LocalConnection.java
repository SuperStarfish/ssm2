package com.sem.ssm2.client.connection;

import com.sem.ssm2.client.Client;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.Query;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Connection state where no connection is made with the server.
 */
public final class LocalConnection implements Connection {
    /**
     * The connection with the server.
     */
    protected Socket cConnection;
    /**
     * InputStream for objects from the server.
     */
    protected ObjectInputStream cInputStream;
    /**
     * OutputStream for objects to the server.
     */
    protected ObjectOutputStream cOutputStream;
    /**
     * Boolean whether the connection is currently accepting new requests (not waiting for response).
     */
    protected boolean cAcceptingRequest;

    protected Client client;

    /**
     * Attempts to create a new connection with the server. Fails after cConnectionTimeOut milliseconds.
     *
     * @param ip   The IP to connect to.
     * @param port The port to connect to.
     * @throws IOException Exception if connection fails.
     */
    public LocalConnection(Client client, final String ip, final int port) throws IOException {
        this.client = client;
        cConnection = new Socket(ip, port);
        cOutputStream = new ObjectOutputStream(cConnection.getOutputStream());
        cOutputStream.flush();
        cInputStream = new ObjectInputStream(cConnection.getInputStream());
        cAcceptingRequest = true;
    }

    /**
     * Has an empty body because the server connection has already been made.
     * @param ip   The IP to connect to.
     * @param port The port to connect to.
     */
    @Override
    public void connect(final String ip, final int port) { }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(final Query query, final ResponseHandler responseHandler) {
        if (cAcceptingRequest) {
            cAcceptingRequest = false;
            try {
                cOutputStream.writeObject(query);
                cOutputStream.flush();
                Response response = (Response) cInputStream.readObject();
                cAcceptingRequest = true;
                if (responseHandler != null) {
                    responseHandler.handleResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() {
        try {
            cConnection.close();
            client.setLocalConnection(new UnConnected(client));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}