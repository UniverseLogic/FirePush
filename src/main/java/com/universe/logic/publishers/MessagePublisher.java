package com.universe.logic.publishers;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.universe.logic.message.PublisherMsg;

/**
 * Created by frank_zhao on 2017/2/26.
 */
public class MessagePublisher extends UntypedActor {

    private final ActorRef mediator;


    public MessagePublisher() {
        mediator = DistributedPubSub.get(getContext().system()).mediator();
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        //forward everything message.
        if (message instanceof PublisherMsg) {
            mediator.forward(new DistributedPubSubMediator.Publish("PUSH", message), getContext());
        } else {
            unhandled(message);
        }
    }
}
