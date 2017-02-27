package com.universe.logic.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.universe.logic.AppSystem;
import com.universe.logic.message.CallbackMsg;
import com.universe.logic.message.CloseChannelMsg;
import com.universe.logic.message.LoginMsg;
import io.netty.channel.Channel;

import java.util.Random;

/**
 * Created by frank_zhao on 2017/2/26.
 *
 * @apiNote protoc to message actor
 */
public class AgentActor extends UntypedActor {

    private final Channel channel;
    private final ActorRef personActor;

    public AgentActor(Channel channel) {
        this.channel = channel;
        personActor = AppSystem.getAppServer().personActor();
    }

    @Override
    public void preStart() throws Exception {

    }

    @Override
    public void postStop() throws Exception {
        getContext().unbecome();
    }

    public static final Props props(Channel channel) {
        return Props.create(AgentActor.class, channel);
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            //messages...
            //if message is login
            personActor.tell(new LoginMsg(System.currentTimeMillis(), "frank" + new Random().nextInt(100)), getSelf());
            //after handler other message
        } else if (message instanceof CallbackMsg) {
            String reply = ((CallbackMsg) message).getMessage();
            this.channel.write(reply);
        } else if (message instanceof CloseChannelMsg) {
            //after to do something
            System.out.println("channel close...");
        }
    }
}
