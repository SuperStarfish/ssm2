package com.sem.ssm2.multiplayer;

import com.badlogic.gdx.Gdx;
import com.sem.ssm2.structures.HostData;

import java.io.IOException;
import java.net.*;

/**
 * Client of a multi-player stroll.
 */
public class MultiPlayerClient extends Host {
    /**
     * The IP to connect to.
     */
    protected String cIP;
    /**
     * The port to connect to.
     */
    protected int cPort;

    /**
     * Creates a new Client that connects to the given port.
     *
     * @param hostData The host data to connect to.
     * @throws IOException When connection couldn't be established.
     */
    public MultiPlayerClient(HostData hostData) throws IOException {
        super();
        cIP = hostData.getcIp();
        cPort = hostData.getcPort();
    }

    @Override
    protected Socket createSocket() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(cIP, cPort), cFiveMinutes);
            return socket;
        } catch (SocketTimeoutException | ConnectException e) {
            Gdx.app.debug("MultiplayerClient", "Failed to connect to host.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isHost() {
        return false;
    }
}
