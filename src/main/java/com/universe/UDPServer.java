package com.universe;

import com.universe.net.BootstrapFactory;

/**
 * Created by zg on 2017/2/7.
 */
public class UDPServer {

    public static void main(String[] args) {
        BootstrapFactory.startUDP(46000);
    }

}
