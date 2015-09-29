package com.sem.ssm2.client.connection;

import com.sem.ssm2.client.Client;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.Query;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A state in which the Client is connected to the server.
 */
public final class RemoteConnection implements Connection {
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
    protected boolean cWaiting;
    /**
     * A queue that is used as buffer/storage for connection packets that must be sent but is no room for yet.
     */
    protected ConcurrentLinkedQueue<ConnectionPacket> cBuffer;

    /**
     * Attempts to create a new connection with the server. Fails after cConnectionTimeOut milliseconds.
     *
     * @param ip   The IP to connect to.
     * @param port The port to connect to.
     * @throws IOException Exception if connection fails.
     */
    public RemoteConnection(final String ip, final int port) throws IOException {
        cConnection = new Socket(ip, port);
        cOutputStream = new ObjectOutputStream(cConnection.getOutputStream());
        cInputStream = new ObjectInputStream(cConnection.getInputStream());
        cWaiting = true;
        cBuffer = new ConcurrentLinkedQueue<ConnectionPacket>();
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
        cBuffer.add(new ConnectionPacket(query, responseHandler));
        if (cWaiting) {
            cWaiting = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!cBuffer.isEmpty()) {
                        sendPacket(cBuffer.poll());
                    }
                    cWaiting = true;
                }
            }).start();
        }
    }

    /**
     * Handles the transfer of a single connection packet.
     *
     * @param connectionPacket the connection packet to be handled.
     */
    protected void sendPacket(final ConnectionPacket connectionPacket) {
        try {
            cOutputStream.writeObject(connectionPacket.getQuery());
            cOutputStream.flush();
            final Response response = (Response) cInputStream.readObject();
            Client.getInstance().addPostRunnables(new Runnable() {
                @Override
                public void run() {
                    if (connectionPacket.getResponseHandler() != null) {
                        connectionPacket.getResponseHandler().handleResponse(response);
                    }
                }
            });
        } catch(SocketException e) {
            Client.getInstance().setRemoteConnection(new UnConnected());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            cConnection.close();
            Client.getInstance().setRemoteConnection(new UnConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}