package com.universe.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zg on 2017/2/7.
 */
public class WebSocketEncoder extends MessageToByteEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeInt(msg.length());
        buf.writeBytes(msg.getBytes("UTF-8"));
        ctx.writeAndFlush(buf);
    }
}
