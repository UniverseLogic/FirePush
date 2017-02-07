package com.universe.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by zg on 2017/2/7.
 */
public class TCPDecoder extends ByteToMessageDecoder {
    private final int headLength = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > headLength) {
            int length = in.readInt();
            if (in.readableBytes() >= length) {
                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                out.add(new String(bytes, "UTF-8"));
                return;
            }
            in.resetReaderIndex();
            break;
        }
    }
}
