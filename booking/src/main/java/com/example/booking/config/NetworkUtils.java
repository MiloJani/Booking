package com.example.booking.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    public static String getServerIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // Handle the exception as needed
            return "localhost";
        }
    }
}

