package com.sem.ssm2.desktop.util;

import com.sem.ssm2.client.UserIDResolver;

/**
 * Gets the id of the desktop device.
 */
public class DesktopIDResolver implements UserIDResolver {
    @Override
    public String getID() {
        return "Desktop/" + System.getProperty("user.name");
    }
}