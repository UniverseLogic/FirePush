package com.universe.net.handler;

import com.universe.net.message.UDPPacketReqMsg;
import com.universe.net.message.UDPPacketRspMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by zg on 2017/2/7.
 */
public class UDPServerHandler extends SimpleChannelInboundHandler<UDPPacketReqMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UDPPacketReqMsg msg) throws Exception {
        System.out.println("come in..");
        System.out.println(msg);
        ctx.writeAndFlush(new UDPPacketRspMsg(new String("server complete"), msg.getInetSocketAddress()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.toString());
    }
}
