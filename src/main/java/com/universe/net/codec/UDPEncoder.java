package com.universe.net.codec;

import com.universe.net.message.UDPPacketRspMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by zg on 2017/2/7.
 */
public class UDPEncoder extends MessageToMessageEncoder<UDPPacketRspMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UDPPacketRspMsg msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeInt(msg.getMsg().length());
        buf.writeBytes(msg.getMsg().getBytes("UTF-8"));
        out.add(new DatagramPacket(buf, msg.getInetSocketAddress()));
    }
}
