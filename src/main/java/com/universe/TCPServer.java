package com.universe;

import akka.actor.ActorRef;
import com.universe.logic.AppSystem;
import com.universe.logic.message.PublisherMsg;
import com.universe.net.BootstrapFactory;
import com.universe.net.pipeline.TCPPipeline;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class TCPServer {

    public static void main(String[] args) throws InterruptedException {
        BootstrapFactory.startTCP(46003, new TCPPipeline());
        AppSystem.getAppServer();

        //test publisher
        while (true) {
            AppSystem.getAppServer().publisherActor().tell(new PublisherMsg("i`m publisher"), ActorRef.noSender());
            Thread.sleep(1000);
        }
    }
}
