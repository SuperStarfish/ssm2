package com.sem.ssm2.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class used to make static calls from the Java Libraries. Used for testing.
 */
public class StaticsCaller {

    /**
     * Returns the localhost.
     *
     * @return Localhost.
     * @throws UnknownHostException Localhost could not be found.
     */
    public InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    /**
     * Generates an InetAddress from a string.
     *
     * @param ip The IP to try and make the connection to.
     * @return The InetAddress to that IP.
     * @throws UnknownHostException Host could not be created.
     */
    public InetAddress getByName(final String ip) throws UnknownHostException {
        return InetAddress.getByName(ip);
    }
}