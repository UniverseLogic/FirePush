package com.universe;

import com.universe.net.BootstrapFactory;
import com.universe.net.pipeline.WebSocketPipeline;

/**
 * Created by zg on 2017/2/7.
 */
public class WebSocketServer {

    public static void main(String[] args) {
        BootstrapFactory.startTCP(46002, new WebSocketPipeline());
    }
}
