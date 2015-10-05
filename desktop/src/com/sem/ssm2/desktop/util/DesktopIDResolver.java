package com.sem.ssm2.desktop.util;

import com.sem.ssm2.client.UserIDResolver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Gets the id of the desktop device.
 */
public class DesktopIDResolver implements UserIDResolver {
    @Override
    public String getID() {
//        InetAddress ip;
//        try {
//
//            ip = InetAddress.getLocalHost();
//
//            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//
//            byte[] mac = network.getHardwareAddress();
//
//            StringBuilder sb = new StringBuilder();
//            sb.append("Desktop/");
//            for (int i = 0; i < mac.length; i++) {
//                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//            }
//            return sb.toString();
//        } catch (UnknownHostException e) {
//
//            e.printStackTrace();
//
//        } catch (SocketException e){
//
//            e.printStackTrace();
//
//        }
        return "Desktop/" + System.getProperty("user.name");
    }
}