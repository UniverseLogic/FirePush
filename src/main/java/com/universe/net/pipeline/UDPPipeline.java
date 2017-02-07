package com.universe.net.pipeline;

import com.universe.net.codec.UDPDecoder;
import com.universe.net.codec.UDPEncoder;
import com.universe.net.handler.UDPServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class UDPPipeline extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast("decoder", new UDPDecoder());
        channelPipeline.addLast("encoder", new UDPEncoder());
        channelPipeline.addLast("handler", new UDPServerHandler());
    }
}
