package com.universe.net.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by zg on 2017/2/7.
 */
public class TCPServerHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("come in tcp handler...");
        System.out.println(msg);
        ctx.write(new String("tcpServer complete..."));
    }
}
