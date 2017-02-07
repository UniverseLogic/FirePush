package com.test;

import com.universe.net.codec.TCPDecoder;
import com.universe.net.codec.TCPEncoder;
import com.universe.net.codec.UDPDecoder;
import com.universe.net.codec.UDPEncoder;
import com.universe.net.protoc.UDPPacketReqMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * Created by frank_zhao on 2017/2/7.
 */
public class ClientTest extends TestCase {


    public void testUDPClient() throws UnsupportedEncodingException {
        String str = "hello,world";
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes("UTF-8"));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline channelPipeline = channel.pipeline();
                            channelPipeline.addLast("decoder", new UDPDecoder());
                            channelPipeline.addLast("encoder", new UDPEncoder());
                            channelPipeline.addLast("handler", new SimpleChannelInboundHandler<UDPPacketReqMsg>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, UDPPacketReqMsg msg) throws Exception {
                                    System.out.println("i`m client");
                                    System.out.println(msg);
                                }
                            });
                        }
                    });

            Channel ch = b.bind(0).sync().channel();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    ch.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            });

            ch.writeAndFlush(new DatagramPacket(
                    buf, new InetSocketAddress(InetAddress.getLocalHost(), 46000))).sync();


            if (!ch.closeFuture().await(51000)) {
                System.err.println("request timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }


    public void testTCPClient() throws UnsupportedEncodingException {
        String str = "hello,world";
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes("UTF-8"));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            ChannelPipeline channelPipeline = channel.pipeline();
                            channelPipeline.addLast("decoder", new TCPDecoder());
                            channelPipeline.addLast("encoder", new TCPEncoder());
                            channelPipeline.addLast("handler", new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                    System.out.println("i`m tcp client");
                                    System.out.println(msg);
                                }
                            });
                        }
                    });

            Channel ch = b.connect("192.168.1.106", 46003).sync().channel();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    ch.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }
            });

            ch.writeAndFlush(buf).sync();


            if (!ch.closeFuture().await(51000)) {
                System.err.println("request timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
