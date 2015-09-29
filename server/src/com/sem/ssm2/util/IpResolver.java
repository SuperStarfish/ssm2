package com.sem.ssm2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Class to determine your ip.
 */
public class IpResolver {
    /**
     * String to be used for the URL to check external IP.
     */
    protected static final String VERIFY = "http://checkip.amazonaws.com";
    /**
     * URL used to check the external IP address.
     */
    protected URL cWhatIsMyIP;

    /**
     * Resolves the IP for the devices.
     */
    public IpResolver() {
        try {
            cWhatIsMyIP = new URL(VERIFY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Contacts the Amazonaws ip check service. If it fails, the server is offline and the external IP address cannot
     * be created. Otherwise returns the external IP.
     *
     * @return The external IP.
     * @throws UnknownHostException If offline, the UnknownHostException is send back to start().
     */
    public final String getExternalIP() throws UnknownHostException {
        String result = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cWhatIsMyIP.openStream(),
                    Charset.forName("UTF-8")));
            result = in.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}