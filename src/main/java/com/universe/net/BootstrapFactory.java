package com.universe.net;

import com.universe.net.codec.UDPDecoder;
import com.universe.net.codec.UDPEncoder;
import com.universe.net.handler.UDPServerHandler;
import com.universe.net.handler.WebSocketServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * Created by zg on 2017/2/7.
 */
public class BootstrapFactory {


    public static void startUDP(int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Channel channel;
        try {
            bootstrap.group(loopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline channelPipeline = channel.pipeline();
                            channelPipeline.addLast("decoder", new UDPDecoder());
                            channelPipeline.addLast("encoder", new UDPEncoder());
                            channelPipeline.addLast("handler", new UDPServerHandler());
                        }
                    });
            InetAddress address = InetAddress.getLocalHost();
            channel = bootstrap.bind(address, port).sync().channel();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    loopGroup.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void startWebSocket(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Channel channel;
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            channelPipeline.addLast("codec", new HttpServerCodec());
                            channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            channelPipeline.addLast("compression", new WebSocketServerCompressionHandler());
                            channelPipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });
            InetAddress address = InetAddress.getLocalHost();
            channel = serverBootstrap.bind(address, port).sync().channel();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


}
