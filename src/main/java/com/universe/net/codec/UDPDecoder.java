package com.universe.net.codec;

import com.universe.net.message.UDPPacketReqMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by zg on 2017/2/7.
 */
public class UDPDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private final int headLength = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf in = msg.content();
        while (in.readableBytes() > headLength) {
            int length = in.readInt();
            if (in.readableBytes() >= length) {
                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                out.add(new UDPPacketReqMsg(new String(bytes, "UTF-8"), msg.sender()));
                return;
            }
            in.resetReaderIndex();
            break;
        }
    }
}
