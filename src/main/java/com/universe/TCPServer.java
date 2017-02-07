package com.universe;

import com.universe.net.BootstrapFactory;
import com.universe.net.pipeline.TCPPipeline;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class TCPServer {

    public static void main(String[] args) {
        BootstrapFactory.startTCP(46003, new TCPPipeline());
    }
}
