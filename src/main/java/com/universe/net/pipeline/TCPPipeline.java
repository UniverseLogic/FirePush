package com.universe.net.pipeline;

import com.universe.net.codec.TCPDecoder;
import com.universe.net.codec.TCPEncoder;
import com.universe.net.handler.TCPServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class TCPPipeline extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast("decoder", new TCPDecoder());
        channelPipeline.addLast("encoder", new TCPEncoder());
        channelPipeline.addLast("handler", new TCPServerHandler());
    }
}
