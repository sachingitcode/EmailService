/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceir.CEIRPostman.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Service
public class VirtualIpAddressUtil {

    @Value("${virtualIp}")
    String virtualIp;

    private final Logger log = LogManager.getLogger(VirtualIpAddressUtil.class);

    public boolean checkVip() {
        boolean isVipFound = false;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    if (enumIpAddr.nextElement().toString().contains(virtualIp)) {
                        isVipFound = true;
                    }
                }
            }
        } catch (SocketException e) {
            log.error(" (error retrieving network interface list " + e);
        }
        log.info("is vip found: " + isVipFound);
        return isVipFound;
    }

}

