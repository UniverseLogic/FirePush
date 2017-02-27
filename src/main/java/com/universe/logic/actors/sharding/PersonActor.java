package com.universe.logic.actors.sharding;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.universe.logic.message.CallbackMsg;
import com.universe.logic.message.LoginMsg;
import com.universe.logic.message.PublisherMsg;

/**
 * Created by frank_zhao on 2017/2/26.
 */
public class PersonActor extends UntypedActor {

    private final ActorRef subscriber;

    private Long userId;
    private String userName;
    private ActorRef agentActor;


    public PersonActor() {
        subscriber = DistributedPubSub.get(getContext().system()).mediator();
    }

    @Override
    public void preStart() throws Exception {
        subscriber.tell(new DistributedPubSubMediator.Subscribe("PUSH", getSelf()), getSelf());
    }

    @Override
    public void postStop() throws Exception {
        subscriber.tell(new DistributedPubSubMediator.Unsubscribe("PUSH", getSelf()), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof LoginMsg) {
            System.out.println("login...");
            LoginMsg loginMsg = (LoginMsg) message;
            this.userId = loginMsg.getUserId();
            this.userName = loginMsg.getUserName();
            this.agentActor = getSender();
            getContext().watch(this.agentActor);
            //callback
            sendCallback("who are you");
            //must login. after handler other message
            become();
        }
        commonMsg(message);
    }

    void become() {
        getContext().become(msg -> {
            if (msg instanceof PublisherMsg) {
                System.out.println("i`m receive publish");
                sendCallback(((PublisherMsg) msg).getMessage());
            }
            commonMsg(msg);
        });
    }

    void commonMsg(Object message) {
        if (message instanceof Terminated) {
            //kill self
            getContext().unwatch(this.agentActor);
            getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
    }

    void sendCallback(String message) {
        this.agentActor.tell(new CallbackMsg(this.userId, message), ActorRef.noSender());
    }
}
