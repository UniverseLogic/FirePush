package com.universe.net.pipeline;

import com.universe.net.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class WebSocketPipeline extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast("codec", new HttpServerCodec());
        channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        channelPipeline.addLast("compression", new WebSocketServerCompressionHandler());
        channelPipeline.addLast("handler", new WebSocketServerHandler());
    }
}
