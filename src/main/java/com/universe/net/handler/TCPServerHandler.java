package com.universe.net.handler;

import akka.actor.ActorRef;
import com.universe.logic.AppSystem;
import com.universe.logic.actors.AgentActor;
import com.universe.logic.message.CloseChannelMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by zg on 2017/2/7.
 */
public class TCPServerHandler extends SimpleChannelInboundHandler<String> {

    private ActorRef agentActor;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        agentActor = AppSystem.getAppServer().actorSystem().actorOf(AgentActor.props(ctx.channel()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("come in tcp handler...");
        System.out.println(msg);
        agentActor.tell(msg, ActorRef.noSender());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //exception handler
        agentActor.tell(new CloseChannelMsg(), ActorRef.noSender());
    }


}
