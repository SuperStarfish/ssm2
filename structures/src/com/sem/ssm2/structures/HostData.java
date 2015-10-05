package com.sem.ssm2.structures;

import java.io.Serializable;

public class HostData implements Serializable {
    /**
     * Ip of the host.
     */
    protected String cIp;
    /**
     * Port of the host.
     */
    protected int cPort;

    /**
     * Creates a new object that can be send to another client. Contains Host information.
     * @param ip The IP of the host.
     * @param port The port of the host.
     */
    public HostData(String ip, int port) {
        cIp = ip;
        cPort = port;
    }

    /**
     * Gets the IP.
     * @return The IP.
     */
    public String getcIp() {
        return cIp;
    }

    /**
     * Gets the port.
     * @return The port.
     */
    public int getcPort() {
        return cPort;
    }

    @Override
    public String toString() {
        return cIp + ":" + cPort;
    }
}